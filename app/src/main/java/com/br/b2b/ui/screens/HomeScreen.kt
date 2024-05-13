package com.br.b2b.ui.screens

import android.Manifest
import android.app.Application
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.br.b2b.data.dummy.NavigationDrawerData
import com.br.b2b.domain.model.CartItem
import com.br.b2b.domain.model.Product
import com.br.b2b.domain.routes.Screen
import com.br.b2b.ui.components.BottomSheetModalFilter
import com.br.b2b.ui.components.CategoriesButton
import com.br.b2b.ui.components.ConfirmDialog
import com.br.b2b.ui.components.FilterButton
import com.br.b2b.ui.components.HistoryItem
import com.br.b2b.ui.components.PagerIndicator
import com.br.b2b.ui.components.SegmentedButton
import com.br.b2b.ui.theme.BgColor
import com.br.b2b.ui.viewmodel.CartItemViewModel
import com.br.b2b.ui.viewmodel.StoreViewModel
import com.br.b2b.ui.viewmodel.ThemeViewModel
import com.br.b2b.ui.viewmodel.UserViewModel
import com.br.b2b.util.ComposeTheme
import com.br.b2b.util.FormatCurrency
import com.br.b2b.util.Section
import com.br.b2b.util.VoiceToTextParser
import com.br.b2b.util.applyCpfMask
import com.br.jetpacktest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    themeViewModel: ThemeViewModel,
    storeViewModel: StoreViewModel,
    cartItemViewModel: CartItemViewModel
) {
    val navigationItems = NavigationDrawerData.items
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        storeViewModel.fetchCategories()
        storeViewModel.fetchProducts()
    }

    BackHandler {
        openDialog.value = true
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        DrawerHeader(openDialog, userViewModel)
                    }
                    itemsIndexed(navigationItems) { index, item ->
                        NavigationDrawerItem(modifier = Modifier.padding(
                            NavigationDrawerItemDefaults.ItemPadding
                        ),
                            label = { Text(text = item.title) },
                            selected = index == selectedItemIndex,
                            onClick = {
                                try {
                                    navController.navigate(item.route)
                                } catch (e: Exception) {
                                    Log.d("Navigation Exception", e.toString())
                                }
                                selectedItemIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    }, contentDescription = item.title
                                )
                            },
                            badge = {
                                item.badgeCount?.let {
                                    Text(text = item.badgeCount.toString())
                                }
                            })
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        SegmentedButton(
                            items = listOf("Dia", "Noite"),
                            onItemSelection = { selectedItemIndex ->
                                themeViewModel.setTheme(if (selectedItemIndex == 0) ComposeTheme.Light else ComposeTheme.Dark)
                            }
                        )
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        HomeContent(scope, drawerState, navController, storeViewModel, cartItemViewModel)
        ConfirmDialog(
            dialogState = openDialog,
            message = "Tem certeza que deseja sair?",
            onConfirm = {
                userViewModel.deleteAllUsers()
                navController.navigate(Screen.Login.route)
            },
        )
    }
}

@Composable
private fun DrawerHeader(openDialog: MutableState<Boolean>, userViewModel: UserViewModel) {
    val loggedInUser by userViewModel.users.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        userViewModel.fetchAllUsers()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 26.dp, horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                modifier = Modifier
                    .border(
                        1.dp, Color.LightGray, CircleShape
                    )
                    .padding(12.dp)
                    .size(70.dp, 70.dp),
                contentDescription = "Person",
                tint = MaterialTheme.colorScheme.inversePrimary
            )
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = loggedInUser.firstOrNull()?.name ?: "Convidado",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = applyCpfMask(loggedInUser.firstOrNull()?.cpf ?: "Convidado", 0).first,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W400,
                )
                Row(
                    horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
                ) {
                    Button(colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ), onClick = { openDialog.value = true }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.ic_logout),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Logout"
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
)
private fun HomeContent(
    scope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavHostController,
    storeViewModel: StoreViewModel,
    cartItemViewModel: CartItemViewModel
) {
    val query by storeViewModel.query.collectAsStateWithLifecycle()
    val active by storeViewModel.active.collectAsStateWithLifecycle()
    val historyItems by storeViewModel.searchHistory.collectAsStateWithLifecycle(emptyList())
    val sheetState = rememberModalBottomSheetState(true)
    val snackbarHostState = remember { SnackbarHostState() }
    val products by storeViewModel.products.collectAsStateWithLifecycle()
    val filteredProducts by storeViewModel.filteredProducts.collectAsStateWithLifecycle()
    val categories by storeViewModel.categories.collectAsStateWithLifecycle()
    val productsInCart by cartItemViewModel.cartItems.collectAsStateWithLifecycle()
    val bannerPager = rememberPagerState { categories?.size ?: 0 }
    val application = LocalContext.current.applicationContext as Application
    val voiceToTextParser by remember { mutableStateOf(VoiceToTextParser(application)) }
    val voiceState by voiceToTextParser.state.collectAsStateWithLifecycle()
    var canRecord by remember { mutableStateOf(false) }

    val recordLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            canRecord = isGranted
            if (!isGranted) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Permissões do microfone não concedidas.",
                        actionLabel = null,
                        withDismissAction = false,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        })

    LaunchedEffect(key1 = recordLauncher) {
        recordLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                CenterAlignedTopAppBar(title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Compra",
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(20F, TextUnitType.Sp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Certa",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = TextUnit(20F, TextUnitType.Sp),
                            color = Color(0xFF0097b2)
                        )
                    }
                }, navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                }, actions = {
                    TopAppBarActions(navController, productsInCart)
                })
                Text(
                    modifier = Modifier.padding(start = 16.dp, bottom = 12.dp),
                    text = "Olá! Você está pronto para descobrir algo incrível hoje?",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(21F, TextUnitType.Sp)
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        DockedSearchBar(modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(8.dp),
                            shape = RoundedCornerShape(16.dp),
                            query = query.ifEmpty { voiceState.spokenText },
                            onQueryChange = { currentQuery ->
                                storeViewModel.setQuery(currentQuery.ifBlank { voiceState.spokenText })
                                if (query.length == 1) {
                                    voiceState.spokenText = ""
                                }
                            },
                            onSearch = {
                                val cleanedQuery = query.trim()
                                val cleanedSpokenText = voiceState.spokenText.trim()
                                val searchTerms = listOf(cleanedQuery, cleanedSpokenText)
                                    .filter { it.isNotEmpty() && !historyItems.contains(it) }
                                searchTerms.forEach { term ->
                                    storeViewModel.saveSearchHistory(term)
                                    storeViewModel.findProducts(term)
                                }
                                storeViewModel.toggleSearchBar()
                            }, active = active,
                            onActiveChange = { storeViewModel.toggleSearchBar() },
                            placeholder = { Text(text = "Procure...") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search"
                                )
                            },
                            trailingIcon = {
                                Row {
                                    val icon = if (voiceState.isSpeaking) {
                                        painterResource(id = R.drawable.ic_hearing)
                                    } else {
                                        painterResource(id = R.drawable.ic_mic)
                                    }

                                    Icon(painter = icon,
                                        contentDescription = "Voice",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .clickable {
                                                if (voiceState.isSpeaking) {
                                                    voiceToTextParser.stopListening()
                                                } else {
                                                    voiceToTextParser.startListening()
                                                }
                                            }
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    if (active) {
                                        Icon(imageVector = Icons.Filled.Close,
                                            contentDescription = "Search",
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .clickable {
                                                    if (query.isNotEmpty() || voiceState.spokenText.isNotEmpty()) {
                                                        storeViewModel.setQuery("")
                                                        voiceState.spokenText = ""
                                                    }
                                                    storeViewModel.toggleSearchBar()
                                                }
                                        )
                                    }
                                }
                            },
                            content = {
                                historyItems.reversed().forEach { itemName ->
                                    HistoryItem(name = itemName, storeViewModel)
                                }
                            }
                        )
                        FilterButton(
                            onClick = { scope.launch { sheetState.show() } },
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .width(70.dp)
                        )
                    }
                }
            }
        },
        content = { contentPadding ->
            if (!filteredProducts.isNullOrEmpty()) {
                Column {
                    ProductGrid(
                        query = query,
                        modifier = Modifier.padding(contentPadding),
                        filteredProducts = filteredProducts,
                        navController = navController,
                        storeViewModel = storeViewModel
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    item(
                        key = Section.Categories.id,
                        contentType = Section.Categories.contentType
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                            text = "Categorias",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = TextUnit(16F, TextUnitType.Sp)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp, top = 8.dp, start = 12.dp, end = 12.dp)
                        ) {
                            items(items = categories.orEmpty(),
                                key = { it.id },
                                contentType = { it.id }) { categories ->
                                CategoriesButton(
                                    name = categories.name,
                                    onClick = {
                                        storeViewModel.findProducts(categories.name)
                                        storeViewModel.setQuery(categories.name)
                                    }
                                )
                            }
                        }
                    }

                    item(
                        key = Section.Banners.id, contentType = Section.Banners.contentType
                    ) {
                        HorizontalPager(
                            modifier = Modifier.padding(top = 16.dp),
                            state = bannerPager,
                        ) { index ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .padding(start = 22.dp, end = 22.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { },
                                elevation = CardDefaults.cardElevation(3.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                SubcomposeAsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    model = categories?.get(index)?.image,
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
                        }
                        PagerIndicator(bannerPager, categories)
                    }

                    item(
                        key = Section.FeaturedProducts.id,
                        contentType = Section.FeaturedProducts.contentType
                    ) {
                        SectionTitle(
                            title = "Produtos em destaque", modifier = Modifier.padding(16.dp)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(end = 64.dp),
                        ) {
                            items(items = products.orEmpty(),
                                key = { item -> item.id },
                                contentType = { item -> item.id }) { product ->
                                ProductItem(product = product, onProductClicked = {
                                    navController.navigate(Screen.ProductDetail.route + "/${it.id}")
                                }, onFavoriteClicked = {
                                    storeViewModel.toggleFavoriteStatus(product.id)
                                })
                            }
                        }
                    }

                    item(
                        key = Section.RecommendedProducts.id,
                        contentType = Section.RecommendedProducts.contentType
                    ) {
                        SectionTitle(
                            title = "Produtos Recomendados", modifier = Modifier.padding(16.dp)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(end = 64.dp),
                        ) {
                            items(items = products.orEmpty(),
                                key = { item -> item.id },
                                contentType = { item -> item.id }) { product ->
                                ProductItem(product = product, onProductClicked = {
                                    navController.navigate(Screen.ProductDetail.route + "/${it.id}")
                                }, onFavoriteClicked = {
                                    storeViewModel.toggleFavoriteStatus(product.id)
                                })
                            }
                        }
                    }
                }
            }
        }
    )
    if (sheetState.isVisible) {
        BottomSheetModalFilter(sheetState, scope)
    }
}


@Composable
private fun TopAppBarActions(navController: NavHostController, productsInCart: List<CartItem>) {
    Row {
        IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_notifications),
                modifier = Modifier.size(24.dp),
                contentDescription = "Notifications"
            )
        }
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
}


@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextUnit(16F, TextUnitType.Sp)
        )
        Text(text = "Ver mais",
            fontWeight = FontWeight.SemiBold,
            fontSize = TextUnit(13F, TextUnitType.Sp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable { })
    }
}

@Composable
fun ProductItem(
    product: Product,
    onProductClicked: (Product) -> Unit,
    onFavoriteClicked: (Product) -> Unit
) {
    var isFavorited by rememberSaveable { mutableStateOf(product.isFavorited) }
    val url by rememberSaveable { mutableStateOf(product.images[0]) }
    Card(elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .width(180.dp)
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


@Composable
fun ProductItemFilter(
    product: Product,
    onProductClicked: (Product) -> Unit,
    onFavoriteClicked: (Product) -> Unit
) {
    var isFavorited by rememberSaveable { mutableStateOf(product.isFavorited) }
    val url by rememberSaveable { mutableStateOf(product.images[0]) }
    val halfScreenWidth = LocalConfiguration.current.screenWidthDp.dp / 2

    Card(elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .width(halfScreenWidth)
            .height(250.dp)
            .padding(6.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onProductClicked.invoke(product)
            }
            .padding(3.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
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
                        }, modifier = Modifier.size(24.dp)
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
                        maxLines = 1,
                        text = product.title,
                        textAlign = TextAlign.Start,
                        fontSize = TextUnit(9F, TextUnitType.Sp),
                        fontWeight = FontWeight.W500
                    )
                    Text(
                        maxLines = 1,
                        text = FormatCurrency(product.price),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(12F, TextUnitType.Sp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductGrid(
    query: String,
    modifier: Modifier,
    filteredProducts: List<Product>?,
    navController: NavController,
    storeViewModel: StoreViewModel
) {
    LaunchedEffect(Unit) {
        storeViewModel.fetchProducts()
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Exibindo resultados para: ", fontSize = 16.sp)
            Text(text = query, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(
                items = filteredProducts.orEmpty(),
                key = { item -> item.id }) { product ->
                ProductItemFilter(
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
