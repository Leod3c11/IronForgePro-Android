package com.ironforge.pro.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ironforge.pro.data.ExerciseRepository
import com.ironforge.pro.data.LocalStorageManager
import com.ironforge.pro.model.*
import com.ironforge.pro.ui.theme.AppThemePreset
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.math.roundToInt

data class IronForgeUiState(
    val isOnboardingCompleted: Boolean = false,
    val userProfile: UserProfile = UserProfile(),
    val themePreset: AppThemePreset = AppThemePreset.HYPER_ORANGE,

    // Key: 1 (Mon) .. 7 (Sun) -> WorkoutDay.id or null (Rest Day)
    val weeklySchedule: Map<Int, String?> = emptyMap(),

    val sessionHistory: List<WorkoutSessionRecord> = emptyList(),
    val exercisePrs: Map<String, Double> = emptyMap(),
    val customWorkouts: List<WorkoutDay> = emptyList(),
    val customVideoUris: Map<String, String> = emptyMap(),
    val bodyMeasurements: List<BodyMeasurementRecord> = emptyList(),

    // Active Guided Workout Session
    val isWorkoutActive: Boolean = false,
    val activeWorkoutTitle: String = "",
    val activeExercises: List<ActiveExerciseSession> = emptyList(),
    val activeExerciseIndex: Int = 0,
    val focusedSetIndex: Int = 0,
    val workoutElapsedSeconds: Int = 0,
    val lastFinishedSession: WorkoutSessionRecord? = null,
    val expandedVideoExerciseIds: Set<String> = emptySet(),

    // Smart Rest Timer (HyperOS Dynamic Island)
    val restTotalSeconds: Int = 90,
    val restRemainingSeconds: Int = 0,
    val isRestTimerRunning: Boolean = false,

    // Encyclopedia Filters
    val searchQuery: String = "",
    val selectedMuscleFilter: MuscleGroup? = null,
    val selectedEquipmentFilter: EquipmentType? = null,
    val inspectedExercise: Exercise? = null,

    // Science Lab Calculators
    val oneRmWeightInput: String = "60",
    val oneRmRepsInput: String = "8",
    val plateTargetWeight: String = "60",
    val userWeightKg: String = "75",
    val userHeightCm: String = "175",
    val userAge: String = "25",
    val isMale: Boolean = true,
    val activityLevelIndex: Int = 2,
    val nutritionGoalIndex: Int = 0,
    val weeklySetsEstimator: Map<MuscleGroup, Int> = mapOf(
        MuscleGroup.CHEST to 12,
        MuscleGroup.BACK to 14,
        MuscleGroup.SHOULDERS to 12,
        MuscleGroup.BICEPS to 10,
        MuscleGroup.TRICEPS to 10,
        MuscleGroup.QUADS to 12,
        MuscleGroup.HAMSTRINGS to 10,
        MuscleGroup.GLUTES to 8,
        MuscleGroup.CALVES to 10,
        MuscleGroup.CORE to 8
    )
)

class IronForgeViewModel(application: Application) : AndroidViewModel(application) {

    private val storage = LocalStorageManager(application.applicationContext)

    private val _uiState = MutableStateFlow(
        run {
            val savedProfile = storage.loadUserProfile()
            val savedCustom = storage.loadCustomWorkouts()
            var savedSchedule = storage.loadWeeklySchedule()
            if (savedSchedule.isEmpty()) {
                savedSchedule = buildDefaultWeeklySchedule(savedProfile.selectedSplitId, savedCustom.firstOrNull())
            }
            IronForgeUiState(
                isOnboardingCompleted = storage.isOnboardingCompleted(),
                userProfile = savedProfile,
                themePreset = storage.loadThemePreset(),
                weeklySchedule = savedSchedule,
                sessionHistory = storage.loadSessionHistory(),
                exercisePrs = storage.loadExercisePrs(),
                customWorkouts = savedCustom,
                customVideoUris = storage.loadCustomVideoUris(),
                bodyMeasurements = storage.loadBodyMeasurements(),
                userWeightKg = savedProfile.weightKg.toInt().toString(),
                userHeightCm = savedProfile.heightCm.toInt().toString(),
                userAge = savedProfile.age.toString(),
                isMale = savedProfile.isMale
            )
        }
    )
    val uiState: StateFlow<IronForgeUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var workoutClockJob: Job? = null

    fun resolveWorkoutDayById(dayId: String?): WorkoutDay? {
        if (dayId.isNullOrBlank()) return null
        _uiState.value.customWorkouts.find { it.id == dayId }?.let { return it }
        ExerciseRepository.defaultSplits.forEach { split ->
            split.days.find { it.id == dayId }?.let { return it }
        }
        return null
    }

    fun allAvailableWorkoutDays(): List<WorkoutDay> {
        val list = mutableListOf<WorkoutDay>()
        list.addAll(_uiState.value.customWorkouts)
        ExerciseRepository.defaultSplits.forEach { split ->
            list.addAll(split.days)
        }
        return list
    }

    fun assignWorkoutToWeekday(isoDayOfWeek: Int, workoutDayId: String?) {
        val updated = _uiState.value.weeklySchedule.toMutableMap()
        updated[isoDayOfWeek.coerceIn(1, 7)] = workoutDayId
        storage.saveWeeklySchedule(updated)
        _uiState.update { it.copy(weeklySchedule = updated) }
    }

    fun applySplitToWeeklyCalendar(splitId: String) {
        val schedule = buildDefaultWeeklySchedule(splitId, null)
        storage.saveWeeklySchedule(schedule)
        val updatedProfile = _uiState.value.userProfile.copy(selectedSplitId = splitId)
        storage.saveUserProfile(updatedProfile)
        _uiState.update { it.copy(weeklySchedule = schedule, userProfile = updatedProfile) }
    }

    fun setThemePreset(preset: AppThemePreset) {
        storage.saveThemePreset(preset)
        _uiState.update { it.copy(themePreset = preset) }
    }

    fun reopenOnboarding() {
        _uiState.update { it.copy(isOnboardingCompleted = false) }
    }

    fun completeOnboarding(
        profile: UserProfile,
        chosenTheme: AppThemePreset,
        customWorkoutTitle: String?,
        customWorkoutExerciseIds: List<String>
    ) {
        storage.saveUserProfile(profile)
        storage.saveThemePreset(chosenTheme)
        storage.setOnboardingCompleted(true)

        val dateFmt = SimpleDateFormat("dd/MM", Locale("pt", "BR")).format(Date())
        val initialMeasure = BodyMeasurementRecord(
            id = "onboard_${UUID.randomUUID()}",
            dateFormatted = dateFmt,
            weightKg = profile.weightKg,
            bodyFatPct = profile.bodyFatPct,
            chestCm = 0.0,
            armCm = 0.0,
            waistCm = 0.0,
            thighCm = 0.0,
            calfCm = 0.0
        )
        val updatedMeasures = listOf(initialMeasure)
        storage.saveBodyMeasurements(updatedMeasures)

        var updatedCustomWorkouts = _uiState.value.customWorkouts
        var createdCustomDay: WorkoutDay? = null
        if (!customWorkoutTitle.isNullOrBlank() && customWorkoutExerciseIds.isNotEmpty()) {
            val planned = customWorkoutExerciseIds.map { id ->
                val ex = ExerciseRepository.getExerciseById(id)
                PlannedExercise(
                    exerciseId = id,
                    targetSets = 3,
                    targetReps = ex?.recommendedReps ?: "8–12",
                    targetRir = 1,
                    restSeconds = ex?.restSeconds ?: 90
                )
            }
            val newDay = WorkoutDay(
                id = "custom_${UUID.randomUUID()}",
                dayLabel = "MEU TREINO",
                title = customWorkoutTitle.trim(),
                focusSummary = "Montado no cadastro inicial • ${profile.goal.label}",
                estimatedMinutes = planned.size * 11,
                exercises = planned
            )
            createdCustomDay = newDay
            updatedCustomWorkouts = listOf(newDay) + updatedCustomWorkouts
            storage.saveCustomWorkouts(updatedCustomWorkouts)
        }

        // Auto-populate the entire weekly calendar based on user's chosen split or custom workout!
        val autoSchedule = buildDefaultWeeklySchedule(profile.selectedSplitId, createdCustomDay)
        storage.saveWeeklySchedule(autoSchedule)

        val goalIdx = when (profile.goal) {
            FitnessGoal.LEAN_BULK -> 0
            FitnessGoal.RECOMP -> 1
            FitnessGoal.CUTTING_MODERATE -> 2
            FitnessGoal.STRENGTH -> 0
        }

        _uiState.update {
            it.copy(
                isOnboardingCompleted = true,
                userProfile = profile,
                themePreset = chosenTheme,
                weeklySchedule = autoSchedule,
                customWorkouts = updatedCustomWorkouts,
                bodyMeasurements = updatedMeasures,
                userWeightKg = profile.weightKg.toInt().toString(),
                userHeightCm = profile.heightCm.toInt().toString(),
                userAge = profile.age.toString(),
                isMale = profile.isMale,
                nutritionGoalIndex = goalIdx
            )
        }
    }

    fun attachCustomVideoUri(exerciseId: String, uriString: String) {
        val updated = _uiState.value.customVideoUris.toMutableMap()
        updated[exerciseId] = uriString
        storage.saveCustomVideoUris(updated)
        _uiState.update { it.copy(customVideoUris = updated) }
    }

    fun toggleInlineVideoInWorkout(exerciseId: String) {
        _uiState.update { s ->
            val current = s.expandedVideoExerciseIds
            val next = if (current.contains(exerciseId)) current - exerciseId else current + exerciseId
            s.copy(expandedVideoExerciseIds = next)
        }
    }

    // ==================== GUIDED WORKOUT NAVIGATION ====================
    fun selectActiveExercise(index: Int) {
        _uiState.update { s ->
            val safeIdx = index.coerceIn(0, (s.activeExercises.size - 1).coerceAtLeast(0))
            val ex = s.activeExercises.getOrNull(safeIdx)
            val firstIncomplete = ex?.sets?.indexOfFirst { !it.completed }?.takeIf { it >= 0 } ?: 0
            s.copy(activeExerciseIndex = safeIdx, focusedSetIndex = firstIncomplete)
        }
    }

    fun focusSet(exerciseIndex: Int, setIndex: Int) {
        _uiState.update { s ->
            s.copy(
                activeExerciseIndex = exerciseIndex.coerceIn(0, (s.activeExercises.size - 1).coerceAtLeast(0)),
                focusedSetIndex = setIndex.coerceAtLeast(0)
            )
        }
    }

    private fun estimateStartingWeightForUser(exercise: Exercise): Double {
        val state = _uiState.value
        val existingPr = state.exercisePrs[exercise.id]
        if (existingPr != null && existingPr > 0.0) {
            return ((existingPr * 0.78) / 2.5).roundToInt() * 2.5
        }
        if (exercise.equipment == EquipmentType.BODYWEIGHT) return 0.0

        val bw = state.userProfile.weightKg
        val mult = state.userProfile.experience.loadMultiplier
        val rawKg = when (exercise.muscleGroup) {
            MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES ->
                if (exercise.mechanics == MechanicsType.COMPOUND) bw * 0.55 * mult else bw * 0.30 * mult
            MuscleGroup.CHEST, MuscleGroup.BACK ->
                if (exercise.mechanics == MechanicsType.COMPOUND) bw * 0.45 * mult else bw * 0.22 * mult
            MuscleGroup.SHOULDERS ->
                if (exercise.mechanics == MechanicsType.COMPOUND) bw * 0.25 * mult else 8.0 * mult
            MuscleGroup.BICEPS, MuscleGroup.TRICEPS -> 12.0 * mult
            MuscleGroup.CALVES -> bw * 0.45 * mult
            MuscleGroup.CORE -> 15.0 * mult
        }
        return (((rawKg / 2.5).roundToInt()) * 2.5).coerceAtLeast(5.0)
    }

    fun startWorkoutFromDay(day: WorkoutDay) {
        val activeList = day.exercises.mapNotNull { planned ->
            val ex = ExerciseRepository.getExerciseById(planned.exerciseId) ?: return@mapNotNull null
            val startWeight = estimateStartingWeightForUser(ex)
            val sets = (1..planned.targetSets).map { idx ->
                ActiveSetLog(
                    setNumber = idx,
                    setType = if (idx == 1 && ex.mechanics == MechanicsType.COMPOUND) SetType.TOP_SET else SetType.WORKING,
                    weightKg = startWeight,
                    reps = 10,
                    rpe = (10 - planned.targetRir).toDouble().coerceIn(6.0, 10.0),
                    completed = false,
                    isPr = false
                )
            }
            ActiveExerciseSession(
                exercise = ex,
                targetReps = planned.targetReps,
                restSeconds = planned.restSeconds,
                sets = sets
            )
        }

        _uiState.update {
            it.copy(
                isWorkoutActive = true,
                activeWorkoutTitle = day.title,
                activeExercises = activeList,
                activeExerciseIndex = 0,
                focusedSetIndex = 0,
                workoutElapsedSeconds = 0,
                lastFinishedSession = null,
                expandedVideoExerciseIds = emptySet()
            )
        }
        startWorkoutClock()
    }

    fun startEmptyWorkout(title: String = "Treino Livre Personalizado") {
        val defaultEx = ExerciseRepository.allExercises.take(3).map { ex ->
            val startW = estimateStartingWeightForUser(ex)
            ActiveExerciseSession(
                exercise = ex,
                targetReps = ex.recommendedReps,
                restSeconds = ex.restSeconds,
                sets = listOf(
                    ActiveSetLog(1, SetType.WARMUP, (startW * 0.6).roundToInt().toDouble(), 12, 6.0, false),
                    ActiveSetLog(2, SetType.WORKING, startW, 10, 8.5, false),
                    ActiveSetLog(3, SetType.WORKING, startW, 10, 9.0, false)
                )
            )
        }
        _uiState.update {
            it.copy(
                isWorkoutActive = true,
                activeWorkoutTitle = title,
                activeExercises = defaultEx,
                activeExerciseIndex = 0,
                focusedSetIndex = 0,
                workoutElapsedSeconds = 0,
                lastFinishedSession = null,
                expandedVideoExerciseIds = emptySet()
            )
        }
        startWorkoutClock()
    }

    private fun startWorkoutClock() {
        workoutClockJob?.cancel()
        workoutClockJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                _uiState.update { it.copy(workoutElapsedSeconds = it.workoutElapsedSeconds + 1) }
            }
        }
    }

    fun addExerciseToActiveWorkout(exercise: Exercise) {
        val suggestedWeight = estimateStartingWeightForUser(exercise)
        val newSessionEx = ActiveExerciseSession(
            exercise = exercise,
            targetReps = exercise.recommendedReps,
            restSeconds = exercise.restSeconds,
            sets = (1..3).map { idx ->
                ActiveSetLog(idx, SetType.WORKING, suggestedWeight, 10, 8.5, false)
            }
        )
        _uiState.update {
            val newList = it.activeExercises + newSessionEx
            it.copy(
                activeExercises = newList,
                activeExerciseIndex = newList.lastIndex,
                focusedSetIndex = 0
            )
        }
    }

    fun swapExerciseInActiveWorkout(exerciseIndex: Int, newExercise: Exercise) {
        val suggestedWeight = estimateStartingWeightForUser(newExercise)
        _uiState.update { state ->
            val updated = state.activeExercises.mapIndexed { idx, old ->
                if (idx != exerciseIndex) old
                else ActiveExerciseSession(
                    exercise = newExercise,
                    targetReps = newExercise.recommendedReps,
                    restSeconds = newExercise.restSeconds,
                    sets = old.sets.map { s -> s.copy(weightKg = suggestedWeight, completed = false, isPr = false) }
                )
            }
            state.copy(activeExercises = updated, focusedSetIndex = 0)
        }
    }

    fun addSetToExercise(exerciseIndex: Int) {
        _uiState.update { state ->
            var newSetIdx = 0
            val updated = state.activeExercises.mapIndexed { idx, item ->
                if (idx != exerciseIndex) item
                else {
                    val lastSet = item.sets.lastOrNull()
                    val nextNum = item.sets.size + 1
                    newSetIdx = item.sets.size
                    val newSet = ActiveSetLog(
                        setNumber = nextNum,
                        setType = SetType.WORKING,
                        weightKg = lastSet?.weightKg ?: 20.0,
                        reps = lastSet?.reps ?: 10,
                        rpe = lastSet?.rpe ?: 8.5,
                        completed = false
                    )
                    item.copy(sets = item.sets + newSet)
                }
            }
            state.copy(activeExercises = updated, focusedSetIndex = newSetIdx)
        }
    }

    fun updateSetData(
        exerciseIndex: Int,
        setIndex: Int,
        weightKg: Double? = null,
        reps: Int? = null,
        setType: SetType? = null,
        rpe: Double? = null
    ) {
        _uiState.update { state ->
            val updated = state.activeExercises.mapIndexed { idx, item ->
                if (idx != exerciseIndex) item
                else {
                    val newSets = item.sets.mapIndexed { sIdx, set ->
                        if (sIdx != setIndex) set
                        else set.copy(
                            weightKg = (weightKg ?: set.weightKg).coerceAtLeast(0.0),
                            reps = (reps ?: set.reps).coerceIn(1, 100),
                            setType = setType ?: set.setType,
                            rpe = (rpe ?: set.rpe).coerceIn(5.0, 10.0)
                        )
                    }
                    item.copy(sets = newSets)
                }
            }
            state.copy(activeExercises = updated)
        }
    }

    fun toggleSetCompleted(exerciseIndex: Int, setIndex: Int) {
        var shouldStartTimer = false
        var restDuration = 90

        _uiState.update { state ->
            val currentPrs = state.exercisePrs
            var nextExIdx = state.activeExerciseIndex
            var nextSetIdx = state.focusedSetIndex

            val updated = state.activeExercises.mapIndexed { idx, item ->
                if (idx != exerciseIndex) item
                else {
                    val newSets = item.sets.mapIndexed { sIdx, set ->
                        if (sIdx != setIndex) set
                        else {
                            val nowCompleted = !set.completed
                            if (nowCompleted) {
                                shouldStartTimer = true
                                restDuration = item.restSeconds
                            }
                            val est1Rm = estimate1RM(set.weightKg, set.reps)
                            val prevBest = currentPrs[item.exercise.id] ?: 0.0
                            val isNewPr = nowCompleted && set.setType != SetType.WARMUP && est1Rm > prevBest && est1Rm > 0.0
                            set.copy(completed = nowCompleted, isPr = isNewPr)
                        }
                    }
                    if (idx == exerciseIndex) {
                        val nextIncompleteInEx = newSets.indexOfFirst { !it.completed }
                        if (nextIncompleteInEx >= 0) {
                            nextSetIdx = nextIncompleteInEx
                        } else if (exerciseIndex + 1 < state.activeExercises.size) {
                            nextExIdx = exerciseIndex + 1
                            nextSetIdx = 0
                        }
                    }
                    item.copy(sets = newSets)
                }
            }
            state.copy(
                activeExercises = updated,
                activeExerciseIndex = nextExIdx,
                focusedSetIndex = nextSetIdx
            )
        }

        if (shouldStartTimer) {
            startRestTimer(restDuration)
        }
    }

    fun finishAndSaveWorkout() {
        workoutClockJob?.cancel()
        timerJob?.cancel()

        val state = _uiState.value
        val updatedPrs = state.exercisePrs.toMutableMap()
        var totalVolume = 0.0
        var totalWorkingSets = 0
        var prCount = 0
        val muscleBreakdown = mutableMapOf<String, Int>()
        val highlights = mutableListOf<String>()

        state.activeExercises.forEach { sessionEx ->
            var bestSetText: String? = null
            var bestSetScore = 0.0
            sessionEx.sets.filter { it.completed }.forEach { set ->
                totalVolume += set.weightKg * set.reps
                if (set.setType != SetType.WARMUP) {
                    totalWorkingSets += 1
                    val mName = sessionEx.exercise.muscleGroup.displayName
                    muscleBreakdown[mName] = (muscleBreakdown[mName] ?: 0) + 1

                    val est1Rm = estimate1RM(set.weightKg, set.reps)
                    val prev = updatedPrs[sessionEx.exercise.id] ?: 0.0
                    if (est1Rm > prev && est1Rm > 0.0) {
                        updatedPrs[sessionEx.exercise.id] = est1Rm
                        prCount += 1
                    }
                    if (est1Rm >= bestSetScore) {
                        bestSetScore = est1Rm
                        bestSetText = "${sessionEx.exercise.name.take(20)} ${set.weightKg.toInt()}kg × ${set.reps}"
                    }
                }
            }
            if (bestSetText != null && highlights.size < 3) {
                highlights.add(bestSetText!!)
            }
        }

        val now = Date()
        val dateFmt = SimpleDateFormat("dd/MM", Locale("pt", "BR")).format(now)
        val isoFmt = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(now)
        val durationMin = (state.workoutElapsedSeconds / 60).coerceAtLeast(1)

        val record = WorkoutSessionRecord(
            id = UUID.randomUUID().toString(),
            workoutTitle = state.activeWorkoutTitle,
            dateFormatted = dateFmt,
            isoDate = isoFmt,
            timestamp = System.currentTimeMillis(),
            durationMinutes = durationMin,
            totalVolumeKg = totalVolume,
            totalWorkingSets = totalWorkingSets,
            prCount = prCount,
            muscleSetsBreakdown = muscleBreakdown,
            highlightExercises = if (highlights.isEmpty()) listOf("Sessão registrada") else highlights
        )

        val newHistory = listOf(record) + state.sessionHistory
        storage.saveSessionHistory(newHistory)
        storage.saveExercisePrs(updatedPrs)

        _uiState.update {
            it.copy(
                isWorkoutActive = false,
                activeExercises = emptyList(),
                isRestTimerRunning = false,
                restRemainingSeconds = 0,
                sessionHistory = newHistory,
                exercisePrs = updatedPrs,
                lastFinishedSession = record
            )
        }
    }

    fun cancelActiveWorkout() {
        workoutClockJob?.cancel()
        timerJob?.cancel()
        _uiState.update {
            it.copy(
                isWorkoutActive = false,
                activeExercises = emptyList(),
                isRestTimerRunning = false,
                restRemainingSeconds = 0
            )
        }
    }

    fun dismissSummaryBanner() {
        _uiState.update { it.copy(lastFinishedSession = null) }
    }

    fun startRestTimer(seconds: Int) {
        timerJob?.cancel()
        val target = seconds.coerceIn(10, 600)
        _uiState.update {
            it.copy(
                restTotalSeconds = target,
                restRemainingSeconds = target,
                isRestTimerRunning = true
            )
        }
        timerJob = viewModelScope.launch {
            while (_uiState.value.restRemainingSeconds > 0 && _uiState.value.isRestTimerRunning) {
                delay(1000L)
                _uiState.update { s ->
                    val next = (s.restRemainingSeconds - 1).coerceAtLeast(0)
                    if (next == 0) {
                        vibrateAlert()
                        s.copy(restRemainingSeconds = 0, isRestTimerRunning = false)
                    } else {
                        s.copy(restRemainingSeconds = next)
                    }
                }
            }
        }
    }

    fun adjustRestTimer(deltaSeconds: Int) {
        _uiState.update { s ->
            val updated = (s.restRemainingSeconds + deltaSeconds).coerceIn(0, 600)
            val newTotal = maxOf(s.restTotalSeconds, updated)
            s.copy(
                restRemainingSeconds = updated,
                restTotalSeconds = newTotal,
                isRestTimerRunning = updated > 0
            )
        }
    }

    fun skipRestTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(restRemainingSeconds = 0, isRestTimerRunning = false) }
    }

    private fun vibrateAlert() {
        try {
            val ctx = getApplication<Application>().applicationContext
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(450, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val v = ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v?.vibrate(VibrationEffect.createOneShot(450, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    v?.vibrate(450)
                }
            }
        } catch (_: Exception) {
        }
    }

    fun saveCustomWorkoutDay(title: String, focus: String, exerciseIds: List<String>) {
        if (title.isBlank() || exerciseIds.isEmpty()) return
        val planned = exerciseIds.map { id ->
            val ex = ExerciseRepository.getExerciseById(id)
            PlannedExercise(
                exerciseId = id,
                targetSets = 3,
                targetReps = ex?.recommendedReps ?: "8–12",
                targetRir = 1,
                restSeconds = ex?.restSeconds ?: 90
            )
        }
        val newDay = WorkoutDay(
            id = "custom_${UUID.randomUUID()}",
            dayLabel = "CUSTOM",
            title = title.trim(),
            focusSummary = focus.ifBlank { "Protocolo personalizado" },
            estimatedMinutes = planned.size * 11,
            exercises = planned
        )
        val updatedList = listOf(newDay) + _uiState.value.customWorkouts
        storage.saveCustomWorkouts(updatedList)
        _uiState.update { it.copy(customWorkouts = updatedList) }
    }

    fun deleteCustomWorkout(dayId: String) {
        val updatedList = _uiState.value.customWorkouts.filterNot { it.id == dayId }
        storage.saveCustomWorkouts(updatedList)
        _uiState.update { it.copy(customWorkouts = updatedList) }
    }

    fun updateSearchQuery(q: String) {
        _uiState.update { it.copy(searchQuery = q) }
    }

    fun selectMuscleFilter(muscle: MuscleGroup?) {
        _uiState.update { it.copy(selectedMuscleFilter = muscle) }
    }

    fun selectEquipmentFilter(eq: EquipmentType?) {
        _uiState.update { it.copy(selectedEquipmentFilter = eq) }
    }

    fun inspectExercise(exercise: Exercise?) {
        _uiState.update { it.copy(inspectedExercise = exercise) }
    }

    fun updateOneRmInputs(weight: String, reps: String) {
        _uiState.update { it.copy(oneRmWeightInput = weight, oneRmRepsInput = reps) }
    }

    fun updatePlateTargetWeight(weight: String) {
        _uiState.update { it.copy(plateTargetWeight = weight) }
    }

    fun updateNutritionInputs(
        weightKg: String? = null,
        heightCm: String? = null,
        age: String? = null,
        isMale: Boolean? = null,
        activityIndex: Int? = null,
        goalIndex: Int? = null
    ) {
        _uiState.update {
            it.copy(
                userWeightKg = weightKg ?: it.userWeightKg,
                userHeightCm = heightCm ?: it.userHeightCm,
                userAge = age ?: it.userAge,
                isMale = isMale ?: it.isMale,
                activityLevelIndex = activityIndex ?: it.activityLevelIndex,
                nutritionGoalIndex = goalIndex ?: it.nutritionGoalIndex
            )
        }
    }

    fun updateWeeklyVolumeEstimator(muscle: MuscleGroup, sets: Int) {
        _uiState.update { s ->
            val map = s.weeklySetsEstimator.toMutableMap()
            map[muscle] = sets.coerceIn(0, 30)
            s.copy(weeklySetsEstimator = map)
        }
    }

    fun addBodyMeasurement(
        weightKg: Double,
        bodyFatPct: Double,
        chestCm: Double,
        armCm: Double,
        waistCm: Double,
        thighCm: Double,
        calfCm: Double
    ) {
        val dateFmt = SimpleDateFormat("dd/MM", Locale("pt", "BR")).format(Date())
        val rec = BodyMeasurementRecord(
            id = UUID.randomUUID().toString(),
            dateFormatted = dateFmt,
            weightKg = weightKg,
            bodyFatPct = bodyFatPct,
            chestCm = chestCm,
            armCm = armCm,
            waistCm = waistCm,
            thighCm = thighCm,
            calfCm = calfCm
        )
        val updated = _uiState.value.bodyMeasurements + rec
        storage.saveBodyMeasurements(updated)
        _uiState.update { it.copy(bodyMeasurements = updated) }
    }

    companion object {
        fun buildDefaultWeeklySchedule(splitId: String, customDay: WorkoutDay?): Map<Int, String?> {
            if (customDay != null) {
                return mapOf(
                    1 to customDay.id, // Segunda
                    2 to null,         // Terça (Descanso)
                    3 to customDay.id, // Quarta
                    4 to null,         // Quinta (Descanso)
                    5 to customDay.id, // Sexta
                    6 to null,         // Sábado
                    7 to null          // Domingo
                )
            }
            val split = ExerciseRepository.defaultSplits.find { it.id == splitId }
                ?: ExerciseRepository.defaultSplits.first()
            val d = split.days
            return when (split.id) {
                "split_ppl_pro" -> mapOf(
                    1 to d.getOrNull(0)?.id, // Seg: Push A
                    2 to d.getOrNull(1)?.id, // Ter: Pull A
                    3 to d.getOrNull(2)?.id, // Qua: Legs A
                    4 to d.getOrNull(0)?.id, // Qui: Push
                    5 to d.getOrNull(1)?.id, // Sex: Pull
                    6 to d.getOrNull(3)?.id, // Sáb: Legs B
                    7 to null                // Dom: Descanso
                )
                "split_upper_lower" -> mapOf(
                    1 to d.getOrNull(0)?.id, // Seg: Upper 1
                    2 to d.getOrNull(1)?.id, // Ter: Lower 1
                    3 to null,               // Qua: Descanso
                    4 to d.getOrNull(2)?.id, // Qui: Upper 2
                    5 to d.getOrNull(3)?.id, // Sex: Lower 2
                    6 to null,               // Sáb: Descanso
                    7 to null                // Dom: Descanso
                )
                "split_abcde_classic" -> mapOf(
                    1 to d.getOrNull(0)?.id, // Seg: Treino A
                    2 to d.getOrNull(1)?.id, // Ter: Treino B
                    3 to d.getOrNull(2)?.id, // Qua: Treino C
                    4 to d.getOrNull(3)?.id, // Qui: Treino D
                    5 to d.getOrNull(4)?.id, // Sex: Treino E
                    6 to null,               // Sáb: Descanso
                    7 to null                // Dom: Descanso
                )
                "split_arnold" -> mapOf(
                    1 to d.getOrNull(0)?.id, // Seg: Peito & Costas
                    2 to d.getOrNull(1)?.id, // Ter: Ombros & Braços
                    3 to d.getOrNull(2)?.id, // Qua: Pernas & Core
                    4 to d.getOrNull(0)?.id, // Qui: Peito & Costas
                    5 to d.getOrNull(1)?.id, // Sex: Ombros & Braços
                    6 to d.getOrNull(2)?.id, // Sáb: Pernas & Core
                    7 to null                // Dom: Descanso
                )
                else -> mapOf(
                    1 to d.getOrNull(0)?.id, // Seg: Full Body A
                    2 to null,               // Ter: Descanso
                    3 to d.getOrNull(1)?.id, // Qua: Full Body B
                    4 to null,               // Qui: Descanso
                    5 to d.getOrNull(0)?.id, // Sex: Full Body A
                    6 to null,               // Sáb: Descanso
                    7 to null                // Dom: Descanso
                )
            }
        }

        fun estimate1RM(weightKg: Double, reps: Int): Double {
            if (weightKg <= 0.0) return 0.0
            if (reps <= 1) return weightKg
            return ((weightKg * (1.0 + reps / 30.0)) * 10.0).roundToInt() / 10.0
        }

        fun calculatePlatesPerSide(totalWeightKg: Double, barWeightKg: Double = 20.0): List<Double> {
            val perSide = ((totalWeightKg - barWeightKg) / 2.0).coerceAtLeast(0.0)
            val availablePlates = listOf(25.0, 20.0, 15.0, 10.0, 5.0, 2.5, 1.25)
            val result = mutableListOf<Double>()
            var remaining = (perSide * 100).roundToInt() / 100.0
            for (plate in availablePlates) {
                while (remaining >= plate - 0.001) {
                    result.add(plate)
                    remaining = ((remaining - plate) * 100).roundToInt() / 100.0
                }
            }
            return result
        }
    }
}
