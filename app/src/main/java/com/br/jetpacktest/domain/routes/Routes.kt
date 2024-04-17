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
    data object Products : Screen("home", "Produtos")
    data object Offers : Screen("offers", "Ofertas")
    data object News : Screen("news", "Novidades")
    data object Client : Screen("client", "Cliente")
    data  object Orders: Screen("orders", "Meus Pedidos")
    data object Titles: Screen("titles", "Títulos")
    data object MyCards: Screen("mycards", "Meus Cartões")
    data object Culture: Screen("culture", "Quem Somos")
    data object Contact: Screen("contact", "Contato")
    data object Tutorial: Screen("tutorial", "Tutorial")
    data object Settings: Screen("settings", "Configurações")
    data object Login : Screen("login", "Login")
    data object Notifications: Screen("notifications", "Notificações")
    data object Favorites : Screen("favorites", "Favoritos")
    data object Register : Screen("register", "Registrar-se")
    data object Profile : Screen("profile", "Perfil")
    data object Email : Screen("email", "Email")
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