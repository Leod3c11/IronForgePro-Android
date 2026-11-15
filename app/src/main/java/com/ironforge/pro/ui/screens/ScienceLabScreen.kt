package com.ironforge.pro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.model.MuscleGroup
import com.ironforge.pro.model.ScienceArticle
import com.ironforge.pro.ui.components.HyperPillTag
import com.ironforge.pro.ui.components.HyperSquircleCard
import com.ironforge.pro.ui.components.OlympicPlateVisualizer
import com.ironforge.pro.ui.components.SectionHeader
import com.ironforge.pro.ui.theme.*
import com.ironforge.pro.viewmodel.IronForgeUiState
import com.ironforge.pro.viewmodel.IronForgeViewModel
import kotlin.math.roundToInt

@Composable
fun ScienceLabScreen(
    state: IronForgeUiState,
    viewModel: IronForgeViewModel
) {
    val palette = LocalIronPalette.current
    val subTabs = listOf("1RM & Zonas", "Anilhas", "Dieta & Macros", "Volume MEV/MRV", "Guias")
    var selectedTab by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HyperBgDark),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionHeader(
                title = "Laboratório de Ciência",
                subtitle = "Calculadoras de força, anilhas olímpicas, dieta e volume"
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(subTabs.size) { index ->
                    FilterChip(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        shape = CircleShape,
                        label = { Text(subTabs[index], fontWeight = FontWeight.Bold) },
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

        when (selectedTab) {
            0 -> {
                item {
                    OneRmCalculatorSection(
                        weightInput = state.oneRmWeightInput,
                        repsInput = state.oneRmRepsInput,
                        onUpdate = { w, r -> viewModel.updateOneRmInputs(w, r) }
                    )
                }
            }
            1 -> {
                item {
                    PlateCalculatorSection(
                        targetWeightInput = state.plateTargetWeight,
                        onUpdateWeight = { viewModel.updatePlateTargetWeight(it) }
                    )
                }
            }
            2 -> {
                item {
                    NutritionMacroCalculatorSection(
                        state = state,
                        viewModel = viewModel
                    )
                }
            }
            3 -> {
                item {
                    HyperSquircleCard(accentGlow = true) {
                        Text(
                            text = "Planejador de Volume Semanal (Séries Válidas)",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Compare suas séries semanais por grupo muscular com os marcos científicos MEV, MAV e MRV.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
                items(MuscleGroup.entries) { muscle ->
                    val currentSets = state.weeklySetsEstimator[muscle] ?: muscle.mavSets
                    VolumeLandmarkCard(
                        muscle = muscle,
                        currentSets = currentSets,
                        onChangeSets = { viewModel.updateWeeklyVolumeEstimator(muscle, it) }
                    )
                }
            }
            4 -> {
                items(ExerciseRepository.scienceArticles) { article ->
                    ScienceArticleCard(article = article)
                }
            }
        }
    }
}

@Composable
private fun PlateCalculatorSection(
    targetWeightInput: String,
    onUpdateWeight: (String) -> Unit
) {
    val weight = (targetWeightInput.toDoubleOrNull() ?: 60.0).coerceIn(20.0, 400.0)

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        HyperSquircleCard(accentGlow = true) {
            Text("Calculadora Visual de Anilhas Olímpicas", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
            Text("Considera barra olímpica oficial de 20 kg", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = targetWeightInput,
                onValueChange = onUpdateWeight,
                label = { Text("Carga Total Desejada (kg)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(40, 60, 80, 100, 120, 160).forEach { preset ->
                    FilterChip(
                        selected = weight.toInt() == preset,
                        onClick = { onUpdateWeight(preset.toString()) },
                        shape = CircleShape,
                        label = { Text("${preset}kg", fontSize = 11.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            OlympicPlateVisualizer(totalWeightKg = weight)
        }
    }
}

@Composable
private fun OneRmCalculatorSection(
    weightInput: String,
    repsInput: String,
    onUpdate: (String, String) -> Unit
) {
    val palette = LocalIronPalette.current
    val weight = (weightInput.toDoubleOrNull() ?: 60.0).coerceAtLeast(1.0)
    val reps = (repsInput.toIntOrNull() ?: 8).coerceIn(1, 30)

    val epley1Rm = IronForgeViewModel.estimate1RM(weight, reps)
    val brzycki1Rm = if (reps == 1) weight else ((weight * (36.0 / (37.0 - reps))) * 10.0).roundToInt() / 10.0

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        HyperSquircleCard(accentGlow = true) {
            Text("Calculadora de Força Máxima (1RM)", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { onUpdate(it, repsInput) },
                    label = { Text("Carga (kg)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = repsInput,
                    onValueChange = { onUpdate(weightInput, it) },
                    label = { Text("Repetições") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("1RM EPLEY", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Text("${epley1Rm} kg", style = MaterialTheme.typography.headlineMedium, color = palette.primary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("1RM BRZYCKI", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Text("${brzycki1Rm} kg", style = MaterialTheme.typography.headlineMedium, color = palette.secondary)
                }
            }
        }

        HyperSquircleCard {
            Text("Tabela de Zonas de Intensidade", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            val zones = listOf(
                Triple("95% (1–2 reps)", "Força Máxima Neural", 0.95),
                Triple("90% (3–4 reps)", "Força & Potência", 0.90),
                Triple("85% (5–6 reps)", "Hipertrofia Miofibrilar / Top Set", 0.85),
                Triple("80% (7–8 reps)", "Hipertrofia & Tensão Mecânica", 0.80),
                Triple("75% (9–11 reps)", "Hipertrofia Clássica (Zona Ouro)", 0.75),
                Triple("70% (12–14 reps)", "Hipertrofia & Estresse Metabólico", 0.70),
                Triple("60% (15–20 reps)", "Resistência & Capilarização", 0.60)
            )
            zones.forEachIndexed { idx, (pctLabel, desc, mult) ->
                val loadKg = ((epley1Rm * mult) * 2.0).roundToInt() / 2.0
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(pctLabel, style = MaterialTheme.typography.labelLarge, color = TextPrimary)
                        Text(desc, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    }
                    HyperPillTag("$loadKg kg", palette.primaryGlow)
                }
                if (idx < zones.lastIndex) {
                    HorizontalDivider(color = palette.hairline.copy(alpha = 0.5f))
                }
            }
        }
    }
}

@Composable
private fun NutritionMacroCalculatorSection(
    state: IronForgeUiState,
    viewModel: IronForgeViewModel
) {
    val palette = LocalIronPalette.current
    val weight = (state.userWeightKg.toDoubleOrNull() ?: 75.0).coerceIn(35.0, 220.0)
    val height = (state.userHeightCm.toDoubleOrNull() ?: 175.0).coerceIn(120.0, 230.0)
    val age = (state.userAge.toIntOrNull() ?: 25).coerceIn(14, 90)

    val bmr = if (state.isMale) {
        (10.0 * weight) + (6.25 * height) - (5.0 * age) + 5.0
    } else {
        (10.0 * weight) + (6.25 * height) - (5.0 * age) - 161.0
    }

    val activityMultipliers = listOf(1.2, 1.375, 1.55, 1.725)
    val activityLabels = listOf("Sedentário", "Leve (1-3x)", "Moderado (4-5x)", "Intenso (6x)")
    val tdee = bmr * activityMultipliers[state.activityLevelIndex.coerceIn(0, 3)]

    val goalOffsets = listOf(350.0, 0.0, -450.0, -650.0)
    val goalLabels = listOf("Bulking Limpo", "Manutenção", "Cutting Leve", "Cutting Forte")
    val targetKcal = (tdee + goalOffsets[state.nutritionGoalIndex.coerceIn(0, 3)]).roundToInt()

    val proteinMultiplier = if (state.nutritionGoalIndex >= 2) 2.2 else 2.0
    val proteinGrams = (weight * proteinMultiplier).roundToInt()
    val fatGrams = (weight * 0.85).roundToInt()
    val remainingKcal = (targetKcal - (proteinGrams * 4) - (fatGrams * 9)).coerceAtLeast(200)
    val carbGrams = (remainingKcal / 4.0).roundToInt()
    val waterLiters = ((weight * 40.0) / 100.0).roundToInt() / 10.0

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        HyperSquircleCard(accentGlow = true) {
            Text("Calculadora Metabólica & Macronutrientes", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = state.isMale,
                    onClick = { viewModel.updateNutritionInputs(isMale = true) },
                    shape = CircleShape,
                    label = { Text("Masculino") }
                )
                FilterChip(
                    selected = !state.isMale,
                    onClick = { viewModel.updateNutritionInputs(isMale = false) },
                    shape = CircleShape,
                    label = { Text("Feminino") }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.userWeightKg,
                    onValueChange = { viewModel.updateNutritionInputs(weightKg = it) },
                    label = { Text("Peso (kg)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = state.userHeightCm,
                    onValueChange = { viewModel.updateNutritionInputs(heightCm = it) },
                    label = { Text("Altura (cm)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = state.userAge,
                    onValueChange = { viewModel.updateNutritionInputs(age = it) },
                    label = { Text("Idade") },
                    singleLine = true,
                    modifier = Modifier.weight(0.8f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text("Nível de Atividade Semanal:", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(activityLabels.size) { idx ->
                    FilterChip(
                        selected = state.activityLevelIndex == idx,
                        onClick = { viewModel.updateNutritionInputs(activityIndex = idx) },
                        shape = CircleShape,
                        label = { Text(activityLabels[idx], fontSize = 11.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text("Objetivo Físico:", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(goalLabels.size) { idx ->
                    FilterChip(
                        selected = state.nutritionGoalIndex == idx,
                        onClick = { viewModel.updateNutritionInputs(goalIndex = idx) },
                        shape = CircleShape,
                        label = { Text(goalLabels[idx], fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = palette.primary,
                            selectedLabelColor = palette.onPrimaryAccent
                        )
                    )
                }
            }
        }

        HyperSquircleCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("META CALÓRICA DIÁRIA", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Text("$targetKcal kcal", style = MaterialTheme.typography.displayLarge, color = palette.primary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("TMB: ${bmr.roundToInt()} kcal", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text("GET: ${tdee.roundToInt()} kcal", style = MaterialTheme.typography.labelSmall, color = palette.secondary)
                    Text("Água: ${waterLiters}L / dia", style = MaterialTheme.typography.labelSmall, color = HyperEmerald)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MacroBox(
                    title = "PROTEÍNAS",
                    grams = "${proteinGrams}g",
                    sub = "${proteinGrams * 4} kcal (${proteinMultiplier}g/kg)",
                    accent = HyperEmerald,
                    modifier = Modifier.weight(1f)
                )
                MacroBox(
                    title = "CARBOIDRATOS",
                    grams = "${carbGrams}g",
                    sub = "${carbGrams * 4} kcal",
                    accent = palette.primaryGlow,
                    modifier = Modifier.weight(1f)
                )
                MacroBox(
                    title = "GORDURAS",
                    grams = "${fatGrams}g",
                    sub = "${fatGrams * 9} kcal (0.85g/kg)",
                    accent = palette.secondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MacroBox(
    title: String,
    grams: String,
    sub: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(HyperTileElevated)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = MaterialTheme.typography.labelSmall, color = accent)
        Spacer(modifier = Modifier.height(4.dp))
        Text(grams, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
        Text(sub, style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 10.sp)
    }
}

@Composable
private fun VolumeLandmarkCard(
    muscle: MuscleGroup,
    currentSets: Int,
    onChangeSets: (Int) -> Unit
) {
    val palette = LocalIronPalette.current
    val (statusLabel, statusColor) = when {
        currentSets < muscle.mevSets -> "Sub-estímulo (< MEV)" to HyperAmber
        currentSets <= muscle.mavSets -> "Zona Ótima (MEV–MAV)" to HyperEmerald
        currentSets <= muscle.mrvSets -> "Alta Carga (MAV–MRV)" to palette.secondary
        else -> "Risco Overreaching (> MRV)" to HyperCrimson
    }

    HyperSquircleCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(muscle.displayName, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Text(
                    text = "MEV: ${muscle.mevSets}  •  MAV: ${muscle.mavSets}  •  MRV: ${muscle.mrvSets}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(HyperTileElevated)
                        .clickable { onChangeSets(currentSets - 1) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("-", color = TextPrimary, fontWeight = FontWeight.Black)
                }
                Text(
                    text = "$currentSets séries",
                    style = MaterialTheme.typography.labelLarge,
                    color = palette.primaryGlow
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(HyperTileElevated)
                        .clickable { onChangeSets(currentSets + 1) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", color = palette.primaryGlow, fontWeight = FontWeight.Black)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HyperPillTag(statusLabel, statusColor)
    }
}

@Composable
private fun ScienceArticleCard(article: ScienceArticle) {
    val palette = LocalIronPalette.current
    var expanded by remember { mutableStateOf(false) }

    HyperSquircleCard(
        onClick = { expanded = !expanded },
        accentGlow = expanded
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HyperPillTag(article.category, palette.secondary)
            Text("${article.readTimeMinutes} min leitura", style = MaterialTheme.typography.labelSmall, color = TextMuted)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(article.title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(article.summary, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)

        if (expanded) {
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = palette.hairline)
            Spacer(modifier = Modifier.height(10.dp))

            Text("PONTOS-CHAVE PRÁTICOS", style = MaterialTheme.typography.labelSmall, color = palette.primaryGlow)
            Spacer(modifier = Modifier.height(6.dp))
            article.keyTakeaways.forEach { point ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("• ", color = palette.primary, fontWeight = FontWeight.Bold)
                    Text(point, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text("PROTOCOLO DETALHADO", style = MaterialTheme.typography.labelSmall, color = HyperEmerald)
            Spacer(modifier = Modifier.height(4.dp))
            Text(article.detailedProtocol, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        } else {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Toque para ler o protocolo completo →",
                style = MaterialTheme.typography.labelSmall,
                color = palette.primaryGlow
            )
        }
    }
}
