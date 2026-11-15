package com.ironforge.pro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.model.Exercise
import com.ironforge.pro.model.SetType
import com.ironforge.pro.ui.components.CircularRestTimerBanner
import com.ironforge.pro.ui.components.IronCard
import com.ironforge.pro.ui.components.MuscleTag
import com.ironforge.pro.ui.theme.*
import com.ironforge.pro.viewmodel.IronForgeUiState
import com.ironforge.pro.viewmodel.IronForgeViewModel
import kotlin.math.roundToInt

@Composable
fun ActiveWorkoutScreen(
    state: IronForgeUiState,
    viewModel: IronForgeViewModel
) {
    var showAddExerciseDialog by remember { mutableStateOf(false) }
    var showCancelConfirm by remember { mutableStateOf(false) }

    val liveVolume = remember(state.activeExercises) {
        state.activeExercises.sumOf { ex ->
            ex.sets.filter { it.completed }.sumOf { it.weightKg * it.reps }
        }
    }
    val completedSetsCount = remember(state.activeExercises) {
        state.activeExercises.sumOf { ex -> ex.sets.count { it.completed } }
    }

    val mins = state.workoutElapsedSeconds / 60
    val secs = state.workoutElapsedSeconds % 60
    val clockStr = String.format("%02d:%02d", mins, secs)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDeep)
    ) {
        // Sticky Top Session Bar
        Surface(
            color = CarbonSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { showCancelConfirm = true }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancelar", tint = CrimsonPeak)
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.activeWorkoutTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            maxLines = 1
                        )
                        Text(
                            text = "Tempo: $clockStr  •  Volume: ${liveVolume.roundToInt()} kg  •  Séries: $completedSetsCount",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricVolt
                        )
                    }
                    Button(
                        onClick = { viewModel.finishAndSaveWorkout() },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPR),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Finalizar", color = ObsidianDeep, fontWeight = FontWeight.Black, fontSize = 12.sp)
                    }
                }

                // Active Rest Timer or Quick Presets
                Spacer(modifier = Modifier.height(8.dp))
                if (state.isRestTimerRunning || state.restRemainingSeconds > 0) {
                    CircularRestTimerBanner(
                        remainingSeconds = state.restRemainingSeconds,
                        totalSeconds = state.restTotalSeconds,
                        onAdjust = { viewModel.adjustRestTimer(it) },
                        onSkip = { viewModel.skipRestTimer() }
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = CyanPulse, modifier = Modifier.size(18.dp))
                        Text("Timer Rápido:", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        listOf(60, 90, 120, 180).forEach { sec ->
                            AssistChip(
                                onClick = { viewModel.startRestTimer(sec) },
                                label = { Text("${sec}s", fontSize = 11.sp, color = CyanPulse) }
                            )
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            itemsIndexed(state.activeExercises) { exIndex, sessionEx ->
                IronCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = sessionEx.exercise.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary
                            )
                            Text(
                                text = "${sessionEx.exercise.muscleGroup.displayName} • Alvo: ${sessionEx.targetReps} • Descanso: ${sessionEx.restSeconds}s",
                                style = MaterialTheme.typography.labelSmall,
                                color = CyanPulse
                            )
                        }
                        TextButton(onClick = { viewModel.addSetToExercise(exIndex) }) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = ElectricVolt, modifier = Modifier.size(16.dp))
                            Text("Série", color = ElectricVolt, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("TIPO", style = MaterialTheme.typography.labelSmall, color = TextMuted, modifier = Modifier.width(46.dp))
                        Text("CARGA (KG)", style = MaterialTheme.typography.labelSmall, color = TextMuted, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                        Text("REPS", style = MaterialTheme.typography.labelSmall, color = TextMuted, modifier = Modifier.weight(0.85f), textAlign = TextAlign.Center)
                        Text("1RM", style = MaterialTheme.typography.labelSmall, color = TextMuted, modifier = Modifier.width(46.dp), textAlign = TextAlign.Center)
                        Text("OK", style = MaterialTheme.typography.labelSmall, color = TextMuted, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
                    }

                    sessionEx.sets.forEachIndexed { setIndex, setLog ->
                        val typeColor = when (setLog.setType) {
                            SetType.WARMUP -> TextMuted
                            SetType.FEEDER -> CyanPulse
                            SetType.WORKING -> ElectricVolt
                            SetType.TOP_SET -> AmberWarn
                            SetType.DROP_SET -> PurpleHyper
                        }
                        val est1Rm = IronForgeViewModel.estimate1RM(setLog.weightKg, setLog.reps).roundToInt()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (setLog.completed) EmeraldPR.copy(alpha = 0.12f) else SteelElevated
                                )
                                .padding(horizontal = 6.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Cycle SetType pill
                            Box(
                                modifier = Modifier
                                    .width(44.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(typeColor.copy(alpha = 0.18f))
                                    .clickable {
                                        val values = SetType.entries
                                        val nextType = values[(setLog.setType.ordinal + 1) % values.size]
                                        viewModel.updateSetData(exIndex, setIndex, setType = nextType)
                                    }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = setLog.setType.code,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                    color = typeColor
                                )
                            }

                            // Weight Stepper
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SmallStepButton("-") {
                                    viewModel.updateSetData(exIndex, setIndex, weightKg = setLog.weightKg - 2.5)
                                }
                                Text(
                                    text = if (setLog.weightKg % 1.0 == 0.0) "${setLog.weightKg.toInt()}" else "${setLog.weightKg}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                )
                                SmallStepButton("+") {
                                    viewModel.updateSetData(exIndex, setIndex, weightKg = setLog.weightKg + 2.5)
                                }
                            }

                            // Reps Stepper
                            Row(
                                modifier = Modifier.weight(0.85f),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SmallStepButton("-") {
                                    viewModel.updateSetData(exIndex, setIndex, reps = setLog.reps - 1)
                                }
                                Text(
                                    text = "${setLog.reps}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                )
                                SmallStepButton("+") {
                                    viewModel.updateSetData(exIndex, setIndex, reps = setLog.reps + 1)
                                }
                            }

                            // Estimated 1RM or PR badge
                            Box(modifier = Modifier.width(46.dp), contentAlignment = Alignment.Center) {
                                if (setLog.isPr) {
                                    Text("PR!", style = MaterialTheme.typography.labelSmall, color = EmeraldPR, fontWeight = FontWeight.Black)
                                } else {
                                    Text("${est1Rm}k", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                }
                            }

                            // Complete Check Button
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (setLog.completed) EmeraldPR else CarbonSurface)
                                    .clickable { viewModel.toggleSetCompleted(exIndex, setIndex) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Concluir série",
                                    tint = if (setLog.completed) ObsidianDeep else TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = { showAddExerciseDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = CyanPulse)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Adicionar Exercício à Sessão", color = CyanPulse, fontWeight = FontWeight.Bold)
                }
            }

            item {
                Button(
                    onClick = { viewModel.finishAndSaveWorkout() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricVolt),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = ObsidianDeep)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CONCLUIR E SALVAR TREINO",
                        color = ObsidianDeep,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }

    if (showAddExerciseDialog) {
        AddExercisePickerDialog(
            onDismiss = { showAddExerciseDialog = false },
            onSelect = { ex ->
                viewModel.addExerciseToActiveWorkout(ex)
                showAddExerciseDialog = false
            }
        )
    }

    if (showCancelConfirm) {
        AlertDialog(
            onDismissRequest = { showCancelConfirm = false },
            containerColor = CarbonSurface,
            title = { Text("Descartar Sessão?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("Os dados deste treino em andamento não serão salvos no histórico.", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        showCancelConfirm = false
                        viewModel.cancelActiveWorkout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonPeak)
                ) {
                    Text("Descartar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelConfirm = false }) {
                    Text("Continuar Treinando", color = ElectricVolt)
                }
            }
        )
    }
}

@Composable
private fun SmallStepButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(CarbonSurface)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = ElectricVolt,
            fontWeight = FontWeight.Black,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun AddExercisePickerDialog(
    onDismiss: () -> Unit,
    onSelect: (Exercise) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val list = remember(query) {
        ExerciseRepository.allExercises.filter {
            query.isBlank() || it.name.contains(query, ignoreCase = true) ||
                it.muscleGroup.displayName.contains(query, ignoreCase = true)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CarbonSurface,
        title = { Text("Adicionar Exercício", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.heightIn(max = 380.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Buscar por nome ou músculo...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.height(270.dp)) {
                    items(list) { ex ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(ex) }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(ex.name, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text(ex.muscleGroup.displayName, color = CyanPulse, fontSize = 11.sp)
                            }
                            Icon(Icons.Default.AddCircle, contentDescription = null, tint = ElectricVolt)
                        }
                        HorizontalDivider(color = GraphiteBorder.copy(alpha = 0.4f))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar", color = TextSecondary)
            }
        }
    )
}
