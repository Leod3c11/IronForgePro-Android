package com.ironforge.pro.ui.components

import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.ironforge.pro.model.EquipmentType
import com.ironforge.pro.model.Exercise
import com.ironforge.pro.model.MotionPattern
import com.ironforge.pro.ui.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ExerciseMiniVideoPlayer(
    exercise: Exercise,
    customVideoUri: String? = null,
    height: Dp = 175.dp,
    compact: Boolean = false,
    onAttachVideoClick: (() -> Unit)? = null
) {
    var isPlaying by remember { mutableStateOf(true) }
    var slowMotion by remember { mutableStateOf(false) }
    var useCustomMp4 by remember(customVideoUri) { mutableStateOf(customVideoUri != null) }

    // Smooth continuous cycle [0f..1f]
    val cycleDuration = if (slowMotion) 5200 else 2800
    val infiniteTransition = rememberInfiniteTransition(label = "exerciseMotion")
    val animatedCycle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = cycleDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cycleProgress"
    )
    var pausedCycle by remember { mutableFloatStateOf(0.35f) }
    val rawT = if (isPlaying) {
        pausedCycle = animatedCycle
        animatedCycle
    } else {
        pausedCycle
    }

    // Biomechanical phase calculation:
    // 0.00..0.45 = Eccentric (lowering/stretching)
    // 0.45..0.55 = Stretch Pause
    // 0.55..0.88 = Concentric (lifting/contracting)
    // 0.88..1.00 = Peak Contraction
    val (repFactor, phaseLabel, phaseColor) = remember(rawT) {
        when {
            rawT < 0.45f -> {
                val p = rawT / 0.45f
                val smooth = (1f - cos(p * PI.toFloat())) / 2f
                Triple(smooth, "EXCÊNTRICA", HyperCyan)
            }
            rawT < 0.55f -> {
                Triple(1f, "ALONGAMENTO", HyperAmber)
            }
            rawT < 0.88f -> {
                val p = (rawT - 0.55f) / 0.33f
                val smooth = 1f - ((1f - cos(p * PI.toFloat())) / 2f)
                Triple(smooth, "CONCÊNTRICA", HyperOrange)
            }
            else -> {
                Triple(0f, "PICO CONTR.", HyperEmerald)
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF090C12),
        border = BorderStroke(1.dp, HyperHairline)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (useCustomMp4 && customVideoUri != null) {
                AndroidView(
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            setVideoURI(Uri.parse(customVideoUri))
                            setOnPreparedListener { mp ->
                                mp.isLooping = true
                                mp.setVolume(0f, 0f)
                                start()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Native 60fps Biomechanical Execution Canvas
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawStudioBackdrop()
                    drawBiomechanicalExercise(
                        pattern = exercise.motionPattern,
                        equipment = exercise.equipment,
                        t = repFactor, // 0f = contracted/top, 1f = stretched/bottom
                        contractionGlow = 1f - repFactor
                    )
                }
            }

            // Top HUD Overlay (HyperOS Video Player Chrome)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Live REC/DEMO indicator dot
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xB30B0E14))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(phaseColor)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (useCustomMp4) "VÍDEO MP4" else phaseLabel,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = phaseColor,
                                fontSize = 10.sp
                            )
                        }
                    }

                    if (!compact) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xB30B0E14))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "TUT ${exercise.tempoTUT}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                // Right Controls: Speed + Play/Pause + MP4 Attach
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (customVideoUri != null) {
                        SmallHudPill(
                            text = if (useCustomMp4) "3D Sim" else "MP4",
                            accent = HyperCyan,
                            onClick = { useCustomMp4 = !useCustomMp4 }
                        )
                    } else if (onAttachVideoClick != null && !compact) {
                        SmallHudPill(
                            text = "+ MP4",
                            accent = HyperCyan,
                            onClick = onAttachVideoClick
                        )
                    }

                    if (!useCustomMp4) {
                        SmallHudPill(
                            text = if (slowMotion) "0.5x" else "1.0x",
                            accent = if (slowMotion) HyperVolt else TextPrimary,
                            onClick = { slowMotion = !slowMotion }
                        )

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xCC151A24))
                                .clickable { isPlaying = !isPlaying },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = HyperOrange,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Bottom Timeline Scrubber Bar
            if (!useCustomMp4) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = exercise.motionPattern.biomechLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "Ativação: ${((1f - repFactor) * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = HyperOrangeGlow,
                            fontSize = 10.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FFFFFF))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(rawT.coerceIn(0.02f, 1f))
                                .fillMaxHeight()
                                .background(
                                    Brush.horizontalGradient(listOf(HyperCyan, HyperOrange))
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SmallHudPill(
    text: String,
    accent: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xCC151A24))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = accent,
            fontSize = 10.sp
        )
    }
}

private fun DrawScope.drawStudioBackdrop() {
    // Radial spotlight behind athlete
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                HyperOrange.copy(alpha = 0.14f),
                HyperCyan.copy(alpha = 0.05f),
                Color.Transparent
            ),
            center = Offset(size.width * 0.5f, size.height * 0.52f),
            radius = size.minDimension * 0.72f
        ),
        center = Offset(size.width * 0.5f, size.height * 0.52f),
        radius = size.minDimension * 0.72f
    )

    // Subtle perspective gym floor line
    val floorY = size.height * 0.80f
    drawLine(
        color = HyperHairline.copy(alpha = 0.7f),
        start = Offset(size.width * 0.12f, floorY),
        end = Offset(size.width * 0.88f, floorY),
        strokeWidth = 1.5.dp.toPx()
    )
}

private fun DrawScope.drawBiomechanicalExercise(
    pattern: MotionPattern,
    equipment: EquipmentType,
    t: Float, // 0f = top/contracted, 1f = bottom/stretched
    contractionGlow: Float
) {
    val cx = size.width * 0.50f
    val floorY = size.height * 0.80f
    val bodyColor = Color(0xFF8F9EB8)
    val jointColor = Color(0xFFD8E2F5)
    val machineColor = Color(0xFF3A465E)
    val muscleGlow = HyperOrange.copy(alpha = 0.45f + 0.55f * contractionGlow)
    val strokeBody = 7.dp.toPx()
    val strokeLimb = 5.5.dp.toPx()

    when (pattern) {
        MotionPattern.BENCH_PRESS, MotionPattern.INCLINE_PRESS -> {
            val isIncline = pattern == MotionPattern.INCLINE_PRESS
            // Bench
            val benchStart = Offset(cx - 55.dp.toPx(), floorY - 22.dp.toPx())
            val benchEnd = Offset(cx + 48.dp.toPx(), if (isIncline) floorY - 46.dp.toPx() else floorY - 22.dp.toPx())
            drawLine(machineColor, Offset(cx - 42.dp.toPx(), floorY), Offset(cx - 42.dp.toPx(), benchStart.y), 4.dp.toPx())
            drawLine(machineColor, Offset(cx + 32.dp.toPx(), floorY), Offset(cx + 32.dp.toPx(), benchEnd.y - 4.dp.toPx()), 4.dp.toPx())
            drawLine(machineColor, benchStart, benchEnd, 6.dp.toPx(), cap = StrokeCap.Round)

            // Athlete lying
            val hip = Offset(cx - 22.dp.toPx(), benchStart.y - 5.dp.toPx())
            val shoulder = Offset(cx + 22.dp.toPx(), if (isIncline) benchEnd.y - 8.dp.toPx() else benchStart.y - 5.dp.toPx())
            val head = Offset(shoulder.x + 15.dp.toPx(), shoulder.y - (if (isIncline) 6.dp.toPx() else 0f))
            val knee = Offset(hip.x - 24.dp.toPx(), hip.y + 4.dp.toPx())
            val foot = Offset(knee.x - 6.dp.toPx(), floorY)

            drawLine(bodyColor, foot, knee, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, knee, hip, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, hip, shoulder, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), head)

            // Active Pectoral Muscle Highlight
            drawCircle(muscleGlow, (9f + 4f * contractionGlow).dp.toPx(), Offset(shoulder.x - 6.dp.toPx(), shoulder.y - 4.dp.toPx()))

            // Animated Arms & Weight (t=0 top extended, t=1 chest level)
            val handY = shoulder.y - (44.dp.toPx() * (1f - 0.68f * t))
            val hand = Offset(shoulder.x - 2.dp.toPx(), handY)
            val elbow = Offset(shoulder.x + 12.dp.toPx() * t, (shoulder.y + handY) / 2f + 4.dp.toPx() * t)
            drawLine(bodyColor, shoulder, elbow, strokeLimb, cap = StrokeCap.Round)
            drawLine(jointColor, elbow, hand, strokeLimb, cap = StrokeCap.Round)

            // Barbell or Dumbbell Plate
            drawCircle(HyperCyan, 11.dp.toPx(), hand)
            drawCircle(HyperOrange, 6.dp.toPx(), hand)
        }

        MotionPattern.FLY_CROSSOVER, MotionPattern.REVERSE_FLY -> {
            // Front view chest fly or rear delt fly
            val hip = Offset(cx, floorY - 38.dp.toPx())
            val chest = Offset(cx, floorY - 76.dp.toPx())
            val head = Offset(cx, chest.y - 16.dp.toPx())

            drawLine(bodyColor, Offset(cx - 12.dp.toPx(), floorY), hip, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, Offset(cx + 12.dp.toPx(), floorY), hip, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, hip, chest, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), head)

            // Active chest/shoulder glow
            drawCircle(muscleGlow, (11f + 4f * contractionGlow).dp.toPx(), chest)

            // Sweeping arms: t=0 closed in front, t=1 wide open
            val spreadX = (10f + 42f * t).dp.toPx()
            val spreadY = chest.y + (8f - 10f * t).dp.toPx()
            val leftHand = Offset(cx - spreadX, spreadY)
            val rightHand = Offset(cx + spreadX, spreadY)

            // Cable lines from top corners
            if (equipment == EquipmentType.CABLE) {
                drawLine(HyperCyan.copy(alpha = 0.5f), Offset(cx - 72.dp.toPx(), 16.dp.toPx()), leftHand, 1.5.dp.toPx())
                drawLine(HyperCyan.copy(alpha = 0.5f), Offset(cx + 72.dp.toPx(), 16.dp.toPx()), rightHand, 1.5.dp.toPx())
            }
            drawLine(jointColor, Offset(cx - 10.dp.toPx(), chest.y), leftHand, strokeLimb, cap = StrokeCap.Round)
            drawLine(jointColor, Offset(cx + 10.dp.toPx(), chest.y), rightHand, strokeLimb, cap = StrokeCap.Round)
            drawCircle(HyperOrange, 5.dp.toPx(), leftHand)
            drawCircle(HyperOrange, 5.dp.toPx(), rightHand)
        }

        MotionPattern.DIPS -> {
            // Parallel bar
            drawLine(machineColor, Offset(cx - 35.dp.toPx(), floorY - 52.dp.toPx()), Offset(cx + 35.dp.toPx(), floorY - 52.dp.toPx()), 4.dp.toPx())
            val dropY = 22.dp.toPx() * t
            val hand = Offset(cx, floorY - 52.dp.toPx())
            val shoulder = Offset(cx + 10.dp.toPx(), hand.y - 26.dp.toPx() + dropY)
            val hip = Offset(cx - 4.dp.toPx(), shoulder.y + 32.dp.toPx())
            val knee = Offset(hip.x - 10.dp.toPx(), hip.y + 22.dp.toPx())
            val elbow = Offset(shoulder.x - 14.dp.toPx() * t, (shoulder.y + hand.y) / 2f)

            drawLine(bodyColor, knee, hip, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, hip, shoulder, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x + 6.dp.toPx(), shoulder.y - 14.dp.toPx()))
            drawCircle(muscleGlow, 10.dp.toPx(), Offset(shoulder.x - 2.dp.toPx(), shoulder.y + 6.dp.toPx()))
            drawLine(bodyColor, shoulder, elbow, strokeLimb, cap = StrokeCap.Round)
            drawLine(jointColor, elbow, hand, strokeLimb, cap = StrokeCap.Round)
        }

        MotionPattern.LAT_PULLDOWN, MotionPattern.PULL_UP -> {
            val isPullUp = pattern == MotionPattern.PULL_UP
            val barBaseY = 22.dp.toPx()
            val hip = Offset(cx, floorY - 34.dp.toPx())
            val bodyLift = if (isPullUp) 26.dp.toPx() * (1f - t) else 0f
            val currentHip = Offset(cx, hip.y - bodyLift)
            val shoulder = Offset(cx, currentHip.y - 38.dp.toPx())

            if (!isPullUp) {
                // Seat
                drawLine(machineColor, Offset(cx - 20.dp.toPx(), floorY - 28.dp.toPx()), Offset(cx + 20.dp.toPx(), floorY - 28.dp.toPx()), 5.dp.toPx())
                drawLine(bodyColor, Offset(cx + 22.dp.toPx(), floorY), Offset(cx + 22.dp.toPx(), floorY - 28.dp.toPx()), strokeLimb)
            } else {
                drawLine(bodyColor, Offset(cx - 6.dp.toPx(), currentHip.y + 30.dp.toPx()), currentHip, strokeLimb)
            }

            drawLine(bodyColor, currentHip, shoulder, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), Offset(cx, shoulder.y - 14.dp.toPx()))

            // Glowing Lats (V-taper triangles)
            drawCircle(muscleGlow, (11f + 4f * contractionGlow).dp.toPx(), Offset(cx, shoulder.y + 12.dp.toPx()))

            val barY = if (isPullUp) barBaseY else (shoulder.y - 6.dp.toPx() - 36.dp.toPx() * t)
            if (!isPullUp) {
                drawLine(HyperCyan.copy(alpha = 0.6f), Offset(cx, 8.dp.toPx()), Offset(cx, barY), 2.dp.toPx())
            }
            drawLine(HyperOrange, Offset(cx - 34.dp.toPx(), barY), Offset(cx + 34.dp.toPx(), barY), 4.dp.toPx(), cap = StrokeCap.Round)

            val lHand = Offset(cx - 26.dp.toPx(), barY)
            val rHand = Offset(cx + 26.dp.toPx(), barY)
            val lElbow = Offset(cx - 28.dp.toPx(), (shoulder.y + barY) / 2f + 6.dp.toPx() * (1f - t))
            val rElbow = Offset(cx + 28.dp.toPx(), (shoulder.y + barY) / 2f + 6.dp.toPx() * (1f - t))
            drawLine(bodyColor, Offset(cx - 9.dp.toPx(), shoulder.y), lElbow, strokeLimb, cap = StrokeCap.Round)
            drawLine(jointColor, lElbow, lHand, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, Offset(cx + 9.dp.toPx(), shoulder.y), rElbow, strokeLimb, cap = StrokeCap.Round)
            drawLine(jointColor, rElbow, rHand, strokeLimb, cap = StrokeCap.Round)
        }

        MotionPattern.ROW_HORIZONTAL -> {
            // Seated or bent-over horizontal row
            val hip = Offset(cx - 18.dp.toPx(), floorY - 34.dp.toPx())
            val shoulder = Offset(cx - 6.dp.toPx(), hip.y - 36.dp.toPx())
            drawLine(bodyColor, Offset(cx + 24.dp.toPx(), floorY - 26.dp.toPx()), hip, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, hip, shoulder, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x, shoulder.y - 14.dp.toPx()))
            drawCircle(muscleGlow, (11f + 4f * contractionGlow).dp.toPx(), Offset(shoulder.x - 4.dp.toPx(), shoulder.y + 10.dp.toPx()))

            // Pulley anchor on right
            val pulley = Offset(cx + 68.dp.toPx(), shoulder.y + 12.dp.toPx())
            drawCircle(machineColor, 6.dp.toPx(), pulley)

            val handX = shoulder.x + 8.dp.toPx() + 34.dp.toPx() * t
            val hand = Offset(handX, shoulder.y + 12.dp.toPx())
            val elbow = Offset(shoulder.x - 14.dp.toPx() * (1f - t) + 12.dp.toPx() * t, shoulder.y + 14.dp.toPx())
            drawLine(HyperCyan.copy(alpha = 0.6f), pulley, hand, 2.dp.toPx())
            drawLine(bodyColor, shoulder, elbow, strokeLimb, cap = StrokeCap.Round)
            drawLine(jointColor, elbow, hand, strokeLimb, cap = StrokeCap.Round)
            drawCircle(HyperOrange, 5.dp.toPx(), hand)
        }

        MotionPattern.STRAIGHT_ARM_PULL -> {
            val hip = Offset(cx - 12.dp.toPx(), floorY - 42.dp.toPx())
            val shoulder = Offset(cx - 2.dp.toPx(), hip.y - 36.dp.toPx())
            drawLine(bodyColor, Offset(cx - 14.dp.toPx(), floorY), hip, strokeLimb)
            drawLine(bodyColor, hip, shoulder, strokeBody)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x + 2.dp.toPx(), shoulder.y - 14.dp.toPx()))
            drawCircle(muscleGlow, 10.dp.toPx(), Offset(shoulder.x - 4.dp.toPx(), shoulder.y + 14.dp.toPx()))

            val angle = (-0.2f + 1.3f * (1f - t)).toDouble() // 0=down at hip, 1=overhead
            val armLen = 36.dp.toPx()
            val hand = Offset(shoulder.x + (cos(angle) * armLen).toFloat(), shoulder.y - (sin(angle) * armLen).toFloat())
            drawLine(HyperCyan.copy(alpha = 0.5f), Offset(cx + 65.dp.toPx(), 18.dp.toPx()), hand, 1.8.dp.toPx())
            drawLine(jointColor, shoulder, hand, strokeLimb, cap = StrokeCap.Round)
            drawCircle(HyperOrange, 5.dp.toPx(), hand)
        }

        MotionPattern.OVERHEAD_PRESS -> {
            val hip = Offset(cx, floorY - 34.dp.toPx())
            val shoulder = Offset(cx, hip.y - 38.dp.toPx())
            drawLine(machineColor, Offset(cx - 16.dp.toPx(), floorY), Offset(cx - 16.dp.toPx(), shoulder.y - 6.dp.toPx()), 5.dp.toPx())
            drawLine(bodyColor, Offset(cx + 20.dp.toPx(), floorY), hip, strokeLimb)
            drawLine(bodyColor, hip, shoulder, strokeBody)
            drawCircle(jointColor, 7.dp.toPx(), Offset(cx, shoulder.y - 14.dp.toPx()))
            drawCircle(muscleGlow, (10f + 4f * contractionGlow).dp.toPx(), shoulder)

            val handY = shoulder.y - 36.dp.toPx() * (1f - 0.85f * t)
            val hand = Offset(cx + 4.dp.toPx(), handY)
            val elbow = Offset(cx + 16.dp.toPx() * t, (shoulder.y + handY) / 2f + 6.dp.toPx() * t)
            drawLine(bodyColor, shoulder, elbow, strokeLimb)
            drawLine(jointColor, elbow, hand, strokeLimb)
            drawCircle(HyperOrange, 7.dp.toPx(), hand)
        }

        MotionPattern.LATERAL_RAISE -> {
            val hip = Offset(cx, floorY - 42.dp.toPx())
            val shoulder = Offset(cx, hip.y - 38.dp.toPx())
            drawLine(bodyColor, Offset(cx - 10.dp.toPx(), floorY), hip, strokeLimb)
            drawLine(bodyColor, Offset(cx + 10.dp.toPx(), floorY), hip, strokeLimb)
            drawLine(bodyColor, hip, shoulder, strokeBody)
            drawCircle(jointColor, 7.dp.toPx(), Offset(cx, shoulder.y - 14.dp.toPx()))

            // Glowing lateral delts
            drawCircle(muscleGlow, 8.dp.toPx(), Offset(cx - 12.dp.toPx(), shoulder.y))
            drawCircle(muscleGlow, 8.dp.toPx(), Offset(cx + 12.dp.toPx(), shoulder.y))

            val lift = 1f - t // 1 = shoulder height, 0 = down at sides
            val armLen = 34.dp.toPx()
            val angle = (0.15 + lift * 1.35)
            val dx = (sin(angle) * armLen).toFloat()
            val dy = (cos(angle) * armLen).toFloat()
            val lHand = Offset(cx - 10.dp.toPx() - dx, shoulder.y + dy)
            val rHand = Offset(cx + 10.dp.toPx() + dx, shoulder.y + dy)
            drawLine(jointColor, Offset(cx - 10.dp.toPx(), shoulder.y), lHand, strokeLimb, cap = StrokeCap.Round)
            drawLine(jointColor, Offset(cx + 10.dp.toPx(), shoulder.y), rHand, strokeLimb, cap = StrokeCap.Round)
            drawCircle(HyperOrange, 5.dp.toPx(), lHand)
            drawCircle(HyperOrange, 5.dp.toPx(), rHand)
        }

        MotionPattern.BICEP_CURL, MotionPattern.PREACHER_CURL -> {
            val hip = Offset(cx - 10.dp.toPx(), floorY - 42.dp.toPx())
            val shoulder = Offset(cx - 10.dp.toPx(), hip.y - 38.dp.toPx())
            val elbow = Offset(cx - 6.dp.toPx(), shoulder.y + 24.dp.toPx())
            drawLine(bodyColor, Offset(cx - 10.dp.toPx(), floorY), hip, strokeLimb)
            drawLine(bodyColor, hip, shoulder, strokeBody)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x, shoulder.y - 14.dp.toPx()))
            drawLine(bodyColor, shoulder, elbow, strokeLimb)

            // Glowing Biceps Peak
            drawCircle(muscleGlow, (7f + 4f * contractionGlow).dp.toPx(), Offset(shoulder.x + 5.dp.toPx(), shoulder.y + 12.dp.toPx()))

            // Forearm curl: t=0 curled up, t=1 extended down
            val angle = (-1.1 + 2.3 * (1f - t))
            val forearmLen = 25.dp.toPx()
            val hand = Offset(elbow.x + (cos(angle) * forearmLen).toFloat(), elbow.y - (sin(angle) * forearmLen).toFloat())
            drawLine(jointColor, elbow, hand, strokeLimb, cap = StrokeCap.Round)
            drawCircle(HyperOrange, 6.dp.toPx(), hand)
        }

        MotionPattern.TRICEP_PUSHDOWN, MotionPattern.OVERHEAD_TRICEP, MotionPattern.SKULLCRUSHER -> {
            val hip = Offset(cx - 14.dp.toPx(), floorY - 42.dp.toPx())
            val shoulder = Offset(cx - 10.dp.toPx(), hip.y - 38.dp.toPx())
            drawLine(bodyColor, Offset(cx - 14.dp.toPx(), floorY), hip, strokeLimb)
            drawLine(bodyColor, hip, shoulder, strokeBody)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x, shoulder.y - 14.dp.toPx()))

            val isOverhead = pattern == MotionPattern.OVERHEAD_TRICEP
            val elbow = if (isOverhead) Offset(shoulder.x + 4.dp.toPx(), shoulder.y - 22.dp.toPx())
                        else Offset(shoulder.x - 4.dp.toPx(), shoulder.y + 22.dp.toPx())
            drawLine(bodyColor, shoulder, elbow, strokeLimb)
            drawCircle(muscleGlow, (7f + 4f * contractionGlow).dp.toPx(), Offset(shoulder.x - 5.dp.toPx(), (shoulder.y + elbow.y) / 2f))

            val forearmLen = 24.dp.toPx()
            val hand = if (isOverhead) {
                Offset(elbow.x + 18.dp.toPx() * (1f - t) - 10.dp.toPx() * t, elbow.y - 20.dp.toPx() * (1f - t) + 12.dp.toPx() * t)
            } else {
                Offset(elbow.x + 6.dp.toPx() + 14.dp.toPx() * t, elbow.y + forearmLen * (1f - 0.85f * t))
            }
            drawLine(HyperCyan.copy(alpha = 0.5f), Offset(cx + 42.dp.toPx(), 16.dp.toPx()), hand, 1.5.dp.toPx())
            drawLine(jointColor, elbow, hand, strokeLimb, cap = StrokeCap.Round)
            drawCircle(HyperOrange, 5.5.dp.toPx(), hand)
        }

        MotionPattern.SQUAT, MotionPattern.LUNGE_SPLIT -> {
            // t=0 standing tall, t=1 deep squat
            val foot = Offset(cx, floorY)
            val knee = Offset(cx + 16.dp.toPx() * t, floorY - 34.dp.toPx() + 6.dp.toPx() * t)
            val hip = Offset(cx - 26.dp.toPx() * t, floorY - 68.dp.toPx() + 32.dp.toPx() * t)
            val shoulder = Offset(hip.x + 14.dp.toPx() * t, hip.y - 36.dp.toPx() + 4.dp.toPx() * t)

            drawLine(bodyColor, foot, knee, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, knee, hip, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, hip, shoulder, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x + 2.dp.toPx(), shoulder.y - 13.dp.toPx()))

            // Glowing Quads & Glutes
            drawCircle(muscleGlow, (9f + 4f * contractionGlow).dp.toPx(), Offset((hip.x + knee.x) / 2f + 4.dp.toPx(), (hip.y + knee.y) / 2f))

            // Barbell on traps
            drawCircle(HyperCyan, 12.dp.toPx(), shoulder)
            drawCircle(HyperOrange, 6.dp.toPx(), shoulder)
        }

        MotionPattern.LEG_PRESS -> {
            // 45-degree leg press sled
            val seat = Offset(cx - 32.dp.toPx(), floorY - 24.dp.toPx())
            val shoulder = Offset(cx - 48.dp.toPx(), floorY - 46.dp.toPx())
            drawLine(machineColor, shoulder, seat, 6.dp.toPx())
            drawLine(bodyColor, shoulder, seat, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x - 6.dp.toPx(), shoulder.y - 10.dp.toPx()))

            val foot = Offset(cx + 34.dp.toPx() - 22.dp.toPx() * t, floorY - 68.dp.toPx() + 20.dp.toPx() * t)
            val knee = Offset(seat.x + 26.dp.toPx() - 12.dp.toPx() * t, seat.y - 26.dp.toPx() - 6.dp.toPx() * t)
            drawLine(bodyColor, seat, knee, strokeLimb, cap = StrokeCap.Round)
            drawLine(jointColor, knee, foot, strokeLimb, cap = StrokeCap.Round)
            drawCircle(muscleGlow, 10.dp.toPx(), Offset((seat.x + knee.x) / 2f, (seat.y + knee.y) / 2f - 4.dp.toPx()))

            // Sled plate
            drawLine(HyperOrange, Offset(foot.x - 8.dp.toPx(), foot.y - 14.dp.toPx()), Offset(foot.x + 10.dp.toPx(), foot.y + 12.dp.toPx()), 6.dp.toPx(), cap = StrokeCap.Round)
        }

        MotionPattern.LEG_EXTENSION, MotionPattern.LEG_CURL -> {
            val isExtension = pattern == MotionPattern.LEG_EXTENSION
            val hip = Offset(cx - 14.dp.toPx(), floorY - 36.dp.toPx())
            val shoulder = Offset(cx - 22.dp.toPx(), hip.y - 34.dp.toPx())
            val knee = Offset(cx + 16.dp.toPx(), hip.y)

            drawLine(machineColor, Offset(cx - 20.dp.toPx(), floorY), Offset(cx + 16.dp.toPx(), floorY), 4.dp.toPx())
            drawLine(bodyColor, shoulder, hip, strokeBody)
            drawLine(bodyColor, hip, knee, strokeLimb)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x, shoulder.y - 12.dp.toPx()))

            val angle = if (isExtension) (-1.35 + 1.35 * (1f - t)) else (-0.1 - 1.35 * (1f - t))
            val shinLen = 32.dp.toPx()
            val ankle = Offset(knee.x + (cos(angle) * shinLen).toFloat(), knee.y - (sin(angle) * shinLen).toFloat())
            drawLine(jointColor, knee, ankle, strokeLimb, cap = StrokeCap.Round)
            drawCircle(HyperOrange, 6.dp.toPx(), ankle)

            val glowPos = if (isExtension) Offset(cx + 2.dp.toPx(), hip.y - 5.dp.toPx()) else Offset(cx + 2.dp.toPx(), hip.y + 6.dp.toPx())
            drawCircle(muscleGlow, (9f + 3f * contractionGlow).dp.toPx(), glowPos)
        }

        MotionPattern.RDL_HINGE, MotionPattern.DEADLIFT -> {
            // Hip hinge: t=0 upright, t=1 hinged forward
            val foot = Offset(cx, floorY)
            val knee = Offset(cx + 4.dp.toPx(), floorY - 34.dp.toPx())
            val hip = Offset(cx - 18.dp.toPx() * t, floorY - 68.dp.toPx() + 6.dp.toPx() * t)
            val torsoAngle = 1.45 - 1.25 * t // radians from horizontal
            val torsoLen = 40.dp.toPx()
            val shoulder = Offset(hip.x + (cos(torsoAngle) * torsoLen).toFloat(), hip.y - (sin(torsoAngle) * torsoLen).toFloat())

            drawLine(bodyColor, foot, knee, strokeLimb)
            drawLine(bodyColor, knee, hip, strokeLimb)
            drawLine(bodyColor, hip, shoulder, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x + 8.dp.toPx(), shoulder.y - 8.dp.toPx()))

            // Glowing Hamstrings & Glutes
            drawCircle(muscleGlow, 9.dp.toPx(), Offset(hip.x - 4.dp.toPx(), hip.y + 14.dp.toPx()))

            // Arms hanging vertically with barbell
            val bar = Offset(shoulder.x, shoulder.y + 34.dp.toPx())
            drawLine(jointColor, shoulder, bar, strokeLimb)
            drawCircle(HyperCyan, 11.dp.toPx(), bar)
            drawCircle(HyperOrange, 5.5.dp.toPx(), bar)
        }

        MotionPattern.HIP_THRUST -> {
            // Bench support on left
            val benchTop = Offset(cx - 32.dp.toPx(), floorY - 32.dp.toPx())
            drawLine(machineColor, Offset(cx - 42.dp.toPx(), floorY), benchTop, 6.dp.toPx())
            val foot = Offset(cx + 38.dp.toPx(), floorY)
            val knee = Offset(cx + 32.dp.toPx(), floorY - 32.dp.toPx())
            val hip = Offset(cx + 2.dp.toPx(), floorY - 32.dp.toPx() + 20.dp.toPx() * t)

            drawLine(bodyColor, foot, knee, strokeLimb)
            drawLine(bodyColor, knee, hip, strokeLimb)
            drawLine(bodyColor, hip, benchTop, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), Offset(benchTop.x - 10.dp.toPx(), benchTop.y - 6.dp.toPx()))

            // Glowing Glutes + Barbell on hip
            drawCircle(muscleGlow, (10f + 4f * contractionGlow).dp.toPx(), Offset(hip.x, hip.y + 5.dp.toPx()))
            drawCircle(HyperCyan, 11.dp.toPx(), Offset(hip.x, hip.y - 5.dp.toPx()))
            drawCircle(HyperOrange, 5.5.dp.toPx(), Offset(hip.x, hip.y - 5.dp.toPx()))
        }

        MotionPattern.CALF_RAISE -> {
            val liftY = 14.dp.toPx() * (1f - t)
            drawLine(machineColor, Offset(cx - 24.dp.toPx(), floorY - 8.dp.toPx()), Offset(cx + 24.dp.toPx(), floorY - 8.dp.toPx()), 6.dp.toPx())
            val ankle = Offset(cx, floorY - 12.dp.toPx() - liftY)
            val knee = Offset(cx, ankle.y - 34.dp.toPx())
            val hip = Offset(cx, knee.y - 34.dp.toPx())
            drawLine(bodyColor, Offset(cx + 8.dp.toPx(), floorY - 10.dp.toPx()), ankle, strokeLimb)
            drawLine(bodyColor, ankle, knee, strokeLimb)
            drawLine(bodyColor, knee, hip, strokeBody)
            drawCircle(muscleGlow, (8f + 4f * contractionGlow).dp.toPx(), Offset(cx - 5.dp.toPx(), (ankle.y + knee.y) / 2f))
        }

        MotionPattern.CRUNCH_FLEXION, MotionPattern.LEG_RAISE, MotionPattern.AB_WHEEL -> {
            val hip = Offset(cx - 10.dp.toPx(), floorY - 22.dp.toPx())
            val curl = 1f - t
            val shoulder = Offset(hip.x - 28.dp.toPx() + 16.dp.toPx() * curl, hip.y - 18.dp.toPx() * curl)
            val knee = Offset(hip.x + 26.dp.toPx(), hip.y - 16.dp.toPx() * curl)
            drawLine(bodyColor, knee, hip, strokeLimb)
            drawLine(bodyColor, hip, shoulder, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x - 10.dp.toPx(), shoulder.y - 6.dp.toPx()))
            drawCircle(muscleGlow, (10f + 4f * contractionGlow).dp.toPx(), Offset(hip.x - 8.dp.toPx(), hip.y - 6.dp.toPx()))
        }
    }
}
