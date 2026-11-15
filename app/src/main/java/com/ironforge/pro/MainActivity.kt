package com.ironforge.pro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ironforge.pro.ui.screens.*
import com.ironforge.pro.ui.theme.*
import com.ironforge.pro.viewmodel.IronForgeViewModel

enum class MainNavTab(val label: String, val icon: ImageVector) {
    DASHBOARD("Dashboard", Icons.Default.Insights),
    WORKOUTS("Treinos", Icons.Default.FitnessCenter),
    ENCYCLOPEDIA("Exercícios", Icons.Default.MenuBook),
    SCIENCE("Ciência", Icons.Default.Science)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(ObsidianDeep.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(CarbonSurface.toArgb())
        )
        setContent {
            IronForgeTheme {
                IronForgeAppRoot()
            }
        }
    }
}

@Composable
fun IronForgeAppRoot(viewModel: IronForgeViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    var currentTab by remember { mutableStateOf(MainNavTab.DASHBOARD) }

    if (state.isWorkoutActive) {
        Scaffold(
            containerColor = ObsidianDeep
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
            containerColor = ObsidianDeep,
            bottomBar = {
                NavigationBar(
                    containerColor = CarbonSurface,
                    contentColor = TextPrimary
                ) {
                    MainNavTab.entries.forEach { tab ->
                        val selected = currentTab == tab
                        NavigationBarItem(
                            selected = selected,
                            onClick = { currentTab = tab },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label
                                )
                            },
                            label = {
                                Text(
                                    text = tab.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ObsidianDeep,
                                selectedTextColor = ElectricVolt,
                                indicatorColor = ElectricVolt,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    MainNavTab.DASHBOARD -> DashboardScreen(
                        state = state,
                        onStartWorkoutDay = { viewModel.startWorkoutFromDay(it) },
                        onStartEmptyWorkout = { viewModel.startEmptyWorkout() },
                        onDismissSummary = { viewModel.dismissSummaryBanner() },
                        onAddMeasurement = { w, bf, ch, arm, waist, thigh, calf ->
                            viewModel.addBodyMeasurement(w, bf, ch, arm, waist, thigh, calf)
                        }
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
                        onInspectExercise = { viewModel.inspectExercise(it) }
                    )
                    MainNavTab.SCIENCE -> ScienceLabScreen(
                        state = state,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
