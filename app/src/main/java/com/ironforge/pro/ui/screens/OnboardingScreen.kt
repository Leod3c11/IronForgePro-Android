package com.ironforge.pro.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.R
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.model.*
import com.ironforge.pro.ui.components.*
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
    var step by remember { mutableIntStateOf(0) }

    var name by remember { mutableStateOf(if (initialProfile.name == "Atleta") "" else initialProfile.name) }
    var isMale by remember { mutableStateOf(initialProfile.isMale) }
    var ageFloat by remember { mutableFloatStateOf(initialProfile.age.toFloat().coerceIn(16f, 75f)) }
    var weightFloat by remember { mutableFloatStateOf(initialProfile.weightKg.toFloat().coerceIn(40f, 160f)) }
    var heightFloat by remember { mutableFloatStateOf(initialProfile.heightCm.toFloat().coerceIn(140f, 215f)) }
    var bfFloat by remember { mutableFloatStateOf(initialProfile.bodyFatPct.toFloat().coerceIn(5f, 42f)) }
    var selectedExp by remember { mutableStateOf(initialProfile.experience) }
    var selectedGoal by remember { mutableStateOf(initialProfile.goal) }

    var workoutMode by remember { mutableIntStateOf(0) }
    var selectedSplitId by remember { mutableStateOf(ExerciseRepository.defaultSplits.first().id) }
    var customWorkoutName by remember { mutableStateOf("Meu Treino A — Personalizado") }
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

    val totalSteps = 4

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.bg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // BeFit-Inspired Top Navigation & Smooth Segmented Progress Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (step > 0) {
                    Surface(
                        modifier = Modifier
                            .size(38.dp)
                            .clickable { step -= 1 },
                        shape = CircleShape,
                        color = palette.tileElevated,
                        border = BorderStroke(1.dp, palette.hairline)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Voltar",
                                tint = palette.textPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Brush.linearGradient(listOf(palette.primary, palette.secondary))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = palette.onPrimaryAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "IRONFORGE PRO",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                            color = palette.textPrimary,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Text(
                    text = "PASSO ${step + 1} DE $totalSteps",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                    color = palette.primaryGlow,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Clean BeFit Segmented Progress Pill Track
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (i in 0 until totalSteps) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (i <= step) {
                                    Brush.horizontalGradient(listOf(palette.primary, palette.secondary))
                                } else {
                                    Brush.horizontalGradient(listOf(palette.tileElevated, palette.tileElevated))
                                }
                            )
                    )
                }
            }
        }

        // Main Step Content
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            AnimatedContent(
                targetState = step,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "befitOnboardingStep"
            ) { currentStep ->
                when (currentStep) {
                    // =========================================================================
                    // STEP 0: BEFIT HERO ENTRANCE + VISUAL GENDER SELECTION + ATHLETE NAME
                    // =========================================================================
                    0 -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 20.dp)
                        ) {
                            item {
                                Column {
                                    Text(
                                        text = "Vamos personalizar seu plano de treino",
                                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                                        color = palette.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Biomecânica adaptada ao seu perfil fisiológico e metas reais.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = palette.textSecondary
                                    )
                                }
                            }

                            // Hero Studio Video Card (BeFit Entrance Video)
                            item {
                                EmbeddedStudioVideoCard(
                                    rawResId = R.raw.befit_welcome_pt,
                                    badge = "TREINO INTELIGENTE",
                                    title = "Ciência & Alta Performance",
                                    subtitle = "Execuções reais em vídeo e periodização automática",
                                    modifier = Modifier.fillMaxWidth(),
                                    height = 172.dp,
                                    initialPlaying = true
                                )
                            }

                            // Visual Gender Selection Cards (Inspired by BeFit Gender Screen)
                            item {
                                Text(
                                    text = "Selecione seu perfil biológico",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = palette.textPrimary
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    GenderVisualCard(
                                        title = "Masculino",
                                        subtitle = "Foco força & densidade",
                                        imageRes = R.drawable.visual_goal_bulk_male,
                                        selected = isMale,
                                        onClick = { isMale = true },
                                        modifier = Modifier.weight(1f)
                                    )
                                    GenderVisualCard(
                                        title = "Feminino",
                                        subtitle = "Foco definição & glúteos",
                                        imageRes = R.drawable.visual_goal_recomp_female,
                                        selected = !isMale,
                                        onClick = { isMale = false },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            // Clean Name Card
                            item {
                                Surface(
                                    shape = RoundedCornerShape(22.dp),
                                    color = palette.card,
                                    border = BorderStroke(1.dp, palette.hairline)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "Como devemos te chamar?",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = palette.textPrimary
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        OutlinedTextField(
                                            value = name,
                                            onValueChange = { name = it },
                                            placeholder = { Text("Digite seu nome ou apelido", color = palette.textMuted) },
                                            leadingIcon = {
                                                Icon(Icons.Default.Person, contentDescription = null, tint = palette.primary)
                                            },
                                            singleLine = true,
                                            shape = RoundedCornerShape(16.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // =========================================================================
                    // STEP 1: VISUAL GOAL CARDS (BEFIT STYLE) + EXPERIENCE LEVEL
                    // =========================================================================
                    1 -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            contentPadding = PaddingValues(bottom = 20.dp)
                        ) {
                            item {
                                Column {
                                    Text(
                                        text = "Qual é o seu objetivo principal?",
                                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                                        color = palette.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Ajustamos volume semanal, descanso e superávit/déficit calórico.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = palette.textSecondary
                                    )
                                }
                            }

                            items(FitnessGoal.entries) { goal ->
                                val selected = selectedGoal == goal
                                val goalImgRes = resolveGoalIllustrationRes(goal, isMale)
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedGoal = goal },
                                    shape = RoundedCornerShape(22.dp),
                                    color = if (selected) palette.tileElevated else palette.card,
                                    border = BorderStroke(
                                        width = if (selected) 2.dp else 1.dp,
                                        color = if (selected) palette.primary else palette.hairline
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = goalImgRes),
                                            contentDescription = goal.label,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(72.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                        )
                                        Spacer(modifier = Modifier.width(14.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = goal.label,
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                                color = palette.textPrimary
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = when (goal) {
                                                    FitnessGoal.LEAN_BULK -> "Ganho máximo de massa muscular com progressão de carga"
                                                    FitnessGoal.CUTTING_MODERATE -> "Definição muscular preservando força e massa magra"
                                                    FitnessGoal.RECOMP -> "Queima de gordura simultânea ao ganho de tônus"
                                                    FitnessGoal.STRENGTH -> "Força bruta nos levantamentos básicos e potência"
                                                },
                                                style = MaterialTheme.typography.bodySmall,
                                                color = palette.textSecondary
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = if (selected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                            contentDescription = null,
                                            tint = if (selected) palette.primary else palette.textMuted,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Seu nível atual na musculação",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = palette.textPrimary
                                )
                            }

                            items(ExperienceLevel.entries) { lvl ->
                                val selected = selectedExp == lvl
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedExp = lvl },
                                    shape = RoundedCornerShape(18.dp),
                                    color = if (selected) palette.primary.copy(alpha = 0.14f) else palette.card,
                                    border = BorderStroke(
                                        width = if (selected) 1.5.dp else 1.dp,
                                        color = if (selected) palette.primary else palette.hairline
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = when (lvl) {
                                                ExperienceLevel.BEGINNER -> Icons.Default.DirectionsWalk
                                                ExperienceLevel.INTERMEDIATE -> Icons.Default.FitnessCenter
                                                ExperienceLevel.ADVANCED -> Icons.Default.Whatshot
                                            },
                                            contentDescription = null,
                                            tint = if (selected) palette.primary else palette.textSecondary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(lvl.label, color = palette.textPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                            Text(lvl.subtitle, color = palette.textSecondary, fontSize = 12.sp)
                                        }
                                        if (selected) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = palette.primary)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // =========================================================================
                    // STEP 2: INTERACTIVE BIOMETRIC SLIDERS + BEFIT TRANSFORMATION PROOF
                    // =========================================================================
                    2 -> {
                        val ageInt = ageFloat.roundToInt()
                        val weightClean = (weightFloat * 10).roundToInt() / 10.0
                        val heightInt = heightFloat.roundToInt()
                        val bfClean = (bfFloat * 10).roundToInt() / 10.0
                        val bmi = weightClean / ((heightInt / 100.0) * (heightInt / 100.0))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            contentPadding = PaddingValues(bottom = 20.dp)
                        ) {
                            item {
                                Column {
                                    Text(
                                        text = "Suas medidas biométricas",
                                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                                        color = palette.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Deslize para ajustar rapidamente sem precisar digitar.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = palette.textSecondary
                                    )
                                }
                            }

                            // Interactive Slider Cards (BeFit Tactile Picker Style)
                            item {
                                BiometricSliderCard(
                                    icon = Icons.Default.Cake,
                                    label = "IDADE",
                                    valueDisplay = "$ageInt anos",
                                    value = ageFloat,
                                    valueRange = 16f..75f,
                                    onValueChange = { ageFloat = it }
                                )
                            }

                            item {
                                BiometricSliderCard(
                                    icon = Icons.Default.MonitorWeight,
                                    label = "PESO CORPORAL",
                                    valueDisplay = "${String.format("%.1f", weightClean)} kg",
                                    value = weightFloat,
                                    valueRange = 40f..160f,
                                    onValueChange = { weightFloat = it }
                                )
                            }

                            item {
                                BiometricSliderCard(
                                    icon = Icons.Default.Height,
                                    label = "ALTURA",
                                    valueDisplay = "$heightInt cm",
                                    value = heightFloat,
                                    valueRange = 140f..215f,
                                    onValueChange = { heightFloat = it }
                                )
                            }

                            item {
                                BiometricSliderCard(
                                    icon = Icons.Default.Speed,
                                    label = "PERCENTUAL DE GORDURA ESTIMADO",
                                    valueDisplay = "${String.format("%.1f", bfClean)}%",
                                    value = bfFloat,
                                    valueRange = 5f..42f,
                                    onValueChange = { bfFloat = it }
                                )
                            }

                            // Live Physiological Summary + BeFit Transformation Proof Card
                            item {
                                Surface(
                                    shape = RoundedCornerShape(22.dp),
                                    color = palette.card,
                                    border = BorderStroke(1.dp, palette.primary.copy(alpha = 0.35f))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Image(
                                                painter = painterResource(
                                                    id = if (isMale) R.drawable.befit_male_before else R.drawable.befit_female_before
                                                ),
                                                contentDescription = "Antes",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .width(52.dp)
                                                    .height(76.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                            )
                                            Image(
                                                painter = painterResource(
                                                    id = if (isMale) R.drawable.befit_male_after else R.drawable.befit_female_after
                                                ),
                                                contentDescription = "Depois",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .width(52.dp)
                                                    .height(76.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            HyperPillTag("IMC ${String.format("%.1f", bmi)}", palette.primaryGlow)
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = "Projeção de Evolução 12 Semanas",
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                color = palette.textPrimary
                                            )
                                            Text(
                                                text = "Protocolo calibrado para ${selectedGoal.label.lowercase()} com sobrecarga progressiva.",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = palette.textSecondary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // =========================================================================
                    // STEP 3: WORKOUT SPLIT + THEME PRESET + BEFIT READY BANNER
                    // =========================================================================
                    3 -> {
                        val finalWeight = ((weightFloat * 10).roundToInt() / 10.0).coerceIn(35.0, 220.0)
                        val finalHeight = heightFloat.roundToInt().coerceIn(120, 230)
                        val finalAge = ageFloat.roundToInt().coerceIn(14, 90)
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

                        val filteredExercises = remember(exerciseSearch, selectedMuscleFilter) {
                            ExerciseRepository.allExercises.filter { ex ->
                                val qOk = exerciseSearch.isBlank() || ex.name.contains(exerciseSearch, ignoreCase = true)
                                val mOk = selectedMuscleFilter == null || ex.muscleGroup == selectedMuscleFilter
                                qOk && mOk
                            }
                        }

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            contentPadding = PaddingValues(bottom = 20.dp)
                        ) {
                            // BeFit Plan Ready Hero Card
                            item {
                                Surface(
                                    shape = RoundedCornerShape(24.dp),
                                    color = palette.card,
                                    border = BorderStroke(1.5.dp, palette.primary.copy(alpha = 0.45f))
                                ) {
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        AnimatedGifView(
                                            assetPath = "gifs/plan_review_confetti.gif",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(130.dp)
                                                .align(Alignment.TopCenter)
                                        )
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                HyperPillTag("PLANO PRONTO", HyperEmerald)
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = "Tudo pronto, ${name.ifBlank { "Atleta" }}!",
                                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                                                    color = palette.textPrimary
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "$targetKcal kcal/dia • ${proteinG}g Prot • ${carbG}g Carb • ${fatG}g Gord",
                                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                                    color = palette.primaryGlow
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Image(
                                                painter = painterResource(
                                                    id = if (isMale) R.drawable.befit_checkpoint_male else R.drawable.befit_checkpoint_female
                                                ),
                                                contentDescription = null,
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier.size(84.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Quick Theme Selector Row (AMOLED / Light / Titanium)
                            item {
                                Column {
                                    Text(
                                        text = "Visual do Aplicativo",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = palette.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        items(AppThemePreset.entries) { preset ->
                                            val sel = currentTheme == preset
                                            Surface(
                                                modifier = Modifier.clickable { onPreviewTheme(preset) },
                                                shape = CircleShape,
                                                color = if (sel) palette.primary else palette.card,
                                                border = BorderStroke(1.dp, if (sel) palette.primary else palette.hairline)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(12.dp)
                                                            .clip(CircleShape)
                                                            .background(Color(preset.bgHex))
                                                            .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = preset.title,
                                                        color = if (sel) palette.onPrimaryAccent else palette.textPrimary,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 12.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Workout Split Mode Toggle
                            item {
                                Column {
                                    Text(
                                        text = "Escolha sua Divisão de Treino",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = palette.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        FilterChip(
                                            selected = workoutMode == 0,
                                            onClick = { workoutMode = 0 },
                                            shape = CircleShape,
                                            label = { Text("Divisão Pronta") }
                                        )
                                        FilterChip(
                                            selected = workoutMode == 1,
                                            onClick = { workoutMode = 1 },
                                            shape = CircleShape,
                                            label = { Text("Personalizar (${customExerciseIds.size})") }
                                        )
                                    }
                                }
                            }

                            if (workoutMode == 0) {
                                items(ExerciseRepository.defaultSplits) { split ->
                                    val isSel = selectedSplitId == split.id
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedSplitId = split.id },
                                        shape = RoundedCornerShape(20.dp),
                                        color = if (isSel) palette.tileElevated else palette.card,
                                        border = BorderStroke(
                                            width = if (isSel) 2.dp else 1.dp,
                                            color = if (isSel) palette.primary else palette.hairline
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(14.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                HyperPillTag("${split.frequencyPerWeek}x / SEMANA", palette.primaryGlow)
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(split.name, style = MaterialTheme.typography.titleMedium, color = palette.textPrimary)
                                                Text(split.subtitle, style = MaterialTheme.typography.bodySmall, color = palette.textSecondary)
                                            }
                                            Icon(
                                                imageVector = if (isSel) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                                contentDescription = null,
                                                tint = if (isSel) palette.primary else palette.textMuted
                                            )
                                        }
                                    }
                                }
                            } else {
                                item {
                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = palette.card,
                                        border = BorderStroke(1.dp, palette.hairline)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            OutlinedTextField(
                                                value = customWorkoutName,
                                                onValueChange = { customWorkoutName = it },
                                                label = { Text("Nome da Ficha") },
                                                singleLine = true,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            OutlinedTextField(
                                                value = exerciseSearch,
                                                onValueChange = { exerciseSearch = it },
                                                label = { Text("Buscar entre ${ExerciseRepository.allExercises.size} exercícios...") },
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
                                }

                                items(filteredExercises) { ex ->
                                    val checked = customExerciseIds.contains(ex.id)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (checked) palette.primary.copy(alpha = 0.14f) else palette.card)
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
                                        ExerciseVisualThumb(exercise = ex, size = 40.dp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(ex.name, color = palette.textPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                            Text("${ex.muscleGroup.displayName} • ${ex.recommendedReps}", color = palette.textSecondary, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // BeFit Full-Width Pill Bottom CTA
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Button(
                onClick = {
                    if (step < totalSteps - 1) {
                        step += 1
                    } else {
                        val profile = UserProfile(
                            name = name.ifBlank { "Atleta" },
                            isMale = isMale,
                            age = ageFloat.roundToInt().coerceIn(14, 90),
                            weightKg = ((weightFloat * 10).roundToInt() / 10.0).coerceIn(35.0, 220.0),
                            heightCm = heightFloat.roundToInt().coerceIn(120, 230).toDouble(),
                            bodyFatPct = ((bfFloat * 10).roundToInt() / 10.0).coerceIn(3.0, 50.0),
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
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                shape = CircleShape
            ) {
                Text(
                    text = if (step < totalSteps - 1) "CONTINUAR" else "COMEÇAR MEU TREINO",
                    color = palette.onPrimaryAccent,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = if (step < totalSteps - 1) Icons.Default.ArrowForward else Icons.Default.Check,
                    contentDescription = null,
                    tint = palette.onPrimaryAccent
                )
            }
        }
    }
}

@Composable
private fun GenderVisualCard(
    title: String,
    subtitle: String,
    imageRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalIronPalette.current
    Surface(
        modifier = modifier
            .height(176.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        color = palette.card,
        border = BorderStroke(
            width = if (selected) 2.5.dp else 1.dp,
            color = if (selected) palette.primary else palette.hairline
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color(0x66060910),
                                Color(0xE6060910)
                            )
                        )
                    )
            )
            if (selected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = palette.primary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(24.dp)
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.82f),
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun BiometricSliderCard(
    icon: ImageVector,
    label: String,
    valueDisplay: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    val palette = LocalIronPalette.current
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = palette.card,
        border = BorderStroke(1.dp, palette.hairline)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, tint = palette.primary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = palette.textSecondary
                    )
                }
                Text(
                    text = valueDisplay,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    color = palette.primaryGlow
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                colors = SliderDefaults.colors(
                    thumbColor = palette.primary,
                    activeTrackColor = palette.primary,
                    inactiveTrackColor = palette.tileElevated
                )
            )
        }
    }
}
