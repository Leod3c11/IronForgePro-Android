package com.ironforge.pro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.data.ExerciseRepository
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDeep),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Brand Header
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
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ElectricVolt)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "PRO",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                color = ObsidianDeep
                            )
                        }
                    }
                    Text(
                        text = "Ciência, Hipertrofia & Alta Performance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = onStartEmptyWorkout,
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricVolt),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = ObsidianDeep,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Treino Livre",
                        color = ObsidianDeep,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Post-Workout Summary Banner (if just finished)
        if (state.lastFinishedSession != null) {
            item {
                val summary = state.lastFinishedSession
                IronCard(highlightBorder = true, containerColor = SteelElevated) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = EmeraldPR,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "TREINO CONCLUÍDO!",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = EmeraldPR
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
                        MuscleTag("Volume: ${summary.totalVolumeKg.roundToInt()} kg", ElectricVolt)
                        MuscleTag("Séries: ${summary.totalWorkingSets}", CyanPulse)
                        MuscleTag("PRs: ${summary.prCount}", EmeraldPR)
                    }
                }
            }
        }

        // KPI Metrics Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IronCard(modifier = Modifier.weight(1f)) {
                    Text("TONELAGEM", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${(totalTonnage / 1000.0 * 10).roundToInt() / 10.0}t",
                        style = MaterialTheme.typography.headlineMedium,
                        color = ElectricVolt
                    )
                    Text("Movimentados", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
                IronCard(modifier = Modifier.weight(1f)) {
                    Text("SESSÕES", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${state.sessionHistory.size}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = CyanPulse
                    )
                    Text("Concluídas", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
                IronCard(modifier = Modifier.weight(1f)) {
                    Text("RECORDES", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$totalPrs",
                        style = MaterialTheme.typography.headlineMedium,
                        color = EmeraldPR
                    )
                    Text("PRs Batidos", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
            }
        }

        // Quick-Start Next Suggested Session
        item {
            val nextWorkout = ExerciseRepository.defaultSplits.first().days.first()
            IronCard(highlightBorder = true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        MuscleTag("SUGESTÃO DO DIA • ${nextWorkout.estimatedMinutes} MIN", ElectricVolt)
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
                        modifier = Modifier.size(50.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = ElectricVolt)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Iniciar",
                            tint = ObsidianDeep,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }

        // Volume Progression Bar Chart
        item {
            IronCard {
                SectionHeader(
                    title = "Evolução de Tonelagem",
                    subtitle = "Volume total (kg) levantado nas últimas sessões"
                )
                Spacer(modifier = Modifier.height(8.dp))
                VolumeBarChart(sessions = state.sessionHistory)
            }
        }

        // Hexagonal Muscle Balance Radar Chart
        item {
            IronCard {
                SectionHeader(
                    title = "Equilíbrio Muscular",
                    subtitle = "Distribuição de séries válidas por grupamento"
                )
                Spacer(modifier = Modifier.height(6.dp))
                MuscleBalanceRadarChart(muscleSets = aggregatedMuscleSets)
            }
        }

        // Top Personal Records (1RM)
        item {
            IronCard {
                SectionHeader(
                    title = "Recordes Pessoais (1RM Est.)",
                    subtitle = "Melhores marcas calculadas pela fórmula de Epley"
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
                                color = ElectricVolt
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = exName,
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary
                            )
                        }
                        MuscleTag("${entry.value.roundToInt()} kg", EmeraldPR)
                    }
                    if (idx < topPrs.lastIndex) {
                        HorizontalDivider(color = GraphiteBorder.copy(alpha = 0.5f))
                    }
                }
            }
        }

        // Body Measurements Tracker
        item {
            val latest = state.bodyMeasurements.lastOrNull()
            IronCard {
                SectionHeader(
                    title = "Antropometria & Medidas",
                    subtitle = if (latest != null) "Última aferição em ${latest.dateFormatted}" else "Acompanhe sua evolução física",
                    trailing = {
                        TextButton(onClick = { showMeasureDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = ElectricVolt, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Registrar", color = ElectricVolt, fontWeight = FontWeight.Bold)
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

        // Recent Sessions History
        item {
            SectionHeader(
                title = "Histórico Recente",
                subtitle = "Suas últimas sessões de treinamento"
            )
        }

        items(state.sessionHistory.take(5)) { session ->
            IronCard {
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
                    MuscleTag("${session.totalVolumeKg.roundToInt()} kg", CyanPulse)
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

        item { Spacer(modifier = Modifier.height(24.dp)) }
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
            .clip(RoundedCornerShape(10.dp))
            .background(SteelElevated)
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
    initial: com.ironforge.pro.model.BodyMeasurementRecord?,
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
        containerColor = CarbonSurface,
        title = { Text("Nova Aferição Corporal", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text("Peso (kg)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = bf,
                        onValueChange = { bf = it },
                        label = { Text("BF (%)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = chest,
                        onValueChange = { chest = it },
                        label = { Text("Peitoral (cm)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = arm,
                        onValueChange = { arm = it },
                        label = { Text("Braço (cm)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = waist,
                        onValueChange = { waist = it },
                        label = { Text("Cintura (cm)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = thigh,
                        onValueChange = { thigh = it },
                        label = { Text("Coxa (cm)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                OutlinedTextField(
                    value = calf,
                    onValueChange = { calf = it },
                    label = { Text("Panturrilha (cm)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
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
                colors = ButtonDefaults.buttonColors(containerColor = ElectricVolt)
            ) {
                Text("Salvar Medidas", color = ObsidianDeep, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextSecondary)
            }
        }
    )
}
