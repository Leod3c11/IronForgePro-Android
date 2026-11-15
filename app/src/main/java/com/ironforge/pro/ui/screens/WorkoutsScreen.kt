package com.ironforge.pro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.model.Exercise
import com.ironforge.pro.model.WorkoutDay
import com.ironforge.pro.ui.components.ExerciseMiniVideoPlayer
import com.ironforge.pro.ui.components.HyperPillTag
import com.ironforge.pro.ui.components.HyperSquircleCard
import com.ironforge.pro.ui.components.SectionHeader
import com.ironforge.pro.ui.theme.*
import com.ironforge.pro.viewmodel.IronForgeUiState

@Composable
fun WorkoutsScreen(
    state: IronForgeUiState,
    onStartWorkoutDay: (WorkoutDay) -> Unit,
    onSaveCustomWorkout: (String, String, List<String>) -> Unit,
    onDeleteCustomWorkout: (String) -> Unit
) {
    val palette = LocalIronPalette.current
    val allSplits = ExerciseRepository.defaultSplits
    var selectedTabIndex by remember {
        mutableIntStateOf(if (state.customWorkouts.isNotEmpty()) allSplits.size else 0)
    }
    var showBuilderDialog by remember { mutableStateOf(false) }
    var previewExercise by remember { mutableStateOf<Exercise?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HyperBgDark),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionHeader(
                title = "Programas & Divisões",
                subtitle = "Toque no ícone de vídeo para ver a execução",
                trailing = {
                    Button(
                        onClick = { showBuilderDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = palette.onPrimaryAccent, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Criar Ficha", color = palette.onPrimaryAccent, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(allSplits.size) { index ->
                    val split = allSplits[index]
                    FilterChip(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        shape = CircleShape,
                        label = {
                            Text(
                                text = split.name.substringBefore("—").trim(),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = palette.primary,
                            selectedLabelColor = palette.onPrimaryAccent,
                            containerColor = HyperCardDark,
                            labelColor = TextSecondary
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = selectedTabIndex == allSplits.size,
                        onClick = { selectedTabIndex = allSplits.size },
                        shape = CircleShape,
                        label = {
                            Text(
                                text = "Minhas Fichas (${state.customWorkouts.size})",
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = palette.secondary,
                            selectedLabelColor = HyperBgDark,
                            containerColor = HyperCardDark,
                            labelColor = TextSecondary
                        )
                    )
                }
            }
        }

        if (selectedTabIndex < allSplits.size) {
            val activeSplit = allSplits[selectedTabIndex]

            item {
                HyperSquircleCard(accentGlow = true) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HyperPillTag("${activeSplit.frequencyPerWeek}x / SEMANA", palette.primaryGlow)
                        HyperPillTag(activeSplit.level, palette.secondary)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = activeSplit.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = activeSplit.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            items(activeSplit.days) { day ->
                WorkoutDayCard(
                    day = day,
                    onStart = { onStartWorkoutDay(day) },
                    onPreviewExercise = { previewExercise = it }
                )
            }
        } else {
            if (state.customWorkouts.isEmpty()) {
                item {
                    HyperSquircleCard {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(42.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Nenhuma ficha personalizada criada ainda",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Toque em '+ Criar Ficha' para montar seu treino sob medida.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }
            } else {
                items(state.customWorkouts) { customDay ->
                    WorkoutDayCard(
                        day = customDay,
                        onStart = { onStartWorkoutDay(customDay) },
                        onDelete = { onDeleteCustomWorkout(customDay.id) },
                        onPreviewExercise = { previewExercise = it }
                    )
                }
            }
        }
    }

    if (showBuilderDialog) {
        CustomWorkoutBuilderDialog(
            onDismiss = { showBuilderDialog = false },
            onSave = { title, focus, selectedIds ->
                onSaveCustomWorkout(title, focus, selectedIds)
                selectedTabIndex = allSplits.size
                showBuilderDialog = false
            }
        )
    }

    if (previewExercise != null) {
        val ex = previewExercise!!
        AlertDialog(
            onDismissRequest = { previewExercise = null },
            containerColor = HyperCardDark,
            shape = RoundedCornerShape(26.dp),
            title = {
                Column {
                    HyperPillTag(ex.muscleGroup.displayName, palette.primaryGlow)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(ex.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ExerciseMiniVideoPlayer(
                        exercise = ex,
                        customVideoUri = state.customVideoUris[ex.id],
                        height = 190.dp
                    )
                    Text(
                        text = ex.executionSteps.joinToString("\n• ", prefix = "• "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { previewExercise = null },
                    colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                    shape = CircleShape
                ) {
                    Text("Fechar", color = palette.onPrimaryAccent, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun WorkoutDayCard(
    day: WorkoutDay,
    onStart: () -> Unit,
    onDelete: (() -> Unit)? = null,
    onPreviewExercise: (Exercise) -> Unit
) {
    val palette = LocalIronPalette.current
    HyperSquircleCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HyperPillTag(day.dayLabel, palette.primaryGlow)
                HyperPillTag("~${day.estimatedMinutes} min", palette.secondary)
            }
            if (onDelete != null) {
                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Remover", tint = HyperCrimson)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = day.title,
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )
        Text(
            text = day.focusSummary,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = palette.hairline.copy(alpha = 0.6f))
        Spacer(modifier = Modifier.height(10.dp))

        day.exercises.forEachIndexed { idx, planned ->
            val ex = ExerciseRepository.getExerciseById(planned.exerciseId)
            if (ex != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPreviewExercise(ex) }
                        .padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircleFilled,
                            contentDescription = "Ver Mini Vídeo",
                            tint = palette.secondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "${idx + 1}. ${ex.name}",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = TextPrimary
                            )
                            Text(
                                text = "${ex.muscleGroup.displayName} • RIR ${planned.targetRir} • ${planned.restSeconds}s",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                    }
                    Text(
                        text = "${planned.targetSets} × ${planned.targetReps}",
                        style = MaterialTheme.typography.labelLarge,
                        color = palette.primaryGlow
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
            shape = CircleShape
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = palette.onPrimaryAccent)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "INICIAR TREINO AGORA",
                color = palette.onPrimaryAccent,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun CustomWorkoutBuilderDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, List<String>) -> Unit
) {
    val palette = LocalIronPalette.current
    var title by remember { mutableStateOf("") }
    var focus by remember { mutableStateOf("") }
    var search by remember { mutableStateOf("") }
    val selectedIds = remember { mutableStateListOf<String>() }

    val filtered = remember(search) {
        ExerciseRepository.allExercises.filter {
            search.isBlank() || it.name.contains(search, ignoreCase = true) ||
                it.muscleGroup.displayName.contains(search, ignoreCase = true)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = HyperCardDark,
        shape = RoundedCornerShape(26.dp),
        title = {
            Text("Criar Ficha Personalizada", color = TextPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 440.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nome do Treino (ex.: Peito & Ombros)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = focus,
                    onValueChange = { focus = it },
                    label = { Text("Objetivo / Observação") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    label = { Text("Filtrar entre ${ExerciseRepository.allExercises.size} exercícios (${selectedIds.size})") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    items(filtered) { ex ->
                        val checked = selectedIds.contains(ex.id)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (checked) selectedIds.remove(ex.id) else selectedIds.add(ex.id)
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = {
                                    if (it) selectedIds.add(ex.id) else selectedIds.remove(ex.id)
                                },
                                colors = CheckboxDefaults.colors(checkedColor = palette.primary, checkmarkColor = palette.onPrimaryAccent)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(ex.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text(ex.muscleGroup.displayName, color = TextMuted, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(title, focus, selectedIds.toList()) },
                enabled = title.isNotBlank() && selectedIds.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                shape = CircleShape
            ) {
                Text("Salvar Ficha", color = palette.onPrimaryAccent, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextSecondary)
            }
        }
    )
}
