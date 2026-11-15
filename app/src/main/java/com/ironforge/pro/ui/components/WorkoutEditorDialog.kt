package com.ironforge.pro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.model.MuscleGroup
import com.ironforge.pro.model.PlannedExercise
import com.ironforge.pro.model.WorkoutDay
import com.ironforge.pro.ui.theme.*

@Composable
fun WorkoutEditorDialog(
    day: WorkoutDay,
    isBuiltInEdited: Boolean = false,
    onDismiss: () -> Unit,
    onSave: (WorkoutDay) -> Unit,
    onResetDefault: (() -> Unit)? = null
) {
    val palette = LocalIronPalette.current
    var title by remember(day.id) { mutableStateOf(day.title) }
    var focusSummary by remember(day.id) { mutableStateOf(day.focusSummary) }
    val exercises = remember(day.id) {
        mutableStateListOf<PlannedExercise>().apply { addAll(day.exercises) }
    }
    var showAddExercisePicker by remember { mutableStateOf(false) }

    val repPresets = listOf("5–8", "6–8", "6–10", "8–12", "10–14", "12–15", "15–20")
    val restPresets = listOf(60, 75, 90, 120, 150, 180)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = palette.card,
        shape = RoundedCornerShape(28.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Editar Treino", color = palette.textPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Personalize exercícios, séries, reps e descanso", color = palette.textSecondary, fontSize = 11.sp)
                }
                if (isBuiltInEdited && onResetDefault != null) {
                    TextButton(onClick = onResetDefault) {
                        Text("Restaurar", color = HyperAmber, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 470.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título do Treino") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = focusSummary,
                    onValueChange = { focusSummary = it },
                    label = { Text("Foco / Descrição") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EXERCÍCIOS (${exercises.size})",
                        style = MaterialTheme.typography.labelSmall,
                        color = palette.primaryGlow
                    )
                    TextButton(
                        onClick = { showAddExercisePicker = true },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = palette.primary, modifier = Modifier.size(16.dp))
                        Text("Adicionar Exercício", color = palette.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(275.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(exercises) { idx, planned ->
                        val ex = ExerciseRepository.getExerciseById(planned.exerciseId)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(palette.tileElevated)
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${idx + 1}. ${ex?.name ?: planned.exerciseId}",
                                        color = palette.textPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = ex?.muscleGroup?.displayName ?: "",
                                        color = palette.secondary,
                                        fontSize = 11.sp
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (idx > 0) {
                                        IconButton(
                                            onClick = {
                                                val item = exercises.removeAt(idx)
                                                exercises.add(idx - 1, item)
                                            },
                                            modifier = Modifier.size(26.dp)
                                        ) {
                                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Subir", tint = palette.textSecondary, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                    if (idx < exercises.lastIndex) {
                                        IconButton(
                                            onClick = {
                                                val item = exercises.removeAt(idx)
                                                exercises.add(idx + 1, item)
                                            },
                                            modifier = Modifier.size(26.dp)
                                        ) {
                                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Descer", tint = palette.textSecondary, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                    if (exercises.size > 1) {
                                        IconButton(
                                            onClick = { exercises.removeAt(idx) },
                                            modifier = Modifier.size(26.dp)
                                        ) {
                                            Icon(Icons.Default.DeleteOutline, contentDescription = "Remover", tint = HyperCrimson, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }

                            // Sets + Reps + Rest Controls
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Sets Stepper
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text("Séries:", color = palette.textMuted, fontSize = 11.sp)
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(palette.card)
                                            .clickable {
                                                if (planned.targetSets > 1) {
                                                    exercises[idx] = planned.copy(targetSets = planned.targetSets - 1)
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("-", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                    }
                                    Text("${planned.targetSets}", color = palette.primaryGlow, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(palette.card)
                                            .clickable {
                                                if (planned.targetSets < 10) {
                                                    exercises[idx] = planned.copy(targetSets = planned.targetSets + 1)
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("+", color = palette.primaryGlow, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                    }
                                }

                                // Cycle Reps Preset
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(palette.card)
                                        .clickable {
                                            val curIdx = repPresets.indexOf(planned.targetReps)
                                            val next = repPresets[(curIdx + 1) % repPresets.size]
                                            exercises[idx] = planned.copy(targetReps = next)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text("Reps: ${planned.targetReps}", color = palette.secondary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }

                                // Cycle Rest Preset
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(palette.card)
                                        .clickable {
                                            val curIdx = restPresets.indexOf(planned.restSeconds)
                                            val next = restPresets[(curIdx + 1) % restPresets.size]
                                            exercises[idx] = planned.copy(restSeconds = next)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text("${planned.restSeconds}s", color = palette.textPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && exercises.isNotEmpty()) {
                        onSave(
                            day.copy(
                                title = title.trim(),
                                focusSummary = focusSummary.trim(),
                                exercises = exercises.toList()
                            )
                        )
                    }
                },
                enabled = title.isNotBlank() && exercises.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                shape = CircleShape
            ) {
                Text("Salvar Alterações", color = palette.onPrimaryAccent, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = palette.textSecondary)
            }
        }
    )

    if (showAddExercisePicker) {
        var query by remember { mutableStateOf("") }
        var muscleFilter by remember { mutableStateOf<MuscleGroup?>(null) }
        val filtered = remember(query, muscleFilter) {
            ExerciseRepository.allExercises.filter { ex ->
                val mOk = muscleFilter == null || ex.muscleGroup == muscleFilter
                val qOk = query.isBlank() || ex.name.contains(query, ignoreCase = true) ||
                    ex.muscleGroup.displayName.contains(query, ignoreCase = true)
                mOk && qOk
            }
        }

        AlertDialog(
            onDismissRequest = { showAddExercisePicker = false },
            containerColor = palette.card,
            shape = RoundedCornerShape(26.dp),
            title = { Text("Adicionar Exercício ao Treino", color = palette.textPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { Text("Buscar exercício...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        item {
                            FilterChip(
                                selected = muscleFilter == null,
                                onClick = { muscleFilter = null },
                                shape = CircleShape,
                                label = { Text("Todos", fontSize = 11.sp) }
                            )
                        }
                        items(MuscleGroup.entries) { mg ->
                            FilterChip(
                                selected = muscleFilter == mg,
                                onClick = { muscleFilter = if (muscleFilter == mg) null else mg },
                                shape = CircleShape,
                                label = { Text(mg.shortName, fontSize = 11.sp) }
                            )
                        }
                    }
                    LazyColumn(modifier = Modifier.height(230.dp)) {
                        items(filtered) { ex ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        exercises.add(
                                            PlannedExercise(
                                                exerciseId = ex.id,
                                                targetSets = 3,
                                                targetReps = ex.recommendedReps.removeSuffix(" reps"),
                                                targetRir = 1,
                                                restSeconds = ex.restSeconds
                                            )
                                        )
                                        showAddExercisePicker = false
                                    }
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(ex.name, color = palette.textPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Text("${ex.muscleGroup.displayName} • ${ex.recommendedReps}", color = palette.secondary, fontSize = 11.sp)
                                }
                                Icon(Icons.Default.AddCircle, contentDescription = null, tint = palette.primary)
                            }
                            HorizontalDivider(color = palette.hairline.copy(alpha = 0.4f))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddExercisePicker = false }) {
                    Text("Fechar", color = palette.textSecondary)
                }
            }
        )
    }
}
