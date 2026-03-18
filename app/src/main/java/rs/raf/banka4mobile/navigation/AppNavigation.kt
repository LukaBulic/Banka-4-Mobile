package rs.raf.banka4mobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import rs.raf.banka4mobile.presentation.home.HomeScreen
import rs.raf.banka4mobile.presentation.login.LoginScreen
import rs.raf.banka4mobile.presentation.splash.SplashScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = { navController.navigateToLogin() }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigateToHome() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}

fun NavController.navigateToHome() {
    navigate(Screen.Home.route) {
        popUpTo(Screen.Login.route) {
            inclusive = true
        }
    }
}

fun NavController.navigateToLogin() {
    navigate(Screen.Login.route) {
        popUpTo(graph.startDestinationId) {
            inclusive = true
        }
    }
}