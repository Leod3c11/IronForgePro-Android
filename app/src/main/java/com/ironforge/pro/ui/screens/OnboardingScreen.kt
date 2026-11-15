package com.ironforge.pro.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.model.*
import com.ironforge.pro.ui.components.HyperPillTag
import com.ironforge.pro.ui.components.HyperSquircleCard
import com.ironforge.pro.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun OnboardingScreen(
    initialProfile: UserProfile,
    currentTheme: AppThemePreset,
    onPreviewTheme: (AppThemePreset) -> Unit,
    onComplete: (UserProfile, AppThemePreset, String?, List<String>) -> Unit
) {
    val palette = LocalIronPalette.current
    var step by remember { mutableIntStateOf(0) } // 0..3

    // Step 1: Athlete Bio State
    var name by remember { mutableStateOf(if (initialProfile.name == "Atleta") "" else initialProfile.name) }
    var isMale by remember { mutableStateOf(initialProfile.isMale) }
    var ageInput by remember { mutableStateOf(initialProfile.age.toString()) }
    var weightInput by remember { mutableStateOf(initialProfile.weightKg.toString()) }
    var heightInput by remember { mutableStateOf(initialProfile.heightCm.toString()) }
    var bfInput by remember { mutableStateOf(initialProfile.bodyFatPct.toString()) }
    var selectedExp by remember { mutableStateOf(initialProfile.experience) }
    var selectedGoal by remember { mutableStateOf(initialProfile.goal) }

    // Step 3: Workout Setup State
    var workoutMode by remember { mutableIntStateOf(0) } // 0 = Split Científico, 1 = Montar Meu Treino
    var selectedSplitId by remember { mutableStateOf(ExerciseRepository.defaultSplits.first().id) }
    var customWorkoutName by remember { mutableStateOf("Meu Treino A — Full Performance") }
    var exerciseSearch by remember { mutableStateOf("") }
    var selectedMuscleFilter by remember { mutableStateOf<MuscleGroup?>(null) }
    val customExerciseIds = remember {
        mutableStateListOf(
            "chest_bench_barbell",
            "back_lat_pulldown",
            "shld_overhead_press_dumbbell",
            "quad_high_bar_squat",
            "bi_barbell_ez_curl",
            "tri_cable_pushdown_rope"
        )
    }

    val stepTitles = listOf(
        "1. Cadastro & Biometria",
        "2. Temas Android 17",
        "3. Monte seu Treino",
        "4. Calibração Final"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HyperBgDark)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        // Top Wizard Progress Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.linearGradient(listOf(palette.primary, palette.secondary))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = palette.onPrimaryAccent, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "CONFIGURAÇÃO INICIAL",
                        style = MaterialTheme.typography.labelSmall,
                        color = palette.primaryGlow
                    )
                    Text(
                        text = stepTitles[step],
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                }
            }

            HyperPillTag("Etapa ${step + 1}/4", palette.secondary)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Segmented HyperOS Progress Pill Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (i in 0..3) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(5.dp)
                        .clip(CircleShape)
                        .background(
                            if (i <= step) palette.primary else HyperTileElevated
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step Body
        Box(modifier = Modifier.weight(1f)) {
            AnimatedContent(
                targetState = step,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "onboardingStep"
            ) { currentStep ->
                when (currentStep) {
                    0 -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            item {
                                HyperSquircleCard(accentGlow = true) {
                                    Text("Identificação do Atleta", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                                    Text("Esses dados calibram suas calorias, macros e cargas iniciais.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = name,
                                        onValueChange = { name = it },
                                        label = { Text("Seu Nome ou Apelido") },
                                        placeholder = { Text("Ex.: Leonardo") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text("Sexo Biológico:", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        FilterChip(
                                            selected = isMale,
                                            onClick = { isMale = true },
                                            shape = CircleShape,
                                            label = { Text("Masculino") }
                                        )
                                        FilterChip(
                                            selected = !isMale,
                                            onClick = { isMale = false },
                                            shape = CircleShape,
                                            label = { Text("Feminino") }
                                        )
                                    }
                                }
                            }

                            item {
                                HyperSquircleCard {
                                    Text("Medidas Corporais Atuais", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        OutlinedTextField(
                                            value = ageInput,
                                            onValueChange = { ageInput = it },
                                            label = { Text("Idade") },
                                            singleLine = true,
                                            modifier = Modifier.weight(1f)
                                        )
                                        OutlinedTextField(
                                            value = weightInput,
                                            onValueChange = { weightInput = it },
                                            label = { Text("Peso (kg)") },
                                            singleLine = true,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        OutlinedTextField(
                                            value = heightInput,
                                            onValueChange = { heightInput = it },
                                            label = { Text("Altura (cm)") },
                                            singleLine = true,
                                            modifier = Modifier.weight(1f)
                                        )
                                        OutlinedTextField(
                                            value = bfInput,
                                            onValueChange = { bfInput = it },
                                            label = { Text("% Gordura (Est.)") },
                                            singleLine = true,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }

                            item {
                                HyperSquircleCard {
                                    Text("Nível de Experiência", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    ExperienceLevel.entries.forEach { lvl ->
                                        val selected = selectedExp == lvl
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(if (selected) palette.primary.copy(alpha = 0.16f) else HyperTileElevated)
                                                .clickable { selectedExp = lvl }
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(selected = selected, onClick = { selectedExp = lvl })
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column {
                                                Text(lvl.label, color = TextPrimary, fontWeight = FontWeight.Bold)
                                                Text(lvl.subtitle, color = TextSecondary, fontSize = 12.sp)
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                HyperSquircleCard {
                                    Text("Objetivo Principal", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    FitnessGoal.entries.forEach { goal ->
                                        val selected = selectedGoal == goal
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(if (selected) palette.primary.copy(alpha = 0.16f) else HyperTileElevated)
                                                .clickable { selectedGoal = goal }
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(selected = selected, onClick = { selectedGoal = goal })
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(goal.label, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            item {
                                HyperSquircleCard(accentGlow = true) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Palette, contentDescription = null, tint = palette.primary)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Motor de Temas Android 17", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Toque em qualquer paleta abaixo para aplicar as cores em tempo real em todo o aplicativo (botões, gráficos, Ilha Dinâmica e vídeos).",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                }
                            }

                            items(AppThemePreset.entries) { preset ->
                                val isSelected = currentTheme == preset
                                val swatchPrimary = Color(preset.primaryHex)
                                val swatchSecondary = Color(preset.secondaryHex)
                                val swatchTertiary = Color(preset.tertiaryHex)

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onPreviewTheme(preset) },
                                    shape = RoundedCornerShape(22.dp),
                                    color = if (isSelected) HyperTileElevated else HyperCardDark,
                                    border = BorderStroke(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) palette.primary else HyperHairline
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = preset.title,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = TextPrimary
                                                )
                                                if (preset.isDynamicWallpaper) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    HyperPillTag("WALLPAPER", palette.secondary)
                                                }
                                            }
                                            Text(
                                                text = preset.subtitle,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = TextSecondary
                                            )
                                        }

                                        // Android 17 Material You Tri-Tone Swatch Circle
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Box(modifier = Modifier.size(18.dp).clip(CircleShape).background(swatchPrimary))
                                            Box(modifier = Modifier.size(18.dp).clip(CircleShape).background(swatchSecondary))
                                            Box(modifier = Modifier.size(18.dp).clip(CircleShape).background(swatchTertiary))
                                            if (isSelected) {
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = palette.primary)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        val filteredExercises = remember(exerciseSearch, selectedMuscleFilter) {
                            ExerciseRepository.allExercises.filter { ex ->
                                val qOk = exerciseSearch.isBlank() || ex.name.contains(exerciseSearch, ignoreCase = true)
                                val mOk = selectedMuscleFilter == null || ex.muscleGroup == selectedMuscleFilter
                                qOk && mOk
                            }
                        }

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            item {
                                HyperSquircleCard(accentGlow = true) {
                                    Text("Escolha ou Monte seu Treino", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        FilterChip(
                                            selected = workoutMode == 0,
                                            onClick = { workoutMode = 0 },
                                            shape = CircleShape,
                                            label = { Text("Divisão Científica Pronta") }
                                        )
                                        FilterChip(
                                            selected = workoutMode == 1,
                                            onClick = { workoutMode = 1 },
                                            shape = CircleShape,
                                            label = { Text("Montar Minha Ficha (${customExerciseIds.size})") }
                                        )
                                    }
                                }
                            }

                            if (workoutMode == 0) {
                                items(ExerciseRepository.defaultSplits) { split ->
                                    val isSel = selectedSplitId == split.id
                                    HyperSquircleCard(
                                        accentGlow = isSel,
                                        containerColor = if (isSel) HyperTileElevated else HyperCardDark,
                                        onClick = { selectedSplitId = split.id }
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            HyperPillTag("${split.frequencyPerWeek}x / SEMANA", palette.primaryGlow)
                                            if (isSel) {
                                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = palette.primary)
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(split.name, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                                        Text(split.description, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                                    }
                                }
                            } else {
                                item {
                                    HyperSquircleCard {
                                        OutlinedTextField(
                                            value = customWorkoutName,
                                            onValueChange = { customWorkoutName = it },
                                            label = { Text("Nome da Sua Ficha") },
                                            singleLine = true,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        OutlinedTextField(
                                            value = exerciseSearch,
                                            onValueChange = { exerciseSearch = it },
                                            label = { Text("Buscar exercícios...") },
                                            singleLine = true,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            item {
                                                FilterChip(
                                                    selected = selectedMuscleFilter == null,
                                                    onClick = { selectedMuscleFilter = null },
                                                    shape = CircleShape,
                                                    label = { Text("Todos", fontSize = 11.sp) }
                                                )
                                            }
                                            items(MuscleGroup.entries) { m ->
                                                FilterChip(
                                                    selected = selectedMuscleFilter == m,
                                                    onClick = { selectedMuscleFilter = if (selectedMuscleFilter == m) null else m },
                                                    shape = CircleShape,
                                                    label = { Text(m.shortName, fontSize = 11.sp) }
                                                )
                                            }
                                        }
                                    }
                                }

                                items(filteredExercises) { ex ->
                                    val checked = customExerciseIds.contains(ex.id)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (checked) palette.primary.copy(alpha = 0.14f) else HyperCardDark)
                                            .clickable {
                                                if (checked) customExerciseIds.remove(ex.id) else customExerciseIds.add(ex.id)
                                            }
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = checked,
                                            onCheckedChange = {
                                                if (it) customExerciseIds.add(ex.id) else customExerciseIds.remove(ex.id)
                                            }
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(ex.name, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                            Text("${ex.muscleGroup.displayName} • ${ex.recommendedReps}", color = TextSecondary, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    3 -> {
                        val finalWeight = (weightInput.toDoubleOrNull() ?: 80.0).coerceIn(35.0, 220.0)
                        val finalHeight = (heightInput.toDoubleOrNull() ?: 178.0).coerceIn(120.0, 230.0)
                        val finalAge = (ageInput.toIntOrNull() ?: 26).coerceIn(14, 90)
                        val bmr = if (isMale) {
                            (10.0 * finalWeight) + (6.25 * finalHeight) - (5.0 * finalAge) + 5.0
                        } else {
                            (10.0 * finalWeight) + (6.25 * finalHeight) - (5.0 * finalAge) - 161.0
                        }
                        val tdee = bmr * 1.55
                        val targetKcal = (tdee + selectedGoal.kcalOffset).roundToInt()
                        val proteinG = (finalWeight * selectedGoal.proteinPerKg).roundToInt()
                        val fatG = (finalWeight * 0.85).roundToInt()
                        val carbG = ((targetKcal - proteinG * 4 - fatG * 9).coerceAtLeast(200) / 4.0).roundToInt()
                        val chosenSplit = ExerciseRepository.defaultSplits.find { it.id == selectedSplitId }
                            ?: ExerciseRepository.defaultSplits.first()

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            item {
                                HyperSquircleCard(accentGlow = true) {
                                    Text(
                                        text = "Tudo Pronto, ${name.ifBlank { "Atleta" }}!",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Seu perfil fisiológico, tema Android 17 e rotina de treino foram calibrados.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        HyperPillTag("${finalWeight} kg", palette.primaryGlow)
                                        HyperPillTag("${finalHeight.toInt()} cm", palette.secondary)
                                        HyperPillTag(selectedExp.label, HyperEmerald)
                                    }
                                }
                            }

                            item {
                                HyperSquircleCard {
                                    Text("Metas Nutricionais Calculadas", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("$targetKcal kcal / dia", style = MaterialTheme.typography.displayLarge, color = palette.primary)
                                    Text("TMB: ${bmr.roundToInt()} kcal • GET: ${tdee.roundToInt()} kcal", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        HyperPillTag("Proteína: ${proteinG}g", HyperEmerald)
                                        HyperPillTag("Carbo: ${carbG}g", palette.primaryGlow)
                                        HyperPillTag("Gordura: ${fatG}g", palette.secondary)
                                    }
                                }
                            }

                            item {
                                HyperSquircleCard {
                                    Text("Treino & Visual Aplicados", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (workoutMode == 1) "Ficha Personalizada: $customWorkoutName (${customExerciseIds.size} exercícios)"
                                               else "Divisão Ativa: ${chosenSplit.name}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = palette.primaryGlow
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Tema Ativo: ${currentTheme.title}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = palette.secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Navigation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (step > 0) {
                OutlinedButton(
                    onClick = { step -= 1 },
                    modifier = Modifier
                        .weight(0.42f)
                        .height(52.dp),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Voltar", fontWeight = FontWeight.Bold)
                }
            }

            Button(
                onClick = {
                    if (step < 3) {
                        step += 1
                    } else {
                        val profile = UserProfile(
                            name = name.ifBlank { "Atleta" },
                            isMale = isMale,
                            age = (ageInput.toIntOrNull() ?: 26).coerceIn(14, 90),
                            weightKg = (weightInput.toDoubleOrNull() ?: 80.0).coerceIn(35.0, 220.0),
                            heightCm = (heightInput.toDoubleOrNull() ?: 178.0).coerceIn(120.0, 230.0),
                            bodyFatPct = (bfInput.toDoubleOrNull() ?: 13.5).coerceIn(3.0, 50.0),
                            experience = selectedExp,
                            goal = selectedGoal,
                            selectedSplitId = selectedSplitId
                        )
                        onComplete(
                            profile,
                            currentTheme,
                            if (workoutMode == 1) customWorkoutName else null,
                            if (workoutMode == 1) customExerciseIds.toList() else emptyList()
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                shape = CircleShape
            ) {
                Text(
                    text = if (step < 3) "CONTINUAR" else "ENTRAR NO IRONFORGE PRO",
                    color = palette.onPrimaryAccent,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = if (step < 3) Icons.Default.ArrowForward else Icons.Default.Check,
                    contentDescription = null,
                    tint = palette.onPrimaryAccent
                )
            }
        }
    }
}
