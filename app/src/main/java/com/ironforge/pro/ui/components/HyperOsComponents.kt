package com.ironforge.pro.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.model.WorkoutSessionRecord
import com.ironforge.pro.ui.theme.*
import com.ironforge.pro.viewmodel.IronForgeViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HyperSquircleCard(
    modifier: Modifier = Modifier,
    accentGlow: Boolean = false,
    containerColor: Color = HyperCardDark,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val palette = LocalIronPalette.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.975f else 1f,
        animationSpec = spring(dampingRatio = 0.72f, stiffness = 420f),
        label = "hyperCardScale"
    )

    val borderBrush = if (accentGlow) {
        Brush.verticalGradient(listOf(palette.primary, palette.secondary.copy(alpha = 0.55f)))
    } else {
        Brush.verticalGradient(listOf(HyperSpecularTop, palette.hairline))
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            ),
        shape = RoundedCornerShape(24.dp),
        color = containerColor,
        border = BorderStroke(if (accentGlow) 1.4.dp else 1.dp, borderBrush)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun HyperDynamicIsland(
    isTimerRunning: Boolean,
    remainingSeconds: Int,
    totalSeconds: Int,
    workoutClockSeconds: Int,
    onAdjustTimer: (Int) -> Unit,
    onSkipTimer: () -> Unit
) {
    val palette = LocalIronPalette.current
    val progress = if (totalSeconds > 0) remainingSeconds.toFloat() / totalSeconds.toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(300),
        label = "islandRing"
    )

    val showExpandedTimer = isTimerRunning || remainingSeconds > 0
    val wMins = workoutClockSeconds / 60
    val wSecs = workoutClockSeconds % 60
    val workoutClockText = String.format("%02d:%02d", wMins, wSecs)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(spring(dampingRatio = 0.78f, stiffness = 360f)),
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFF06080C),
        border = BorderStroke(
            width = 1.2.dp,
            color = if (showExpandedTimer) palette.primary else palette.hairline
        )
    ) {
        if (showExpandedTimer) {
            val rMins = remainingSeconds / 60
            val rSecs = remainingSeconds % 60
            val restStr = String.format("%02d:%02d", rMins, rSecs)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(46.dp)) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val sw = 4.5.dp.toPx()
                            drawArc(
                                color = palette.tileElevated,
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = sw, cap = StrokeCap.Round)
                            )
                            drawArc(
                                color = palette.primary,
                                startAngle = -90f,
                                sweepAngle = 360f * animatedProgress,
                                useCenter = false,
                                style = Stroke(width = sw, cap = StrokeCap.Round)
                            )
                        }
                        Text(
                            text = restStr,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                            color = palette.primaryGlow,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "ILHA DINÂMICA • DESCANSO",
                            style = MaterialTheme.typography.labelSmall,
                            color = palette.secondary,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "Sessão: $workoutClockText",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IslandChipButton("-15s", TextPrimary, palette.onPrimaryAccent) { onAdjustTimer(-15) }
                    IslandChipButton("+15s", palette.secondary, palette.onPrimaryAccent) { onAdjustTimer(15) }
                    IslandChipButton("OK", palette.primary, palette.onPrimaryAccent, filled = true) { onSkipTimer() }
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = palette.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SESSÃO ATIVA • $workoutClockText",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(60, 90, 120).forEach { sec ->
                        IslandChipButton("${sec}s", palette.secondary, palette.onPrimaryAccent) { onAdjustTimer(sec) }
                    }
                }
            }
        }
    }
}

@Composable
private fun IslandChipButton(
    text: String,
    accent: Color,
    onFilledText: Color,
    filled: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (filled) accent else HyperTileElevated)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
            color = if (filled) onFilledText else accent,
            fontSize = 11.sp
        )
    }
}

@Composable
fun HyperFloatingDock(
    items: List<Pair<String, ImageVector>>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalIronPalette.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = CircleShape,
            color = HyperGlassOverlay,
            border = BorderStroke(1.dp, Brush.verticalGradient(listOf(HyperSpecularTop, palette.hairline))),
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, (label, icon) ->
                    val selected = index == selectedIndex
                    val bgColor = if (selected) palette.primary else Color.Transparent
                    val contentColor = if (selected) palette.onPrimaryAccent else TextSecondary

                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(bgColor)
                            .clickable { onSelect(index) }
                            .padding(
                                horizontal = if (selected) 16.dp else 14.dp,
                                vertical = 10.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = contentColor,
                            modifier = Modifier.size(20.dp)
                        )
                        if (selected) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelLarge,
                                color = contentColor,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
        trailing?.invoke()
    }
}

@Composable
fun HyperPillTag(
    text: String,
    accent: Color = LocalIronPalette.current.secondary
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(accent.copy(alpha = 0.16f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = accent
        )
    }
}

@Composable
fun OlympicPlateVisualizer(totalWeightKg: Double) {
    val palette = LocalIronPalette.current
    val plates = remember(totalWeightKg) {
        IronForgeViewModel.calculatePlatesPerSide(totalWeightKg, 20.0)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
        ) {
            val cy = size.height / 2f
            drawRoundRect(
                color = Color(0xFF9FB0CC),
                topLeft = Offset(16.dp.toPx(), cy - 4.dp.toPx()),
                size = Size(size.width - 32.dp.toPx(), 8.dp.toPx()),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            val sleeveStart = size.width * 0.28f
            drawRoundRect(
                color = Color(0xFFD5E0F2),
                topLeft = Offset(sleeveStart - 8.dp.toPx(), cy - 10.dp.toPx()),
                size = Size(8.dp.toPx(), 20.dp.toPx()),
                cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
            )

            var curX = sleeveStart + 2.dp.toPx()
            plates.forEach { plateKg ->
                val (pColor, pHeightDp) = when (plateKg) {
                    25.0 -> HyperCrimson to 68f
                    20.0 -> palette.secondary to 64f
                    15.0 -> HyperAmber to 56f
                    10.0 -> HyperEmerald to 48f
                    5.0 -> TextPrimary to 38f
                    2.5 -> palette.primary to 30f
                    else -> HyperViolet to 24f
                }
                val pWidth = 11.dp.toPx()
                val pHeight = pHeightDp.dp.toPx()
                drawRoundRect(
                    color = pColor,
                    topLeft = Offset(curX, cy - pHeight / 2f),
                    size = Size(pWidth, pHeight),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                )
                curX += pWidth + 3.dp.toPx()
            }
        }

        val summaryText = if (totalWeightKg <= 20.0) {
            "Apenas Barra Olímpica (20 kg)"
        } else {
            "Cada lado: " + plates.joinToString(" + ") {
                if (it % 1.0 == 0.0) "${it.toInt()}kg" else "${it}kg"
            }
        }
        Text(
            text = summaryText,
            style = MaterialTheme.typography.labelLarge,
            color = palette.primaryGlow
        )
    }
}

@Composable
fun VolumeBarChart(
    sessions: List<WorkoutSessionRecord>,
    modifier: Modifier = Modifier
) {
    val palette = LocalIronPalette.current
    val recent = sessions.take(6).reversed()
    if (recent.isEmpty()) return
    val maxVol = (recent.maxOfOrNull { it.totalVolumeKg } ?: 10000.0).coerceAtLeast(1000.0)

    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(top = 8.dp, bottom = 4.dp)
        ) {
            val barCount = recent.size
            val spacing = 14.dp.toPx()
            val totalSpacing = spacing * (barCount + 1)
            val barWidth = ((size.width - totalSpacing) / barCount).coerceAtLeast(18f)

            for (i in 1..3) {
                val y = size.height * (i / 4f)
                drawLine(
                    color = palette.hairline.copy(alpha = 0.55f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            recent.forEachIndexed { index, session ->
                val ratio = (session.totalVolumeKg / maxVol).toFloat().coerceIn(0.10f, 1f)
                val barHeight = size.height * ratio
                val left = spacing + index * (barWidth + spacing)
                val top = size.height - barHeight

                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(palette.primary, palette.secondary.copy(alpha = 0.8f)),
                        startY = top,
                        endY = size.height
                    ),
                    topLeft = Offset(left, top),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            recent.forEach { session ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${(session.totalVolumeKg / 1000.0 * 10).toInt() / 10.0}t",
                        style = MaterialTheme.typography.labelSmall,
                        color = palette.primaryGlow
                    )
                    Text(
                        text = session.dateFormatted,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }
        }
    }
}

@Composable
fun MuscleBalanceRadarChart(
    muscleSets: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    val palette = LocalIronPalette.current
    val axes = listOf("Peitoral", "Costas", "Ombros", "Braços", "Quadríceps", "Posterior")
    val values = listOf(
        (muscleSets["Peitoral"] ?: 10).toFloat(),
        (muscleSets["Costas & Dorsais"] ?: 12).toFloat(),
        (muscleSets["Ombros (Deltoides)"] ?: 11).toFloat(),
        ((muscleSets["Bíceps Braquial"] ?: 6) + (muscleSets["Tríceps Braquial"] ?: 6)).toFloat(),
        (muscleSets["Quadríceps"] ?: 11).toFloat(),
        ((muscleSets["Posteriores de Coxa"] ?: 7) + (muscleSets["Glúteos"] ?: 5)).toFloat()
    )
    val maxScale = 20f

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(205.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(180.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.minDimension * 0.42f
                val sides = axes.size

                for (ring in 1..4) {
                    val r = radius * (ring / 4f)
                    val path = Path()
                    for (i in 0 until sides) {
                        val angle = (2.0 * PI * i / sides) - PI / 2.0
                        val x = center.x + (r * cos(angle)).toFloat()
                        val y = center.y + (r * sin(angle)).toFloat()
                        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }
                    path.close()
                    drawPath(
                        path = path,
                        color = palette.hairline.copy(alpha = 0.7f),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                for (i in 0 until sides) {
                    val angle = (2.0 * PI * i / sides) - PI / 2.0
                    val x = center.x + (radius * cos(angle)).toFloat()
                    val y = center.y + (radius * sin(angle)).toFloat()
                    drawLine(
                        color = palette.hairline.copy(alpha = 0.6f),
                        start = center,
                        end = Offset(x, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                val dataPath = Path()
                values.forEachIndexed { i, v ->
                    val norm = (v / maxScale).coerceIn(0.15f, 1.0f)
                    val r = radius * norm
                    val angle = (2.0 * PI * i / sides) - PI / 2.0
                    val x = center.x + (r * cos(angle)).toFloat()
                    val y = center.y + (r * sin(angle)).toFloat()
                    if (i == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
                }
                dataPath.close()

                drawPath(
                    path = dataPath,
                    color = palette.primary.copy(alpha = 0.24f)
                )
                drawPath(
                    path = dataPath,
                    color = palette.primary,
                    style = Stroke(width = 2.2.dp.toPx())
                )

                values.forEachIndexed { i, v ->
                    val norm = (v / maxScale).coerceIn(0.15f, 1.0f)
                    val r = radius * norm
                    val angle = (2.0 * PI * i / sides) - PI / 2.0
                    val x = center.x + (r * cos(angle)).toFloat()
                    val y = center.y + (r * sin(angle)).toFloat()
                    drawCircle(
                        color = palette.secondary,
                        radius = 4.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            axes.take(3).forEachIndexed { idx, label ->
                HyperPillTag("$label: ${values[idx].toInt()}s", palette.primaryGlow)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            axes.drop(3).forEachIndexed { idx, label ->
                HyperPillTag("$label: ${values[idx + 3].toInt()}s", palette.secondary)
            }
        }
    }
}
