package com.br.b2b.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.br.b2b.domain.model.Product
import com.br.b2b.domain.routes.Screen
import com.br.b2b.ui.viewmodel.StoreViewModel
import com.br.b2b.util.FormatCurrency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

const val appBarTitle = "Detalhes"
const val appBarTitleFontSize = 18.0f
const val productTitleFontSize = 22.0f
const val productPriceFontSize = 18.0f
const val deliveryInfoFontSize = 12.0f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController, storeViewModel: StoreViewModel, productId: Int?
) {
    val product by storeViewModel.foundProduct.collectAsStateWithLifecycle()
    val productQuantityInCart by storeViewModel.productQuantityInCart.collectAsStateWithLifecycle()
    var isFavorited by rememberSaveable { mutableStateOf(product?.isFavorited ?: false) }
    val productQuantityInCartFlow = remember { MutableStateFlow(productQuantityInCart) }

    LaunchedEffect(productQuantityInCart) {
        productQuantityInCartFlow.value = productQuantityInCart
    }
    LaunchedEffect(product) {
        product?.let {
            isFavorited = it.isFavorited
            storeViewModel.getProductQuantityInCart(it.id)
        }
    }

    LaunchedEffect(Unit) {
        storeViewModel.findProductById(productId ?: 0)
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                text = appBarTitle,
                fontSize = TextUnit(appBarTitleFontSize, TextUnitType.Sp)
            )
        }, actions = {
            IconButton(onClick = {
                isFavorited = !isFavorited
                productId?.let { id ->
                    storeViewModel.toggleFavoriteStatus(id)
                }
            }) {
                Icon(
                    imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    tint = if (isFavorited) Color.Red else Color.Black,
                    contentDescription = "Favorite"
                )
            }
        }, navigationIcon = {
            IconButton(onClick = { navController.navigate(Screen.Products.route) }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Voltar"
                )
            }
        })
    }, content = { innerPadding ->
        ProductDetailContent(product, Modifier.padding(innerPadding))
    }, bottomBar = {
        if (productQuantityInCart == 0) {
            AddToCartButton(onClick = {
                product?.let {
                    storeViewModel.addProductToCart(product = it, onSuccess = {
                        storeViewModel.getProductQuantityInCart(it.id)
                    })
                }
            })
        } else {
            QuantitySelector(
                storeViewModel,
                navController,
                product = product,
                quantity = productQuantityInCart,
                onQuantityChange = { newQuantity ->
                    product?.let {
                        val quantityFlow = flowOf(newQuantity)
                        storeViewModel.updateCartItemQuantity(it.id, quantityFlow)
                    }
                }
            )
        }
    })
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
        onClick = onClick, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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
        ImageRequest.Builder(LocalContext.current).data(data = product?.images?.get(0))
            .apply(block = fun ImageRequest.Builder.() {
                scale(Scale.FILL)
            }).build()
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
    storeViewModel: StoreViewModel,
    navController: NavController,
    product: Product?,
    quantity: Int,
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
    val quantityFlow = remember { MutableStateFlow(quantity) }

    // Atualiza o MutableStateFlow sempre que a quantidade muda
    LaunchedEffect(quantity) {
        quantityFlow.value = quantity
    }

    // Chama a função modificada com o Flow de quantidades
    LaunchedEffect(product?.id) {
        product?.id?.let { productId ->
            storeViewModel.updateCartItemQuantity(productId, quantityFlow)
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
            IconButton(
                onClick = {
                    onQuantityChange(quantity - 1)
                }) {
                Icon(Icons.Filled.Remove, contentDescription = "Diminuir quantidade")
            }
            Text(text = quantity.toString(), fontWeight = FontWeight.Bold)
            IconButton(onClick = { onQuantityChange(quantity + 1) }) {
                Icon(Icons.Filled.Add, contentDescription = "Aumentar quantidade")
            }
            Button(onClick = { navController.navigate(Screen.CartItem.route) }) {
                Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "")
            }
        }
    }
}
