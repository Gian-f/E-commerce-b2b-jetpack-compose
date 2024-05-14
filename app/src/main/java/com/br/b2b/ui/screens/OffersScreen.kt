package com.br.b2b.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.br.b2b.domain.model.Product
import com.br.b2b.domain.routes.Screen
import com.br.b2b.ui.theme.BgColor
import com.br.b2b.ui.viewmodel.CartItemViewModel
import com.br.b2b.ui.viewmodel.StoreViewModel
import com.br.b2b.util.FormatCurrency
import com.br.b2b.util.Section
import com.br.jetpacktest.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersScreen(
    navController: NavController,
    storeViewModel: StoreViewModel,
    cartItemViewModel: CartItemViewModel
) {
    val featuredProducts by storeViewModel.recommendedProducts.collectAsStateWithLifecycle()
    val productsInCart by cartItemViewModel.cartItems.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Ofertas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_ios),
                            contentDescription = "Go Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.CartItem.route) }) {
                        Box(modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingCart,
                                contentDescription = "Shopping Cart"
                            )
                            if (productsInCart.isNotEmpty()) {
                                Badge(
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    containerColor = Color.Red,
                                )
                            }
                        }
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            item(
                key = Section.FeaturedProducts.id,
                contentType = Section.FeaturedProducts.contentType
            ) {
                SectionTitle(
                    title = "Produtos em desconto!", modifier = Modifier.padding(16.dp), false
                )
                LazyColumn(
                    contentPadding = PaddingValues(end = 64.dp),
                ) {
                    items(
                        items = featuredProducts.orEmpty(),
                        key = { item -> item.id },
                        contentType = { item -> item.id }) { product ->
                        ProductOfferItem(
                            product = product,
                            onProductClicked = {
                                navController.navigate(Screen.ProductDetail.route + "/${it.id}")
                            },
                            onFavoriteClicked = {
                                storeViewModel.toggleFavoriteStatus(product.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductOfferItem(
    product: Product,
    onProductClicked: (Product) -> Unit,
    onFavoriteClicked: (Product) -> Unit
) {
    var isFavorited by rememberSaveable { mutableStateOf(product.isFavorited) }
    val url by rememberSaveable { mutableStateOf(product.images[0]) }
    androidx.compose.material3.Card(elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .padding(12.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onProductClicked.invoke(product)
            }
            .padding(3.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(url)
                        .crossfade(true)
                        .build(),
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier.requiredSize(50.dp),
                            color = Color.Black,
                            strokeWidth = 1.dp,
                        )
                    },
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.FillBounds,
                )
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(top = 5.dp, end = 5.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .size(35.dp)
                        .align(Alignment.TopEnd)
                        .background(Color.White)
                        .clickable {
                            isFavorited = !isFavorited
                            onFavoriteClicked.invoke(product.copy(isFavorited = isFavorited))
                        }) {
                    IconButton(
                        onClick = {
                            isFavorited = !isFavorited
                            onFavoriteClicked.invoke(product.copy(isFavorited = isFavorited))
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favoritar produto",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Surface(
                color = BgColor, tonalElevation = 20.dp, shadowElevation = 12.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = product.title,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        fontSize = TextUnit(9F, TextUnitType.Sp),
                        fontWeight = FontWeight.W500
                    )
                    Text(
                        text = FormatCurrency(product.price),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        fontSize = TextUnit(12F, TextUnitType.Sp)
                    )
                }
            }
        }
    }
}