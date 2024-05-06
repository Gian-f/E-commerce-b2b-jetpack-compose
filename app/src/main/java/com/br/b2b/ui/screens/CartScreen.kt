package com.br.b2b.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.br.b2b.domain.model.CartItem
import com.br.b2b.ui.viewmodel.CartItemViewModel

@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartItemViewModel
) {
    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Seu Carrinho", style = MaterialTheme.typography.headlineSmall)

        LazyColumn {
            items(cartItems) { cartItem ->
                CartItemRow(
                    item = cartItem,
                    onQuantityChange = { newQuantity ->
                        cartViewModel.updateCartItemQuantity(cartItem.productId, newQuantity)
                    },
                    onRemoveItem = { productId ->
                        cartViewModel.removeFromCart(productId)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { cartViewModel.clearCart() }) {
            Text("Limpar Carrinho")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Navegue para a tela de checkout */ }) {
            Text("Finalizar Compra")
        }
    }
}


@Composable
fun CartItemRow(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemoveItem: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = item.productTitle, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Quantidade: ${item.quantity}", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Slider(
            value = item.quantity.toFloat(),
            onValueChange = { newQuantity ->
                onQuantityChange(newQuantity.toInt())
            },
            valueRange = 1f..10f,
            steps = 1
        )

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(onClick = { onRemoveItem(item.productId) }) {
            Icon(Icons.Filled.Delete, contentDescription = "Remover item")
        }
    }
}
