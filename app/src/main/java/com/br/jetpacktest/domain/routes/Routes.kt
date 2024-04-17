package com.br.jetpacktest.domain.routes

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity

sealed class Screen(val route: String, val title: String) {
    object Products : Screen("home", "Produtos")
    object Offers : Screen("offers", "Ofertas")
    object News : Screen("news", "Novidades")
    object Client : Screen("client", "Cliente")
    object Orders: Screen("orders", "Meus Pedidos")
    object Titles: Screen("titles", "Títulos")
    object MyCards: Screen("mycards", "Meus Cartões")
    object Culture: Screen("culture", "Quem Somos")
    object Contact: Screen("contact", "Contato")
    object Tutorial: Screen("tutorial", "Tutorial")
    object Settings: Screen("settings", "Configurações")
    object Login : Screen("login", "Login")
    object Notifications: Screen("notifications", "Notificações")
    object Favorites : Screen("favorites", "Favoritos")
    object Profile : Screen("profile", "Perfil")
    object Email : Screen("email", "Email")
}

private val LocalBackPressedDispatcher =
    staticCompositionLocalOf<OnBackPressedDispatcherOwner?> { null }


private class ComposableBackNavigationHandler(enabled: Boolean) : OnBackPressedCallback(enabled) {
    lateinit var onBackPressed: () -> Unit

    override fun handleOnBackPressed() {
        onBackPressed()
    }
}


@Composable
internal fun ComposableHandler(
    enabled: Boolean = true,
    onBackPressed: () -> Unit,
) {
    val dispatcher = (LocalBackPressedDispatcher.current ?: return).onBackPressedDispatcher

    val handler = remember { ComposableBackNavigationHandler(enabled) }

    DisposableEffect(dispatcher) {
        dispatcher.addCallback(handler)
        onDispose { handler.remove() }
    }

    LaunchedEffect(enabled) {
        handler.isEnabled = enabled
        handler.onBackPressed = onBackPressed
    }
}

@Composable
internal fun OnBackPress(onBackPressed: () -> Unit) {
    val context = LocalContext.current
    val fragmentActivity = context as? FragmentActivity

    if (fragmentActivity != null) {
        CompositionLocalProvider(
            LocalBackPressedDispatcher provides fragmentActivity
        ) {
            ComposableHandler {
                onBackPressed()
            }
        }
    } else {
        Log.e("", "onBackPressed error")
    }
}