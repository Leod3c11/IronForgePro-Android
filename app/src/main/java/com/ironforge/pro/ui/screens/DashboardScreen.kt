package com.ironforge.pro.ui.screens

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.model.BodyMeasurementRecord
import com.ironforge.pro.model.MuscleGroup
import com.ironforge.pro.model.WorkoutDay
import com.ironforge.pro.ui.components.*
import com.ironforge.pro.ui.theme.*
import com.ironforge.pro.viewmodel.IronForgeUiState
import kotlin.math.roundToInt

@Composable
fun DashboardScreen(
    state: IronForgeUiState,
    onStartWorkoutDay: (WorkoutDay) -> Unit,
    onStartEmptyWorkout: () -> Unit,
    onDismissSummary: () -> Unit,
    onSelectTheme: (AppThemePreset) -> Unit,
    onReopenOnboarding: () -> Unit,
    onAddMeasurement: (Double, Double, Double, Double, Double, Double, Double) -> Unit
) {
    val palette = LocalIronPalette.current
    var showMeasureDialog by remember { mutableStateOf(false) }
    var showThemeStudioDialog by remember { mutableStateOf(false) }

    val totalTonnage = state.sessionHistory.sumOf { it.totalVolumeKg }
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
        val now = System.currentTimeMillis()
        MuscleGroup.entries.map { muscle ->
            val lastTrainedSession = state.sessionHistory.firstOrNull { rec ->
                (rec.muscleSetsBreakdown[muscle.displayName] ?: 0) > 0
            }
            val readinessPct = if (lastTrainedSession == null) {
                100
            } else {
                val hoursElapsed = ((now - lastTrainedSession.timestamp) / 3_600_000.0).coerceAtLeast(1.0)
                ((hoursElapsed / muscle.recoveryHours.toDouble()) * 100.0).roundToInt().coerceIn(32, 100)
            }
            muscle to readinessPct
        }
    }
    val avgReadiness = remember(muscleReadinessList) {
        muscleReadinessList.map { it.second }.average().roundToInt()
    }

    val chosenSplit = ExerciseRepository.defaultSplits.find { it.id == state.userProfile.selectedSplitId }
        ?: ExerciseRepository.defaultSplits.first()
    val nextWorkout = state.customWorkouts.firstOrNull() ?: chosenSplit.days.first()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HyperBgDark),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Personalized HyperOS Top App Bar + Android 17 Theme Studio Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Olá, ${state.userProfile.name}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(listOf(palette.primary, palette.secondary))
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "PRO",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                color = palette.onPrimaryAccent,
                                fontSize = 10.sp
                            )
                        }
                    }
                    Text(
                        text = "${state.userProfile.goal.label} • ${state.userProfile.weightKg} kg",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    FilledIconButton(
                        onClick = { showThemeStudioDialog = true },
                        modifier = Modifier.size(42.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = HyperTileElevated)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Temas Android 17",
                            tint = palette.primary
                        )
                    }

                    Button(
                        onClick = onStartEmptyWorkout,
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

        // Post-Workout Summary Notification Pill
        if (state.lastFinishedSession != null) {
            item {
                val summary = state.lastFinishedSession
                HyperSquircleCard(accentGlow = true, containerColor = HyperTileElevated) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = HyperEmerald,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "SESSÃO REGISTRADA COM SUCESSO",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = HyperEmerald
                                )
                                Text(
                                    text = summary.workoutTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary
                                )
                            }
                        }
                        IconButton(onClick = onDismissSummary) {
                            Icon(Icons.Default.Close, contentDescription = "Fechar", tint = TextSecondary)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        HyperPillTag("Volume: ${summary.totalVolumeKg.roundToInt()} kg", palette.primaryGlow)
                        HyperPillTag("Séries: ${summary.totalWorkingSets}", palette.secondary)
                        HyperPillTag("PRs: ${summary.prCount}", HyperEmerald)
                    }
                }
            }
        }

        // Next Recommended Session (Always ready for the user's first workout!)
        item {
            val leadExercise = ExerciseRepository.getExerciseById(nextWorkout.exercises.first().exerciseId)

            HyperSquircleCard(accentGlow = true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        HyperPillTag("SUA ROTINA ATIVA • ${nextWorkout.estimatedMinutes} MIN", palette.primaryGlow)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = nextWorkout.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary
                        )
                        Text(
                            text = "${nextWorkout.exercises.size} exercícios • ${nextWorkout.focusSummary}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    FilledIconButton(
                        onClick = { onStartWorkoutDay(nextWorkout) },
                        modifier = Modifier.size(52.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = palette.primary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Iniciar",
                            tint = palette.onPrimaryAccent,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                if (leadExercise != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    ExerciseMiniVideoPlayer(
                        exercise = leadExercise,
                        customVideoUri = state.customVideoUris[leadExercise.id],
                        height = 135.dp,
                        compact = true
                    )
                }
            }
        }

        // 2x2 Bento Grid (100% Real User Metrics — starts at 0 until user trains!)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HyperSquircleCard(modifier = Modifier.weight(1.15f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("TONELAGEM TOTAL", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = palette.primary, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${(totalTonnage / 1000.0 * 10).roundToInt() / 10.0} t",
                            style = MaterialTheme.typography.displayLarge,
                            color = TextPrimary
                        )
                        Text(
                            text = if (totalTonnage == 0.0) "Pronto para iniciar" else "${totalTonnage.roundToInt()} kg acumulados",
                            style = MaterialTheme.typography.labelSmall,
                            color = palette.primaryGlow
                        )
                    }

                    HyperSquircleCard(modifier = Modifier.weight(0.85f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("PRONTIDÃO", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Icon(Icons.Default.BatteryChargingFull, contentDescription = null, tint = HyperEmerald, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "$avgReadiness%",
                            style = MaterialTheme.typography.displayLarge,
                            color = HyperEmerald
                        )
                        Text(
                            text = "Energia Muscular",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
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
                            Text("TREINOS", style = MaterialTheme.typography.labelSmall, color = TextMuted)
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
                            Text("RECORDES (PR)", style = MaterialTheme.typography.labelSmall, color = TextMuted)
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

        // Anthropometry & Body Measurements (Shows user's exact onboarding weight & BF, "—" until circumferences are added)
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

        // Muscle Recovery Readiness Strip
        item {
            HyperSquircleCard {
                SectionHeader(
                    title = "Status de Recuperação Muscular",
                    subtitle = "100% recuperado até você registrar suas séries"
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(muscleReadinessList) { (muscle, pct) ->
                        val color = when {
                            pct >= 90 -> HyperEmerald
                            pct >= 65 -> palette.secondary
                            else -> HyperAmber
                        }
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(HyperTileElevated)
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$pct%",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                color = color
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = muscle.shortName,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Volume Bar Chart
        item {
            HyperSquircleCard {
                SectionHeader(
                    title = "Evolução de Tonelagem",
                    subtitle = "Volume total levantado nas suas sessões"
                )
                Spacer(modifier = Modifier.height(8.dp))
                VolumeBarChart(sessions = state.sessionHistory)
            }
        }

        // Hexagonal Muscle Balance Radar
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

        // Personal Records (Clean empty state until user logs sets)
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
                        color = TextSecondary
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
                                    color = TextPrimary
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

        // Recent Sessions
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
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Inicie sua rotina acima ou crie uma ficha na aba Treinos para começar a registrar sua evolução.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
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
                                color = TextPrimary
                            )
                            Text(
                                text = "${session.dateFormatted} • ${session.durationMinutes} min • ${session.totalWorkingSets} séries válidas",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                        HyperPillTag("${session.totalVolumeKg.roundToInt()} kg", palette.secondary)
                    }
                    if (session.highlightExercises.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Destaques: ${session.highlightExercises.joinToString("  •  ")}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }

    // Android 17 Theme Studio & Profile Modal
    if (showThemeStudioDialog) {
        AlertDialog(
            onDismissRequest = { showThemeStudioDialog = false },
            containerColor = HyperCardDark,
            shape = RoundedCornerShape(28.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Palette, contentDescription = null, tint = palette.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Temas Android 17 & Perfil", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 420.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(AppThemePreset.entries) { preset ->
                        val selected = state.themePreset == preset
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectTheme(preset) },
                            shape = RoundedCornerShape(18.dp),
                            color = if (selected) HyperTileElevated else HyperBgDark,
                            border = BorderStroke(if (selected) 1.6.dp else 1.dp, if (selected) palette.primary else HyperHairline)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(preset.title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(preset.subtitle, color = TextSecondary, fontSize = 11.sp)
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(Color(preset.primaryHex)))
                                    Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(Color(preset.secondaryHex)))
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
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(HyperTileElevated)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, style = MaterialTheme.typography.labelLarge, color = TextPrimary)
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
        containerColor = HyperCardDark,
        shape = RoundedCornerShape(26.dp),
        title = { Text("Atualizar Suas Medidas", color = TextPrimary, fontWeight = FontWeight.Bold) },
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
                Text("Cancelar", color = TextSecondary)
            }
        }
    )
}
