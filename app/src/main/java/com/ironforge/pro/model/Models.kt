package com.ironforge.pro.model

enum class ExperienceLevel(val label: String, val subtitle: String, val loadMultiplier: Double) {
    BEGINNER("Iniciante", "0 a 12 meses de treino contínuo", 0.65),
    INTERMEDIATE("Intermediário", "1 a 3 anos • Domina técnica básica", 1.0),
    ADVANCED("Avançado", "3+ anos • Periodização & alta carga", 1.35)
}

enum class FitnessGoal(val label: String, val kcalOffset: Double, val proteinPerKg: Double) {
    LEAN_BULK("Bulking Limpo (Hipertrofia)", 350.0, 2.0),
    RECOMP("Recomposição Corporal", 0.0, 2.1),
    CUTTING_MODERATE("Cutting (Definição Muscular)", -450.0, 2.2),
    STRENGTH("Força Bruta & Powerbuilding", 250.0, 1.9)
}

data class UserProfile(
    val name: String = "Atleta",
    val isMale: Boolean = true,
    val age: Int = 25,
    val weightKg: Double = 75.0,
    val heightCm: Double = 175.0,
    val bodyFatPct: Double = 14.0,
    val experience: ExperienceLevel = ExperienceLevel.INTERMEDIATE,
    val goal: FitnessGoal = FitnessGoal.LEAN_BULK,
    val weeklyDaysGoal: Int = 4,
    val selectedSplitId: String = "split_ppl_pro"
)

enum class MuscleGroup(
    val displayName: String,
    val shortName: String,
    val mevSets: Int,
    val mavSets: Int,
    val mrvSets: Int,
    val recoveryHours: Int
) {
    CHEST("Peitoral", "Peito", 8, 16, 22, 48),
    BACK("Costas & Dorsais", "Costas", 10, 18, 25, 48),
    SHOULDERS("Ombros (Deltoides)", "Ombros", 8, 16, 22, 36),
    BICEPS("Bíceps Braquial", "Bíceps", 6, 14, 20, 36),
    TRICEPS("Tríceps Braquial", "Tríceps", 6, 12, 18, 36),
    QUADS("Quadríceps", "Quads", 8, 16, 22, 72),
    HAMSTRINGS("Posteriores de Coxa", "Posterior", 6, 12, 18, 72),
    GLUTES("Glúteos", "Glúteos", 4, 12, 18, 48),
    CALVES("Panturrilhas", "Panturrilha", 8, 14, 20, 24),
    CORE("Abdômen & Core", "Core", 6, 12, 18, 24)
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

enum class MotionPattern(val biomechLabel: String) {
    BENCH_PRESS("Empurrar Horizontal"),
    INCLINE_PRESS("Empurrar Inclinado 30°"),
    FLY_CROSSOVER("Adução Horizontal"),
    DIPS("Mergulho Paralelas"),
    LAT_PULLDOWN("Puxada Vertical"),
    ROW_HORIZONTAL("Remada Horizontal"),
    PULL_UP("Barra Fixa Suspensa"),
    STRAIGHT_ARM_PULL("Extensão de Ombro"),
    OVERHEAD_PRESS("Empurrar Vertical"),
    LATERAL_RAISE("Abdução Escapular"),
    REVERSE_FLY("Abdução Horizontal"),
    BICEP_CURL("Flexão de Cotovelo"),
    PREACHER_CURL("Flexão Scott Isolada"),
    TRICEP_PUSHDOWN("Extensão de Cotovelo"),
    OVERHEAD_TRICEP("Extensão Overhead"),
    SKULLCRUSHER("Extensão Testa Supina"),
    SQUAT("Agachamento Profundo"),
    LEG_PRESS("Leg Press 45°"),
    LEG_EXTENSION("Extensão de Joelho"),
    LUNGE_SPLIT("Avanço Unilateral"),
    RDL_HINGE("Dobradiça de Quadril"),
    LEG_CURL("Flexão de Joelho"),
    HIP_THRUST("Extensão de Quadril"),
    CALF_RAISE("Flexão Plantar"),
    CRUNCH_FLEXION("Flexão Torácica"),
    LEG_RAISE("Retroversão Pélvica"),
    AB_WHEEL("Anti-Extensão Core"),
    DEADLIFT("Levantamento Terra")
}

enum class SetType(val code: String, val label: String, val educationalHint: String) {
    WARMUP("W", "Aquecimento", "Carga leve (~50%) para lubrificar articulações e preparar o sistema nervoso sem gerar fadiga."),
    FEEDER("F", "Preparatória", "Série intermediária (~75%) com poucas reps para sentir a carga antes da série principal."),
    WORKING("T", "Trabalho", "Série válida de hipertrofia: execute com amplitude total deixando 1 a 2 reps na reserva (RIR 1–2)."),
    TOP_SET("TOP", "Top Set", "Série mais pesada do exercício: busque progressão de carga ou repetições com técnica impecável."),
    DROP_SET("DROP", "Drop-Set", "Ao atingir a falha, reduza a carga em 20–25% imediatamente e continue sem descanso.")
}

data class Exercise(
    val id: String,
    val name: String,
    val muscleGroup: MuscleGroup,
    val motionPattern: MotionPattern,
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
    val isoDate: String = "",
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
    val chestCm: Double = 0.0,
    val armCm: Double = 0.0,
    val waistCm: Double = 0.0,
    val thighCm: Double = 0.0,
    val calfCm: Double = 0.0
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
