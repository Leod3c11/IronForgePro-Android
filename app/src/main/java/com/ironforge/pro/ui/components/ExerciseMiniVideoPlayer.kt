package com.ironforge.pro.ui.components

import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

@Composable
fun ExerciseMiniVideoPlayer(
    exercise: Exercise,
    customVideoUri: String? = null,
    height: Dp = 185.dp,
    compact: Boolean = false,
    onAttachVideoClick: (() -> Unit)? = null
) {
    val palette = LocalIronPalette.current
    var isPlaying by remember { mutableStateOf(true) }
    var slowMotion by remember { mutableStateOf(false) }
    var useCustomMp4 by remember(customVideoUri) { mutableStateOf(customVideoUri != null) }
    var manualProgress by remember { mutableFloatStateOf(0.25f) }
    var repCounter by remember(exercise.id) { mutableIntStateOf(1) }
    var lastRawT by remember(exercise.id) { mutableFloatStateOf(0f) }

    val cycleDuration = if (slowMotion) 5400 else 2900
    val infiniteTransition = rememberInfiniteTransition(label = "exerciseStudioMotion")
    val animatedCycle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = cycleDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cycleProgress"
    )

    val rawT = if (isPlaying) {
        manualProgress = animatedCycle
        if (animatedCycle < 0.15f && lastRawT > 0.85f) {
            repCounter += 1
        }
        lastRawT = animatedCycle
        animatedCycle
    } else {
        manualProgress
    }

    // 0.00..0.44 = Eccentric (controlled stretch)
    // 0.44..0.54 = Bottom Stretch Pause
    // 0.54..0.88 = Concentric (explosive contraction)
    // 0.88..1.00 = Peak Contraction Squeeze
    val (repFactor, phaseLabel, phaseColor, isConcentric) = remember(rawT, palette) {
        when {
            rawT < 0.44f -> {
                val p = rawT / 0.44f
                val smooth = (1f - cos(p * PI.toFloat())) / 2f
                Quadruple(smooth, "EXCÊNTRICA", palette.secondary, false)
            }
            rawT < 0.54f -> {
                Quadruple(1f, "ALONGAMENTO", HyperAmber, false)
            }
            rawT < 0.88f -> {
                val p = (rawT - 0.54f) / 0.34f
                val smooth = 1f - ((1f - cos(p * PI.toFloat())) / 2f)
                Quadruple(smooth, "CONCÊNTRICA", palette.primary, true)
            }
            else -> {
                Quadruple(0f, "PICO CONTR.", HyperEmerald, true)
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFF070A10),
        border = BorderStroke(1.2.dp, palette.hairline)
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
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures { _, dragAmount ->
                                isPlaying = false
                                val delta = dragAmount / size.width.coerceAtLeast(1)
                                manualProgress = (manualProgress + delta).mod(1f)
                            }
                        }
                ) {
                    drawStudioEnvironment(palette.primary, palette.secondary)
                    drawSculptedBiomechanicalAthlete(
                        pattern = exercise.motionPattern,
                        equipment = exercise.equipment,
                        t = repFactor,
                        contractionGlow = 1f - repFactor,
                        isConcentric = isConcentric,
                        primaryAccent = palette.primary,
                        secondaryAccent = palette.secondary
                    )
                }
            }

            // Top Studio HUD Bar
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
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xCC090C12))
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
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                color = phaseColor,
                                fontSize = 10.sp
                            )
                        }
                    }

                    if (!useCustomMp4) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xCC090C12))
                                .padding(horizontal = 7.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (compact) "TUT ${exercise.tempoTUT}" else "REP $repCounter • TUT ${exercise.tempoTUT}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFD0DCF0),
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    if (customVideoUri != null) {
                        SmallHudPill(
                            text = if (useCustomMp4) "3D Studio" else "MP4",
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
                            accent = if (slowMotion) palette.tertiary else Color(0xFFF5F8FF),
                            onClick = { slowMotion = !slowMotion }
                        )

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xD9151A24))
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

            // Bottom Biomechanical Telemetry & Interactive Timeline Scrubber
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
                            text = "${exercise.motionPattern.biomechLabel} • ${exercise.equipment.label}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFA2B0C8),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "Tensão Alvo: ${((1f - repFactor) * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = palette.primaryGlow,
                            fontSize = 10.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
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

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
private fun SmallHudPill(
    text: String,
    accent: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xD9151A24))
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

private fun DrawScope.drawStudioEnvironment(primary: Color, secondary: Color) {
    // 1. Studio Spotlight Halo
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                primary.copy(alpha = 0.16f),
                secondary.copy(alpha = 0.06f),
                Color.Transparent
            ),
            center = Offset(size.width * 0.5f, size.height * 0.50f),
            radius = size.minDimension * 0.78f
        ),
        center = Offset(size.width * 0.5f, size.height * 0.50f),
        radius = size.minDimension * 0.78f
    )

    // 2. Subtle Architectural Telemetry Grid Lines
    val gridColor = Color(0xFF1D2536).copy(alpha = 0.45f)
    val stepX = size.width / 8f
    val stepY = size.height / 5f
    for (i in 1..7) {
        drawLine(gridColor, Offset(stepX * i, 12.dp.toPx()), Offset(stepX * i, size.height * 0.81f), 0.8.dp.toPx())
    }
    for (j in 1..3) {
        drawLine(gridColor, Offset(size.width * 0.10f, stepY * j), Offset(size.width * 0.90f, stepY * j), 0.8.dp.toPx())
    }

    // 3. Studio Floor Plane + Ambient Shadow
    val floorY = size.height * 0.81f
    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xAA000000), Color.Transparent),
            center = Offset(size.width * 0.5f, floorY + 2.dp.toPx()),
            radius = 65.dp.toPx()
        ),
        topLeft = Offset(size.width * 0.5f - 65.dp.toPx(), floorY - 5.dp.toPx()),
        size = Size(130.dp.toPx(), 14.dp.toPx())
    )
    drawLine(
        brush = Brush.horizontalGradient(
            listOf(Color.Transparent, Color(0xFF3A4660), Color.Transparent)
        ),
        start = Offset(size.width * 0.08f, floorY),
        end = Offset(size.width * 0.92f, floorY),
        strokeWidth = 1.8.dp.toPx()
    )
}

/**
 * Draws an anatomically sculpted limb segment with depth shading and specular rim highlight
 */
private fun DrawScope.drawSculptedSegment(
    start: Offset,
    end: Offset,
    thickness: Float,
    baseColor: Color,
    highlightColor: Color
) {
    // Drop shadow outline
    drawLine(
        color = Color(0xAA05070B),
        start = start + Offset(0f, 2f),
        end = end + Offset(0f, 2f),
        strokeWidth = thickness + 2.5f,
        cap = StrokeCap.Round
    )
    // Main sculpted muscle/limb body
    drawLine(
        color = baseColor,
        start = start,
        end = end,
        strokeWidth = thickness,
        cap = StrokeCap.Round
    )
    // Specular anatomical contour highlight
    drawLine(
        color = highlightColor.copy(alpha = 0.55f),
        start = start,
        end = end,
        strokeWidth = thickness * 0.34f,
        cap = StrokeCap.Round
    )
}

/**
 * Draws an articulated anatomical joint cap
 */
private fun DrawScope.drawJointNode(center: Offset, radius: Float, accent: Color) {
    drawCircle(color = Color(0xFF0A0E16), radius = radius + 1.5f, center = center)
    drawCircle(color = Color(0xFFCBD7EC), radius = radius, center = center)
    drawCircle(color = accent, radius = radius * 0.45f, center = center)
}

/**
 * Draws an illuminated target muscle belly with concentric physiological heat glow
 */
private fun DrawScope.drawMuscleHeatmap(
    center: Offset,
    radiusDp: Float,
    glowIntensity: Float,
    primaryAccent: Color,
    secondaryAccent: Color
) {
    val r = (radiusDp + 5f * glowIntensity).dp.toPx()
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                primaryAccent.copy(alpha = (0.55f + 0.40f * glowIntensity).coerceIn(0f, 0.95f)),
                secondaryAccent.copy(alpha = 0.25f * glowIntensity),
                Color.Transparent
            ),
            center = center,
            radius = r * 1.45f
        ),
        center = center,
        radius = r * 1.45f
    )
    drawCircle(
        color = primaryAccent.copy(alpha = 0.85f),
        radius = (radiusDp * 0.45f).dp.toPx(),
        center = center
    )
}

/**
 * Draws realistic Olympic Barbell / Dumbbell / Cable Attachment at hand position
 */
private fun DrawScope.drawEquipmentLoad(
    center: Offset,
    equipment: EquipmentType,
    primaryAccent: Color,
    secondaryAccent: Color,
    barWidthDp: Float = 0f
) {
    when (equipment) {
        EquipmentType.BARBELL -> {
            if (barWidthDp > 0f) {
                val halfW = (barWidthDp / 2f).dp.toPx()
                drawLine(Color(0xFFE2EAF8), Offset(center.x - halfW, center.y), Offset(center.x + halfW, center.y), 4.dp.toPx(), cap = StrokeCap.Round)
                for (side in listOf(-1f, 1f)) {
                    val px = center.x + side * (halfW - 6.dp.toPx())
                    drawRoundRect(secondaryAccent, Offset(px - 3.dp.toPx(), center.y - 13.dp.toPx()), Size(6.dp.toPx(), 26.dp.toPx()), CornerRadius(3f, 3f))
                    drawRoundRect(primaryAccent, Offset(px + side * 5.dp.toPx() - 2.5.dp.toPx(), center.y - 10.dp.toPx()), Size(5.dp.toPx(), 20.dp.toPx()), CornerRadius(2f, 2f))
                }
            } else {
                // Side-view Olympic bumper plate stack
                drawCircle(Color(0xFF0A0E16), 14.5.dp.toPx(), center)
                drawCircle(secondaryAccent, 13.dp.toPx(), center)
                drawCircle(Color(0xFF151C28), 9.5.dp.toPx(), center)
                drawCircle(primaryAccent, 7.5.dp.toPx(), center)
                drawCircle(Color(0xFFE5EEFF), 3.dp.toPx(), center)
            }
        }
        EquipmentType.DUMBBELL -> {
            drawRoundRect(
                color = secondaryAccent,
                topLeft = Offset(center.x - 11.dp.toPx(), center.y - 6.dp.toPx()),
                size = Size(22.dp.toPx(), 12.dp.toPx()),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            drawRoundRect(
                color = primaryAccent,
                topLeft = Offset(center.x - 7.dp.toPx(), center.y - 4.5.dp.toPx()),
                size = Size(14.dp.toPx(), 9.dp.toPx()),
                cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
            )
        }
        EquipmentType.CABLE, EquipmentType.MACHINE -> {
            drawCircle(Color(0xFF0A0E16), 7.dp.toPx(), center)
            drawCircle(primaryAccent, 5.5.dp.toPx(), center)
            drawCircle(Color.White, 2.dp.toPx(), center)
        }
        EquipmentType.BODYWEIGHT -> {
            drawCircle(primaryAccent, 4.5.dp.toPx(), center)
        }
    }
}

/**
 * Draws dashed biomechanical path guide + directional arrow
 */
private fun DrawScope.drawMotionPathGuide(
    topPoint: Offset,
    bottomPoint: Offset,
    isConcentric: Boolean,
    primaryAccent: Color,
    secondaryAccent: Color
) {
    val activeCol = if (isConcentric) primaryAccent else secondaryAccent
    drawLine(
        color = activeCol.copy(alpha = 0.42f),
        start = topPoint,
        end = bottomPoint,
        strokeWidth = 2.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
    )
    val tip = if (isConcentric) topPoint else bottomPoint
    val dir = if (isConcentric) -1f else 1f
    val arrowSize = 5.dp.toPx()
    val path = Path().apply {
        moveTo(tip.x, tip.y)
        lineTo(tip.x - arrowSize, tip.y - dir * arrowSize)
        lineTo(tip.x + arrowSize, tip.y - dir * arrowSize)
        close()
    }
    drawPath(path, color = activeCol)
}

private fun DrawScope.drawSculptedBiomechanicalAthlete(
    pattern: MotionPattern,
    equipment: EquipmentType,
    t: Float,
    contractionGlow: Float,
    isConcentric: Boolean,
    primaryAccent: Color,
    secondaryAccent: Color
) {
    val cx = size.width * 0.50f
    val floorY = size.height * 0.81f
    val limbBase = Color(0xFF7B8CA8)
    val limbHi = Color(0xFFD6E2F8)
    val torsoBase = Color(0xFF637491)
    val torsoHi = Color(0xFFB8C8E4)
    val benchMetal = Color(0xFF333E54)
    val benchPad = Color(0xFF1F2737)
    val torsoW = 10.dp.toPx()
    val thighW = 8.dp.toPx()
    val armW = 6.5.dp.toPx()

    when (pattern) {
        MotionPattern.BENCH_PRESS, MotionPattern.INCLINE_PRESS -> {
            val isIncline = pattern == MotionPattern.INCLINE_PRESS
            val benchStart = Offset(cx - 56.dp.toPx(), floorY - 22.dp.toPx())
            val benchEnd = Offset(cx + 48.dp.toPx(), if (isIncline) floorY - 48.dp.toPx() else floorY - 22.dp.toPx())
            drawLine(benchMetal, Offset(cx - 42.dp.toPx(), floorY), Offset(cx - 42.dp.toPx(), benchStart.y), 4.dp.toPx())
            drawLine(benchMetal, Offset(cx + 32.dp.toPx(), floorY), Offset(cx + 32.dp.toPx(), benchEnd.y - 4.dp.toPx()), 4.dp.toPx())
            drawLine(benchPad, benchStart, benchEnd, 8.dp.toPx(), cap = StrokeCap.Round)

            val hip = Offset(cx - 22.dp.toPx(), benchStart.y - 6.dp.toPx())
            val shoulder = Offset(cx + 20.dp.toPx(), if (isIncline) benchEnd.y - 9.dp.toPx() else benchStart.y - 6.dp.toPx())
            val head = Offset(shoulder.x + 15.dp.toPx(), shoulder.y - (if (isIncline) 6.dp.toPx() else 1.dp.toPx()))
            val knee = Offset(hip.x - 24.dp.toPx(), hip.y + 4.dp.toPx())
            val foot = Offset(knee.x - 6.dp.toPx(), floorY)

            drawSculptedSegment(foot, knee, armW, limbBase, limbHi)
            drawSculptedSegment(knee, hip, thighW, limbBase, limbHi)
            drawSculptedSegment(hip, shoulder, torsoW, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), head)

            // Active Pectoral Muscle Heatmap
            drawMuscleHeatmap(Offset(shoulder.x - 6.dp.toPx(), shoulder.y - 5.dp.toPx()), 11f, contractionGlow, primaryAccent, secondaryAccent)

            val topHand = Offset(shoulder.x - 2.dp.toPx(), shoulder.y - 46.dp.toPx())
            val botHand = Offset(shoulder.x - 2.dp.toPx(), shoulder.y - 14.dp.toPx())
            drawMotionPathGuide(topHand, botHand, isConcentric, primaryAccent, secondaryAccent)

            val handY = topHand.y + (botHand.y - topHand.y) * t
            val hand = Offset(shoulder.x - 2.dp.toPx(), handY)
            val elbow = Offset(shoulder.x + 13.dp.toPx() * t, (shoulder.y + handY) / 2f + 4.dp.toPx() * t)
            drawSculptedSegment(shoulder, elbow, armW, limbBase, limbHi)
            drawSculptedSegment(elbow, hand, armW * 0.85f, limbBase, limbHi)
            drawJointNode(shoulder, 3.5.dp.toPx(), primaryAccent)
            drawJointNode(elbow, 3.dp.toPx(), secondaryAccent)
            drawEquipmentLoad(hand, equipment, primaryAccent, secondaryAccent)
        }

        MotionPattern.FLY_CROSSOVER, MotionPattern.REVERSE_FLY -> {
            val hip = Offset(cx, floorY - 38.dp.toPx())
            val chest = Offset(cx, floorY - 76.dp.toPx())
            val head = Offset(cx, chest.y - 16.dp.toPx())

            drawSculptedSegment(Offset(cx - 12.dp.toPx(), floorY), hip, thighW, limbBase, limbHi)
            drawSculptedSegment(Offset(cx + 12.dp.toPx(), floorY), hip, thighW, limbBase, limbHi)
            drawSculptedSegment(hip, chest, torsoW * 1.15f, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), head)
            drawMuscleHeatmap(chest, 13f, contractionGlow, primaryAccent, secondaryAccent)

            val spreadX = (10f + 44f * t).dp.toPx()
            val spreadY = chest.y + (8f - 10f * t).dp.toPx()
            val leftHand = Offset(cx - spreadX, spreadY)
            val rightHand = Offset(cx + spreadX, spreadY)

            if (equipment == EquipmentType.CABLE) {
                drawLine(secondaryAccent.copy(alpha = 0.55f), Offset(cx - 76.dp.toPx(), 16.dp.toPx()), leftHand, 1.8.dp.toPx())
                drawLine(secondaryAccent.copy(alpha = 0.55f), Offset(cx + 76.dp.toPx(), 16.dp.toPx()), rightHand, 1.8.dp.toPx())
            }
            drawSculptedSegment(Offset(cx - 10.dp.toPx(), chest.y), leftHand, armW, limbBase, limbHi)
            drawSculptedSegment(Offset(cx + 10.dp.toPx(), chest.y), rightHand, armW, limbBase, limbHi)
            drawEquipmentLoad(leftHand, equipment, primaryAccent, secondaryAccent)
            drawEquipmentLoad(rightHand, equipment, primaryAccent, secondaryAccent)
        }

        MotionPattern.DIPS -> {
            drawLine(benchMetal, Offset(cx - 38.dp.toPx(), floorY - 52.dp.toPx()), Offset(cx + 38.dp.toPx(), floorY - 52.dp.toPx()), 5.dp.toPx(), cap = StrokeCap.Round)
            val dropY = 22.dp.toPx() * t
            val hand = Offset(cx, floorY - 52.dp.toPx())
            val shoulder = Offset(cx + 10.dp.toPx(), hand.y - 26.dp.toPx() + dropY)
            val hip = Offset(cx - 4.dp.toPx(), shoulder.y + 32.dp.toPx())
            val knee = Offset(hip.x - 10.dp.toPx(), hip.y + 22.dp.toPx())
            val elbow = Offset(shoulder.x - 14.dp.toPx() * t, (shoulder.y + hand.y) / 2f)

            drawSculptedSegment(knee, hip, thighW, limbBase, limbHi)
            drawSculptedSegment(hip, shoulder, torsoW, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), Offset(shoulder.x + 6.dp.toPx(), shoulder.y - 14.dp.toPx()))
            drawMuscleHeatmap(Offset(shoulder.x - 2.dp.toPx(), shoulder.y + 6.dp.toPx()), 11f, contractionGlow, primaryAccent, secondaryAccent)
            drawSculptedSegment(shoulder, elbow, armW, limbBase, limbHi)
            drawSculptedSegment(elbow, hand, armW * 0.85f, limbBase, limbHi)
            drawJointNode(elbow, 3.dp.toPx(), secondaryAccent)
        }

        MotionPattern.LAT_PULLDOWN, MotionPattern.PULL_UP -> {
            val isPullUp = pattern == MotionPattern.PULL_UP
            val barBaseY = 22.dp.toPx()
            val hip = Offset(cx, floorY - 34.dp.toPx())
            val bodyLift = if (isPullUp) 26.dp.toPx() * (1f - t) else 0f
            val currentHip = Offset(cx, hip.y - bodyLift)
            val shoulder = Offset(cx, currentHip.y - 38.dp.toPx())

            if (!isPullUp) {
                drawLine(benchPad, Offset(cx - 22.dp.toPx(), floorY - 28.dp.toPx()), Offset(cx + 22.dp.toPx(), floorY - 28.dp.toPx()), 7.dp.toPx(), cap = StrokeCap.Round)
                drawSculptedSegment(Offset(cx + 22.dp.toPx(), floorY), Offset(cx + 22.dp.toPx(), floorY - 28.dp.toPx()), thighW, limbBase, limbHi)
            } else {
                drawSculptedSegment(Offset(cx - 6.dp.toPx(), currentHip.y + 30.dp.toPx()), currentHip, thighW, limbBase, limbHi)
            }

            drawSculptedSegment(currentHip, shoulder, torsoW * 1.15f, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), Offset(cx, shoulder.y - 14.dp.toPx()))
            drawMuscleHeatmap(Offset(cx, shoulder.y + 12.dp.toPx()), 13f, contractionGlow, primaryAccent, secondaryAccent)

            val barY = if (isPullUp) barBaseY else (shoulder.y - 6.dp.toPx() - 36.dp.toPx() * t)
            if (!isPullUp) {
                drawLine(secondaryAccent.copy(alpha = 0.65f), Offset(cx, 8.dp.toPx()), Offset(cx, barY), 2.dp.toPx())
            }
            drawLine(primaryAccent, Offset(cx - 38.dp.toPx(), barY), Offset(cx + 38.dp.toPx(), barY), 4.5.dp.toPx(), cap = StrokeCap.Round)

            val lHand = Offset(cx - 28.dp.toPx(), barY)
            val rHand = Offset(cx + 28.dp.toPx(), barY)
            val lElbow = Offset(cx - 30.dp.toPx(), (shoulder.y + barY) / 2f + 6.dp.toPx() * (1f - t))
            val rElbow = Offset(cx + 30.dp.toPx(), (shoulder.y + barY) / 2f + 6.dp.toPx() * (1f - t))
            drawSculptedSegment(Offset(cx - 9.dp.toPx(), shoulder.y), lElbow, armW, limbBase, limbHi)
            drawSculptedSegment(lElbow, lHand, armW * 0.85f, limbBase, limbHi)
            drawSculptedSegment(Offset(cx + 9.dp.toPx(), shoulder.y), rElbow, armW, limbBase, limbHi)
            drawSculptedSegment(rElbow, rHand, armW * 0.85f, limbBase, limbHi)
            drawJointNode(lElbow, 3.dp.toPx(), secondaryAccent)
            drawJointNode(rElbow, 3.dp.toPx(), secondaryAccent)
        }

        MotionPattern.ROW_HORIZONTAL -> {
            val hip = Offset(cx - 18.dp.toPx(), floorY - 34.dp.toPx())
            val shoulder = Offset(cx - 6.dp.toPx(), hip.y - 36.dp.toPx())
            drawSculptedSegment(Offset(cx + 24.dp.toPx(), floorY - 26.dp.toPx()), hip, thighW, limbBase, limbHi)
            drawSculptedSegment(hip, shoulder, torsoW, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), Offset(shoulder.x, shoulder.y - 14.dp.toPx()))
            drawMuscleHeatmap(Offset(shoulder.x - 4.dp.toPx(), shoulder.y + 10.dp.toPx()), 12f, contractionGlow, primaryAccent, secondaryAccent)

            val pulley = Offset(cx + 72.dp.toPx(), shoulder.y + 12.dp.toPx())
            drawCircle(benchMetal, 7.dp.toPx(), pulley)

            val handX = shoulder.x + 8.dp.toPx() + 36.dp.toPx() * t
            val hand = Offset(handX, shoulder.y + 12.dp.toPx())
            val elbow = Offset(shoulder.x - 14.dp.toPx() * (1f - t) + 12.dp.toPx() * t, shoulder.y + 14.dp.toPx())
            drawLine(secondaryAccent.copy(alpha = 0.65f), pulley, hand, 2.dp.toPx())
            drawSculptedSegment(shoulder, elbow, armW, limbBase, limbHi)
            drawSculptedSegment(elbow, hand, armW * 0.85f, limbBase, limbHi)
            drawJointNode(elbow, 3.dp.toPx(), secondaryAccent)
            drawEquipmentLoad(hand, equipment, primaryAccent, secondaryAccent)
        }

        MotionPattern.STRAIGHT_ARM_PULL -> {
            val hip = Offset(cx - 12.dp.toPx(), floorY - 42.dp.toPx())
            val shoulder = Offset(cx - 2.dp.toPx(), hip.y - 36.dp.toPx())
            drawSculptedSegment(Offset(cx - 14.dp.toPx(), floorY), hip, thighW, limbBase, limbHi)
            drawSculptedSegment(hip, shoulder, torsoW, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), Offset(shoulder.x + 2.dp.toPx(), shoulder.y - 14.dp.toPx()))
            drawMuscleHeatmap(Offset(shoulder.x - 4.dp.toPx(), shoulder.y + 14.dp.toPx()), 11f, contractionGlow, primaryAccent, secondaryAccent)

            val angle = (-0.2f + 1.3f * (1f - t)).toDouble()
            val armLen = 36.dp.toPx()
            val hand = Offset(shoulder.x + (cos(angle) * armLen).toFloat(), shoulder.y - (sin(angle) * armLen).toFloat())
            drawLine(secondaryAccent.copy(alpha = 0.55f), Offset(cx + 68.dp.toPx(), 18.dp.toPx()), hand, 1.8.dp.toPx())
            drawSculptedSegment(shoulder, hand, armW, limbBase, limbHi)
            drawEquipmentLoad(hand, equipment, primaryAccent, secondaryAccent)
        }

        MotionPattern.OVERHEAD_PRESS -> {
            val hip = Offset(cx, floorY - 34.dp.toPx())
            val shoulder = Offset(cx, hip.y - 38.dp.toPx())
            drawLine(benchPad, Offset(cx - 16.dp.toPx(), floorY), Offset(cx - 16.dp.toPx(), shoulder.y - 6.dp.toPx()), 7.dp.toPx(), cap = StrokeCap.Round)
            drawSculptedSegment(Offset(cx + 20.dp.toPx(), floorY), hip, thighW, limbBase, limbHi)
            drawSculptedSegment(hip, shoulder, torsoW, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), Offset(cx, shoulder.y - 14.dp.toPx()))
            drawMuscleHeatmap(shoulder, 11f, contractionGlow, primaryAccent, secondaryAccent)

            val topHand = Offset(cx + 4.dp.toPx(), shoulder.y - 38.dp.toPx())
            val botHand = Offset(cx + 4.dp.toPx(), shoulder.y - 6.dp.toPx())
            drawMotionPathGuide(topHand, botHand, isConcentric, primaryAccent, secondaryAccent)

            val handY = topHand.y + (botHand.y - topHand.y) * t
            val hand = Offset(cx + 4.dp.toPx(), handY)
            val elbow = Offset(cx + 16.dp.toPx() * t, (shoulder.y + handY) / 2f + 6.dp.toPx() * t)
            drawSculptedSegment(shoulder, elbow, armW, limbBase, limbHi)
            drawSculptedSegment(elbow, hand, armW * 0.85f, limbBase, limbHi)
            drawJointNode(elbow, 3.dp.toPx(), secondaryAccent)
            drawEquipmentLoad(hand, equipment, primaryAccent, secondaryAccent)
        }

        MotionPattern.LATERAL_RAISE -> {
            val hip = Offset(cx, floorY - 42.dp.toPx())
            val shoulder = Offset(cx, hip.y - 38.dp.toPx())
            drawSculptedSegment(Offset(cx - 10.dp.toPx(), floorY), hip, thighW, limbBase, limbHi)
            drawSculptedSegment(Offset(cx + 10.dp.toPx(), floorY), hip, thighW, limbBase, limbHi)
            drawSculptedSegment(hip, shoulder, torsoW * 1.1f, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), Offset(cx, shoulder.y - 14.dp.toPx()))
            drawMuscleHeatmap(Offset(cx - 12.dp.toPx(), shoulder.y), 9f, contractionGlow, primaryAccent, secondaryAccent)
            drawMuscleHeatmap(Offset(cx + 12.dp.toPx(), shoulder.y), 9f, contractionGlow, primaryAccent, secondaryAccent)

            val lift = 1f - t
            val armLen = 35.dp.toPx()
            val angle = (0.15 + lift * 1.38)
            val dx = (sin(angle) * armLen).toFloat()
            val dy = (cos(angle) * armLen).toFloat()
            val lHand = Offset(cx - 10.dp.toPx() - dx, shoulder.y + dy)
            val rHand = Offset(cx + 10.dp.toPx() + dx, shoulder.y + dy)
            drawSculptedSegment(Offset(cx - 10.dp.toPx(), shoulder.y), lHand, armW, limbBase, limbHi)
            drawSculptedSegment(Offset(cx + 10.dp.toPx(), shoulder.y), rHand, armW, limbBase, limbHi)
            drawEquipmentLoad(lHand, equipment, primaryAccent, secondaryAccent)
            drawEquipmentLoad(rHand, equipment, primaryAccent, secondaryAccent)
        }

        MotionPattern.BICEP_CURL, MotionPattern.PREACHER_CURL -> {
            val hip = Offset(cx - 10.dp.toPx(), floorY - 42.dp.toPx())
            val shoulder = Offset(cx - 10.dp.toPx(), hip.y - 38.dp.toPx())
            val elbow = Offset(cx - 6.dp.toPx(), shoulder.y + 24.dp.toPx())
            drawSculptedSegment(Offset(cx - 10.dp.toPx(), floorY), hip, thighW, limbBase, limbHi)
            drawSculptedSegment(hip, shoulder, torsoW, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), Offset(shoulder.x, shoulder.y - 14.dp.toPx()))
            drawSculptedSegment(shoulder, elbow, armW, limbBase, limbHi)
            drawMuscleHeatmap(Offset(shoulder.x + 5.dp.toPx(), shoulder.y + 12.dp.toPx()), 9f, contractionGlow, primaryAccent, secondaryAccent)

            val angle = (-1.1 + 2.3 * (1f - t))
            val forearmLen = 26.dp.toPx()
            val hand = Offset(elbow.x + (cos(angle) * forearmLen).toFloat(), elbow.y - (sin(angle) * forearmLen).toFloat())
            drawSculptedSegment(elbow, hand, armW * 0.85f, limbBase, limbHi)
            drawJointNode(elbow, 3.dp.toPx(), secondaryAccent)
            drawEquipmentLoad(hand, equipment, primaryAccent, secondaryAccent)
        }

        MotionPattern.TRICEP_PUSHDOWN, MotionPattern.OVERHEAD_TRICEP, MotionPattern.SKULLCRUSHER -> {
            val hip = Offset(cx - 14.dp.toPx(), floorY - 42.dp.toPx())
            val shoulder = Offset(cx - 10.dp.toPx(), hip.y - 38.dp.toPx())
            drawSculptedSegment(Offset(cx - 14.dp.toPx(), floorY), hip, thighW, limbBase, limbHi)
            drawSculptedSegment(hip, shoulder, torsoW, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), Offset(shoulder.x, shoulder.y - 14.dp.toPx()))

            val isOverhead = pattern == MotionPattern.OVERHEAD_TRICEP
            val elbow = if (isOverhead) Offset(shoulder.x + 4.dp.toPx(), shoulder.y - 22.dp.toPx())
                        else Offset(shoulder.x - 4.dp.toPx(), shoulder.y + 22.dp.toPx())
            drawSculptedSegment(shoulder, elbow, armW, limbBase, limbHi)
            drawMuscleHeatmap(Offset(shoulder.x - 5.dp.toPx(), (shoulder.y + elbow.y) / 2f), 9f, contractionGlow, primaryAccent, secondaryAccent)

            val forearmLen = 25.dp.toPx()
            val hand = if (isOverhead) {
                Offset(elbow.x + 18.dp.toPx() * (1f - t) - 10.dp.toPx() * t, elbow.y - 20.dp.toPx() * (1f - t) + 12.dp.toPx() * t)
            } else {
                Offset(elbow.x + 6.dp.toPx() + 14.dp.toPx() * t, elbow.y + forearmLen * (1f - 0.85f * t))
            }
            if (equipment == EquipmentType.CABLE) {
                drawLine(secondaryAccent.copy(alpha = 0.55f), Offset(cx + 44.dp.toPx(), 16.dp.toPx()), hand, 1.8.dp.toPx())
            }
            drawSculptedSegment(elbow, hand, armW * 0.85f, limbBase, limbHi)
            drawJointNode(elbow, 3.dp.toPx(), secondaryAccent)
            drawEquipmentLoad(hand, equipment, primaryAccent, secondaryAccent)
        }

        MotionPattern.SQUAT, MotionPattern.LUNGE_SPLIT -> {
            val foot = Offset(cx, floorY)
            val knee = Offset(cx + 16.dp.toPx() * t, floorY - 34.dp.toPx() + 6.dp.toPx() * t)
            val hip = Offset(cx - 26.dp.toPx() * t, floorY - 68.dp.toPx() + 32.dp.toPx() * t)
            val shoulder = Offset(hip.x + 14.dp.toPx() * t, hip.y - 36.dp.toPx() + 4.dp.toPx() * t)

            drawMotionPathGuide(Offset(cx, floorY - 104.dp.toPx()), Offset(cx - 12.dp.toPx(), floorY - 68.dp.toPx()), isConcentric, primaryAccent, secondaryAccent)
            drawSculptedSegment(foot, knee, armW, limbBase, limbHi)
            drawSculptedSegment(knee, hip, thighW * 1.15f, limbBase, limbHi)
            drawSculptedSegment(hip, shoulder, torsoW, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), Offset(shoulder.x + 2.dp.toPx(), shoulder.y - 13.dp.toPx()))
            drawMuscleHeatmap(Offset((hip.x + knee.x) / 2f + 4.dp.toPx(), (hip.y + knee.y) / 2f), 11f, contractionGlow, primaryAccent, secondaryAccent)
            drawJointNode(knee, 3.5.dp.toPx(), secondaryAccent)
            drawJointNode(hip, 3.5.dp.toPx(), primaryAccent)
            drawEquipmentLoad(shoulder, equipment, primaryAccent, secondaryAccent)
        }

        MotionPattern.LEG_PRESS -> {
            val seat = Offset(cx - 32.dp.toPx(), floorY - 24.dp.toPx())
            val shoulder = Offset(cx - 48.dp.toPx(), floorY - 46.dp.toPx())
            drawLine(benchPad, shoulder, seat, 8.dp.toPx(), cap = StrokeCap.Round)
            drawSculptedSegment(shoulder, seat, torsoW, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), Offset(shoulder.x - 6.dp.toPx(), shoulder.y - 10.dp.toPx()))

            val foot = Offset(cx + 34.dp.toPx() - 22.dp.toPx() * t, floorY - 68.dp.toPx() + 20.dp.toPx() * t)
            val knee = Offset(seat.x + 26.dp.toPx() - 12.dp.toPx() * t, seat.y - 26.dp.toPx() - 6.dp.toPx() * t)
            drawSculptedSegment(seat, knee, thighW * 1.15f, limbBase, limbHi)
            drawSculptedSegment(knee, foot, armW, limbBase, limbHi)
            drawMuscleHeatmap(Offset((seat.x + knee.x) / 2f, (seat.y + knee.y) / 2f - 4.dp.toPx()), 11f, contractionGlow, primaryAccent, secondaryAccent)
            drawJointNode(knee, 3.5.dp.toPx(), secondaryAccent)
            drawLine(primaryAccent, Offset(foot.x - 8.dp.toPx(), foot.y - 14.dp.toPx()), Offset(foot.x + 10.dp.toPx(), foot.y + 12.dp.toPx()), 6.5.dp.toPx(), cap = StrokeCap.Round)
        }

        MotionPattern.LEG_EXTENSION, MotionPattern.LEG_CURL -> {
            val isExtension = pattern == MotionPattern.LEG_EXTENSION
            val hip = Offset(cx - 14.dp.toPx(), floorY - 36.dp.toPx())
            val shoulder = Offset(cx - 22.dp.toPx(), hip.y - 34.dp.toPx())
            val knee = Offset(cx + 16.dp.toPx(), hip.y)

            drawLine(benchMetal, Offset(cx - 20.dp.toPx(), floorY), Offset(cx + 16.dp.toPx(), floorY), 4.dp.toPx())
            drawSculptedSegment(shoulder, hip, torsoW, torsoBase, torsoHi)
            drawSculptedSegment(hip, knee, thighW, limbBase, limbHi)
            drawCircle(limbHi, 7.5.dp.toPx(), Offset(shoulder.x, shoulder.y - 12.dp.toPx()))

            val angle = if (isExtension) (-1.35 + 1.35 * (1f - t)) else (-0.1 - 1.35 * (1f - t))
            val shinLen = 32.dp.toPx()
            val ankle = Offset(knee.x + (cos(angle) * shinLen).toFloat(), knee.y - (sin(angle) * shinLen).toFloat())
            drawSculptedSegment(knee, ankle, armW, limbBase, limbHi)
            drawJointNode(knee, 3.5.dp.toPx(), secondaryAccent)
            drawEquipmentLoad(ankle, EquipmentType.MACHINE, primaryAccent, secondaryAccent)

            val glowPos = if (isExtension) Offset(cx + 2.dp.toPx(), hip.y - 5.dp.toPx()) else Offset(cx + 2.dp.toPx(), hip.y + 6.dp.toPx())
            drawMuscleHeatmap(glowPos, 10f, contractionGlow, primaryAccent, secondaryAccent)
        }

        MotionPattern.RDL_HINGE, MotionPattern.DEADLIFT -> {
            val foot = Offset(cx, floorY)
            val knee = Offset(cx + 4.dp.toPx(), floorY - 34.dp.toPx())
            val hip = Offset(cx - 18.dp.toPx() * t, floorY - 68.dp.toPx() + 6.dp.toPx() * t)
            val torsoAngle = 1.45 - 1.25 * t
            val torsoLen = 40.dp.toPx()
            val shoulder = Offset(hip.x + (cos(torsoAngle) * torsoLen).toFloat(), hip.y - (sin(torsoAngle) * torsoLen).toFloat())

            drawSculptedSegment(foot, knee, armW, limbBase, limbHi)
            drawSculptedSegment(knee, hip, thighW, limbBase, limbHi)
            drawSculptedSegment(hip, shoulder, torsoW, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), Offset(shoulder.x + 8.dp.toPx(), shoulder.y - 8.dp.toPx()))
            drawMuscleHeatmap(Offset(hip.x - 4.dp.toPx(), hip.y + 14.dp.toPx()), 11f, contractionGlow, primaryAccent, secondaryAccent)

            val bar = Offset(shoulder.x, shoulder.y + 34.dp.toPx())
            drawSculptedSegment(shoulder, bar, armW * 0.85f, limbBase, limbHi)
            drawJointNode(hip, 3.5.dp.toPx(), primaryAccent)
            drawEquipmentLoad(bar, equipment, primaryAccent, secondaryAccent)
        }

        MotionPattern.HIP_THRUST -> {
            val benchTop = Offset(cx - 32.dp.toPx(), floorY - 32.dp.toPx())
            drawLine(benchPad, Offset(cx - 42.dp.toPx(), floorY), benchTop, 8.dp.toPx(), cap = StrokeCap.Round)
            val foot = Offset(cx + 38.dp.toPx(), floorY)
            val knee = Offset(cx + 32.dp.toPx(), floorY - 32.dp.toPx())
            val hip = Offset(cx + 2.dp.toPx(), floorY - 32.dp.toPx() + 20.dp.toPx() * t)

            drawSculptedSegment(foot, knee, armW, limbBase, limbHi)
            drawSculptedSegment(knee, hip, thighW, limbBase, limbHi)
            drawSculptedSegment(hip, benchTop, torsoW, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), Offset(benchTop.x - 10.dp.toPx(), benchTop.y - 6.dp.toPx()))
            drawMuscleHeatmap(Offset(hip.x, hip.y + 5.dp.toPx()), 11f, contractionGlow, primaryAccent, secondaryAccent)
            drawEquipmentLoad(Offset(hip.x, hip.y - 5.dp.toPx()), equipment, primaryAccent, secondaryAccent)
        }

        MotionPattern.CALF_RAISE -> {
            val liftY = 14.dp.toPx() * (1f - t)
            drawLine(benchMetal, Offset(cx - 24.dp.toPx(), floorY - 8.dp.toPx()), Offset(cx + 24.dp.toPx(), floorY - 8.dp.toPx()), 6.dp.toPx(), cap = StrokeCap.Round)
            val ankle = Offset(cx, floorY - 12.dp.toPx() - liftY)
            val knee = Offset(cx, ankle.y - 34.dp.toPx())
            val hip = Offset(cx, knee.y - 34.dp.toPx())
            drawSculptedSegment(Offset(cx + 8.dp.toPx(), floorY - 10.dp.toPx()), ankle, armW * 0.85f, limbBase, limbHi)
            drawSculptedSegment(ankle, knee, armW, limbBase, limbHi)
            drawSculptedSegment(knee, hip, thighW, limbBase, limbHi)
            drawMuscleHeatmap(Offset(cx - 5.dp.toPx(), (ankle.y + knee.y) / 2f), 10f, contractionGlow, primaryAccent, secondaryAccent)
        }

        MotionPattern.CRUNCH_FLEXION, MotionPattern.LEG_RAISE, MotionPattern.AB_WHEEL -> {
            val hip = Offset(cx - 10.dp.toPx(), floorY - 22.dp.toPx())
            val curl = 1f - t
            val shoulder = Offset(hip.x - 28.dp.toPx() + 16.dp.toPx() * curl, hip.y - 18.dp.toPx() * curl)
            val knee = Offset(hip.x + 26.dp.toPx(), hip.y - 16.dp.toPx() * curl)
            drawSculptedSegment(knee, hip, thighW, limbBase, limbHi)
            drawSculptedSegment(hip, shoulder, torsoW, torsoBase, torsoHi)
            drawCircle(limbHi, 7.5.dp.toPx(), Offset(shoulder.x - 10.dp.toPx(), shoulder.y - 6.dp.toPx()))
            drawMuscleHeatmap(Offset(hip.x - 8.dp.toPx(), hip.y - 6.dp.toPx()), 11f, contractionGlow, primaryAccent, secondaryAccent)
        }
    }
}
