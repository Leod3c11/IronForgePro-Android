package com.ironforge.pro.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.model.WorkoutSessionRecord
import com.ironforge.pro.ui.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun IronCard(
    modifier: Modifier = Modifier,
    highlightBorder: Boolean = false,
    containerColor: Color = CarbonSurface,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = containerColor,
        border = BorderStroke(
            width = if (highlightBorder) 1.5.dp else 1.dp,
            color = if (highlightBorder) ElectricVolt.copy(alpha = 0.65f) else GraphiteBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
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
            .padding(vertical = 4.dp),
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
fun MuscleTag(
    text: String,
    accent: Color = CyanPulse
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(accent.copy(alpha = 0.14f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = accent
        )
    }
}

@Composable
fun CircularRestTimerBanner(
    remainingSeconds: Int,
    totalSeconds: Int,
    onAdjust: (Int) -> Unit,
    onSkip: () -> Unit
) {
    val progress = if (totalSeconds > 0) remainingSeconds.toFloat() / totalSeconds.toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 350),
        label = "timerProgress"
    )
    val mins = remainingSeconds / 60
    val secs = remainingSeconds % 60
    val timeText = String.format("%02d:%02d", mins, secs)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SteelElevated,
        border = BorderStroke(1.5.dp, ElectricVolt)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(54.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeW = 5.dp.toPx()
                        drawArc(
                            color = GraphiteBorder,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = ElectricVolt,
                            startAngle = -90f,
                            sweepAngle = 360f * animatedProgress,
                            useCenter = false,
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = timeText,
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp),
                        color = ElectricVolt
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "DESCANSO ATIVO",
                        style = MaterialTheme.typography.labelSmall,
                        color = CyanPulse
                    )
                    Text(
                        text = "Recupere ATP para próxima série",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilledTonalButton(
                    onClick = { onAdjust(-15) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    modifier = Modifier.height(34.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = CarbonSurface)
                ) {
                    Text("-15s", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                FilledTonalButton(
                    onClick = { onAdjust(15) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    modifier = Modifier.height(34.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = CarbonSurface)
                ) {
                    Text("+15s", color = ElectricVolt, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onSkip,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    modifier = Modifier.height(34.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricVolt)
                ) {
                    Text("Pular", color = ObsidianDeep, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

@Composable
fun VolumeBarChart(
    sessions: List<WorkoutSessionRecord>,
    modifier: Modifier = Modifier
) {
    val recent = sessions.take(6).reversed()
    if (recent.isEmpty()) return
    val maxVol = (recent.maxOfOrNull { it.totalVolumeKg } ?: 10000.0).coerceAtLeast(1000.0)

    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(145.dp)
                .padding(top = 8.dp, bottom = 4.dp)
        ) {
            val barCount = recent.size
            val spacing = 16.dp.toPx()
            val totalSpacing = spacing * (barCount + 1)
            val barWidth = ((size.width - totalSpacing) / barCount).coerceAtLeast(18f)

            // Subtle horizontal grid lines
            for (i in 1..3) {
                val y = size.height * (i / 4f)
                drawLine(
                    color = GraphiteBorder.copy(alpha = 0.55f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            recent.forEachIndexed { index, session ->
                val ratio = (session.totalVolumeKg / maxVol).toFloat().coerceIn(0.08f, 1f)
                val barHeight = size.height * ratio
                val left = spacing + index * (barWidth + spacing)
                val top = size.height - barHeight

                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(ElectricVolt, CyanPulse.copy(alpha = 0.75f)),
                        startY = top,
                        endY = size.height
                    ),
                    topLeft = Offset(left, top),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            recent.forEach { session ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${(session.totalVolumeKg / 1000.0 * 10).toInt() / 10.0}t",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricVolt
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
                .height(210.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(185.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.minDimension * 0.42f
                val sides = axes.size

                // Draw concentric hexagons
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
                        color = GraphiteBorder.copy(alpha = 0.7f),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                // Draw radial spokes
                for (i in 0 until sides) {
                    val angle = (2.0 * PI * i / sides) - PI / 2.0
                    val x = center.x + (radius * cos(angle)).toFloat()
                    val y = center.y + (radius * sin(angle)).toFloat()
                    drawLine(
                        color = GraphiteBorder.copy(alpha = 0.6f),
                        start = center,
                        end = Offset(x, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // Draw data polygon
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
                    color = ElectricVolt.copy(alpha = 0.24f)
                )
                drawPath(
                    path = dataPath,
                    color = ElectricVolt,
                    style = Stroke(width = 2.2.dp.toPx())
                )

                // Draw vertex dots
                values.forEachIndexed { i, v ->
                    val norm = (v / maxScale).coerceIn(0.15f, 1.0f)
                    val r = radius * norm
                    val angle = (2.0 * PI * i / sides) - PI / 2.0
                    val x = center.x + (r * cos(angle)).toFloat()
                    val y = center.y + (r * sin(angle)).toFloat()
                    drawCircle(
                        color = CyanPulse,
                        radius = 4.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }
        }

        // Axis Legend Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            axes.take(3).forEachIndexed { idx, label ->
                MuscleTag("$label: ${values[idx].toInt()}s", ElectricVolt)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            axes.drop(3).forEachIndexed { idx, label ->
                MuscleTag("$label: ${values[idx + 3].toInt()}s", CyanPulse)
            }
        }
    }
}
