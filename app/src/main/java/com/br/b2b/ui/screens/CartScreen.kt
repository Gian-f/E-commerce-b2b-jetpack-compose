package com.br.b2b.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.material3.Card
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
    val total by cartViewModel.total.collectAsStateWithLifecycle()
    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()
    val cartItemsQuantity by cartViewModel.quantity.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        cartViewModel.getAllItemsFromCart()
        cartViewModel.calculateQuantity()
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
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(it)
                        .padding(it)
                ) {
                    Text(
                        text = "Nenhum produto foi encontrado.",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 4.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(it)
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
            }
            Spacer(modifier = Modifier.height(16.dp))
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .consumeWindowInsets(WindowInsets.systemBars)
                    .padding(WindowInsets.systemBars.asPaddingValues())
            ) {
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
                        onClick = {

                        }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Finalizar", fontSize = 12.sp)
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .background(Color.White, CircleShape)
                            ) {
                                Text(
                                    text = "$cartItemsQuantity",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    )

    ConfirmDialog(
        onConfirm = {
            cartViewModel.clearCart(
                onComplete = {
                    cartViewModel.getAllItemsFromCart()
                }
            )
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
    var quantity by remember { mutableIntStateOf(item.quantity) }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Card(
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
            ) { newQuantity ->
                quantity = newQuantity
                if (newQuantity == 0) {
                    cartViewModel.removeFromCart(
                        item.productId,
                        onComplete = {
                            cartViewModel.getAllItemsFromCart()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun QuantitySelector(
    cartItemViewModel: CartItemViewModel,
    cartItem: CartItem,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    val totalPrice = cartItem.unitPrice.times(quantity)

    val formattedTotalPrice = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)) {
            append(FormatCurrency(totalPrice))
        }
        append("/")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = 10.sp)) {
            append("$quantity item(s)")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            IconButton(
                onClick = {
                    onQuantityChange(quantity - 1)
                    cartItemViewModel.updateCartItemQuantity(cartItem.productId, quantity - 1)
                }
            ) {
                Icon(
                    Icons.Filled.Remove,
                    contentDescription = "Diminuir quantidade",
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = quantity.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = {
                    onQuantityChange(quantity + 1)
                    cartItemViewModel.updateCartItemQuantity(cartItem.productId, quantity + 1)
                }
            ) {
                Icon(
                    Icons.Filled.Add, contentDescription = "Aumentar quantidade",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Text(
            text = formattedTotalPrice,
            fontWeight = FontWeight.Bold
        )
    }
}
