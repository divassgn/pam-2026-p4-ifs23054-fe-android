package org.delcom.pam_p4_ifs23054.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.delcom.pam_p4_ifs23054.helper.ConstHelper
import org.delcom.pam_p4_ifs23054.ui.components.CustomSnackbar
import org.delcom.pam_p4_ifs23054.ui.screens.HomeScreen
import org.delcom.pam_p4_ifs23054.ui.screens.PlantsAddScreen
import org.delcom.pam_p4_ifs23054.ui.screens.PlantsDetailScreen
import org.delcom.pam_p4_ifs23054.ui.screens.PlantsEditScreen
import org.delcom.pam_p4_ifs23054.ui.screens.PlantsScreen
import org.delcom.pam_p4_ifs23054.ui.screens.ProfileScreen
import org.delcom.pam_p4_ifs23054.ui.screens.SkincareAddScreen
import org.delcom.pam_p4_ifs23054.ui.screens.SkincareDetailScreen
import org.delcom.pam_p4_ifs23054.ui.screens.SkincareEditScreen
import org.delcom.pam_p4_ifs23054.ui.screens.SkincareScreen
import org.delcom.pam_p4_ifs23054.ui.viewmodels.PlantViewModel
import org.delcom.pam_p4_ifs23054.ui.viewmodels.SkincareViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UIApp(
    navController: NavHostController = rememberNavController(),
    plantViewModel: PlantViewModel,
    skincareViewModel: SkincareViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                CustomSnackbar(snackbarData, onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() })
            }
        },
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = ConstHelper.RouteNames.Home.path,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FA))
        ) {
            // Home
            composable(route = ConstHelper.RouteNames.Home.path) {
                HomeScreen(navController = navController)
            }

            // Profile
            composable(route = ConstHelper.RouteNames.Profile.path) {
                ProfileScreen(navController = navController, plantViewModel = plantViewModel)
            }

            // =====================
            // Plants
            // =====================
            composable(route = ConstHelper.RouteNames.Plants.path) {
                PlantsScreen(navController = navController, plantViewModel = plantViewModel)
            }

            composable(route = ConstHelper.RouteNames.PlantsAdd.path) {
                PlantsAddScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    plantViewModel = plantViewModel
                )
            }

            composable(
                route = ConstHelper.RouteNames.PlantsDetail.path,
                arguments = listOf(navArgument("plantId") { type = NavType.StringType })
            ) { backStackEntry ->
                val plantId = backStackEntry.arguments?.getString("plantId") ?: ""
                PlantsDetailScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    plantViewModel = plantViewModel,
                    plantId = plantId
                )
            }

            composable(
                route = ConstHelper.RouteNames.PlantsEdit.path,
                arguments = listOf(navArgument("plantId") { type = NavType.StringType })
            ) { backStackEntry ->
                val plantId = backStackEntry.arguments?.getString("plantId") ?: ""
                PlantsEditScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    plantViewModel = plantViewModel,
                    plantId = plantId
                )
            }

            // =====================
            // Skincares
            // =====================
            composable(route = ConstHelper.RouteNames.Skincares.path) {
                SkincareScreen(
                    navController = navController,
                    skincareViewModel = skincareViewModel
                )
            }

            composable(route = ConstHelper.RouteNames.SkincareAdd.path) {
                SkincareAddScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    skincareViewModel = skincareViewModel
                )
            }

            composable(
                route = ConstHelper.RouteNames.SkincareDetail.path,
                arguments = listOf(navArgument("skincareId") { type = NavType.StringType })
            ) { backStackEntry ->
                val skincareId = backStackEntry.arguments?.getString("skincareId") ?: ""
                SkincareDetailScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    skincareViewModel = skincareViewModel,
                    skincareId = skincareId
                )
            }

            composable(
                route = ConstHelper.RouteNames.SkincareEdit.path,
                arguments = listOf(navArgument("skincareId") { type = NavType.StringType })
            ) { backStackEntry ->
                val skincareId = backStackEntry.arguments?.getString("skincareId") ?: ""
                SkincareEditScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    skincareViewModel = skincareViewModel,
                    skincareId = skincareId
                )
            }
        }
    }
}
