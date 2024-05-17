package com.br.b2b.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.br.b2b.domain.model.Product
import com.br.b2b.ui.viewmodel.CartItemViewModel
import com.br.b2b.ui.viewmodel.StoreViewModel
import com.br.b2b.util.FormatCurrency
import kotlinx.coroutines.Dispatchers

const val appBarTitle = "Detalhes"
const val appBarTitleFontSize = 18.0f
const val productTitleFontSize = 22.0f
const val productPriceFontSize = 18.0f
const val deliveryInfoFontSize = 12.0f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    cartItemViewModel: CartItemViewModel,
    storeViewModel: StoreViewModel,
    productId: Int?,
    onBackPress: () -> Unit,
    onNavigate: () -> Unit
) {
    val product by storeViewModel.foundProduct.collectAsStateWithLifecycle()
    val foundedCartItem by cartItemViewModel.foundedCartItem.collectAsStateWithLifecycle()
    var quantity by remember { mutableIntStateOf(0) }
    var isFavorited by rememberSaveable { mutableStateOf(product?.isFavorited ?: false) }

    LaunchedEffect(Unit) {
        cartItemViewModel.getAllItemsFromCart()
        storeViewModel.findProductById(productId ?: 0)
    }

    LaunchedEffect(foundedCartItem) {
        cartItemViewModel.findCartItemsById(productId ?: 0)
        quantity = foundedCartItem?.quantity ?: 0
    }

    LaunchedEffect(product) {
        product?.let {
            isFavorited = it.isFavorited
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = appBarTitle,
                        fontSize = TextUnit(appBarTitleFontSize, TextUnitType.Sp)
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            isFavorited = !isFavorited
                            productId?.let { id ->
                                storeViewModel.toggleFavoriteStatus(id)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            tint = if (isFavorited) Color.Red else Color.Black,
                            contentDescription = "Favorite"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPress.invoke() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            ProductDetailContent(
                product, Modifier.padding(innerPadding)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .consumeWindowInsets(WindowInsets.systemBars)
                    .padding(WindowInsets.systemBars.asPaddingValues())
            ) {
                if (quantity == 0) {
                    AddToCartButton(
                        onClick = {
                            quantity++
                            product?.let {
                                storeViewModel.addProductToCart(it)
                            }
                        }
                    )
                } else {
                    QuantitySelector(
                        cartItemViewModel = cartItemViewModel,
                        product = product,
                        quantity = quantity,
                        onNavigate = { onNavigate.invoke() }) { newQuantity ->
                        quantity = newQuantity
                        if (newQuantity == 0) {
                            product?.let {
                                storeViewModel.removeProductFromCart(it.id)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ProductDetailContent(
    product: Product?, modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            ProductImage(product)
            Spacer(modifier = Modifier.height(16.dp))
            ProductInfo(product)
        }
    }
}

@Composable
fun AddToCartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.AddShoppingCart,
            contentDescription = "Adicionar ao carrinho",
            modifier = Modifier.padding(12.dp)
        )
        Text(
            text = "Adicionar ao carrinho",
            fontWeight = FontWeight.SemiBold,
            fontSize = TextUnit(16F, TextUnitType.Sp),
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
fun ProductImage(product: Product?) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCacheKey(product?.images?.get(0))
            .dispatcher(Dispatchers.IO)
            .data(data = product?.images?.get(0))
            .apply(block = fun ImageRequest.Builder.() {
                scale(Scale.FILL)
            }
            ).build()
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun ProductInfo(product: Product?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = product?.title ?: "Carregando...",
            maxLines = 1,
            textAlign = TextAlign.Center,
            fontSize = TextUnit(productTitleFontSize, TextUnitType.Sp),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(color = Color.LightGray, thickness = 2.dp)
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = FormatCurrency(product?.price ?: 0.0),
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(productPriceFontSize, TextUnitType.Sp),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.LocalShipping, contentDescription = "Taxa de entrega"
                )
                Text(
                    text = "Entrega grátis!",
                    fontSize = TextUnit(deliveryInfoFontSize, TextUnitType.Sp),
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Todas as taxas já inclusas.", color = Color.Gray, fontWeight = FontWeight.W400
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = product?.description ?: "Carregando...",
            fontSize = TextUnit(productTitleFontSize, TextUnitType.Sp)

        )
    }
}


@Composable
fun QuantitySelector(
    cartItemViewModel: CartItemViewModel,
    product: Product?,
    quantity: Int,
    onNavigate: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    val totalPrice = product?.price?.times(quantity) ?: 0.0

    val formattedTotalPrice = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)) {
            append(FormatCurrency(totalPrice))
        }
        append("/")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = 10.sp)) {
            append("$quantity item(s)")
        }
    }

    HorizontalDivider(color = Color.LightGray)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Text(
            text = formattedTotalPrice, fontWeight = FontWeight.Bold
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            IconButton(onClick = {
                onQuantityChange(quantity - 1)
                cartItemViewModel.updateCartItemQuantity(product?.id ?: 0, quantity - 1)
            }) {
                Icon(Icons.Filled.Remove, contentDescription = "Diminuir quantidade")
            }
            Text(text = quantity.toString(), fontWeight = FontWeight.Bold)
            IconButton(onClick = {
                onQuantityChange(quantity + 1)
                cartItemViewModel.updateCartItemQuantity(product?.id ?: 0, quantity + 1)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Aumentar quantidade")
            }
            Button(shape = RoundedCornerShape(10.dp), onClick = { onNavigate.invoke() }) {
                Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "")
            }
        }
    }
}