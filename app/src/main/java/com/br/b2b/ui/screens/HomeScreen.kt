package com.br.b2b.ui.screens

import android.Manifest
import android.app.Application
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.br.b2b.data.dummy.BannersDummyData.banners
import com.br.b2b.data.dummy.CategoriesDummyData.categories
import com.br.b2b.data.dummy.NavigationDrawerData
import com.br.b2b.data.dummy.NotificationsData.items
import com.br.b2b.data.dummy.ProductsData.products
import com.br.b2b.domain.model.Products
import com.br.b2b.domain.routes.Screen
import com.br.b2b.ui.components.BottomSheetModalFilter
import com.br.b2b.ui.components.CategoriesButton
import com.br.b2b.ui.components.ConfirmDialog
import com.br.b2b.ui.components.FilterButton
import com.br.b2b.ui.components.HistoryItem
import com.br.b2b.ui.components.PagerIndicator
import com.br.b2b.ui.components.SegmentedButton
import com.br.b2b.ui.theme.BgColor
import com.br.b2b.ui.viewmodel.ThemeViewModel
import com.br.b2b.util.ComposeTheme
import com.br.b2b.util.VoiceToTextParser
import com.br.jetpacktest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavHostController, themeViewModel: ThemeViewModel) {
    HomeContent(navController, themeViewModel)
}

@Composable
private fun HomeContent(navController: NavHostController, themeViewModel: ThemeViewModel) {
    val navigationItems = NavigationDrawerData.items
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                LazyColumn {
                    item {
                        DrawerHeader(openDialog)
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
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        SegmentedButton(
                            items = listOf("Dia", "Noite"),
                            onItemSelection = { selectedItemIndex ->
                                themeViewModel.setTheme(if (selectedItemIndex == 0) ComposeTheme.Light else ComposeTheme.Dark)
                            })
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        },
        gesturesEnabled = true,
        drawerState = drawerState
    ) {
        PageContent(scope, drawerState, navController)
        ConfirmDialog(
            dialogState = openDialog,
            message = "Tem certeza que deseja sair?",
            onConfirm = {
                navController.navigate(Screen.Login.route)
            },
        )
    }
}

@Composable
private fun DrawerHeader(openDialog: MutableState<Boolean>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 26.dp, horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Center
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
                tint = MaterialTheme.colorScheme.secondary
            )
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Gian Felipe",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "12345678",
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
private fun PageContent(
    scope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavHostController,
) {
    var isSearchBarVisible by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val items: MutableList<String> = remember { mutableStateListOf() }
    val sheetState = rememberModalBottomSheetState(true)
    val snackbarHostState = remember { SnackbarHostState() }
    val bannerPager = rememberPagerState { banners.size }

    val application = LocalContext.current.applicationContext as Application

    val voiceToTextParser by remember { mutableStateOf(VoiceToTextParser(application)) }

    val voiceState by voiceToTextParser.state.collectAsState()

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
        }
    )

    LaunchedEffect(key1 = recordLauncher) {
        recordLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                CenterAlignedTopAppBar(title = {
                }, navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                }, actions = {
                    TopAppBarActions(navController)
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
                            query = query.ifEmpty { voiceState.spokenText },
                            onQueryChange = { currentQuery ->
                                query = currentQuery.ifBlank { voiceState.spokenText }
                                if (query.length == 1) {
                                    voiceState.spokenText = ""
                                }
                            },
                            onSearch = {
                                val cleanedQuery = query.trim()
                                val cleanedSpokenText = voiceState.spokenText.trim()
                                if (cleanedQuery.isNotEmpty() && !items.contains(cleanedQuery)) {
                                    items.add(cleanedQuery)
                                }
                                if (cleanedSpokenText.isNotEmpty() &&
                                    !items.contains(cleanedSpokenText)
                                ) {
                                    items.add(cleanedSpokenText)
                                }
                                active = false
                            },
                            active = active,
                            onActiveChange = { active = it },
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
                                                        query = ""
                                                        voiceState.spokenText = ""
                                                    } else {
                                                        active = false
                                                        isSearchBarVisible = false
                                                    }
                                                })
                                    }
                                }
                            }) {
                            items.reversed().forEach { itemName ->
                                HistoryItem(name = itemName)
                            }
                        }
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                item {
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
                            .padding(bottom = 12.dp, top = 8.dp, start = 4.dp, end = 12.dp)
                    ) {
                        items(
                            items = categories,
                            key = { it.label },
                            contentType = { it.label }
                        ) { categories ->
                            CategoriesButton(
                                label = categories.label,
                                icon = categories.icon,
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = categories.label,
                                            actionLabel = null,
                                            withDismissAction = true,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                item {
                    HorizontalPager(
                        modifier = Modifier.padding(top = 16.dp),
                        state = bannerPager,
                    ) { index ->
                        val url = banners[index].image
                        val imagePainter = rememberAsyncImagePainter(url)
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
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = imagePainter,
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                            )
                        }
                    }
                    PagerIndicator(bannerPager)
                }

                item {
                    SectionTitle(title = "Produtos em destaque", modifier = Modifier.padding(16.dp))
                    LazyRow(
                        contentPadding = PaddingValues(end = 64.dp),
                    ) {
                        items(
                            items = products,
                            key = { item -> item.id },
                            contentType = { item -> item.id }) { product ->
                            ProductItem(
                                product = product,
                                onFavoriteClicked = {

                                }
                            )
                        }
                    }
                }

                item {
                    SectionTitle(
                        title = "Produtos Recomendados",
                        modifier = Modifier.padding(16.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(end = 64.dp),
                    ) {
                        items(
                            items = products,
                            key = { item -> item.id },
                            contentType = { item -> item.id }) { product ->
                            ProductItem(
                                product = product,
                                onFavoriteClicked = {

                                }
                            )
                        }
                    }
                }
            }
        })
    if (sheetState.isVisible) {
        BottomSheetModalFilter(sheetState, scope)
    }
}

@Composable
private fun TopAppBarActions(navController: NavHostController) {
    BadgedBox(
        badge = {
            if (items.isNotEmpty()) {
                Badge()
            }
        }) {

    }
    Row {
        IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_notifications),
                modifier = Modifier.size(24.dp),
                contentDescription = "Notifications"
            )
        }
        IconButton(onClick = {  }) {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                modifier = Modifier.size(24.dp),
                contentDescription = "Notifications"
            )
        }
    }
}


@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextUnit(16F, TextUnitType.Sp)
        )
        Text(
            text = "Ver mais",
            fontWeight = FontWeight.SemiBold,
            fontSize = TextUnit(13F, TextUnitType.Sp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ProductItem(product: Products, onFavoriteClicked: () -> Unit) {
    val imagePainter = rememberAsyncImagePainter(product.image)
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(230.dp)
            .padding(12.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { },
        elevation = CardDefaults.cardElevation(16.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = imagePainter,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                )
                IconButton(
                    onClick = onFavoriteClicked,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (product.isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favoritar produto",
                        tint = Color.Red
                    )
                }
            }
            Surface(
                color = BgColor,
                modifier = Modifier
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = product.description,
                        textAlign = TextAlign.Start,
                        fontSize = TextUnit(11F, TextUnitType.Sp),
                        fontWeight = FontWeight.W400
                    )
                    Text(
                        text = product.price,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(13F, TextUnitType.Sp)
                    )
                }
            }
        }
    }
}
