package com.br.b2b.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.br.b2b.domain.model.CartItem
import com.br.b2b.ui.components.ConfirmDialog
import com.br.b2b.ui.viewmodel.CartItemViewModel
import com.br.b2b.util.FormatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartItemViewModel
) {
    val openDialog = remember { mutableStateOf(false) }
    var total by remember { mutableDoubleStateOf(0.0) }
    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        total = cartItems.sumOf { it.totalPrice }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Meu Carrinho") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { openDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    items = cartItems,
                    key = { cartItem ->
                        cartItem.id
                    },
                    contentType = { cartItem ->
                        cartItem.id
                    }
                ) { cartItem ->
                    CartItemRow(
                        item = cartItem,
                        onQuantityChange = { newQuantity ->
                            cartViewModel.updateCartItemQuantity(
                                cartItem.productId,
                                newQuantity
                            )
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
        },
        bottomBar = {
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Valor total:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    Text(text = FormatCurrency(total))
                }
                Button(onClick = { /*TODO*/ }) {
                    Text("Finalizar")
                }
            }
        }
    )

    ConfirmDialog(
        onConfirm = {

        },
        dialogState = openDialog,
        message = "Você deseja remover todos os itens do seu carrinho?"
    )
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

        androidx.compose.material3.Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(start = 22.dp, end = 22.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable { },
            elevation = CardDefaults.cardElevation(3.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = item.productImages[0],
                alignment = Alignment.Center,
                contentScale = ContentScale.FillBounds,
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.requiredSize(50.dp),
                        color = Color.Black,
                        strokeWidth = 1.dp,
                    )
                },
                contentDescription = null,
            )
        }

        Column {
            Text(
                text = item.productTitle,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Quantidade: ${item.quantity}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = FormatCurrency(item.unitPrice),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
    }
}
