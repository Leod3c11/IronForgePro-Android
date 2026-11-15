package com.ironforge.pro.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ironforge.pro.model.BodyMeasurementRecord
import com.ironforge.pro.model.WorkoutDay
import com.ironforge.pro.model.WorkoutSessionRecord

class LocalStorageManager(context: Context) {

    private val prefs = context.getSharedPreferences("ironforge_hyperos_v2", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun loadSessionHistory(): List<WorkoutSessionRecord> {
        val json = prefs.getString("session_history", null)
        if (json.isNullOrBlank()) {
            val seed = defaultSeedHistory()
            saveSessionHistory(seed)
            return seed
        }
        return try {
            val type = object : TypeToken<List<WorkoutSessionRecord>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveSessionHistory(sessions: List<WorkoutSessionRecord>) {
        prefs.edit().putString("session_history", gson.toJson(sessions)).apply()
    }

    fun loadExercisePrs(): Map<String, Double> {
        val json = prefs.getString("exercise_prs", null)
        if (json.isNullOrBlank()) {
            val defaultPrs = mapOf(
                "chest_bench_barbell" to 105.0,
                "quad_high_bar_squat" to 140.0,
                "ham_rdl_barbell" to 130.0,
                "back_barbell_row" to 90.0,
                "shld_overhead_press_dumbbell" to 32.0,
                "glute_hip_thrust" to 160.0
            )
            saveExercisePrs(defaultPrs)
            return defaultPrs
        }
        return try {
            val type = object : TypeToken<Map<String, Double>>() {}.type
            gson.fromJson(json, type) ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    fun saveExercisePrs(prs: Map<String, Double>) {
        prefs.edit().putString("exercise_prs", gson.toJson(prs)).apply()
    }

    fun loadCustomWorkouts(): List<WorkoutDay> {
        val json = prefs.getString("custom_workouts", null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<WorkoutDay>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveCustomWorkouts(workouts: List<WorkoutDay>) {
        prefs.edit().putString("custom_workouts", gson.toJson(workouts)).apply()
    }

    fun loadCustomVideoUris(): Map<String, String> {
        val json = prefs.getString("custom_video_uris", null) ?: return emptyMap()
        return try {
            val type = object : TypeToken<Map<String, String>>() {}.type
            gson.fromJson(json, type) ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    fun saveCustomVideoUris(uris: Map<String, String>) {
        prefs.edit().putString("custom_video_uris", gson.toJson(uris)).apply()
    }

    fun loadBodyMeasurements(): List<BodyMeasurementRecord> {
        val json = prefs.getString("body_measurements", null)
        if (json.isNullOrBlank()) {
            val seed = listOf(
                BodyMeasurementRecord("m1", "01/10", 79.4, 14.2, 104.0, 39.2, 81.5, 59.0, 38.5),
                BodyMeasurementRecord("m2", "15/10", 80.1, 13.8, 105.0, 39.6, 81.0, 59.8, 38.8),
                BodyMeasurementRecord("m3", "01/11", 80.8, 13.5, 106.2, 40.1, 80.5, 60.5, 39.0),
                BodyMeasurementRecord("m4", "15/11", 81.5, 13.1, 107.0, 40.5, 80.0, 61.2, 39.3)
            )
            saveBodyMeasurements(seed)
            return seed
        }
        return try {
            val type = object : TypeToken<List<BodyMeasurementRecord>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveBodyMeasurements(records: List<BodyMeasurementRecord>) {
        prefs.edit().putString("body_measurements", gson.toJson(records)).apply()
    }

    private fun defaultSeedHistory(): List<WorkoutSessionRecord> {
        val now = System.currentTimeMillis()
        val hourMs = 3_600_000L
        return listOf(
            WorkoutSessionRecord(
                id = "seed_1",
                workoutTitle = "Push A — Peito, Ombros & Tríceps",
                dateFormatted = "Ontem",
                timestamp = now - 22 * hourMs,
                durationMinutes = 64,
                totalVolumeKg = 9840.0,
                totalWorkingSets = 20,
                prCount = 2,
                muscleSetsBreakdown = mapOf(
                    "Peitoral" to 7,
                    "Ombros (Deltoides)" to 7,
                    "Tríceps Braquial" to 6
                ),
                highlightExercises = listOf("Supino Reto 90kg × 8", "Elevação Lateral 16kg × 14")
            ),
            WorkoutSessionRecord(
                id = "seed_2",
                workoutTitle = "Pull A — Costas, Posterior & Bíceps",
                dateFormatted = "Há 3 dias",
                timestamp = now - 70 * hourMs,
                durationMinutes = 68,
                totalVolumeKg = 10420.0,
                totalWorkingSets = 20,
                prCount = 1,
                muscleSetsBreakdown = mapOf(
                    "Costas & Dorsais" to 10,
                    "Ombros (Deltoides)" to 4,
                    "Bíceps Braquial" to 6
                ),
                highlightExercises = listOf("Remada Curvada 80kg × 8", "Rosca Barra W 36kg × 10")
            ),
            WorkoutSessionRecord(
                id = "seed_3",
                workoutTitle = "Legs A — Quadríceps & Panturrilhas",
                dateFormatted = "Há 5 dias",
                timestamp = now - 118 * hourMs,
                durationMinutes = 72,
                totalVolumeKg = 13650.0,
                totalWorkingSets = 20,
                prCount = 2,
                muscleSetsBreakdown = mapOf(
                    "Quadríceps" to 10,
                    "Posteriores de Coxa" to 3,
                    "Panturrilhas" to 4,
                    "Abdômen & Core" to 3
                ),
                highlightExercises = listOf("Agachamento Livre 120kg × 7", "Leg Press 45° 240kg × 12")
            ),
            WorkoutSessionRecord(
                id = "seed_4",
                workoutTitle = "Legs B — Posteriores & Glúteos",
                dateFormatted = "Há 7 dias",
                timestamp = now - 166 * hourMs,
                durationMinutes = 63,
                totalVolumeKg = 11920.0,
                totalWorkingSets = 21,
                prCount = 1,
                muscleSetsBreakdown = mapOf(
                    "Posteriores de Coxa" to 7,
                    "Glúteos" to 4,
                    "Quadríceps" to 3,
                    "Panturrilhas" to 4,
                    "Abdômen & Core" to 3
                ),
                highlightExercises = listOf("Stiff RDL 110kg × 8", "Hip Thrust 140kg × 10")
            )
        )
    }
}
