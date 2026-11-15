package com.ironforge.pro.ui.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.model.EquipmentType
import com.ironforge.pro.model.Exercise
import com.ironforge.pro.model.MuscleGroup
import com.ironforge.pro.ui.components.ExerciseMiniVideoPlayer
import com.ironforge.pro.ui.components.HyperPillTag
import com.ironforge.pro.ui.components.HyperSquircleCard
import com.ironforge.pro.ui.components.SectionHeader
import com.ironforge.pro.ui.theme.*
import com.ironforge.pro.viewmodel.IronForgeUiState

@Composable
fun EncyclopediaScreen(
    state: IronForgeUiState,
    onSearchChange: (String) -> Unit,
    onSelectMuscle: (MuscleGroup?) -> Unit,
    onSelectEquipment: (EquipmentType?) -> Unit,
    onInspectExercise: (Exercise?) -> Unit,
    onAttachVideoUri: (String, String) -> Unit
) {
    val palette = LocalIronPalette.current
    val context = LocalContext.current
    var pendingVideoExerciseId by remember { mutableStateOf<String?>(null) }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        val exId = pendingVideoExerciseId
        if (uri != null && exId != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {
            }
            onAttachVideoUri(exId, uri.toString())
        }
        pendingVideoExerciseId = null
    }

    val filteredExercises = remember(
        state.searchQuery,
        state.selectedMuscleFilter,
        state.selectedEquipmentFilter
    ) {
        ExerciseRepository.allExercises.filter { ex ->
            val matchQuery = state.searchQuery.isBlank() ||
                ex.name.contains(state.searchQuery, ignoreCase = true) ||
                ex.primaryTarget.contains(state.searchQuery, ignoreCase = true) ||
                ex.muscleGroup.displayName.contains(state.searchQuery, ignoreCase = true)
            val matchMuscle = state.selectedMuscleFilter == null || ex.muscleGroup == state.selectedMuscleFilter
            val matchEq = state.selectedEquipmentFilter == null || ex.equipment == state.selectedEquipmentFilter
            matchQuery && matchMuscle && matchEq
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HyperBgDark),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionHeader(
                title = "Enciclopédia Biomecânica",
                subtitle = "${filteredExercises.size} exercícios com mini vídeo de execução"
            )
        }

        item {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar exercício, músculo alvo ou equipamento...", color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = palette.primary) },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpar", tint = TextSecondary)
                        }
                    }
                },
                singleLine = true,
                shape = CircleShape
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = state.selectedMuscleFilter == null,
                        onClick = { onSelectMuscle(null) },
                        shape = CircleShape,
                        label = { Text("Todos Músculos") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = palette.primary,
                            selectedLabelColor = palette.onPrimaryAccent,
                            containerColor = HyperCardDark,
                            labelColor = TextSecondary
                        )
                    )
                }
                items(MuscleGroup.entries) { muscle ->
                    FilterChip(
                        selected = state.selectedMuscleFilter == muscle,
                        onClick = {
                            onSelectMuscle(if (state.selectedMuscleFilter == muscle) null else muscle)
                        },
                        shape = CircleShape,
                        label = { Text(muscle.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = palette.primary,
                            selectedLabelColor = palette.onPrimaryAccent,
                            containerColor = HyperCardDark,
                            labelColor = TextSecondary
                        )
                    )
                }
            }
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(EquipmentType.entries) { eq ->
                    FilterChip(
                        selected = state.selectedEquipmentFilter == eq,
                        onClick = {
                            onSelectEquipment(if (state.selectedEquipmentFilter == eq) null else eq)
                        },
                        shape = CircleShape,
                        label = { Text(eq.label, fontSize = 12.sp) },
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

        items(filteredExercises, key = { it.id }) { exercise ->
            HyperSquircleCard(
                onClick = { onInspectExercise(exercise) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        HyperPillTag(exercise.muscleGroup.displayName, palette.primaryGlow)
                        HyperPillTag(exercise.equipment.label, palette.secondary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Detalhes", style = MaterialTheme.typography.labelSmall, color = palette.primaryGlow)
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = palette.primaryGlow, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = "Alvo: ${exercise.primaryTarget}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                ExerciseMiniVideoPlayer(
                    exercise = exercise,
                    customVideoUri = state.customVideoUris[exercise.id],
                    height = 148.dp,
                    compact = true,
                    onAttachVideoClick = {
                        pendingVideoExerciseId = exercise.id
                        videoPickerLauncher.launch("video/*")
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Reps: ${exercise.recommendedReps}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Text("TUT: ${exercise.tempoTUT}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Text("Descanso: ${exercise.restSeconds}s", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
            }
        }
    }

    if (state.inspectedExercise != null) {
        val inspected = state.inspectedExercise
        ExerciseDetailDialog(
            exercise = inspected,
            customVideoUri = state.customVideoUris[inspected.id],
            onAttachMp4 = {
                pendingVideoExerciseId = inspected.id
                videoPickerLauncher.launch("video/*")
            },
            onDismiss = { onInspectExercise(null) }
        )
    }
}

@Composable
private fun ExerciseDetailDialog(
    exercise: Exercise,
    customVideoUri: String?,
    onAttachMp4: () -> Unit,
    onDismiss: () -> Unit
) {
    val palette = LocalIronPalette.current
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = HyperCardDark,
        shape = RoundedCornerShape(28.dp),
        title = {
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    HyperPillTag(exercise.muscleGroup.displayName, palette.primaryGlow)
                    HyperPillTag(exercise.mechanics.label, palette.secondary)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExerciseMiniVideoPlayer(
                    exercise = exercise,
                    customVideoUri = customVideoUri,
                    height = 205.dp,
                    compact = false,
                    onAttachVideoClick = onAttachMp4
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(HyperTileElevated)
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("MÚSCULO ALVO PRINCIPAL", style = MaterialTheme.typography.labelSmall, color = palette.primaryGlow)
                        Text(exercise.primaryTarget, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        if (exercise.synergists.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("SINERGISTAS: ${exercise.synergists.joinToString(", ")}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ParamChip("Faixa Ideal", exercise.recommendedReps)
                    ParamChip("Cadência TUT", exercise.tempoTUT)
                    ParamChip("Descanso", "${exercise.restSeconds}s")
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(HyperTileElevated)
                        .padding(10.dp)
                ) {
                    val m = exercise.muscleGroup
                    Text(
                        text = "Marcos Semanais (${m.displayName}): MEV ${m.mevSets}s • MAV ${m.mavSets}s • MRV ${m.mrvSets}s • Recuperação ~${m.recoveryHours}h",
                        style = MaterialTheme.typography.labelSmall,
                        color = palette.secondary
                    )
                }

                Text("EXECUÇÃO BIOMECÂNICA PASSO A PASSO", style = MaterialTheme.typography.labelLarge, color = palette.primaryGlow)
                exercise.executionSteps.forEachIndexed { idx, step ->
                    Row(verticalAlignment = Alignment.Top) {
                        Text("${idx + 1}. ", color = palette.primary, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        Text(step, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Text("DICAS PRO DE HIPERTROFIA", style = MaterialTheme.typography.labelLarge, color = HyperEmerald)
                exercise.biomechanicsTips.forEach { tip ->
                    Row(verticalAlignment = Alignment.Top) {
                        Text("• ", color = HyperEmerald, fontWeight = FontWeight.Black)
                        Text(tip, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Text("ERROS COMUNS A EVITAR", style = MaterialTheme.typography.labelLarge, color = HyperCrimson)
                exercise.commonMistakes.forEach { err ->
                    Row(verticalAlignment = Alignment.Top) {
                        Text("✕ ", color = HyperCrimson, fontWeight = FontWeight.Bold)
                        Text(err, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                shape = CircleShape
            ) {
                Text("Entendido", color = palette.onPrimaryAccent, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun ParamChip(label: String, value: String) {
    val palette = LocalIronPalette.current
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(HyperTileElevated)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Text(value, style = MaterialTheme.typography.labelLarge, color = palette.primaryGlow)
    }
}
