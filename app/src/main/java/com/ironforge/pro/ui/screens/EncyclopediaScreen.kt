package com.ironforge.pro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.model.EquipmentType
import com.ironforge.pro.model.Exercise
import com.ironforge.pro.model.MuscleGroup
import com.ironforge.pro.ui.components.IronCard
import com.ironforge.pro.ui.components.MuscleTag
import com.ironforge.pro.ui.components.SectionHeader
import com.ironforge.pro.ui.theme.*
import com.ironforge.pro.viewmodel.IronForgeUiState

@Composable
fun EncyclopediaScreen(
    state: IronForgeUiState,
    onSearchChange: (String) -> Unit,
    onSelectMuscle: (MuscleGroup?) -> Unit,
    onSelectEquipment: (EquipmentType?) -> Unit,
    onInspectExercise: (Exercise?) -> Unit
) {
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
            .background(ObsidianDeep),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionHeader(
                title = "Enciclopédia Biomecânica",
                subtitle = "${filteredExercises.size} exercícios detalhados com execução e TUT"
            )
        }

        item {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar exercício, músculo alvo ou equipamento...", color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ElectricVolt) },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpar", tint = TextSecondary)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )
        }

        // Muscle Group Filter Row
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = state.selectedMuscleFilter == null,
                        onClick = { onSelectMuscle(null) },
                        label = { Text("Todos Músculos") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricVolt,
                            selectedLabelColor = ObsidianDeep,
                            containerColor = CarbonSurface,
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
                        label = { Text(muscle.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricVolt,
                            selectedLabelColor = ObsidianDeep,
                            containerColor = CarbonSurface,
                            labelColor = TextSecondary
                        )
                    )
                }
            }
        }

        // Equipment Filter Row
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(EquipmentType.entries) { eq ->
                    FilterChip(
                        selected = state.selectedEquipmentFilter == eq,
                        onClick = {
                            onSelectEquipment(if (state.selectedEquipmentFilter == eq) null else eq)
                        },
                        label = { Text(eq.label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyanPulse,
                            selectedLabelColor = ObsidianDeep,
                            containerColor = CarbonSurface,
                            labelColor = TextSecondary
                        )
                    )
                }
            }
        }

        items(filteredExercises) { exercise ->
            IronCard(
                modifier = Modifier.clickable { onInspectExercise(exercise) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        MuscleTag(exercise.muscleGroup.displayName, ElectricVolt)
                        MuscleTag(exercise.equipment.label, CyanPulse)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
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
                Spacer(modifier = Modifier.height(8.dp))
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

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }

    if (state.inspectedExercise != null) {
        ExerciseDetailDialog(
            exercise = state.inspectedExercise,
            onDismiss = { onInspectExercise(null) }
        )
    }
}

@Composable
private fun ExerciseDetailDialog(
    exercise: Exercise,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CarbonSurface,
        title = {
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    MuscleTag(exercise.muscleGroup.displayName, ElectricVolt)
                    MuscleTag(exercise.mechanics.label, CyanPulse)
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
                    .heightIn(max = 460.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Anatomical Targets
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SteelElevated)
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("MÚSCULO ALVO PRINCIPAL", style = MaterialTheme.typography.labelSmall, color = ElectricVolt)
                        Text(exercise.primaryTarget, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        if (exercise.synergists.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("SINERGISTAS: ${exercise.synergists.joinToString(", ")}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                    }
                }

                // Parameters Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ParamChip("Faixa Ideal", exercise.recommendedReps)
                    ParamChip("Cadência TUT", exercise.tempoTUT)
                    ParamChip("Descanso", "${exercise.restSeconds}s")
                }

                // Hypertrophy Landmarks for this muscle
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SteelElevated)
                        .padding(10.dp)
                ) {
                    val m = exercise.muscleGroup
                    Text(
                        text = "Marcos Semanais (${m.displayName}): MEV ${m.mevSets} séries • MAV ${m.mavSets} séries • MRV ${m.mrvSets} séries • Recuperação ~${m.recoveryHours}h",
                        style = MaterialTheme.typography.labelSmall,
                        color = CyanPulse
                    )
                }

                // Execution Steps
                Text("EXECUÇÃO BIOMECÂNICA PASSO A PASSO", style = MaterialTheme.typography.labelLarge, color = ElectricVolt)
                exercise.executionSteps.forEachIndexed { idx, step ->
                    Row(verticalAlignment = Alignment.Top) {
                        Text("${idx + 1}. ", color = ElectricVolt, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        Text(step, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // Pro Biomechanics Tips
                Text("DICAS PRO DE HIPERTROFIA", style = MaterialTheme.typography.labelLarge, color = EmeraldPR)
                exercise.biomechanicsTips.forEach { tip ->
                    Row(verticalAlignment = Alignment.Top) {
                        Text("• ", color = EmeraldPR, fontWeight = FontWeight.Black)
                        Text(tip, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // Common Mistakes
                Text("ERROS COMUNS A EVITAR", style = MaterialTheme.typography.labelLarge, color = CrimsonPeak)
                exercise.commonMistakes.forEach { err ->
                    Row(verticalAlignment = Alignment.Top) {
                        Text("✕ ", color = CrimsonPeak, fontWeight = FontWeight.Bold)
                        Text(err, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricVolt)
            ) {
                Text("Entendido", color = ObsidianDeep, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun ParamChip(label: String, value: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SteelElevated)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Text(value, style = MaterialTheme.typography.labelLarge, color = ElectricVolt)
    }
}
