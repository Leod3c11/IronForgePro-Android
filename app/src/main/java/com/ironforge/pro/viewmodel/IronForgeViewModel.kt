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
    val sessionHistory: List<WorkoutSessionRecord> = emptyList(),
    val exercisePrs: Map<String, Double> = emptyMap(),
    val customWorkouts: List<WorkoutDay> = emptyList(),
    val customVideoUris: Map<String, String> = emptyMap(),
    val bodyMeasurements: List<BodyMeasurementRecord> = emptyList(),

    // Active Workout Session
    val isWorkoutActive: Boolean = false,
    val activeWorkoutTitle: String = "",
    val activeExercises: List<ActiveExerciseSession> = emptyList(),
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
    val oneRmWeightInput: String = "100",
    val oneRmRepsInput: String = "6",
    val plateTargetWeight: String = "100",
    val userWeightKg: String = "80",
    val userHeightCm: String = "178",
    val userAge: String = "26",
    val isMale: Boolean = true,
    val activityLevelIndex: Int = 2,
    val nutritionGoalIndex: Int = 0,
    val weeklySetsEstimator: Map<MuscleGroup, Int> = mapOf(
        MuscleGroup.CHEST to 14,
        MuscleGroup.BACK to 16,
        MuscleGroup.SHOULDERS to 15,
        MuscleGroup.BICEPS to 12,
        MuscleGroup.TRICEPS to 12,
        MuscleGroup.QUADS to 14,
        MuscleGroup.HAMSTRINGS to 10,
        MuscleGroup.GLUTES to 8,
        MuscleGroup.CALVES to 12,
        MuscleGroup.CORE to 9
    )
)

class IronForgeViewModel(application: Application) : AndroidViewModel(application) {

    private val storage = LocalStorageManager(application.applicationContext)

    private val _uiState = MutableStateFlow(
        IronForgeUiState(
            sessionHistory = storage.loadSessionHistory(),
            exercisePrs = storage.loadExercisePrs(),
            customWorkouts = storage.loadCustomWorkouts(),
            customVideoUris = storage.loadCustomVideoUris(),
            bodyMeasurements = storage.loadBodyMeasurements()
        )
    )
    val uiState: StateFlow<IronForgeUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var workoutClockJob: Job? = null

    fun attachCustomVideoUri(exerciseId: String, uriString: String) {
        val updated = _uiState.value.customVideoUris.toMutableMap()
        updated[exerciseId] = uriString
        storage.saveCustomVideoUris(updated)
        _uiState.update { it.copy(customVideoUris = updated) }
    }

    fun removeCustomVideoUri(exerciseId: String) {
        val updated = _uiState.value.customVideoUris.toMutableMap()
        updated.remove(exerciseId)
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

    fun startWorkoutFromDay(day: WorkoutDay) {
        val prs = _uiState.value.exercisePrs
        val activeList = day.exercises.mapNotNull { planned ->
            val ex = ExerciseRepository.getExerciseById(planned.exerciseId) ?: return@mapNotNull null
            val baseWeight = ((prs[ex.id] ?: 40.0) * 0.78).roundToInt().toDouble().coerceAtLeast(10.0)
            val sets = (1..planned.targetSets).map { idx ->
                ActiveSetLog(
                    setNumber = idx,
                    setType = if (idx == 1 && ex.mechanics == MechanicsType.COMPOUND) SetType.TOP_SET else SetType.WORKING,
                    weightKg = baseWeight,
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
                workoutElapsedSeconds = 0,
                lastFinishedSession = null,
                expandedVideoExerciseIds = emptySet()
            )
        }
        startWorkoutClock()
    }

    fun startEmptyWorkout(title: String = "Treino Livre HyperOS") {
        val defaultEx = ExerciseRepository.allExercises.take(3).map { ex ->
            ActiveExerciseSession(
                exercise = ex,
                targetReps = ex.recommendedReps,
                restSeconds = ex.restSeconds,
                sets = listOf(
                    ActiveSetLog(1, SetType.WARMUP, 20.0, 12, 6.0, false),
                    ActiveSetLog(2, SetType.WORKING, 40.0, 10, 8.5, false),
                    ActiveSetLog(3, SetType.WORKING, 40.0, 10, 9.0, false)
                )
            )
        }
        _uiState.update {
            it.copy(
                isWorkoutActive = true,
                activeWorkoutTitle = title,
                activeExercises = defaultEx,
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
        val currentPr = _uiState.value.exercisePrs[exercise.id] ?: 30.0
        val suggestedWeight = (currentPr * 0.75).roundToInt().toDouble().coerceAtLeast(10.0)
        val newSessionEx = ActiveExerciseSession(
            exercise = exercise,
            targetReps = exercise.recommendedReps,
            restSeconds = exercise.restSeconds,
            sets = (1..3).map { idx ->
                ActiveSetLog(idx, SetType.WORKING, suggestedWeight, 10, 8.5, false)
            }
        )
        _uiState.update {
            it.copy(activeExercises = it.activeExercises + newSessionEx)
        }
    }

    fun swapExerciseInActiveWorkout(exerciseIndex: Int, newExercise: Exercise) {
        val currentPr = _uiState.value.exercisePrs[newExercise.id] ?: 30.0
        val suggestedWeight = (currentPr * 0.75).roundToInt().toDouble().coerceAtLeast(10.0)
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
            state.copy(activeExercises = updated)
        }
    }

    fun addSetToExercise(exerciseIndex: Int) {
        _uiState.update { state ->
            val updated = state.activeExercises.mapIndexed { idx, item ->
                if (idx != exerciseIndex) item
                else {
                    val lastSet = item.sets.lastOrNull()
                    val nextNum = item.sets.size + 1
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
            state.copy(activeExercises = updated)
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
                            val isNewPr = nowCompleted && set.setType != SetType.WARMUP && est1Rm > prevBest && prevBest > 0.0
                            set.copy(completed = nowCompleted, isPr = isNewPr)
                        }
                    }
                    item.copy(sets = newSets)
                }
            }
            state.copy(activeExercises = updated)
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
                    if (est1Rm > prev) {
                        updatedPrs[sessionEx.exercise.id] = est1Rm
                        if (prev > 0.0) prCount += 1
                    }
                    if (est1Rm > bestSetScore) {
                        bestSetScore = est1Rm
                        bestSetText = "${sessionEx.exercise.name.take(20)} ${set.weightKg.toInt()}kg × ${set.reps}"
                    }
                }
            }
            if (bestSetText != null && highlights.size < 3) {
                highlights.add(bestSetText!!)
            }
        }

        val dateFmt = SimpleDateFormat("dd/MM", Locale("pt", "BR")).format(Date())
        val durationMin = (state.workoutElapsedSeconds / 60).coerceAtLeast(1)

        val record = WorkoutSessionRecord(
            id = UUID.randomUUID().toString(),
            workoutTitle = state.activeWorkoutTitle,
            dateFormatted = dateFmt,
            timestamp = System.currentTimeMillis(),
            durationMinutes = durationMin,
            totalVolumeKg = totalVolume,
            totalWorkingSets = totalWorkingSets,
            prCount = prCount,
            muscleSetsBreakdown = muscleBreakdown,
            highlightExercises = if (highlights.isEmpty()) listOf("Sessão concluída") else highlights
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
        fun estimate1RM(weightKg: Double, reps: Int): Double {
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
