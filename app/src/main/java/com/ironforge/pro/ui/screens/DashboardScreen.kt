package com.ironforge.pro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.model.BodyMeasurementRecord
import com.ironforge.pro.model.MuscleGroup
import com.ironforge.pro.model.WorkoutDay
import com.ironforge.pro.ui.components.*
import com.ironforge.pro.ui.theme.*
import com.ironforge.pro.viewmodel.IronForgeUiState
import kotlin.math.roundToInt

@Composable
fun DashboardScreen(
    state: IronForgeUiState,
    onStartWorkoutDay: (WorkoutDay) -> Unit,
    onStartEmptyWorkout: () -> Unit,
    onDismissSummary: () -> Unit,
    onAddMeasurement: (Double, Double, Double, Double, Double, Double, Double) -> Unit
) {
    var showMeasureDialog by remember { mutableStateOf(false) }

    val totalTonnage = state.sessionHistory.sumOf { it.totalVolumeKg }
    val totalPrs = state.sessionHistory.sumOf { it.prCount }

    val aggregatedMuscleSets = remember(state.sessionHistory) {
        val map = mutableMapOf<String, Int>()
        state.sessionHistory.take(6).forEach { rec ->
            rec.muscleSetsBreakdown.forEach { (k, v) ->
                map[k] = (map[k] ?: 0) + v
            }
        }
        map
    }

    // Dynamic Muscle Recovery Readiness (%) based on recent sessions
    val muscleReadinessList = remember(state.sessionHistory) {
        val now = System.currentTimeMillis()
        MuscleGroup.entries.map { muscle ->
            val lastTrainedSession = state.sessionHistory.firstOrNull { rec ->
                (rec.muscleSetsBreakdown[muscle.displayName] ?: 0) > 0
            }
            val readinessPct = if (lastTrainedSession == null) {
                100
            } else {
                val hoursElapsed = ((now - lastTrainedSession.timestamp) / 3_600_000.0).coerceAtLeast(1.0)
                ((hoursElapsed / muscle.recoveryHours.toDouble()) * 100.0).roundToInt().coerceIn(32, 100)
            }
            muscle to readinessPct
        }
    }
    val avgReadiness = remember(muscleReadinessList) {
        muscleReadinessList.map { it.second }.average().roundToInt()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HyperBgDark),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // HyperOS Top App Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "IRONFORGE",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(listOf(HyperOrange, HyperOrangeGlow))
                                )
                                .padding(horizontal = 9.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "HyperOS",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                color = TextPrimary,
                                fontSize = 10.sp
                            )
                        }
                    }
                    Text(
                        text = "Central de Performance & Biomecânica",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = onStartEmptyWorkout,
                    colors = ButtonDefaults.buttonColors(containerColor = HyperOrange),
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Treino Livre",
                        color = TextPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Post-Workout Summary Notification Pill
        if (state.lastFinishedSession != null) {
            item {
                val summary = state.lastFinishedSession
                HyperSquircleCard(accentGlow = true, containerColor = HyperTileElevated) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = HyperEmerald,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "SESSÃO REGISTRADA COM SUCESSO",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = HyperEmerald
                                )
                                Text(
                                    text = summary.workoutTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary
                                )
                            }
                        }
                        IconButton(onClick = onDismissSummary) {
                            Icon(Icons.Default.Close, contentDescription = "Fechar", tint = TextSecondary)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        HyperPillTag("Volume: ${summary.totalVolumeKg.roundToInt()} kg", HyperOrangeGlow)
                        HyperPillTag("Séries: ${summary.totalWorkingSets}", HyperCyan)
                        HyperPillTag("PRs: ${summary.prCount}", HyperEmerald)
                    }
                }
            }
        }

        // HyperOS Control Center 2x2 Bento Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HyperSquircleCard(
                        modifier = Modifier.weight(1.15f),
                        accentGlow = true
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("TONELAGEM TOTAL", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = HyperOrange, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${(totalTonnage / 1000.0 * 10).roundToInt() / 10.0} t",
                            style = MaterialTheme.typography.displayLarge,
                            color = TextPrimary
                        )
                        Text(
                            text = "${totalTonnage.roundToInt()} kg acumulados",
                            style = MaterialTheme.typography.labelSmall,
                            color = HyperOrangeGlow
                        )
                    }

                    HyperSquircleCard(modifier = Modifier.weight(0.85f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("PRONTIDÃO", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Icon(Icons.Default.BatteryChargingFull, contentDescription = null, tint = HyperEmerald, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "$avgReadiness%",
                            style = MaterialTheme.typography.displayLarge,
                            color = HyperEmerald
                        )
                        Text(
                            text = "Recuperação SNC",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HyperSquircleCard(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("TREINOS", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = HyperCyan, modifier = Modifier.size(15.dp))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${state.sessionHistory.size} sessões",
                            style = MaterialTheme.typography.titleLarge,
                            color = HyperCyan
                        )
                    }

                    HyperSquircleCard(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("RECORDES (PR)", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = HyperVolt, modifier = Modifier.size(15.dp))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$totalPrs marcas",
                            style = MaterialTheme.typography.titleLarge,
                            color = HyperVolt
                        )
                    }
                }
            }
        }

        // Muscle Recovery Readiness Strip (HyperOS Health Widget)
        item {
            HyperSquircleCard {
                SectionHeader(
                    title = "Status de Recuperação Muscular",
                    subtitle = "Baseado na janela fisiológica de 24h a 72h"
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(muscleReadinessList) { (muscle, pct) ->
                        val color = when {
                            pct >= 90 -> HyperEmerald
                            pct >= 65 -> HyperCyan
                            else -> HyperAmber
                        }
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(HyperTileElevated)
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$pct%",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                color = color
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = muscle.shortName,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Featured Next Workout with Live Mini-Video Preview
        item {
            val nextWorkout = ExerciseRepository.defaultSplits.first().days.first()
            val leadExercise = ExerciseRepository.getExerciseById(nextWorkout.exercises.first().exerciseId)
            HyperSquircleCard(accentGlow = true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        HyperPillTag("TREINO RECOMENDADO • ${nextWorkout.estimatedMinutes} MIN", HyperOrangeGlow)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = nextWorkout.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary
                        )
                        Text(
                            text = "${nextWorkout.exercises.size} exercícios • ${nextWorkout.focusSummary}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    FilledIconButton(
                        onClick = { onStartWorkoutDay(nextWorkout) },
                        modifier = Modifier.size(52.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = HyperOrange)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Iniciar",
                            tint = TextPrimary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                if (leadExercise != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    ExerciseMiniVideoPlayer(
                        exercise = leadExercise,
                        customVideoUri = state.customVideoUris[leadExercise.id],
                        height = 135.dp,
                        compact = true
                    )
                }
            }
        }

        // Volume Bar Chart
        item {
            HyperSquircleCard {
                SectionHeader(
                    title = "Evolução de Tonelagem",
                    subtitle = "Volume total levantado nas últimas sessões"
                )
                Spacer(modifier = Modifier.height(8.dp))
                VolumeBarChart(sessions = state.sessionHistory)
            }
        }

        // Hexagonal Muscle Balance Radar
        item {
            HyperSquircleCard {
                SectionHeader(
                    title = "Equilíbrio Muscular",
                    subtitle = "Distribuição de séries válidas por grupamento"
                )
                Spacer(modifier = Modifier.height(6.dp))
                MuscleBalanceRadarChart(muscleSets = aggregatedMuscleSets)
            }
        }

        // Top Personal Records
        item {
            HyperSquircleCard {
                SectionHeader(
                    title = "Recordes Pessoais (1RM Est.)",
                    subtitle = "Melhores marcas estimadas pela fórmula de Epley"
                )
                Spacer(modifier = Modifier.height(10.dp))
                val topPrs = state.exercisePrs.entries.sortedByDescending { it.value }.take(5)
                topPrs.forEachIndexed { idx, entry ->
                    val exName = ExerciseRepository.getExerciseById(entry.key)?.name ?: entry.key
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "#${idx + 1}",
                                style = MaterialTheme.typography.labelLarge,
                                color = HyperOrange
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = exName,
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary
                            )
                        }
                        HyperPillTag("${entry.value.roundToInt()} kg", HyperEmerald)
                    }
                    if (idx < topPrs.lastIndex) {
                        HorizontalDivider(color = HyperHairline.copy(alpha = 0.5f))
                    }
                }
            }
        }

        // Anthropometry & Body Measurements
        item {
            val latest = state.bodyMeasurements.lastOrNull()
            HyperSquircleCard {
                SectionHeader(
                    title = "Antropometria & Medidas",
                    subtitle = if (latest != null) "Última aferição em ${latest.dateFormatted}" else "Acompanhe sua evolução física",
                    trailing = {
                        TextButton(onClick = { showMeasureDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = HyperOrange, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Registrar", color = HyperOrange, fontWeight = FontWeight.Bold)
                        }
                    }
                )
                if (latest != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MeasureStatBox("Peso", "${latest.weightKg} kg")
                        MeasureStatBox("Gordura", "${latest.bodyFatPct}%")
                        MeasureStatBox("Braço", "${latest.armCm} cm")
                        MeasureStatBox("Cintura", "${latest.waistCm} cm")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MeasureStatBox("Peitoral", "${latest.chestCm} cm")
                        MeasureStatBox("Coxa", "${latest.thighCm} cm")
                        MeasureStatBox("Panturrilha", "${latest.calfCm} cm")
                        val leanMass = (latest.weightKg * (1.0 - latest.bodyFatPct / 100.0) * 10).roundToInt() / 10.0
                        MeasureStatBox("Massa Magra", "$leanMass kg")
                    }
                }
            }
        }

        // Recent Sessions
        item {
            SectionHeader(
                title = "Histórico Recente",
                subtitle = "Suas últimas sessões concluídas"
            )
        }

        items(state.sessionHistory.take(5)) { session ->
            HyperSquircleCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = session.workoutTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Text(
                            text = "${session.dateFormatted} • ${session.durationMinutes} min • ${session.totalWorkingSets} séries válidas",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                    HyperPillTag("${session.totalVolumeKg.roundToInt()} kg", HyperCyan)
                }
                if (session.highlightExercises.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Destaques: ${session.highlightExercises.joinToString("  •  ")}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }
        }
    }

    if (showMeasureDialog) {
        BodyMeasurementDialog(
            initial = state.bodyMeasurements.lastOrNull(),
            onDismiss = { showMeasureDialog = false },
            onSave = { w, bf, ch, arm, waist, thigh, calf ->
                onAddMeasurement(w, bf, ch, arm, waist, thigh, calf)
                showMeasureDialog = false
            }
        )
    }
}

@Composable
private fun MeasureStatBox(label: String, value: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(HyperTileElevated)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, style = MaterialTheme.typography.labelLarge, color = TextPrimary)
    }
}

@Composable
private fun BodyMeasurementDialog(
    initial: BodyMeasurementRecord?,
    onDismiss: () -> Unit,
    onSave: (Double, Double, Double, Double, Double, Double, Double) -> Unit
) {
    var weight by remember { mutableStateOf((initial?.weightKg ?: 80.0).toString()) }
    var bf by remember { mutableStateOf((initial?.bodyFatPct ?: 13.5).toString()) }
    var chest by remember { mutableStateOf((initial?.chestCm ?: 105.0).toString()) }
    var arm by remember { mutableStateOf((initial?.armCm ?: 40.0).toString()) }
    var waist by remember { mutableStateOf((initial?.waistCm ?: 80.0).toString()) }
    var thigh by remember { mutableStateOf((initial?.thighCm ?: 60.0).toString()) }
    var calf by remember { mutableStateOf((initial?.calfCm ?: 39.0).toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = HyperCardDark,
        shape = RoundedCornerShape(26.dp),
        title = { Text("Nova Aferição Corporal", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Peso (kg)") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = bf, onValueChange = { bf = it }, label = { Text("BF (%)") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = chest, onValueChange = { chest = it }, label = { Text("Peitoral (cm)") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = arm, onValueChange = { arm = it }, label = { Text("Braço (cm)") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = waist, onValueChange = { waist = it }, label = { Text("Cintura (cm)") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = thigh, onValueChange = { thigh = it }, label = { Text("Coxa (cm)") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                OutlinedTextField(value = calf, onValueChange = { calf = it }, label = { Text("Panturrilha (cm)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        weight.toDoubleOrNull() ?: 80.0,
                        bf.toDoubleOrNull() ?: 13.5,
                        chest.toDoubleOrNull() ?: 105.0,
                        arm.toDoubleOrNull() ?: 40.0,
                        waist.toDoubleOrNull() ?: 80.0,
                        thigh.toDoubleOrNull() ?: 60.0,
                        calf.toDoubleOrNull() ?: 39.0
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = HyperOrange),
                shape = CircleShape
            ) {
                Text("Salvar Medidas", color = TextPrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextSecondary)
            }
        }
    )
}
