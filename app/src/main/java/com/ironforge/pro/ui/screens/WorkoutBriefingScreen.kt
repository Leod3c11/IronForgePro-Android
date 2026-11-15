package com.ironforge.pro.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.model.Exercise
import com.ironforge.pro.model.MuscleGroup
import com.ironforge.pro.model.WorkoutDay
import com.ironforge.pro.ui.components.ExerciseMiniVideoPlayer
import com.ironforge.pro.ui.components.ExerciseVisualThumb
import com.ironforge.pro.ui.components.HyperPillTag
import com.ironforge.pro.ui.components.HyperSquircleCard
import com.ironforge.pro.ui.components.WorkoutEditorDialog
import com.ironforge.pro.ui.theme.*
import com.ironforge.pro.viewmodel.IronForgeUiState
import com.ironforge.pro.viewmodel.IronForgeViewModel
import kotlin.math.roundToInt

@Composable
fun WorkoutBriefingScreen(
    day: WorkoutDay,
    state: IronForgeUiState,
    viewModel: IronForgeViewModel,
    onBack: () -> Unit,
    onStartFocusedWorkout: () -> Unit
) {
    val palette = LocalIronPalette.current
    var previewExercise by remember { mutableStateOf<Exercise?>(null) }
    var showSwitchWorkoutDialog by remember { mutableStateOf(false) }
    var showEditWorkoutDialog by remember { mutableStateOf(false) }

    val totalSets = remember(day) { day.exercises.sumOf { it.targetSets } }
    val estimatedTonnage = remember(day, state.exercisePrs, state.userProfile) {
        viewModel.estimatePlannedWorkoutTonnage(day)
    }

    // Collect both Primary & Synergist muscles recruited in today's workout
    val (primaryMuscles, synergistMuscles) = remember(day) {
        val prim = LinkedHashSet<MuscleGroup>()
        val syn = LinkedHashSet<MuscleGroup>()
        day.exercises.forEach { planned ->
            val ex = ExerciseRepository.getExerciseById(planned.exerciseId)
            if (ex != null) {
                prim.add(ex.muscleGroup)
                ex.secondaryMuscleGroups().forEach { syn.add(it) }
            }
        }
        syn.removeAll(prim)
        prim.toList() to syn.toList()
    }

    // Subtle breathing pulse for the centered Start Button
    val infinitePulse = rememberInfiniteTransition(label = "startPulse")
    val pulseScale by infinitePulse.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.045f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.bg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = palette.textPrimary)
            }
            Text(
                text = "PREPARAÇÃO DO TREINO",
                style = MaterialTheme.typography.labelLarge,
                color = palette.primaryGlow
            )
            Row {
                TextButton(onClick = { showEditWorkoutDialog = true }) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = palette.primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Editar", color = palette.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                TextButton(onClick = { showSwitchWorkoutDialog = true }) {
                    Text("Trocar", color = palette.secondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Today's Workout Overview Header Card
            item {
                HyperSquircleCard(accentGlow = true) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        HyperPillTag(day.dayLabel, palette.primaryGlow)
                        HyperPillTag("~${day.estimatedMinutes} min", palette.secondary)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = day.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = palette.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = day.focusSummary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.textSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3 Key Session Stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BriefingStatPill(
                            label = "EXERCÍCIOS",
                            value = "${day.exercises.size}",
                            modifier = Modifier.weight(1f)
                        )
                        BriefingStatPill(
                            label = "SÉRIES TOTAIS",
                            value = "$totalSets",
                            modifier = Modifier.weight(1f)
                        )
                        BriefingStatPill(
                            label = "CARGA EST.",
                            value = "${estimatedTonnage.roundToInt()} kg",
                            modifier = Modifier.weight(1.15f)
                        )
                    }
                }
            }

            // Prominent Centered "INICIAR TREINO" Focal Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = pulseScale
                                scaleY = pulseScale
                            }
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(listOf(palette.primary, palette.secondary))
                            )
                            .clickable(onClick = onStartFocusedWorkout)
                            .padding(horizontal = 36.dp, vertical = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = palette.onPrimaryAccent,
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = "INICIAR TREINO",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                color = palette.onPrimaryAccent,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Foco guiado exercício por exercício com mini vídeo e coach ao vivo",
                        style = MaterialTheme.typography.labelSmall,
                        color = palette.textMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Muscles Recruited Today (Primary + Secondary Synergists)
            item {
                HyperSquircleCard {
                    Text(
                        text = "Músculos Ativados Hoje",
                        style = MaterialTheme.typography.titleMedium,
                        color = palette.textPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("ALVOS PRINCIPAIS (100% ESTÍMULO):", style = MaterialTheme.typography.labelSmall, color = palette.primaryGlow)
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(primaryMuscles) { m ->
                            HyperPillTag(m.displayName, palette.primaryGlow)
                        }
                    }

                    if (synergistMuscles.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("SINERGISTAS SECUNDÁRIOS ATIVOS:", style = MaterialTheme.typography.labelSmall, color = palette.secondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(synergistMuscles) { m ->
                                HyperPillTag(m.displayName, palette.secondary)
                            }
                        }
                    }
                }
            }

            // Sequence of Exercises in Today's Session
            item {
                Text(
                    text = "Sequência de Exercícios (${day.exercises.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = palette.textPrimary
                )
            }

            itemsIndexed(day.exercises) { idx, planned ->
                val ex = ExerciseRepository.getExerciseById(planned.exerciseId)
                if (ex != null) {
                    val estWeight = viewModel.estimateStartingWeightForUser(ex)
                    HyperSquircleCard(
                        onClick = { previewExercise = ex }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ExerciseVisualThumb(exercise = ex, size = 44.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "${idx + 1}. ${ex.name}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = palette.textPrimary
                                    )
                                    Text(
                                        text = "${ex.muscleGroup.displayName} • ${planned.targetSets} séries × ${planned.targetReps} • Carga sugerida: ${estWeight.toInt()} kg",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = palette.textSecondary
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.OndemandVideo,
                                contentDescription = "Ver execução",
                                tint = palette.secondary
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }

    if (previewExercise != null) {
        val ex = previewExercise!!
        AlertDialog(
            onDismissRequest = { previewExercise = null },
            containerColor = palette.card,
            shape = RoundedCornerShape(26.dp),
            title = {
                Column {
                    HyperPillTag(ex.muscleGroup.displayName, palette.primaryGlow)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(ex.name, color = palette.textPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
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
                        color = palette.textSecondary
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

    if (showEditWorkoutDialog) {
        WorkoutEditorDialog(
            day = day,
            isBuiltInEdited = state.editedWorkoutDays.containsKey(day.id),
            onDismiss = { showEditWorkoutDialog = false },
            onSave = { updated ->
                viewModel.saveEditedWorkoutDay(updated)
                showEditWorkoutDialog = false
            },
            onResetDefault = {
                viewModel.resetEditedWorkoutDay(day.id)
                showEditWorkoutDialog = false
            }
        )
    }

    if (showSwitchWorkoutDialog) {
        val allDays = remember(state.customWorkouts) { viewModel.allAvailableWorkoutDays() }
        AlertDialog(
            onDismissRequest = { showSwitchWorkoutDialog = false },
            containerColor = palette.card,
            shape = RoundedCornerShape(26.dp),
            title = { Text("Escolher Outro Treino Hoje", color = palette.textPrimary, fontWeight = FontWeight.Bold) },
            text = {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 360.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(allDays) { candidate ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showSwitchWorkoutDialog = false
                                    viewModel.openWorkoutBriefing(candidate)
                                },
                            shape = RoundedCornerShape(14.dp),
                            color = palette.tileElevated,
                            border = BorderStroke(1.dp, palette.hairline)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(candidate.title, color = palette.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("${candidate.exercises.size} exercícios • ${candidate.focusSummary}", color = palette.textSecondary, fontSize = 11.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSwitchWorkoutDialog = false }) {
                    Text("Cancelar", color = palette.textSecondary)
                }
            }
        )
    }
}

@Composable
private fun BriefingStatPill(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val palette = LocalIronPalette.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(palette.tileElevated)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = palette.textMuted, fontSize = 9.sp)
        Spacer(modifier = Modifier.height(3.dp))
        Text(value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black), color = palette.textPrimary)
    }
}
