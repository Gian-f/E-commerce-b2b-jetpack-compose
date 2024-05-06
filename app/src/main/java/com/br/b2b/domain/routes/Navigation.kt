package com.br.b2b.domain.routes

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.br.b2b.ui.screens.CardsScreen
import com.br.b2b.ui.screens.CartScreen
import com.br.b2b.ui.screens.HomeScreen
import com.br.b2b.ui.screens.NotificationsScreen
import com.br.b2b.ui.screens.OrdersScreen
import com.br.b2b.ui.screens.ProductDetailScreen
import com.br.b2b.ui.screens.ProfileScreen
import com.br.b2b.ui.screens.RegisterScreen
import com.br.b2b.ui.screens.SplashScreen
import com.br.b2b.ui.viewmodel.CartItemViewModel
import com.br.b2b.ui.viewmodel.LoginViewModel
import com.br.b2b.ui.viewmodel.SignUpViewModel
import com.br.b2b.ui.viewmodel.StoreViewModel
import com.br.b2b.ui.viewmodel.ThemeViewModel


@Composable
fun Navigation(navController: NavHostController) {
    val themeViewModel = hiltViewModel<ThemeViewModel>()
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val storeViewModel = hiltViewModel<StoreViewModel>()
    val cartItemViewModel = hiltViewModel<CartItemViewModel>()
    val signUpViewModel = hiltViewModel<SignUpViewModel>()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController, loginViewModel)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController, loginViewModel)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController, signUpViewModel)
        }
        composable(
            Screen.Products.route,
        ) {
            HomeScreen(navController, themeViewModel, storeViewModel)
        }

        composable(
            route = "${Screen.ProductDetail.route}/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            ProductDetailScreen(navController, storeViewModel, productId)
        }

        composable(
            Screen.Orders.route
        ) {
            OrdersScreen(navController)
        }
        composable(Screen.CartItem.route) {
            CartScreen(navController, cartItemViewModel)
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen(navController)
        }
        composable(
            Screen.Profile.route
        ) {
            ProfileScreen(navController)
        }
        composable(Screen.MyCards.route) {
            CardsScreen(navController)
        }
    }
}
