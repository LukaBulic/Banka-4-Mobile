package rs.raf.banka4mobile.navigation

sealed class Screen(val route: String) {

    data object Splash : Screen("splash")

    data object Login : Screen("login")

    data object Home : Screen("home")

}