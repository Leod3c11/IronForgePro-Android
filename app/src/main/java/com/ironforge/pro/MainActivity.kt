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
            statusBarStyle = SystemBarStyle.dark(HyperBgDark.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(HyperBgDark.toArgb())
        )
        setContent {
            IronForgeAppRoot()
        }
    }
}

@Composable
fun IronForgeAppRoot(viewModel: IronForgeViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    var currentTabIndex by remember { mutableIntStateOf(0) }

    val dockItems = remember {
        MainNavTab.entries.map { it.label to it.icon }
    }

    IronForgeTheme(preset = state.themePreset) {
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
                containerColor = HyperBgDark
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    ActiveWorkoutScreen(state = state, viewModel = viewModel)
                }
            }
        } else {
            Scaffold(
                containerColor = HyperBgDark
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding())
                ) {
                    when (MainNavTab.entries[currentTabIndex]) {
                        MainNavTab.DASHBOARD -> DashboardScreen(
                            state = state,
                            onStartWorkoutDay = { viewModel.startWorkoutFromDay(it) },
                            onStartEmptyWorkout = { viewModel.startEmptyWorkout() },
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
                            onStartWorkoutDay = { viewModel.startWorkoutFromDay(it) }
                        )
                        MainNavTab.WORKOUTS -> WorkoutsScreen(
                            state = state,
                            onStartWorkoutDay = { viewModel.startWorkoutFromDay(it) },
                            onSaveCustomWorkout = { title, focus, ids ->
                                viewModel.saveCustomWorkoutDay(title, focus, ids)
                            },
                            onDeleteCustomWorkout = { viewModel.deleteCustomWorkout(it) }
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
