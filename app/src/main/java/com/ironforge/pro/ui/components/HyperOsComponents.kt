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
import androidx.compose.material.icons.filled.BarChart
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
import androidx.compose.ui.text.style.TextAlign
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
    containerColor: Color = LocalIronPalette.current.card,
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
        Brush.verticalGradient(listOf(palette.primary, palette.secondary.copy(alpha = 0.6f)))
    } else if (palette.isLight) {
        Brush.verticalGradient(listOf(palette.hairline, palette.hairline))
    } else {
        Brush.verticalGradient(listOf(palette.specularTop, palette.hairline))
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
        shadowElevation = if (palette.isLight) 2.dp else 0.dp,
        border = BorderStroke(if (accentGlow) 1.5.dp else 1.dp, borderBrush)
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
        color = Color(0xFF07090E),
        border = BorderStroke(
            width = 1.3.dp,
            color = if (showExpandedTimer) palette.primary else Color(0xFF252D3D)
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
                                color = Color(0xFF1D2433),
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
                            color = Color(0xFFF5F8FF),
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
                            color = Color(0xFFA2B0C8)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IslandChipButton("-15s", Color(0xFFF5F8FF), palette.onPrimaryAccent) { onAdjustTimer(-15) }
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
                        color = Color(0xFFF5F8FF)
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
            .background(if (filled) accent else Color(0xFF1A202E))
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
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = CircleShape,
            color = palette.glassOverlay,
            border = BorderStroke(
                width = 1.1.dp,
                brush = Brush.horizontalGradient(
                    listOf(palette.hairline, palette.primary.copy(alpha = 0.45f), palette.hairline)
                )
            ),
            shadowElevation = 14.dp
        ) {
            Row(
                modifier = Modifier
                    .animateContentSize(spring(dampingRatio = 0.78f, stiffness = 380f))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, (label, icon) ->
                    val selected = index == selectedIndex
                    val bgColor = if (selected) palette.primary else Color.Transparent
                    val contentColor = if (selected) palette.onPrimaryAccent else palette.textSecondary

                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(bgColor)
                            .clickable { onSelect(index) }
                            .padding(
                                horizontal = if (selected) 14.dp else 12.dp,
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
                                fontSize = 12.sp,
                                maxLines = 1
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
    val palette = LocalIronPalette.current
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
                color = palette.textPrimary
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.textSecondary
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
    val palette = LocalIronPalette.current
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(accent.copy(alpha = if (palette.isLight) 0.14f else 0.18f))
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
                color = Color(0xFF64748B),
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
                    5.0 -> Color(0xFF94A3B8) to 38f
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
