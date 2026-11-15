package com.ironforge.pro.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.R
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.model.BodyMeasurementRecord
import com.ironforge.pro.model.MuscleReadinessInfo
import com.ironforge.pro.model.WorkoutDay
import com.ironforge.pro.ui.components.*
import com.ironforge.pro.ui.theme.*
import com.ironforge.pro.viewmodel.IronForgeUiState
import com.ironforge.pro.viewmodel.IronForgeViewModel
import java.time.LocalDate
import kotlin.math.roundToInt

@Composable
fun DashboardScreen(
    state: IronForgeUiState,
    onOpenWorkoutBriefing: (WorkoutDay) -> Unit,
    onDismissSummary: () -> Unit,
    onSelectTheme: (AppThemePreset) -> Unit,
    onReopenOnboarding: () -> Unit,
    onOpenCalendar: () -> Unit,
    onAddMeasurement: (Double, Double, Double, Double, Double, Double, Double) -> Unit
) {
    val palette = LocalIronPalette.current
    var showMeasureDialog by remember { mutableStateOf(false) }
    var showThemeStudioDialog by remember { mutableStateOf(false) }
    var inspectedReadiness by remember { mutableStateOf<MuscleReadinessInfo?>(null) }
    var activeSyncPartner by remember { mutableStateOf("Health Connect") }

    val today = remember { LocalDate.now() }
    val todayIsoWeekday = today.dayOfWeek.value

    val totalWorkingSetsAll = state.sessionHistory.sumOf { it.totalWorkingSets }
    val totalPrs = state.sessionHistory.sumOf { it.prCount }

    val aggregatedMuscleSets = remember(state.sessionHistory) {
        val map = mutableMapOf<String, Int>()
        state.sessionHistory.take(6).forEach { rec ->
            rec.muscleSetsBreakdown.forEach { (k, v) ->
                map[k] = (map[k] ?: 0) + v
            }
        }
        map
    }

    val muscleReadinessList = remember(state.sessionHistory) {
        IronForgeViewModel.computeMultiMuscleReadiness(state.sessionHistory)
    }
    val avgReadiness = remember(muscleReadinessList) {
        muscleReadinessList.map { it.readinessPct }.average().roundToInt()
    }

    fun findWorkoutById(id: String?): WorkoutDay? {
        if (id.isNullOrBlank()) return null
        state.editedWorkoutDays[id]?.let { return it }
        state.customWorkouts.find { it.id == id }?.let { return it }
        ExerciseRepository.defaultSplits.forEach { split ->
            split.days.find { it.id == id }?.let { return it }
        }
        return null
    }

    val todayScheduledWorkout = remember(state.weeklySchedule, state.customWorkouts, state.editedWorkoutDays, todayIsoWeekday) {
        findWorkoutById(state.weeklySchedule[todayIsoWeekday])
    }
    val fallbackDefaultDay = ExerciseRepository.defaultSplits.first().days.first()
    val fallbackWorkout = state.customWorkouts.firstOrNull()
        ?: (state.editedWorkoutDays[fallbackDefaultDay.id] ?: fallbackDefaultDay)
    val heroWorkout = todayScheduledWorkout ?: fallbackWorkout

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.bg),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 104.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Upgraded Top App Bar with Athlete Avatar, PRO Badge & Theme Studio
        item {
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
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(palette.primary, palette.secondary))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.userProfile.name.take(1).uppercase(),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                            color = palette.onPrimaryAccent
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Olá, ${state.userProfile.name}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = palette.textPrimary,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(
                                        Brush.horizontalGradient(listOf(palette.primary, palette.secondary))
                                    )
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "PRO",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                    color = palette.onPrimaryAccent,
                                    fontSize = 9.sp
                                )
                            }
                        }
                        Text(
                            text = "${state.userProfile.goal.label} • ${state.userProfile.weightKg} kg",
                            style = MaterialTheme.typography.bodyMedium,
                            color = palette.textSecondary,
                            maxLines = 1
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    FilledIconButton(
                        onClick = { showThemeStudioDialog = true },
                        modifier = Modifier.size(42.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = palette.tileElevated)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Estúdio de Temas",
                            tint = palette.primary
                        )
                    }

                    Button(
                        onClick = { onOpenWorkoutBriefing(heroWorkout) },
                        colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = palette.onPrimaryAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Treinar",
                            color = palette.onPrimaryAccent,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // 2. Post-Workout Celebration Card with Animated Confetti GIF + 3D Trophy
        if (state.lastFinishedSession != null) {
            item {
                val summary = state.lastFinishedSession
                HyperSquircleCard(accentGlow = true, containerColor = palette.tileElevated) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        AnimatedGifView(
                            assetPath = "gifs/plan_review_confetti.gif",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(125.dp)
                                .align(Alignment.TopCenter)
                        )
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Image(
                                        painter = painterResource(id = R.drawable.visual_exercise_completed),
                                        contentDescription = "Treino Concluído",
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(RoundedCornerShape(14.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "TREINO CONCLUÍDO COM SUCESSO",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = HyperEmerald
                                        )
                                        Text(
                                            text = summary.workoutTitle,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = palette.textPrimary
                                        )
                                    }
                                }
                                IconButton(onClick = onDismissSummary) {
                                    Icon(Icons.Default.Close, contentDescription = "Fechar", tint = palette.textSecondary)
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                HyperPillTag("Tempo: ${summary.durationMinutes} min", palette.primaryGlow)
                                HyperPillTag("Séries: ${summary.totalWorkingSets}", palette.secondary)
                                HyperPillTag("PRs: ${summary.prCount}", HyperEmerald)
                            }
                        }
                    }
                }
            }
        }

        // 3. Today's Scheduled Workout Hero Card (OrbitingGlow + 3D Kettlebell + Full-Width CTA)
        item {
            val leadExercise = heroWorkout.exercises.firstOrNull()?.exerciseId?.let { ExerciseRepository.getExerciseById(it) }
            val badgeText = if (todayScheduledWorkout != null) {
                "TREINO DE HOJE • ${heroWorkout.estimatedMinutes} MIN"
            } else {
                "HOJE É DESCANSO • PRÓXIMO TREINO"
            }

            OrbitingGlowBanner(
                modifier = Modifier.clickable { onOpenWorkoutBriefing(heroWorkout) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            HyperPillTag(badgeText, palette.primaryGlow)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = heroWorkout.title,
                                style = MaterialTheme.typography.titleLarge,
                                color = palette.textPrimary
                            )
                            Text(
                                text = "${heroWorkout.exercises.size} exercícios • ${heroWorkout.focusSummary}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = palette.textSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Image(
                            painter = painterResource(id = R.drawable.visual_kettlebell_3d),
                            contentDescription = "Treino 3D",
                            modifier = Modifier.size(66.dp)
                        )
                    }

                    if (leadExercise != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        ExerciseMiniVideoPlayer(
                            exercise = leadExercise,
                            customVideoUri = state.customVideoUris[leadExercise.id],
                            height = 140.dp,
                            compact = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onOpenWorkoutBriefing(heroWorkout) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = palette.onPrimaryAccent
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "REVISAR & INICIAR TREINO",
                            color = palette.onPrimaryAccent,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            letterSpacing = 0.4.sp
                        )
                    }
                }
            }
        }

        // 4. Weekly Calendar Strip Widget
        item {
            HyperSquircleCard(onClick = onOpenCalendar) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = palette.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sua Semana de Treinos", style = MaterialTheme.typography.titleMedium, color = palette.textPrimary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Abrir Agenda", style = MaterialTheme.typography.labelSmall, color = palette.primaryGlow)
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = palette.primaryGlow, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val weekLabels = listOf("SEG", "TER", "QUA", "QUI", "SEX", "SÁB", "DOM")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    for (isoDay in 1..7) {
                        val isTodayCell = isoDay == todayIsoWeekday
                        val wDay = findWorkoutById(state.weeklySchedule[isoDay])
                        val tag = when {
                            wDay == null -> "OFF"
                            wDay.title.contains("Push", ignoreCase = true) -> "PUSH"
                            wDay.title.contains("Pull", ignoreCase = true) -> "PULL"
                            wDay.title.contains("Legs", ignoreCase = true) -> "LEGS"
                            wDay.title.contains("Upper", ignoreCase = true) -> "UP"
                            wDay.title.contains("Lower", ignoreCase = true) -> "LOW"
                            else -> "TRN"
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isTodayCell) palette.primary else palette.tileElevated
                                )
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = weekLabels[isoDay - 1],
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isTodayCell) palette.onPrimaryAccent else palette.textMuted,
                                fontSize = 9.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                color = when {
                                    isTodayCell -> palette.onPrimaryAccent
                                    wDay != null -> palette.primaryGlow
                                    else -> palette.textSecondary
                                },
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        // 5. 2x2 Bento Grid with Lottie Flame & Target Animations
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HyperSquircleCard(modifier = Modifier.weight(1.05f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("SÉRIES VÁLIDAS", style = MaterialTheme.typography.labelSmall, color = palette.textMuted)
                            LottieStudioAnimation(
                                assetPath = "lotties/flame.json",
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$totalWorkingSetsAll",
                            style = MaterialTheme.typography.displayLarge,
                            color = palette.textPrimary
                        )
                        Text(
                            text = if (totalWorkingSetsAll == 0) "Pronto para iniciar" else "Séries de trabalho concluídas",
                            style = MaterialTheme.typography.labelSmall,
                            color = palette.primaryGlow
                        )
                    }

                    HyperSquircleCard(modifier = Modifier.weight(0.88f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("PRONTIDÃO", style = MaterialTheme.typography.labelSmall, color = palette.textMuted)
                            LottieStudioAnimation(
                                assetPath = "lotties/target.json",
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$avgReadiness%",
                            style = MaterialTheme.typography.displayLarge,
                            color = HyperEmerald
                        )
                        Text(
                            text = "Global + Sinergias",
                            style = MaterialTheme.typography.labelSmall,
                            color = palette.textSecondary
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HyperSquircleCard(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("TREINOS", style = MaterialTheme.typography.labelSmall, color = palette.textMuted)
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = palette.secondary, modifier = Modifier.size(15.dp))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${state.sessionHistory.size} sessões",
                            style = MaterialTheme.typography.titleLarge,
                            color = palette.secondary
                        )
                    }

                    HyperSquircleCard(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("RECORDES (PR)", style = MaterialTheme.typography.labelSmall, color = palette.textMuted)
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = palette.tertiary, modifier = Modifier.size(15.dp))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$totalPrs marcas",
                            style = MaterialTheme.typography.titleLarge,
                            color = palette.tertiary
                        )
                    }
                }
            }
        }

        // 6. Studio Reels & Videos Carousel (Embedded MP4 clips from visual pack)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SectionHeader(
                    title = "Studio Reels & Execuções",
                    subtitle = "Clipes de treino, postura e comunidade IronForge"
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        EmbeddedStudioVideoCard(
                            rawResId = R.raw.studio_showcase_1,
                            badge = "STUDIO REEL 01",
                            title = "Alta Performance & Foco",
                            subtitle = "Controle de cadência e amplitude máxima",
                            modifier = Modifier.width(275.dp),
                            height = 165.dp,
                            initialPlaying = true
                        )
                    }
                    item {
                        EmbeddedStudioVideoCard(
                            rawResId = R.raw.studio_showcase_2,
                            badge = "STUDIO REEL 02",
                            title = "Técnica & Tensão Mecânica",
                            subtitle = "Execução limpa em cada repetição",
                            modifier = Modifier.width(275.dp),
                            height = 165.dp,
                            initialPlaying = false
                        )
                    }
                    item {
                        EmbeddedStudioVideoCard(
                            rawResId = R.raw.studio_community_1,
                            badge = "COMUNIDADE",
                            title = "Desafios & Consistência",
                            subtitle = "Acompanhe sua evolução semanal",
                            modifier = Modifier.width(275.dp),
                            height = 165.dp,
                            initialPlaying = false
                        )
                    }
                }
            }
        }

        // 7. Multi-Muscle Recovery Readiness Strip
        item {
            HyperSquircleCard {
                SectionHeader(
                    title = "Descanso Muscular & Sinergistas",
                    subtitle = "Considera músculos principais e sinergistas • Toque para detalhes"
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(muscleReadinessList) { info ->
                        val color = when {
                            info.readinessPct >= 90 -> HyperEmerald
                            info.readinessPct >= 65 -> palette.secondary
                            else -> HyperAmber
                        }
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(palette.tileElevated)
                                .clickable { inspectedReadiness = info }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${info.readinessPct}%",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                color = color
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = info.muscle.shortName,
                                style = MaterialTheme.typography.labelSmall,
                                color = palette.textPrimary
                            )
                            Text(
                                text = if (info.readinessPct >= 98) "Pronto" else info.roleSummary.substringBefore(" "),
                                style = MaterialTheme.typography.labelSmall,
                                color = palette.textMuted,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }
        }

        // 8. Wearables & Fitness Ecosystem Sync Card
        item {
            HyperSquircleCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        HyperPillTag("ECOSSISTEMA & WEARABLES", palette.secondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Sincronização Ativa: $activeSyncPartner",
                            style = MaterialTheme.typography.titleMedium,
                            color = palette.textPrimary
                        )
                        Text(
                            text = "Conecte seu relógio ou app para importar gasto calórico e frequência cardíaca.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = palette.textSecondary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.visual_watch_ultra),
                        contentDescription = "Smartwatch",
                        modifier = Modifier.size(68.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                val syncPartners = listOf(
                    "Google Fit" to R.drawable.ic_sync_google_fit,
                    "Garmin" to R.drawable.ic_sync_garmin,
                    "Fitbit" to R.drawable.ic_sync_fitbit,
                    "Strava" to R.drawable.ic_sync_strava
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    syncPartners.forEach { (partnerName, iconRes) ->
                        val isConnected = activeSyncPartner == partnerName
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isConnected) palette.primary.copy(alpha = 0.16f) else palette.tileElevated
                                )
                                .clickable { activeSyncPartner = partnerName }
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = iconRes),
                                contentDescription = partnerName,
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = partnerName,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isConnected) palette.primary else palette.textSecondary,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // 9. Anthropometry & Body Measurements
        item {
            val latest = state.bodyMeasurements.lastOrNull()
            HyperSquircleCard {
                SectionHeader(
                    title = "Suas Medidas Corporais",
                    subtitle = if (latest != null) "Atualizado em ${latest.dateFormatted}" else "Registre suas medidas",
                    trailing = {
                        TextButton(onClick = { showMeasureDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = palette.primary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Atualizar", color = palette.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                val curWeight = latest?.weightKg ?: state.userProfile.weightKg
                val curBf = latest?.bodyFatPct ?: state.userProfile.bodyFatPct
                val leanMass = (curWeight * (1.0 - curBf / 100.0) * 10).roundToInt() / 10.0

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MeasureStatBox("Peso", "${curWeight} kg")
                    MeasureStatBox("Gordura", "${curBf}%")
                    MeasureStatBox("Massa Magra", "$leanMass kg")
                    MeasureStatBox("Altura", "${state.userProfile.heightCm.toInt()} cm")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MeasureStatBox("Peitoral", if ((latest?.chestCm ?: 0.0) > 0.0) "${latest!!.chestCm} cm" else "—")
                    MeasureStatBox("Braço", if ((latest?.armCm ?: 0.0) > 0.0) "${latest!!.armCm} cm" else "—")
                    MeasureStatBox("Cintura", if ((latest?.waistCm ?: 0.0) > 0.0) "${latest!!.waistCm} cm" else "—")
                    MeasureStatBox("Coxa", if ((latest?.thighCm ?: 0.0) > 0.0) "${latest!!.thighCm} cm" else "—")
                }
            }
        }

        // 11. Hexagonal Muscle Balance Radar
        item {
            HyperSquircleCard {
                SectionHeader(
                    title = "Equilíbrio Muscular",
                    subtitle = "Distribuição real das suas séries válidas"
                )
                Spacer(modifier = Modifier.height(6.dp))
                MuscleBalanceRadarChart(muscleSets = aggregatedMuscleSets)
            }
        }

        // 12. Personal Records
        item {
            HyperSquircleCard {
                SectionHeader(
                    title = "Seus Recordes Pessoais (1RM Est.)",
                    subtitle = "Calculados automaticamente ao concluir suas séries"
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (state.exercisePrs.isEmpty()) {
                    Text(
                        text = "Nenhum recorde registrado ainda. Ao marcar suas séries como concluídas durante o treino, seu 1RM estimado aparecerá aqui!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.textSecondary
                    )
                } else {
                    val topPrs = state.exercisePrs.entries.sortedByDescending { it.value }.take(5)
                    topPrs.forEachIndexed { idx, entry ->
                        val exName = ExerciseRepository.getExerciseById(entry.key)?.name ?: entry.key
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "#${idx + 1}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = palette.primary
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = exName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = palette.textPrimary
                                )
                            }
                            HyperPillTag("${entry.value.roundToInt()} kg", HyperEmerald)
                        }
                        if (idx < topPrs.lastIndex) {
                            HorizontalDivider(color = palette.hairline.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }

        // 13. Recent Sessions
        item {
            SectionHeader(
                title = "Seu Histórico de Treinos",
                subtitle = "Sessões concluídas por você"
            )
        }

        if (state.sessionHistory.isEmpty()) {
            item {
                HyperSquircleCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = palette.secondary,
                            modifier = Modifier.size(34.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Você ainda não concluiu nenhum treino",
                            style = MaterialTheme.typography.titleMedium,
                            color = palette.textPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Toque em 'Treinar' acima para revisar o treino de hoje e iniciar.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = palette.textSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(state.sessionHistory.take(5)) { session ->
                HyperSquircleCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = session.workoutTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = palette.textPrimary
                            )
                            Text(
                                text = "${session.dateFormatted} • ${session.durationMinutes} min • ${session.totalWorkingSets} séries válidas",
                                style = MaterialTheme.typography.bodyMedium,
                                color = palette.textSecondary
                            )
                        }
                        HyperPillTag("${session.durationMinutes} min", palette.secondary)
                    }
                    if (session.highlightExercises.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Destaques: ${session.highlightExercises.joinToString("  •  ")}",
                            style = MaterialTheme.typography.labelSmall,
                            color = palette.textMuted
                        )
                    }
                }
            }
        }
    }

    // Muscle Readiness & Synergist Detail Modal
    if (inspectedReadiness != null) {
        val info = inspectedReadiness!!
        AlertDialog(
            onDismissRequest = { inspectedReadiness = null },
            containerColor = palette.card,
            shape = RoundedCornerShape(26.dp),
            title = {
                Column {
                    HyperPillTag(info.roleSummary, palette.primaryGlow)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${info.muscle.displayName} • ${info.readinessPct}%", color = palette.textPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (info.readinessPct >= 98) {
                            "Este grupamento está 100% recuperado e pronto para sobrecarga máxima."
                        } else {
                            "Fadiga residual ativa (inclui estímulo direto e como músculo sinergista). Tempo estimado para 100%: ~${info.remainingHours}h."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.textSecondary
                    )
                    if (info.contributingExercises.isNotEmpty()) {
                        Text("EXERCÍCIOS QUE ATIVARAM ESTE MÚSCULO:", style = MaterialTheme.typography.labelSmall, color = palette.secondary)
                        info.contributingExercises.forEach { src ->
                            Text("• $src", style = MaterialTheme.typography.bodyMedium, color = palette.textPrimary)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { inspectedReadiness = null },
                    colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                    shape = CircleShape
                ) {
                    Text("Entendido", color = palette.onPrimaryAccent, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Theme Studio Modal
    if (showThemeStudioDialog) {
        var filterStyle by remember { mutableStateOf<ThemeSurfaceStyle?>(null) }
        val presets = remember(filterStyle) {
            AppThemePreset.entries.filter { filterStyle == null || it.style == filterStyle }
        }

        AlertDialog(
            onDismissRequest = { showThemeStudioDialog = false },
            containerColor = palette.card,
            shape = RoundedCornerShape(28.dp),
            title = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = palette.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Estúdio de Temas", color = palette.textPrimary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        item {
                            FilterChip(
                                selected = filterStyle == null,
                                onClick = { filterStyle = null },
                                shape = CircleShape,
                                label = { Text("Todos", fontSize = 11.sp) }
                            )
                        }
                        items(ThemeSurfaceStyle.entries) { st ->
                            FilterChip(
                                selected = filterStyle == st,
                                onClick = { filterStyle = if (filterStyle == st) null else st },
                                shape = CircleShape,
                                label = { Text(st.label, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 390.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(presets) { preset ->
                        val selected = state.themePreset == preset
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectTheme(preset) },
                            shape = RoundedCornerShape(18.dp),
                            color = if (selected) palette.tileElevated else palette.bg,
                            border = BorderStroke(if (selected) 1.8.dp else 1.dp, if (selected) palette.primary else palette.hairline)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(preset.title, color = palette.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(preset.subtitle, color = palette.textSecondary, fontSize = 11.sp)
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(Color(preset.bgHex))
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(Color(preset.primaryHex))
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(Color(preset.secondaryHex))
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedButton(
                            onClick = {
                                showThemeStudioDialog = false
                                onReopenOnboarding()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = CircleShape
                        ) {
                            Icon(Icons.Default.PersonOutline, contentDescription = null, tint = palette.secondary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Editar Cadastro & Treino Inicial", color = palette.secondary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showThemeStudioDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                    shape = CircleShape
                ) {
                    Text("Aplicar", color = palette.onPrimaryAccent, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (showMeasureDialog) {
        BodyMeasurementDialog(
            initial = state.bodyMeasurements.lastOrNull(),
            fallbackWeight = state.userProfile.weightKg,
            fallbackBf = state.userProfile.bodyFatPct,
            onDismiss = { showMeasureDialog = false },
            onSave = { w, bf, ch, arm, waist, thigh, calf ->
                onAddMeasurement(w, bf, ch, arm, waist, thigh, calf)
                showMeasureDialog = false
            }
        )
    }
}

@Composable
private fun MeasureStatBox(label: String, value: String) {
    val palette = LocalIronPalette.current
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(palette.tileElevated)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = palette.textMuted)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, style = MaterialTheme.typography.labelLarge, color = palette.textPrimary)
    }
}

@Composable
private fun BodyMeasurementDialog(
    initial: BodyMeasurementRecord?,
    fallbackWeight: Double,
    fallbackBf: Double,
    onDismiss: () -> Unit,
    onSave: (Double, Double, Double, Double, Double, Double, Double) -> Unit
) {
    val palette = LocalIronPalette.current
    var weight by remember { mutableStateOf((initial?.weightKg ?: fallbackWeight).toString()) }
    var bf by remember { mutableStateOf((initial?.bodyFatPct ?: fallbackBf).toString()) }
    var chest by remember { mutableStateOf(if ((initial?.chestCm ?: 0.0) > 0.0) initial!!.chestCm.toString() else "") }
    var arm by remember { mutableStateOf(if ((initial?.armCm ?: 0.0) > 0.0) initial!!.armCm.toString() else "") }
    var waist by remember { mutableStateOf(if ((initial?.waistCm ?: 0.0) > 0.0) initial!!.waistCm.toString() else "") }
    var thigh by remember { mutableStateOf(if ((initial?.thighCm ?: 0.0) > 0.0) initial!!.thighCm.toString() else "") }
    var calf by remember { mutableStateOf(if ((initial?.calfCm ?: 0.0) > 0.0) initial!!.calfCm.toString() else "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = palette.card,
        shape = RoundedCornerShape(26.dp),
        title = { Text("Atualizar Suas Medidas", color = palette.textPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Peso (kg)") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = bf, onValueChange = { bf = it }, label = { Text("BF (%)") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = chest, onValueChange = { chest = it }, label = { Text("Peitoral (cm)") }, placeholder = { Text("Opcional") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = arm, onValueChange = { arm = it }, label = { Text("Braço (cm)") }, placeholder = { Text("Opcional") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = waist, onValueChange = { waist = it }, label = { Text("Cintura (cm)") }, placeholder = { Text("Opcional") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = thigh, onValueChange = { thigh = it }, label = { Text("Coxa (cm)") }, placeholder = { Text("Opcional") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                OutlinedTextField(value = calf, onValueChange = { calf = it }, label = { Text("Panturrilha (cm)") }, placeholder = { Text("Opcional") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        weight.toDoubleOrNull() ?: fallbackWeight,
                        bf.toDoubleOrNull() ?: fallbackBf,
                        chest.toDoubleOrNull() ?: 0.0,
                        arm.toDoubleOrNull() ?: 0.0,
                        waist.toDoubleOrNull() ?: 0.0,
                        thigh.toDoubleOrNull() ?: 0.0,
                        calf.toDoubleOrNull() ?: 0.0
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                shape = CircleShape
            ) {
                Text("Salvar Medidas", color = palette.onPrimaryAccent, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = palette.textSecondary)
            }
        }
    )
}
