package com.br.b2b.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.br.b2b.domain.routes.Screen
import com.br.b2b.ui.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    storeViewModel: StoreViewModel,
    productId: Int?
) {
    val product by storeViewModel.foundProduct.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        storeViewModel.findProductById(productId ?: 0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = if (product?.isFavorited == true) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Products.route) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
        content = {
            androidx.compose.material3.Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = product?.title ?: "Loading...",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = product?.description ?: "Loading...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    )
}

