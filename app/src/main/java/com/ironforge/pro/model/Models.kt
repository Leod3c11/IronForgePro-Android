package com.ironforge.pro.model

enum class MuscleGroup(
    val displayName: String,
    val mevSets: Int,
    val mavSets: Int,
    val mrvSets: Int,
    val recoveryHours: Int
) {
    CHEST("Peitoral", 8, 16, 22, 48),
    BACK("Costas & Dorsais", 10, 18, 25, 48),
    SHOULDERS("Ombros (Deltoides)", 8, 16, 22, 36),
    BICEPS("Bíceps Braquial", 6, 14, 20, 36),
    TRICEPS("Tríceps Braquial", 6, 12, 18, 36),
    QUADS("Quadríceps", 8, 16, 22, 72),
    HAMSTRINGS("Posteriores de Coxa", 6, 12, 18, 72),
    GLUTES("Glúteos", 4, 12, 18, 48),
    CALVES("Panturrilhas", 8, 14, 20, 24),
    CORE("Abdômen & Core", 6, 12, 18, 24)
}

enum class EquipmentType(val label: String) {
    BARBELL("Barra Olímpica"),
    DUMBBELL("Halteres"),
    CABLE("Polia / Cabo"),
    MACHINE("Máquina"),
    BODYWEIGHT("Peso Corporal")
}

enum class MechanicsType(val label: String) {
    COMPOUND("Composto Multiarticular"),
    ISOLATION("Isolador Monoarticular")
}

enum class SetType(val code: String, val label: String) {
    WARMUP("W", "Aquecimento"),
    FEEDER("F", "Preparatória"),
    WORKING("T", "Trabalho"),
    TOP_SET("TOP", "Top Set"),
    DROP_SET("DROP", "Drop-Set")
}

data class Exercise(
    val id: String,
    val name: String,
    val muscleGroup: MuscleGroup,
    val primaryTarget: String,
    val synergists: List<String>,
    val equipment: EquipmentType,
    val mechanics: MechanicsType,
    val difficulty: String,
    val recommendedReps: String,
    val tempoTUT: String,
    val restSeconds: Int,
    val executionSteps: List<String>,
    val biomechanicsTips: List<String>,
    val commonMistakes: List<String>
)

data class PlannedExercise(
    val exerciseId: String,
    val targetSets: Int,
    val targetReps: String,
    val targetRir: Int,
    val restSeconds: Int,
    val notes: String = ""
)

data class WorkoutDay(
    val id: String,
    val dayLabel: String,
    val title: String,
    val focusSummary: String,
    val estimatedMinutes: Int,
    val exercises: List<PlannedExercise>
)

data class WorkoutSplit(
    val id: String,
    val name: String,
    val subtitle: String,
    val frequencyPerWeek: Int,
    val level: String,
    val description: String,
    val isCustom: Boolean = false,
    val days: List<WorkoutDay>
)

data class ActiveSetLog(
    val setNumber: Int,
    val setType: SetType = SetType.WORKING,
    val weightKg: Double = 20.0,
    val reps: Int = 10,
    val rpe: Double = 8.5,
    val completed: Boolean = false,
    val isPr: Boolean = false
)

data class ActiveExerciseSession(
    val exercise: Exercise,
    val targetReps: String,
    val restSeconds: Int,
    val sets: List<ActiveSetLog>
)

data class WorkoutSessionRecord(
    val id: String,
    val workoutTitle: String,
    val dateFormatted: String,
    val timestamp: Long,
    val durationMinutes: Int,
    val totalVolumeKg: Double,
    val totalWorkingSets: Int,
    val prCount: Int,
    val muscleSetsBreakdown: Map<String, Int>,
    val highlightExercises: List<String>
)

data class BodyMeasurementRecord(
    val id: String,
    val dateFormatted: String,
    val weightKg: Double,
    val bodyFatPct: Double,
    val chestCm: Double,
    val armCm: Double,
    val waistCm: Double,
    val thighCm: Double,
    val calfCm: Double
)

data class ScienceArticle(
    val id: String,
    val category: String,
    val title: String,
    val readTimeMinutes: Int,
    val summary: String,
    val keyTakeaways: List<String>,
    val detailedProtocol: String
)
