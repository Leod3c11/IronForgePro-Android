package com.ironforge.pro.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.model.Exercise
import com.ironforge.pro.model.SetType
import com.ironforge.pro.ui.components.ExerciseMiniVideoPlayer
import com.ironforge.pro.ui.components.HyperDynamicIsland
import com.ironforge.pro.ui.components.HyperPillTag
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
    val palette = LocalIronPalette.current
    var showAddExerciseDialog by remember { mutableStateOf(false) }
    var swapTargetIndex by remember { mutableStateOf<Int?>(null) }
    var plateInspectWeight by remember { mutableStateOf<Double?>(null) }
    var showCancelConfirm by remember { mutableStateOf(false) }
    var coachTabIndex by remember { mutableIntStateOf(0) }

    val totalSetsAll = remember(state.activeExercises) {
        state.activeExercises.sumOf { it.sets.size }.coerceAtLeast(1)
    }
    val completedSetsAll = remember(state.activeExercises) {
        state.activeExercises.sumOf { ex -> ex.sets.count { it.completed } }
    }
    val liveVolume = remember(state.activeExercises) {
        state.activeExercises.sumOf { ex ->
            ex.sets.filter { it.completed }.sumOf { it.weightKg * it.reps }
        }
    }
    val sessionProgress by animateFloatAsState(
        targetValue = (completedSetsAll.toFloat() / totalSetsAll.toFloat()).coerceIn(0f, 1f),
        label = "sessionProgress"
    )

    val safeExIndex = state.activeExerciseIndex.coerceIn(0, (state.activeExercises.size - 1).coerceAtLeast(0))
    val currentSessionEx = state.activeExercises.getOrNull(safeExIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.bg)
    ) {
        // Top Session Bar + Overall Progress + HyperOS Dynamic Island
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { showCancelConfirm = true }, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Cancelar", tint = HyperCrimson)
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.activeWorkoutTitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = palette.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${(sessionProgress * 100).roundToInt()}% concluído  •  Volume: ${liveVolume.roundToInt()} kg",
                        style = MaterialTheme.typography.labelSmall,
                        color = palette.primaryGlow
                    )
                }
                Button(
                    onClick = { viewModel.finishAndSaveWorkout() },
                    colors = ButtonDefaults.buttonColors(containerColor = HyperEmerald),
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text("Finalizar", color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(palette.tileElevated)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(sessionProgress)
                        .fillMaxHeight()
                        .background(Brush.horizontalGradient(listOf(palette.secondary, palette.primary)))
                )
            }

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

        if (currentSessionEx != null) {
            val exercise = currentSessionEx.exercise
            val safeSetIndex = state.focusedSetIndex.coerceIn(0, (currentSessionEx.sets.size - 1).coerceAtLeast(0))
            val focusedSet = currentSessionEx.sets[safeSetIndex]
            val currentPr = state.exercisePrs[exercise.id] ?: 0.0
            val liveEst1Rm = IronForgeViewModel.estimate1RM(focusedSet.weightKg, focusedSet.reps)
            val willBeatPr = focusedSet.setType != SetType.WARMUP && liveEst1Rm > currentPr && liveEst1Rm > 0.0

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Next-Exercise Transition Banner Notice!
                if (state.transitionCompletedExerciseName != null && state.transitionNextExercise != null) {
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInVertically() + fadeIn(),
                            exit = slideOutVertically() + fadeOut()
                        ) {
                            HyperSquircleCard(
                                accentGlow = true,
                                containerColor = palette.tileElevated
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
                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(CircleShape)
                                                .background(HyperEmerald.copy(alpha = 0.2f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = HyperEmerald)
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "SÉRIES CONCLUÍDAS! VAMOS SEGUIR PARA O PRÓXIMO",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                                color = HyperEmerald
                                            )
                                            Text(
                                                text = "Concluído: ${state.transitionCompletedExerciseName}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = palette.textSecondary
                                            )
                                            Text(
                                                text = "Próximo: ${state.transitionNextExercise.name}",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = palette.textPrimary
                                            )
                                        }
                                    }
                                    Button(
                                        onClick = { viewModel.dismissTransitionBanner() },
                                        colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                                        shape = CircleShape,
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text("OK", color = palette.onPrimaryAccent, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // 1. Horizontal Exercise Stage Carousel
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(state.activeExercises.size) { idx ->
                            val itemEx = state.activeExercises[idx]
                            val isCurrent = idx == safeExIndex
                            val allDone = itemEx.sets.all { it.completed }
                            val bg = when {
                                isCurrent -> palette.primary
                                allDone -> HyperEmerald.copy(alpha = 0.18f)
                                else -> palette.card
                            }
                            val fg = when {
                                isCurrent -> palette.onPrimaryAccent
                                allDone -> HyperEmerald
                                else -> palette.textSecondary
                            }
                            Row(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(bg)
                                    .clickable { viewModel.selectActiveExercise(idx) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (allDone) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = fg, modifier = Modifier.size(15.dp))
                                } else {
                                    Text("${idx + 1}", fontWeight = FontWeight.Black, color = fg, fontSize = 12.sp)
                                }
                                Text(
                                    text = itemEx.exercise.name.take(18),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = fg,
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            }
                        }

                        item {
                            AssistChip(
                                onClick = { showAddExerciseDialog = true },
                                shape = CircleShape,
                                label = { Text("+ Exercício", color = palette.secondary, fontSize = 12.sp) }
                            )
                        }
                    }
                }

                // 2. Hero Biomechanical Stage + Educational Coach Drawer
                item {
                    HyperSquircleCard(
                        modifier = Modifier.animateContentSize(),
                        accentGlow = true
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    HyperPillTag("EXERCÍCIO ${safeExIndex + 1}/${state.activeExercises.size}", palette.primaryGlow)
                                    HyperPillTag(exercise.muscleGroup.shortName, palette.secondary)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = exercise.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = palette.textPrimary
                                )
                                Text(
                                    text = "Alvo: ${exercise.primaryTarget}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = palette.textSecondary
                                )
                            }

                            IconButton(
                                onClick = { swapTargetIndex = safeExIndex },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(palette.tileElevated)
                                    .size(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = "Substituir Exercício",
                                    tint = palette.secondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        ExerciseMiniVideoPlayer(
                            exercise = exercise,
                            customVideoUri = state.customVideoUris[exercise.id],
                            height = 175.dp,
                            compact = false
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        val coachTabs = listOf("Execução", "Biomecânica", "Evitar Erros", "Guia RIR")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            coachTabs.forEachIndexed { idx, label ->
                                val selected = coachTabIndex == idx
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(CircleShape)
                                        .background(if (selected) palette.primary else palette.tileElevated)
                                        .clickable { coachTabIndex = idx }
                                        .padding(vertical = 7.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (selected) palette.onPrimaryAccent else palette.textSecondary,
                                        fontSize = 10.sp,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(palette.tileElevated)
                                .padding(12.dp)
                        ) {
                            when (coachTabIndex) {
                                0 -> {
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text("PASSO A PASSO DA EXECUÇÃO:", style = MaterialTheme.typography.labelSmall, color = palette.primaryGlow)
                                        exercise.executionSteps.forEachIndexed { i, step ->
                                            Row(verticalAlignment = Alignment.Top) {
                                                Text("${i + 1}. ", color = palette.primary, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                                Text(step, color = palette.textPrimary, style = MaterialTheme.typography.bodyMedium)
                                            }
                                        }
                                    }
                                }
                                1 -> {
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text("BIOMECÂNICA & SINERGISTAS:", style = MaterialTheme.typography.labelSmall, color = HyperEmerald)
                                        Text(
                                            text = "Cadência Ideal (TUT): ${exercise.tempoTUT} • Faixa: ${exercise.recommendedReps} • Descanso: ${exercise.restSeconds}s",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = palette.secondary
                                        )
                                        if (exercise.synergists.isNotEmpty()) {
                                            Text("Sinergistas: ${exercise.synergists.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium, color = palette.textSecondary)
                                        }
                                        exercise.biomechanicsTips.forEach { tip ->
                                            Text("• $tip", style = MaterialTheme.typography.bodyMedium, color = palette.textPrimary)
                                        }
                                    }
                                }
                                2 -> {
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text("ERROS COMUNS A EVITAR:", style = MaterialTheme.typography.labelSmall, color = HyperCrimson)
                                        exercise.commonMistakes.forEach { err ->
                                            Text("✕ $err", style = MaterialTheme.typography.bodyMedium, color = palette.textPrimary)
                                        }
                                    }
                                }
                                3 -> {
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text("COMO DOSAR O ESFORÇO (RIR / RPE):", style = MaterialTheme.typography.labelSmall, color = HyperAmber)
                                        Text(
                                            text = "• RIR 2 (RPE 8): Encerre a série quando sentir que faria no máximo mais 2 repetições limpas. Ideal para compostos.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = palette.textPrimary
                                        )
                                        Text(
                                            text = "• RIR 0–1 (RPE 9–10): Próximo ou até a falha concêntrica. Recomendado na última série de isoladores.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = palette.textSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Dynamic Interactive Set Focus Card
                item {
                    HyperSquircleCard(
                        modifier = Modifier.animateContentSize(),
                        containerColor = palette.card,
                        accentGlow = willBeatPr
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "SÉRIE ATUAL ${focusedSet.setNumber} DE ${currentSessionEx.sets.size}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = palette.secondary
                                )
                                Text(
                                    text = focusedSet.setType.label,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = palette.textPrimary
                                )
                            }

                            if (willBeatPr) {
                                HyperPillTag("NOVO PR! (${liveEst1Rm.roundToInt()}kg)", HyperEmerald)
                            } else {
                                HyperPillTag("1RM Est.: ${liveEst1Rm.roundToInt()} kg", palette.primaryGlow)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            SetType.entries.forEach { sType ->
                                val isSel = focusedSet.setType == sType
                                val badgeColor = when (sType) {
                                    SetType.WARMUP -> palette.textSecondary
                                    SetType.FEEDER -> palette.secondary
                                    SetType.WORKING -> palette.primary
                                    SetType.TOP_SET -> HyperAmber
                                    SetType.DROP_SET -> HyperViolet
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) badgeColor else palette.tileElevated)
                                        .clickable {
                                            viewModel.updateSetData(safeExIndex, safeSetIndex, setType = sType)
                                        }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = sType.code,
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black),
                                        color = if (isSel) palette.onPrimaryAccent else badgeColor,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(palette.tileElevated)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "💡 ${focusedSet.setType.educationalHint}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = palette.textSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1.1f)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(palette.tileElevated)
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("CARGA (KG)", style = MaterialTheme.typography.labelSmall, color = palette.textMuted)
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    BigRoundControl("-") {
                                        viewModel.updateSetData(safeExIndex, safeSetIndex, weightKg = focusedSet.weightKg - 2.5)
                                    }
                                    Text(
                                        text = if (focusedSet.weightKg % 1.0 == 0.0) "${focusedSet.weightKg.toInt()}" else "${focusedSet.weightKg}",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = palette.textPrimary,
                                        modifier = Modifier.clickable { plateInspectWeight = focusedSet.weightKg }
                                    )
                                    BigRoundControl("+") {
                                        viewModel.updateSetData(safeExIndex, safeSetIndex, weightKg = focusedSet.weightKg + 2.5)
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    MiniMicroButton("-1kg") {
                                        viewModel.updateSetData(safeExIndex, safeSetIndex, weightKg = focusedSet.weightKg - 1.0)
                                    }
                                    MiniMicroButton("+1kg") {
                                        viewModel.updateSetData(safeExIndex, safeSetIndex, weightKg = focusedSet.weightKg + 1.0)
                                    }
                                    MiniMicroButton("Anilhas") {
                                        plateInspectWeight = focusedSet.weightKg
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .weight(0.9f)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(palette.tileElevated)
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("REPETIÇÕES", style = MaterialTheme.typography.labelSmall, color = palette.textMuted)
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    BigRoundControl("-") {
                                        viewModel.updateSetData(safeExIndex, safeSetIndex, reps = focusedSet.reps - 1)
                                    }
                                    Text(
                                        text = "${focusedSet.reps}",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = palette.primaryGlow
                                    )
                                    BigRoundControl("+") {
                                        viewModel.updateSetData(safeExIndex, safeSetIndex, reps = focusedSet.reps + 1)
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Alvo: ${currentSessionEx.targetReps}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = palette.secondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("PERCEPÇÃO DE ESFORÇO (REPS NA RESERVA):", style = MaterialTheme.typography.labelSmall, color = palette.textMuted)
                        Spacer(modifier = Modifier.height(6.dp))
                        val rirOptions = listOf(
                            Triple(7.0, "RIR 3 • Leve", palette.secondary),
                            Triple(8.0, "RIR 2 • Ideal", HyperEmerald),
                            Triple(9.0, "RIR 1 • Intenso", palette.primary),
                            Triple(10.0, "RIR 0 • Falha", HyperCrimson)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            rirOptions.forEach { (rpeVal, label, accent) ->
                                val selected = focusedSet.rpe == rpeVal
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(CircleShape)
                                        .background(if (selected) accent else palette.tileElevated)
                                        .clickable {
                                            viewModel.updateSetData(safeExIndex, safeSetIndex, rpe = rpeVal)
                                        }
                                        .padding(vertical = 7.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (selected) palette.onPrimaryAccent else palette.textSecondary,
                                        fontSize = 10.sp,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val isLastSetOfExercise = focusedSet.setNumber == currentSessionEx.sets.size
                        val hasNextExercise = safeExIndex + 1 < state.activeExercises.size

                        Button(
                            onClick = { viewModel.toggleSetCompleted(safeExIndex, safeSetIndex) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (focusedSet.completed) HyperEmerald else palette.primary
                            ),
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = if (focusedSet.completed) Icons.Default.CheckCircle else Icons.Default.Bolt,
                                contentDescription = null,
                                tint = if (focusedSet.completed) Color.White else palette.onPrimaryAccent
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when {
                                    focusedSet.completed -> "SÉRIE ${focusedSet.setNumber} CONCLUÍDA ✓"
                                    isLastSetOfExercise && hasNextExercise -> "CONCLUIR ÚLTIMA SÉRIE • PRÓXIMO EXERCÍCIO"
                                    else -> "CONCLUIR SÉRIE ${focusedSet.setNumber} • DESCANSAR ${currentSessionEx.restSeconds}s"
                                },
                                color = if (focusedSet.completed) Color.White else palette.onPrimaryAccent,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // 4. All Sets Timeline for Current Exercise
                item {
                    HyperSquircleCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Séries deste Exercício (${currentSessionEx.sets.count { it.completed }}/${currentSessionEx.sets.size})",
                                style = MaterialTheme.typography.titleMedium,
                                color = palette.textPrimary
                            )
                            TextButton(onClick = { viewModel.addSetToExercise(safeExIndex) }) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = palette.primary, modifier = Modifier.size(16.dp))
                                Text("Nova Série", color = palette.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        currentSessionEx.sets.forEachIndexed { sIdx, sLog ->
                            val isFocused = sIdx == safeSetIndex
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        when {
                                            isFocused -> palette.primary.copy(alpha = 0.16f)
                                            sLog.completed -> HyperEmerald.copy(alpha = 0.12f)
                                            else -> palette.tileElevated
                                        }
                                    )
                                    .clickable { viewModel.focusSet(safeExIndex, sIdx) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    HyperPillTag(sLog.setType.code, if (sLog.completed) HyperEmerald else palette.primaryGlow)
                                    Text(
                                        text = "Série ${sLog.setNumber}: ${sLog.weightKg.toInt()} kg × ${sLog.reps} reps",
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                        color = palette.textPrimary
                                    )
                                }

                                Icon(
                                    imageVector = if (sLog.completed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (sLog.completed) HyperEmerald else palette.textMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
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
            containerColor = palette.card,
            shape = RoundedCornerShape(26.dp),
            title = {
                Text(
                    text = "Anilhas para ${plateInspectWeight!!.toInt()} kg",
                    color = palette.textPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                OlympicPlateVisualizer(totalWeightKg = plateInspectWeight!!)
            },
            confirmButton = {
                Button(
                    onClick = { plateInspectWeight = null },
                    colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                    shape = CircleShape
                ) {
                    Text("Fechar", color = palette.onPrimaryAccent, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (showCancelConfirm) {
        AlertDialog(
            onDismissRequest = { showCancelConfirm = false },
            containerColor = palette.card,
            shape = RoundedCornerShape(26.dp),
            title = { Text("Descartar Sessão?", color = palette.textPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("Os dados deste treino em andamento não serão salvos no histórico.", color = palette.textSecondary) },
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
                    Text("Continuar Treinando", color = palette.primary)
                }
            }
        )
    }
}

@Composable
private fun BigRoundControl(label: String, onClick: () -> Unit) {
    val palette = LocalIronPalette.current
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(palette.card)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = palette.primaryGlow,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp
        )
    }
}

@Composable
private fun MiniMicroButton(label: String, onClick: () -> Unit) {
    val palette = LocalIronPalette.current
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(palette.card)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = palette.secondary,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun ExercisePickerModal(
    title: String,
    onDismiss: () -> Unit,
    onSelect: (Exercise) -> Unit
) {
    val palette = LocalIronPalette.current
    var query by remember { mutableStateOf("") }
    val list = remember(query) {
        ExerciseRepository.allExercises.filter {
            query.isBlank() || it.name.contains(query, ignoreCase = true) ||
                it.muscleGroup.displayName.contains(query, ignoreCase = true)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = palette.card,
        shape = RoundedCornerShape(26.dp),
        title = { Text(title, color = palette.textPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.heightIn(max = 380.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Buscar entre ${ExerciseRepository.allExercises.size} exercícios...") },
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
                                Text(ex.name, color = palette.textPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text(ex.muscleGroup.displayName, color = palette.secondary, fontSize = 11.sp)
                            }
                            Icon(Icons.Default.AddCircle, contentDescription = null, tint = palette.primary)
                        }
                        HorizontalDivider(color = palette.hairline.copy(alpha = 0.4f))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar", color = palette.textSecondary)
            }
        }
    )
}
