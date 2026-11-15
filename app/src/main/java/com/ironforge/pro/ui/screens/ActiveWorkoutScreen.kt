package com.ironforge.pro.ui.screens

import androidx.compose.animation.animateContentSize
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
import com.ironforge.pro.ui.components.ExerciseMiniVideoPlayer
import com.ironforge.pro.ui.components.HyperDynamicIsland
import com.ironforge.pro.ui.components.HyperSquircleCard
import com.ironforge.pro.ui.components.OlympicPlateVisualizer
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
    var swapTargetIndex by remember { mutableStateOf<Int?>(null) }
    var plateInspectWeight by remember { mutableStateOf<Double?>(null) }
    var showCancelConfirm by remember { mutableStateOf(false) }

    val liveVolume = remember(state.activeExercises) {
        state.activeExercises.sumOf { ex ->
            ex.sets.filter { it.completed }.sumOf { it.weightKg * it.reps }
        }
    }
    val completedSetsCount = remember(state.activeExercises) {
        state.activeExercises.sumOf { ex -> ex.sets.count { it.completed } }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HyperBgDark)
    ) {
        // HyperOS Top Control Header + Dynamic Island
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { showCancelConfirm = true }) {
                    Icon(Icons.Default.Close, contentDescription = "Cancelar", tint = HyperCrimson)
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
                        text = "Volume: ${liveVolume.roundToInt()} kg  •  Séries concluídas: $completedSetsCount",
                        style = MaterialTheme.typography.labelSmall,
                        color = HyperOrangeGlow
                    )
                }
                Button(
                    onClick = { viewModel.finishAndSaveWorkout() },
                    colors = ButtonDefaults.buttonColors(containerColor = HyperEmerald),
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text("Finalizar", color = HyperBgDark, fontWeight = FontWeight.Black, fontSize = 12.sp)
                }
            }

            // Xiaomi HyperOS Dynamic Island Live Activity Pill
            HyperDynamicIsland(
                isTimerRunning = state.isRestTimerRunning,
                remainingSeconds = state.restRemainingSeconds,
                totalSeconds = state.restTotalSeconds,
                workoutClockSeconds = state.workoutElapsedSeconds,
                onAdjustTimer = { delta ->
                    if (state.isRestTimerRunning || state.restRemainingSeconds > 0) {
                        viewModel.adjustRestTimer(delta)
                    } else {
                        viewModel.startRestTimer(delta)
                    }
                },
                onSkipTimer = { viewModel.skipRestTimer() }
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            itemsIndexed(state.activeExercises) { exIndex, sessionEx ->
                val isVideoExpanded = state.expandedVideoExerciseIds.contains(sessionEx.exercise.id)

                HyperSquircleCard(
                    modifier = Modifier.animateContentSize()
                ) {
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
                                text = "${sessionEx.exercise.muscleGroup.displayName} • Alvo: ${sessionEx.targetReps} • ${sessionEx.restSeconds}s",
                                style = MaterialTheme.typography.labelSmall,
                                color = HyperCyan
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Toggle Inline Mini Video Demonstration
                            IconButton(
                                onClick = { viewModel.toggleInlineVideoInWorkout(sessionEx.exercise.id) },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(
                                    imageVector = if (isVideoExpanded) Icons.Default.VideocamOff else Icons.Default.OndemandVideo,
                                    contentDescription = "Mini Vídeo Execução",
                                    tint = if (isVideoExpanded) HyperOrange else HyperCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            // Swap Exercise
                            IconButton(
                                onClick = { swapTargetIndex = exIndex },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = "Trocar Exercício",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Expandable Live Biomechanical Mini-Video Player
                    if (isVideoExpanded) {
                        Spacer(modifier = Modifier.height(10.dp))
                        ExerciseMiniVideoPlayer(
                            exercise = sessionEx.exercise,
                            customVideoUri = state.customVideoUris[sessionEx.exercise.id],
                            height = 155.dp,
                            compact = true
                        )
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
                            SetType.FEEDER -> HyperCyan
                            SetType.WORKING -> HyperOrange
                            SetType.TOP_SET -> HyperAmber
                            SetType.DROP_SET -> HyperViolet
                        }
                        val est1Rm = IronForgeViewModel.estimate1RM(setLog.weightKg, setLog.reps).roundToInt()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (setLog.completed) HyperEmerald.copy(alpha = 0.14f) else HyperTileElevated
                                )
                                .padding(horizontal = 6.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
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

                            // Weight Stepper (Tap number to open Plate Calculator!)
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
                                    modifier = Modifier
                                        .clickable { plateInspectWeight = setLog.weightKg }
                                        .padding(horizontal = 6.dp)
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

                            Box(modifier = Modifier.width(46.dp), contentAlignment = Alignment.Center) {
                                if (setLog.isPr) {
                                    Text("PR!", style = MaterialTheme.typography.labelSmall, color = HyperEmerald, fontWeight = FontWeight.Black)
                                } else {
                                    Text("${est1Rm}k", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (setLog.completed) HyperEmerald else HyperCardDark)
                                    .clickable { viewModel.toggleSetCompleted(exIndex, setIndex) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Concluir série",
                                    tint = if (setLog.completed) HyperBgDark else TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Toque na carga para ver anilhas",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                        TextButton(
                            onClick = { viewModel.addSetToExercise(exIndex) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = HyperOrange, modifier = Modifier.size(16.dp))
                            Text("Adicionar Série", color = HyperOrange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = HyperCyan)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Adicionar Exercício à Sessão", color = HyperCyan, fontWeight = FontWeight.Bold)
                }
            }

            item {
                Button(
                    onClick = { viewModel.finishAndSaveWorkout() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HyperOrange),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = TextPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CONCLUIR E SALVAR TREINO",
                        color = TextPrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }

    if (showAddExerciseDialog) {
        ExercisePickerModal(
            title = "Adicionar Exercício",
            onDismiss = { showAddExerciseDialog = false },
            onSelect = { ex ->
                viewModel.addExerciseToActiveWorkout(ex)
                showAddExerciseDialog = false
            }
        )
    }

    if (swapTargetIndex != null) {
        ExercisePickerModal(
            title = "Substituir Exercício",
            onDismiss = { swapTargetIndex = null },
            onSelect = { ex ->
                viewModel.swapExerciseInActiveWorkout(swapTargetIndex!!, ex)
                swapTargetIndex = null
            }
        )
    }

    if (plateInspectWeight != null) {
        AlertDialog(
            onDismissRequest = { plateInspectWeight = null },
            containerColor = HyperCardDark,
            shape = RoundedCornerShape(26.dp),
            title = {
                Text(
                    text = "Anilhas para ${plateInspectWeight!!.toInt()} kg",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                OlympicPlateVisualizer(totalWeightKg = plateInspectWeight!!)
            },
            confirmButton = {
                Button(
                    onClick = { plateInspectWeight = null },
                    colors = ButtonDefaults.buttonColors(containerColor = HyperOrange),
                    shape = CircleShape
                ) {
                    Text("Fechar", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (showCancelConfirm) {
        AlertDialog(
            onDismissRequest = { showCancelConfirm = false },
            containerColor = HyperCardDark,
            shape = RoundedCornerShape(26.dp),
            title = { Text("Descartar Sessão?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("Os dados deste treino em andamento não serão salvos no histórico.", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        showCancelConfirm = false
                        viewModel.cancelActiveWorkout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HyperCrimson),
                    shape = CircleShape
                ) {
                    Text("Descartar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelConfirm = false }) {
                    Text("Continuar Treinando", color = HyperOrange)
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
            .background(HyperCardDark)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = HyperOrangeGlow,
            fontWeight = FontWeight.Black,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun ExercisePickerModal(
    title: String,
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
        containerColor = HyperCardDark,
        shape = RoundedCornerShape(26.dp),
        title = { Text(title, color = TextPrimary, fontWeight = FontWeight.Bold) },
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
                                Text(ex.muscleGroup.displayName, color = HyperCyan, fontSize = 11.sp)
                            }
                            Icon(Icons.Default.AddCircle, contentDescription = null, tint = HyperOrange)
                        }
                        HorizontalDivider(color = HyperHairline.copy(alpha = 0.4f))
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
