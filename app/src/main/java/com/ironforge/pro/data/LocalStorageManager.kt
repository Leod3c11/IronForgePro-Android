package com.ironforge.pro.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ironforge.pro.model.BodyMeasurementRecord
import com.ironforge.pro.model.UserProfile
import com.ironforge.pro.model.WorkoutDay
import com.ironforge.pro.model.WorkoutSessionRecord
import com.ironforge.pro.ui.theme.AppThemePreset

class LocalStorageManager(context: Context) {

    private val prefs = context.getSharedPreferences("ironforge_amoled_v6", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun isOnboardingCompleted(): Boolean =
        prefs.getBoolean("onboarding_completed", false)

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean("onboarding_completed", completed).apply()
    }

    fun loadUserProfile(): UserProfile {
        val json = prefs.getString("user_profile", null) ?: return UserProfile()
        return try {
            gson.fromJson(json, UserProfile::class.java) ?: UserProfile()
        } catch (e: Exception) {
            UserProfile()
        }
    }

    fun saveUserProfile(profile: UserProfile) {
        prefs.edit().putString("user_profile", gson.toJson(profile)).apply()
    }

    fun loadThemePreset(): AppThemePreset {
        val id = prefs.getString("theme_preset_id", AppThemePreset.AMOLED_SOLAR_ORANGE.id)
            ?: AppThemePreset.AMOLED_SOLAR_ORANGE.id
        return AppThemePreset.fromId(id)
    }

    fun saveThemePreset(preset: AppThemePreset) {
        prefs.edit().putString("theme_preset_id", preset.id).apply()
    }

    fun loadWeeklySchedule(): Map<Int, String?> {
        val json = prefs.getString("weekly_schedule", null) ?: return emptyMap()
        return try {
            val type = object : TypeToken<Map<Int, String?>>() {}.type
            gson.fromJson(json, type) ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    fun saveWeeklySchedule(schedule: Map<Int, String?>) {
        prefs.edit().putString("weekly_schedule", gson.toJson(schedule)).apply()
    }

    fun loadSessionHistory(): List<WorkoutSessionRecord> {
        val json = prefs.getString("session_history", null) ?: return emptyList()
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
        val json = prefs.getString("exercise_prs", null) ?: return emptyMap()
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
        val json = prefs.getString("body_measurements", null) ?: return emptyList()
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
}
