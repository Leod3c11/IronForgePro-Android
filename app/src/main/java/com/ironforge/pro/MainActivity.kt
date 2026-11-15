package com.ironforge.pro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ironforge.pro.ui.components.HyperFloatingDock
import com.ironforge.pro.ui.screens.*
import com.ironforge.pro.ui.theme.*
import com.ironforge.pro.viewmodel.IronForgeViewModel

enum class MainNavTab(val label: String, val icon: ImageVector) {
    DASHBOARD("Home", Icons.Default.Insights),
    CALENDAR("Agenda", Icons.Default.CalendarMonth),
    WORKOUTS("Treinos", Icons.Default.FitnessCenter),
    ENCYCLOPEDIA("Vídeos", Icons.Default.OndemandVideo),
    SCIENCE("Ciência", Icons.Default.Science)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.BLACK),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.BLACK)
        )
        setContent {
            IronForgeAppRoot(
                onUpdateSystemBars = { isLight, bgColor ->
                    val argb = bgColor.toArgb()
                    if (isLight) {
                        enableEdgeToEdge(
                            statusBarStyle = SystemBarStyle.light(argb, argb),
                            navigationBarStyle = SystemBarStyle.light(argb, argb)
                        )
                    } else {
                        enableEdgeToEdge(
                            statusBarStyle = SystemBarStyle.dark(argb),
                            navigationBarStyle = SystemBarStyle.dark(argb)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun IronForgeAppRoot(
    viewModel: IronForgeViewModel = viewModel(),
    onUpdateSystemBars: (Boolean, Color) -> Unit = { _, _ -> }
) {
    val state by viewModel.uiState.collectAsState()
    var currentTabIndex by remember { mutableIntStateOf(0) }

    val dockItems = remember {
        MainNavTab.entries.map { it.label to it.icon }
    }

    IronForgeTheme(preset = state.themePreset) {
        val palette = LocalIronPalette.current
        LaunchedEffect(palette.isLight, palette.bg) {
            onUpdateSystemBars(palette.isLight, palette.bg)
        }

        if (!state.isOnboardingCompleted) {
            OnboardingScreen(
                initialProfile = state.userProfile,
                currentTheme = state.themePreset,
                onPreviewTheme = { viewModel.setThemePreset(it) },
                onComplete = { profile, theme, customTitle, customIds ->
                    viewModel.completeOnboarding(profile, theme, customTitle, customIds)
                }
            )
        } else if (state.isWorkoutActive) {
            Scaffold(
                containerColor = palette.bg
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    ActiveWorkoutScreen(state = state, viewModel = viewModel)
                }
            }
        } else if (state.briefingWorkoutDay != null) {
            WorkoutBriefingScreen(
                day = state.briefingWorkoutDay!!,
                state = state,
                viewModel = viewModel,
                onBack = { viewModel.closeWorkoutBriefing() },
                onStartFocusedWorkout = { viewModel.confirmStartFromBriefing() }
            )
        } else {
            Scaffold(
                containerColor = palette.bg
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding())
                ) {
                    when (MainNavTab.entries[currentTabIndex]) {
                        MainNavTab.DASHBOARD -> DashboardScreen(
                            state = state,
                            onOpenWorkoutBriefing = { viewModel.openWorkoutBriefing(it) },
                            onDismissSummary = { viewModel.dismissSummaryBanner() },
                            onSelectTheme = { viewModel.setThemePreset(it) },
                            onReopenOnboarding = { viewModel.reopenOnboarding() },
                            onOpenCalendar = { currentTabIndex = 1 },
                            onAddMeasurement = { w, bf, ch, arm, waist, thigh, calf ->
                                viewModel.addBodyMeasurement(w, bf, ch, arm, waist, thigh, calf)
                            }
                        )
                        MainNavTab.CALENDAR -> CalendarScreen(
                            state = state,
                            viewModel = viewModel,
                            onStartWorkoutDay = { viewModel.openWorkoutBriefing(it) }
                        )
                        MainNavTab.WORKOUTS -> WorkoutsScreen(
                            state = state,
                            onStartWorkoutDay = { viewModel.openWorkoutBriefing(it) },
                            onSaveCustomWorkout = { title, focus, ids ->
                                viewModel.saveCustomWorkoutDay(title, focus, ids)
                            },
                            onDeleteCustomWorkout = { viewModel.deleteCustomWorkout(it) },
                            onSaveEditedWorkoutDay = { viewModel.saveEditedWorkoutDay(it) },
                            onResetEditedWorkoutDay = { viewModel.resetEditedWorkoutDay(it) }
                        )
                        MainNavTab.ENCYCLOPEDIA -> EncyclopediaScreen(
                            state = state,
                            onSearchChange = { viewModel.updateSearchQuery(it) },
                            onSelectMuscle = { viewModel.selectMuscleFilter(it) },
                            onSelectEquipment = { viewModel.selectEquipmentFilter(it) },
                            onInspectExercise = { viewModel.inspectExercise(it) },
                            onAttachVideoUri = { exId, uri -> viewModel.attachCustomVideoUri(exId, uri) }
                        )
                        MainNavTab.SCIENCE -> ScienceLabScreen(
                            state = state,
                            viewModel = viewModel
                        )
                    }

                    HyperFloatingDock(
                        items = dockItems,
                        selectedIndex = currentTabIndex,
                        onSelect = { currentTabIndex = it },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .navigationBarsPadding()
                    )
                }
            }
        }
    }
}
