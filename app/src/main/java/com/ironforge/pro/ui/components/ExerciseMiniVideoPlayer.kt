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
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
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
    val palette = LocalIronPalette.current
    var isPlaying by remember { mutableStateOf(true) }
    var slowMotion by remember { mutableStateOf(false) }
    var useCustomMp4 by remember(customVideoUri) { mutableStateOf(customVideoUri != null) }

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

    val (repFactor, phaseLabel, phaseColor) = remember(rawT, palette) {
        when {
            rawT < 0.45f -> {
                val p = rawT / 0.45f
                val smooth = (1f - cos(p * PI.toFloat())) / 2f
                Triple(smooth, "EXCÊNTRICA", palette.secondary)
            }
            rawT < 0.55f -> {
                Triple(1f, "ALONGAMENTO", HyperAmber)
            }
            rawT < 0.88f -> {
                val p = (rawT - 0.55f) / 0.33f
                val smooth = 1f - ((1f - cos(p * PI.toFloat())) / 2f)
                Triple(smooth, "CONCÊNTRICA", palette.primary)
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
        border = BorderStroke(1.dp, palette.hairline)
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
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawStudioBackdrop(palette.primary, palette.secondary)
                    drawBiomechanicalExercise(
                        pattern = exercise.motionPattern,
                        equipment = exercise.equipment,
                        t = repFactor,
                        contractionGlow = 1f - repFactor,
                        primaryAccent = palette.primary,
                        secondaryAccent = palette.secondary
                    )
                }
            }

            // Top HUD Overlay
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (customVideoUri != null) {
                        SmallHudPill(
                            text = if (useCustomMp4) "3D Sim" else "MP4",
                            accent = palette.secondary,
                            onClick = { useCustomMp4 = !useCustomMp4 }
                        )
                    } else if (onAttachVideoClick != null && !compact) {
                        SmallHudPill(
                            text = "+ MP4",
                            accent = palette.secondary,
                            onClick = onAttachVideoClick
                        )
                    }

                    if (!useCustomMp4) {
                        SmallHudPill(
                            text = if (slowMotion) "0.5x" else "1.0x",
                            accent = if (slowMotion) palette.tertiary else TextPrimary,
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
                                tint = palette.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

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
                            color = palette.primaryGlow,
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
                                    Brush.horizontalGradient(listOf(palette.secondary, palette.primary))
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

private fun DrawScope.drawStudioBackdrop(primary: Color, secondary: Color) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                primary.copy(alpha = 0.14f),
                secondary.copy(alpha = 0.05f),
                Color.Transparent
            ),
            center = Offset(size.width * 0.5f, size.height * 0.52f),
            radius = size.minDimension * 0.72f
        ),
        center = Offset(size.width * 0.5f, size.height * 0.52f),
        radius = size.minDimension * 0.72f
    )

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
    t: Float,
    contractionGlow: Float,
    primaryAccent: Color,
    secondaryAccent: Color
) {
    val cx = size.width * 0.50f
    val floorY = size.height * 0.80f
    val bodyColor = Color(0xFF8F9EB8)
    val jointColor = Color(0xFFD8E2F5)
    val machineColor = Color(0xFF3A465E)
    val muscleGlow = primaryAccent.copy(alpha = 0.45f + 0.55f * contractionGlow)
    val strokeBody = 7.dp.toPx()
    val strokeLimb = 5.5.dp.toPx()

    when (pattern) {
        MotionPattern.BENCH_PRESS, MotionPattern.INCLINE_PRESS -> {
            val isIncline = pattern == MotionPattern.INCLINE_PRESS
            val benchStart = Offset(cx - 55.dp.toPx(), floorY - 22.dp.toPx())
            val benchEnd = Offset(cx + 48.dp.toPx(), if (isIncline) floorY - 46.dp.toPx() else floorY - 22.dp.toPx())
            drawLine(machineColor, Offset(cx - 42.dp.toPx(), floorY), Offset(cx - 42.dp.toPx(), benchStart.y), 4.dp.toPx())
            drawLine(machineColor, Offset(cx + 32.dp.toPx(), floorY), Offset(cx + 32.dp.toPx(), benchEnd.y - 4.dp.toPx()), 4.dp.toPx())
            drawLine(machineColor, benchStart, benchEnd, 6.dp.toPx(), cap = StrokeCap.Round)

            val hip = Offset(cx - 22.dp.toPx(), benchStart.y - 5.dp.toPx())
            val shoulder = Offset(cx + 22.dp.toPx(), if (isIncline) benchEnd.y - 8.dp.toPx() else benchStart.y - 5.dp.toPx())
            val head = Offset(shoulder.x + 15.dp.toPx(), shoulder.y - (if (isIncline) 6.dp.toPx() else 0f))
            val knee = Offset(hip.x - 24.dp.toPx(), hip.y + 4.dp.toPx())
            val foot = Offset(knee.x - 6.dp.toPx(), floorY)

            drawLine(bodyColor, foot, knee, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, knee, hip, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, hip, shoulder, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), head)
            drawCircle(muscleGlow, (9f + 4f * contractionGlow).dp.toPx(), Offset(shoulder.x - 6.dp.toPx(), shoulder.y - 4.dp.toPx()))

            val handY = shoulder.y - (44.dp.toPx() * (1f - 0.68f * t))
            val hand = Offset(shoulder.x - 2.dp.toPx(), handY)
            val elbow = Offset(shoulder.x + 12.dp.toPx() * t, (shoulder.y + handY) / 2f + 4.dp.toPx() * t)
            drawLine(bodyColor, shoulder, elbow, strokeLimb, cap = StrokeCap.Round)
            drawLine(jointColor, elbow, hand, strokeLimb, cap = StrokeCap.Round)
            drawCircle(secondaryAccent, 11.dp.toPx(), hand)
            drawCircle(primaryAccent, 6.dp.toPx(), hand)
        }

        MotionPattern.FLY_CROSSOVER, MotionPattern.REVERSE_FLY -> {
            val hip = Offset(cx, floorY - 38.dp.toPx())
            val chest = Offset(cx, floorY - 76.dp.toPx())
            val head = Offset(cx, chest.y - 16.dp.toPx())

            drawLine(bodyColor, Offset(cx - 12.dp.toPx(), floorY), hip, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, Offset(cx + 12.dp.toPx(), floorY), hip, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, hip, chest, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), head)
            drawCircle(muscleGlow, (11f + 4f * contractionGlow).dp.toPx(), chest)

            val spreadX = (10f + 42f * t).dp.toPx()
            val spreadY = chest.y + (8f - 10f * t).dp.toPx()
            val leftHand = Offset(cx - spreadX, spreadY)
            val rightHand = Offset(cx + spreadX, spreadY)

            if (equipment == EquipmentType.CABLE) {
                drawLine(secondaryAccent.copy(alpha = 0.5f), Offset(cx - 72.dp.toPx(), 16.dp.toPx()), leftHand, 1.5.dp.toPx())
                drawLine(secondaryAccent.copy(alpha = 0.5f), Offset(cx + 72.dp.toPx(), 16.dp.toPx()), rightHand, 1.5.dp.toPx())
            }
            drawLine(jointColor, Offset(cx - 10.dp.toPx(), chest.y), leftHand, strokeLimb, cap = StrokeCap.Round)
            drawLine(jointColor, Offset(cx + 10.dp.toPx(), chest.y), rightHand, strokeLimb, cap = StrokeCap.Round)
            drawCircle(primaryAccent, 5.dp.toPx(), leftHand)
            drawCircle(primaryAccent, 5.dp.toPx(), rightHand)
        }

        MotionPattern.DIPS -> {
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
                drawLine(machineColor, Offset(cx - 20.dp.toPx(), floorY - 28.dp.toPx()), Offset(cx + 20.dp.toPx(), floorY - 28.dp.toPx()), 5.dp.toPx())
                drawLine(bodyColor, Offset(cx + 22.dp.toPx(), floorY), Offset(cx + 22.dp.toPx(), floorY - 28.dp.toPx()), strokeLimb)
            } else {
                drawLine(bodyColor, Offset(cx - 6.dp.toPx(), currentHip.y + 30.dp.toPx()), currentHip, strokeLimb)
            }

            drawLine(bodyColor, currentHip, shoulder, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), Offset(cx, shoulder.y - 14.dp.toPx()))
            drawCircle(muscleGlow, (11f + 4f * contractionGlow).dp.toPx(), Offset(cx, shoulder.y + 12.dp.toPx()))

            val barY = if (isPullUp) barBaseY else (shoulder.y - 6.dp.toPx() - 36.dp.toPx() * t)
            if (!isPullUp) {
                drawLine(secondaryAccent.copy(alpha = 0.6f), Offset(cx, 8.dp.toPx()), Offset(cx, barY), 2.dp.toPx())
            }
            drawLine(primaryAccent, Offset(cx - 34.dp.toPx(), barY), Offset(cx + 34.dp.toPx(), barY), 4.dp.toPx(), cap = StrokeCap.Round)

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
            val hip = Offset(cx - 18.dp.toPx(), floorY - 34.dp.toPx())
            val shoulder = Offset(cx - 6.dp.toPx(), hip.y - 36.dp.toPx())
            drawLine(bodyColor, Offset(cx + 24.dp.toPx(), floorY - 26.dp.toPx()), hip, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, hip, shoulder, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x, shoulder.y - 14.dp.toPx()))
            drawCircle(muscleGlow, (11f + 4f * contractionGlow).dp.toPx(), Offset(shoulder.x - 4.dp.toPx(), shoulder.y + 10.dp.toPx()))

            val pulley = Offset(cx + 68.dp.toPx(), shoulder.y + 12.dp.toPx())
            drawCircle(machineColor, 6.dp.toPx(), pulley)

            val handX = shoulder.x + 8.dp.toPx() + 34.dp.toPx() * t
            val hand = Offset(handX, shoulder.y + 12.dp.toPx())
            val elbow = Offset(shoulder.x - 14.dp.toPx() * (1f - t) + 12.dp.toPx() * t, shoulder.y + 14.dp.toPx())
            drawLine(secondaryAccent.copy(alpha = 0.6f), pulley, hand, 2.dp.toPx())
            drawLine(bodyColor, shoulder, elbow, strokeLimb, cap = StrokeCap.Round)
            drawLine(jointColor, elbow, hand, strokeLimb, cap = StrokeCap.Round)
            drawCircle(primaryAccent, 5.dp.toPx(), hand)
        }

        MotionPattern.STRAIGHT_ARM_PULL -> {
            val hip = Offset(cx - 12.dp.toPx(), floorY - 42.dp.toPx())
            val shoulder = Offset(cx - 2.dp.toPx(), hip.y - 36.dp.toPx())
            drawLine(bodyColor, Offset(cx - 14.dp.toPx(), floorY), hip, strokeLimb)
            drawLine(bodyColor, hip, shoulder, strokeBody)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x + 2.dp.toPx(), shoulder.y - 14.dp.toPx()))
            drawCircle(muscleGlow, 10.dp.toPx(), Offset(shoulder.x - 4.dp.toPx(), shoulder.y + 14.dp.toPx()))

            val angle = (-0.2f + 1.3f * (1f - t)).toDouble()
            val armLen = 36.dp.toPx()
            val hand = Offset(shoulder.x + (cos(angle) * armLen).toFloat(), shoulder.y - (sin(angle) * armLen).toFloat())
            drawLine(secondaryAccent.copy(alpha = 0.5f), Offset(cx + 65.dp.toPx(), 18.dp.toPx()), hand, 1.8.dp.toPx())
            drawLine(jointColor, shoulder, hand, strokeLimb, cap = StrokeCap.Round)
            drawCircle(primaryAccent, 5.dp.toPx(), hand)
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
            drawCircle(primaryAccent, 7.dp.toPx(), hand)
        }

        MotionPattern.LATERAL_RAISE -> {
            val hip = Offset(cx, floorY - 42.dp.toPx())
            val shoulder = Offset(cx, hip.y - 38.dp.toPx())
            drawLine(bodyColor, Offset(cx - 10.dp.toPx(), floorY), hip, strokeLimb)
            drawLine(bodyColor, Offset(cx + 10.dp.toPx(), floorY), hip, strokeLimb)
            drawLine(bodyColor, hip, shoulder, strokeBody)
            drawCircle(jointColor, 7.dp.toPx(), Offset(cx, shoulder.y - 14.dp.toPx()))
            drawCircle(muscleGlow, 8.dp.toPx(), Offset(cx - 12.dp.toPx(), shoulder.y))
            drawCircle(muscleGlow, 8.dp.toPx(), Offset(cx + 12.dp.toPx(), shoulder.y))

            val lift = 1f - t
            val armLen = 34.dp.toPx()
            val angle = (0.15 + lift * 1.35)
            val dx = (sin(angle) * armLen).toFloat()
            val dy = (cos(angle) * armLen).toFloat()
            val lHand = Offset(cx - 10.dp.toPx() - dx, shoulder.y + dy)
            val rHand = Offset(cx + 10.dp.toPx() + dx, shoulder.y + dy)
            drawLine(jointColor, Offset(cx - 10.dp.toPx(), shoulder.y), lHand, strokeLimb, cap = StrokeCap.Round)
            drawLine(jointColor, Offset(cx + 10.dp.toPx(), shoulder.y), rHand, strokeLimb, cap = StrokeCap.Round)
            drawCircle(primaryAccent, 5.dp.toPx(), lHand)
            drawCircle(primaryAccent, 5.dp.toPx(), rHand)
        }

        MotionPattern.BICEP_CURL, MotionPattern.PREACHER_CURL -> {
            val hip = Offset(cx - 10.dp.toPx(), floorY - 42.dp.toPx())
            val shoulder = Offset(cx - 10.dp.toPx(), hip.y - 38.dp.toPx())
            val elbow = Offset(cx - 6.dp.toPx(), shoulder.y + 24.dp.toPx())
            drawLine(bodyColor, Offset(cx - 10.dp.toPx(), floorY), hip, strokeLimb)
            drawLine(bodyColor, hip, shoulder, strokeBody)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x, shoulder.y - 14.dp.toPx()))
            drawLine(bodyColor, shoulder, elbow, strokeLimb)
            drawCircle(muscleGlow, (7f + 4f * contractionGlow).dp.toPx(), Offset(shoulder.x + 5.dp.toPx(), shoulder.y + 12.dp.toPx()))

            val angle = (-1.1 + 2.3 * (1f - t))
            val forearmLen = 25.dp.toPx()
            val hand = Offset(elbow.x + (cos(angle) * forearmLen).toFloat(), elbow.y - (sin(angle) * forearmLen).toFloat())
            drawLine(jointColor, elbow, hand, strokeLimb, cap = StrokeCap.Round)
            drawCircle(primaryAccent, 6.dp.toPx(), hand)
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
            drawLine(secondaryAccent.copy(alpha = 0.5f), Offset(cx + 42.dp.toPx(), 16.dp.toPx()), hand, 1.5.dp.toPx())
            drawLine(jointColor, elbow, hand, strokeLimb, cap = StrokeCap.Round)
            drawCircle(primaryAccent, 5.5.dp.toPx(), hand)
        }

        MotionPattern.SQUAT, MotionPattern.LUNGE_SPLIT -> {
            val foot = Offset(cx, floorY)
            val knee = Offset(cx + 16.dp.toPx() * t, floorY - 34.dp.toPx() + 6.dp.toPx() * t)
            val hip = Offset(cx - 26.dp.toPx() * t, floorY - 68.dp.toPx() + 32.dp.toPx() * t)
            val shoulder = Offset(hip.x + 14.dp.toPx() * t, hip.y - 36.dp.toPx() + 4.dp.toPx() * t)

            drawLine(bodyColor, foot, knee, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, knee, hip, strokeLimb, cap = StrokeCap.Round)
            drawLine(bodyColor, hip, shoulder, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x + 2.dp.toPx(), shoulder.y - 13.dp.toPx()))
            drawCircle(muscleGlow, (9f + 4f * contractionGlow).dp.toPx(), Offset((hip.x + knee.x) / 2f + 4.dp.toPx(), (hip.y + knee.y) / 2f))
            drawCircle(secondaryAccent, 12.dp.toPx(), shoulder)
            drawCircle(primaryAccent, 6.dp.toPx(), shoulder)
        }

        MotionPattern.LEG_PRESS -> {
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
            drawLine(primaryAccent, Offset(foot.x - 8.dp.toPx(), foot.y - 14.dp.toPx()), Offset(foot.x + 10.dp.toPx(), foot.y + 12.dp.toPx()), 6.dp.toPx(), cap = StrokeCap.Round)
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
            drawCircle(primaryAccent, 6.dp.toPx(), ankle)

            val glowPos = if (isExtension) Offset(cx + 2.dp.toPx(), hip.y - 5.dp.toPx()) else Offset(cx + 2.dp.toPx(), hip.y + 6.dp.toPx())
            drawCircle(muscleGlow, (9f + 3f * contractionGlow).dp.toPx(), glowPos)
        }

        MotionPattern.RDL_HINGE, MotionPattern.DEADLIFT -> {
            val foot = Offset(cx, floorY)
            val knee = Offset(cx + 4.dp.toPx(), floorY - 34.dp.toPx())
            val hip = Offset(cx - 18.dp.toPx() * t, floorY - 68.dp.toPx() + 6.dp.toPx() * t)
            val torsoAngle = 1.45 - 1.25 * t
            val torsoLen = 40.dp.toPx()
            val shoulder = Offset(hip.x + (cos(torsoAngle) * torsoLen).toFloat(), hip.y - (sin(torsoAngle) * torsoLen).toFloat())

            drawLine(bodyColor, foot, knee, strokeLimb)
            drawLine(bodyColor, knee, hip, strokeLimb)
            drawLine(bodyColor, hip, shoulder, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), Offset(shoulder.x + 8.dp.toPx(), shoulder.y - 8.dp.toPx()))
            drawCircle(muscleGlow, 9.dp.toPx(), Offset(hip.x - 4.dp.toPx(), hip.y + 14.dp.toPx()))

            val bar = Offset(shoulder.x, shoulder.y + 34.dp.toPx())
            drawLine(jointColor, shoulder, bar, strokeLimb)
            drawCircle(secondaryAccent, 11.dp.toPx(), bar)
            drawCircle(primaryAccent, 5.5.dp.toPx(), bar)
        }

        MotionPattern.HIP_THRUST -> {
            val benchTop = Offset(cx - 32.dp.toPx(), floorY - 32.dp.toPx())
            drawLine(machineColor, Offset(cx - 42.dp.toPx(), floorY), benchTop, 6.dp.toPx())
            val foot = Offset(cx + 38.dp.toPx(), floorY)
            val knee = Offset(cx + 32.dp.toPx(), floorY - 32.dp.toPx())
            val hip = Offset(cx + 2.dp.toPx(), floorY - 32.dp.toPx() + 20.dp.toPx() * t)

            drawLine(bodyColor, foot, knee, strokeLimb)
            drawLine(bodyColor, knee, hip, strokeLimb)
            drawLine(bodyColor, hip, benchTop, strokeBody, cap = StrokeCap.Round)
            drawCircle(jointColor, 7.dp.toPx(), Offset(benchTop.x - 10.dp.toPx(), benchTop.y - 6.dp.toPx()))
            drawCircle(muscleGlow, (10f + 4f * contractionGlow).dp.toPx(), Offset(hip.x, hip.y + 5.dp.toPx()))
            drawCircle(secondaryAccent, 11.dp.toPx(), Offset(hip.x, hip.y - 5.dp.toPx()))
            drawCircle(primaryAccent, 5.5.dp.toPx(), Offset(hip.x, hip.y - 5.dp.toPx()))
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
