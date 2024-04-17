package com.br.jetpacktest.domain.routes

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.br.jetpacktest.ui.screens.CardsScreen
import com.br.jetpacktest.ui.screens.HomeScreen
import com.br.jetpacktest.ui.screens.NotificationsScreen
import com.br.jetpacktest.ui.screens.OrdersScreen
import com.br.jetpacktest.ui.screens.ProfileScreen
import com.br.jetpacktest.ui.screens.SettingsScreen
import com.br.jetpacktest.ui.viewmodel.LoginViewModel
import com.br.jetpacktest.ui.viewmodel.ThemeViewModel


@Composable
fun Navigation(navController: NavHostController) {
    val themeViewModel = hiltViewModel<ThemeViewModel>()
    val loginViewModel = hiltViewModel<LoginViewModel>()
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController, loginViewModel)
        }
        composable(Screen.Products.route) {
            HomeScreen(navController, themeViewModel)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen(navController)
        }
        composable(Screen.Orders.route) {
            OrdersScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(Screen.MyCards.route) {
            CardsScreen(navController)
        }
    }
}
