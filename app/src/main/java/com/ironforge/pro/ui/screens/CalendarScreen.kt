package com.ironforge.pro.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.model.Exercise
import com.ironforge.pro.model.WorkoutDay
import com.ironforge.pro.ui.components.ExerciseMiniVideoPlayer
import com.ironforge.pro.ui.components.HyperPillTag
import com.ironforge.pro.ui.components.HyperSquircleCard
import com.ironforge.pro.ui.components.SectionHeader
import com.ironforge.pro.ui.components.WorkoutEditorDialog
import com.ironforge.pro.ui.theme.*
import com.ironforge.pro.viewmodel.IronForgeUiState
import com.ironforge.pro.viewmodel.IronForgeViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun CalendarScreen(
    state: IronForgeUiState,
    viewModel: IronForgeViewModel,
    onStartWorkoutDay: (WorkoutDay) -> Unit
) {
    val palette = LocalIronPalette.current
    val today = remember { LocalDate.now() }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(today) }
    var showAssignDialog by remember { mutableStateOf(false) }
    var editingWorkoutDay by remember { mutableStateOf<WorkoutDay?>(null) }
    var previewExercise by remember { mutableStateOf<Exercise?>(null) }

    val monthTitle = remember(currentMonth) {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("pt", "BR"))
        currentMonth.format(formatter).replaceFirstChar { it.uppercase() }
    }

    val calendarCells = remember(currentMonth) {
        val firstOfMonth = currentMonth.atDay(1)
        val leadingEmpty = firstOfMonth.dayOfWeek.value - 1
        val daysInMonth = currentMonth.lengthOfMonth()
        val cells = mutableListOf<LocalDate?>()
        repeat(leadingEmpty) { cells.add(null) }
        for (dayNum in 1..daysInMonth) {
            cells.add(currentMonth.atDay(dayNum))
        }
        while (cells.size % 7 != 0) {
            cells.add(null)
        }
        cells.chunked(7)
    }

    val completedByIsoDate = remember(state.sessionHistory) {
        state.sessionHistory.groupBy { it.isoDate }
    }

    val selectedIso = selectedDate.toString()
    val selectedWeekdayIso = selectedDate.dayOfWeek.value
    val scheduledWorkoutId = state.weeklySchedule[selectedWeekdayIso]
    val scheduledWorkout = remember(scheduledWorkoutId, state.customWorkouts, state.editedWorkoutDays) {
        viewModel.resolveWorkoutDayById(scheduledWorkoutId)
    }
    val completedSessionsOnSelectedDate = completedByIsoDate[selectedIso].orEmpty()

    val selectedDateFormatted = remember(selectedDate) {
        val fmt = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM", Locale("pt", "BR"))
        selectedDate.format(fmt).replaceFirstChar { it.uppercase() }
    }

    val weekdayFullName = remember(selectedWeekdayIso) {
        when (selectedWeekdayIso) {
            1 -> "Segunda-feira"
            2 -> "Terça-feira"
            3 -> "Quarta-feira"
            4 -> "Quinta-feira"
            5 -> "Sexta-feira"
            6 -> "Sábado"
            else -> "Domingo"
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.bg),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionHeader(
                title = "Agenda de Treinos",
                subtitle = "Toque em qualquer dia para ver ou editar o treino",
                trailing = {
                    if (selectedDate != today) {
                        TextButton(onClick = {
                            currentMonth = YearMonth.now()
                            selectedDate = today
                        }) {
                            Text("Hoje", color = palette.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        }

        item {
            HyperSquircleCard(accentGlow = true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { currentMonth = currentMonth.minusMonths(1) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.ChevronLeft, contentDescription = "Mês Anterior", tint = palette.textPrimary)
                    }

                    Text(
                        text = monthTitle,
                        style = MaterialTheme.typography.titleLarge,
                        color = palette.textPrimary
                    )

                    IconButton(
                        onClick = { currentMonth = currentMonth.plusMonths(1) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.ChevronRight, contentDescription = "Próximo Mês", tint = palette.textPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("SEG", "TER", "QUA", "QUI", "SEX", "SÁB", "DOM").forEach { dayName ->
                        Text(
                            text = dayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = palette.textMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    calendarCells.forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            week.forEach { date ->
                                if (date == null) {
                                    Box(modifier = Modifier.weight(1f).height(54.dp))
                                } else {
                                    val isSelected = date == selectedDate
                                    val isToday = date == today
                                    val dayWId = state.weeklySchedule[date.dayOfWeek.value]
                                    val dayWorkout = viewModel.resolveWorkoutDayById(dayWId)
                                    val isCompleted = completedByIsoDate.containsKey(date.toString())

                                    // Adaptive contrast-safe cell surface for Light Mode, AMOLED & Dark Titanium!
                                    val cellBg = when {
                                        isSelected -> palette.primary
                                        isToday -> palette.primary.copy(alpha = if (palette.isLight) 0.16f else 0.22f)
                                        else -> palette.tileElevated
                                    }
                                    val numColor = if (isSelected) palette.onPrimaryAccent else palette.textPrimary

                                    val shortTag = when {
                                        isCompleted -> "FEITO"
                                        dayWorkout == null -> "OFF"
                                        dayWorkout.title.contains("Push", ignoreCase = true) -> "PUSH"
                                        dayWorkout.title.contains("Pull", ignoreCase = true) -> "PULL"
                                        dayWorkout.title.contains("Legs", ignoreCase = true) -> "LEGS"
                                        dayWorkout.title.contains("Upper", ignoreCase = true) -> "UPPER"
                                        dayWorkout.title.contains("Lower", ignoreCase = true) -> "LOWER"
                                        dayWorkout.title.contains("Peito", ignoreCase = true) -> "PEITO"
                                        dayWorkout.title.contains("Costas", ignoreCase = true) -> "COSTAS"
                                        dayWorkout.title.contains("Ombro", ignoreCase = true) -> "OMBRO"
                                        dayWorkout.title.contains("Braço", ignoreCase = true) -> "BRAÇO"
                                        dayWorkout.title.contains("Perna", ignoreCase = true) -> "PERNA"
                                        else -> "TREINO"
                                    }

                                    val tagColor = when {
                                        isSelected -> palette.onPrimaryAccent
                                        isCompleted -> HyperEmerald
                                        dayWorkout != null -> palette.primary
                                        else -> palette.textMuted
                                    }

                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(54.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .clickable { selectedDate = date },
                                        shape = RoundedCornerShape(14.dp),
                                        color = cellBg,
                                        border = when {
                                            isToday && !isSelected -> BorderStroke(1.5.dp, palette.primary)
                                            !isSelected -> BorderStroke(0.8.dp, palette.hairline.copy(alpha = 0.65f))
                                            else -> null
                                        }
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(vertical = 4.dp, horizontal = 2.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = date.dayOfMonth.toString(),
                                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                                color = numColor,
                                                fontSize = 13.sp
                                            )
                                            Text(
                                                text = shortTag,
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = tagColor,
                                                fontSize = 8.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Clip
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LegendDot("Treino Programado", palette.primary)
                    LegendDot("Concluído", HyperEmerald)
                    LegendDot("Descanso (OFF)", palette.textMuted)
                }
            }
        }

        // Selected Day Detailed Drawer Card
        item {
            HyperSquircleCard(
                modifier = Modifier.animateContentSize(),
                accentGlow = scheduledWorkout != null || completedSessionsOnSelectedDate.isNotEmpty()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = selectedDateFormatted,
                            style = MaterialTheme.typography.titleLarge,
                            color = palette.textPrimary
                        )
                        Text(
                            text = "Programação recorrente de $weekdayFullName",
                            style = MaterialTheme.typography.bodyMedium,
                            color = palette.textSecondary
                        )
                    }

                    TextButton(onClick = { showAssignDialog = true }) {
                        Icon(Icons.Default.EditCalendar, contentDescription = null, tint = palette.secondary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Trocar Dia", color = palette.secondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                if (completedSessionsOnSelectedDate.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    completedSessionsOnSelectedDate.forEach { rec ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(HyperEmerald.copy(alpha = 0.14f))
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = HyperEmerald, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "TREINO CONCLUÍDO NESTE DIA",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = HyperEmerald
                                    )
                                }
                                Text(rec.workoutTitle, style = MaterialTheme.typography.titleMedium, color = palette.textPrimary)
                                Text(
                                    text = "Volume: ${rec.totalVolumeKg.roundToInt()} kg • ${rec.totalWorkingSets} séries válidas • ${rec.durationMinutes} min",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = palette.textSecondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = palette.hairline.copy(alpha = 0.6f))
                Spacer(modifier = Modifier.height(12.dp))

                if (scheduledWorkout != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            HyperPillTag(scheduledWorkout.dayLabel, palette.primaryGlow)
                            HyperPillTag("~${scheduledWorkout.estimatedMinutes} min", palette.secondary)
                        }
                        TextButton(
                            onClick = { editingWorkoutDay = scheduledWorkout },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = palette.primary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Editar Exercícios", color = palette.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = scheduledWorkout.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = palette.textPrimary
                    )
                    Text(
                        text = scheduledWorkout.focusSummary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.textSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    scheduledWorkout.exercises.forEachIndexed { idx, planned ->
                        val ex = ExerciseRepository.getExerciseById(planned.exerciseId)
                        if (ex != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(palette.tileElevated)
                                    .clickable { previewExercise = ex }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayCircleFilled,
                                        contentDescription = "Ver Mini Vídeo",
                                        tint = palette.secondary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "${idx + 1}. ${ex.name}",
                                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                            color = palette.textPrimary
                                        )
                                        Text(
                                            text = "${ex.muscleGroup.displayName} • RIR ${planned.targetRir} • Descanso ${planned.restSeconds}s",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = palette.textMuted
                                        )
                                    }
                                }
                                Text(
                                    text = "${planned.targetSets} × ${planned.targetReps}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = palette.primaryGlow
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { onStartWorkoutDay(scheduledWorkout) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = palette.primary),
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = palette.onPrimaryAccent)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "INICIAR TREINO DESTE DIA",
                            color = palette.onPrimaryAccent,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(palette.tileElevated)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SelfImprovement,
                            contentDescription = null,
                            tint = palette.secondary,
                            modifier = Modifier.size(38.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Dia de Descanso & Supercompensação",
                            style = MaterialTheme.typography.titleMedium,
                            color = palette.textPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "A síntese proteica miofibrilar e a restauração do sistema nervoso central atingem o pico nas 24h–48h pós-treino. Hidrate-se e bata sua meta de proteínas.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = palette.textSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { showAssignDialog = true },
                            shape = CircleShape
                        ) {
                            Text("Agendar Treino para $weekdayFullName", color = palette.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (editingWorkoutDay != null) {
        val target = editingWorkoutDay!!
        WorkoutEditorDialog(
            day = target,
            isBuiltInEdited = state.editedWorkoutDays.containsKey(target.id),
            onDismiss = { editingWorkoutDay = null },
            onSave = { updated ->
                viewModel.saveEditedWorkoutDay(updated)
                editingWorkoutDay = null
            },
            onResetDefault = {
                viewModel.resetEditedWorkoutDay(target.id)
                editingWorkoutDay = null
            }
        )
    }

    if (showAssignDialog) {
        val availableDays = remember(state.customWorkouts, state.editedWorkoutDays) {
            viewModel.allAvailableWorkoutDays()
        }
        AlertDialog(
            onDismissRequest = { showAssignDialog = false },
            containerColor = palette.card,
            shape = RoundedCornerShape(26.dp),
            title = {
                Text("Treino de $weekdayFullName", color = palette.textPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 380.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.assignWorkoutToWeekday(selectedWeekdayIso, null)
                                    showAssignDialog = false
                                },
                            shape = RoundedCornerShape(16.dp),
                            color = if (scheduledWorkoutId == null) palette.primary.copy(alpha = 0.18f) else palette.tileElevated
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Hotel, contentDescription = null, tint = palette.secondary)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Dia de Descanso (OFF)", color = palette.textPrimary, fontWeight = FontWeight.Bold)
                                    Text("Recuperação muscular e SNC", color = palette.textSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    items(availableDays) { wDay ->
                        val isSel = scheduledWorkoutId == wDay.id
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.assignWorkoutToWeekday(selectedWeekdayIso, wDay.id)
                                    showAssignDialog = false
                                },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSel) palette.primary.copy(alpha = 0.22f) else palette.tileElevated
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(wDay.title, color = palette.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("${wDay.exercises.size} exercícios • ${wDay.focusSummary}", color = palette.textSecondary, fontSize = 11.sp)
                                }
                                if (isSel) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = palette.primary)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAssignDialog = false }) {
                    Text("Fechar", color = palette.textSecondary)
                }
            }
        )
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
                        height = 195.dp
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
                    Text("Entendido", color = palette.onPrimaryAccent, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun LegendDot(label: String, color: Color) {
    val palette = LocalIronPalette.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = palette.textSecondary, fontSize = 10.sp)
    }
}
