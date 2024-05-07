package com.br.b2b.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartItemViewModel
) {
    val openDialog = remember { mutableStateOf(false) }
    var total by remember { mutableDoubleStateOf(0.0) }
    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()
    var cartItemsQuantity by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        cartViewModel.getAllItemsFromCart()
    }

    LaunchedEffect(cartItems) {
        total = cartItems.sumOf { it.totalPrice }
        cartItemsQuantity = cartItems.sumOf { it.quantity }
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
                        cartViewModel = cartViewModel,
                        item = cartItem,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { cartViewModel.clearCart() }) {
                Text("Limpar Carrinho")
            }
        },
        bottomBar = {
            HorizontalDivider(color = Color.LightGray)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
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
                Button(
                    modifier = Modifier.width(150.dp),
                    shape = RoundedCornerShape(10.dp),
                    onClick = { /*TODO*/ }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Finalizar")
                        Box(
                            modifier = Modifier
                                .padding(3.dp)
                                .background(Color.White, CircleShape)
                        ) {
                            Text(
                                text = "$cartItemsQuantity",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
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
    cartViewModel: CartItemViewModel,
    item: CartItem
) {
    var quantity by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        androidx.compose.material3.Card(
            modifier = Modifier
                .width(120.dp)
                .height(80.dp)
                .padding(start = 4.dp, end = 22.dp)
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

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = item.productTitle,
                maxLines = 1,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            QuantitySelector(
                cartItemViewModel = cartViewModel,
                cartItem = item,
                quantity = quantity,
                onQuantityChange = { newQuantity ->
                    quantity = newQuantity
                    if (newQuantity == 0) {
                        cartViewModel.removeFromCart(item.id)
                    }
                }
            )
            Text(
                text = FormatCurrency(item.unitPrice),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
fun QuantitySelector(
    cartItemViewModel: CartItemViewModel,
    cartItem: CartItem,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {

    val debounce = remember {
        MutableStateFlow(quantity)
    }

    LaunchedEffect(debounce) {
        debounce.debounce(500).map { newQuantity ->
            cartItemViewModel.updateCartItemQuantity(cartItem.id, newQuantity)
        }.collect()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        IconButton(
            onClick = {
                onQuantityChange(quantity - 1)
                debounce.value = quantity - 1
            }
        ) {
            Icon(
                Icons.Filled.Remove,
                contentDescription = "Diminuir quantidade",
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = cartItem.quantity.toString(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(
            onClick = {
                onQuantityChange(quantity + 1)
                debounce.value = quantity + 1
            }
        ) {
            Icon(
                Icons.Filled.Add, contentDescription = "Aumentar quantidade",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}