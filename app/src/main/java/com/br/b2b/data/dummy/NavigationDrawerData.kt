package com.br.b2b.data.dummy

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ContactSupport
import androidx.compose.material.icons.automirrored.outlined.ContactSupport
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import com.br.b2b.domain.model.NavigationDrawerItem
import com.br.b2b.domain.routes.Screen

object NavigationDrawerData {
    val items = listOf(
        NavigationDrawerItem(
            title = Screen.Products.title,
            route = Screen.Products.route,
            selectedIcon = Icons.Filled.ShoppingCart,
            unselectedIcon = Icons.Outlined.ShoppingCart,
        ),
        NavigationDrawerItem(
            title = Screen.Offers.title,
            route = Screen.Offers.route,
            selectedIcon = Icons.Filled.LocalOffer,
            unselectedIcon = Icons.Outlined.LocalOffer,
        ),
        NavigationDrawerItem(
            title = Screen.News.title,
            route = Screen.News.route,
            selectedIcon = Icons.Filled.Newspaper,
            unselectedIcon = Icons.Outlined.Newspaper,
            badgeCount = 10
        ),
        /*        NavigationDrawerItem(
                    title = Screen.Client.title,
                    route = Screen.Client.route,
                    selectedIcon = Icons.Filled.Person,
                    unselectedIcon = Icons.Outlined.Person,
                ),*/
        NavigationDrawerItem(
            title = Screen.Orders.title,
            route = Screen.Orders.route,
            selectedIcon = Icons.Filled.ShoppingBag,
            unselectedIcon = Icons.Outlined.ShoppingBag,
        ),
        /*
                NavigationDrawerItem(
                    title = Screen.Titles.title,
                    route = Screen.Titles.route,
                    selectedIcon = Icons.Filled.AccountBalance,
                    unselectedIcon = Icons.Outlined.AccountBalance,
                ),
        */
//        NavigationDrawerItem(
//            title = Screen.MyCards.title,
//            route = Screen.MyCards.route,
//            selectedIcon = Icons.Filled.CreditCard,
//            unselectedIcon = Icons.Outlined.CreditCard,
//        ),
        NavigationDrawerItem(
            title = Screen.Culture.title,
            route = Screen.Culture.route,
            selectedIcon = Icons.Filled.Business,
            unselectedIcon = Icons.Outlined.Business,
        ),
        NavigationDrawerItem(
            title = Screen.Contact.title,
            route = Screen.Contact.route,
            selectedIcon = Icons.AutoMirrored.Filled.ContactSupport,
            unselectedIcon = Icons.AutoMirrored.Outlined.ContactSupport,
        ),
        /*        NavigationDrawerItem(
                    title = Screen.Tutorial.title,
                    route = Screen.Tutorial.route,
                    selectedIcon = Icons.Filled.Help,
                    unselectedIcon = Icons.Outlined.HelpOutline,
                ),*/
        NavigationDrawerItem(
            title = Screen.Settings.title,
            route = Screen.Settings.route,
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        ),
    )
}