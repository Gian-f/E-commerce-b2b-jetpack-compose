package com.br.jetpacktest.ui.screens

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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import com.br.jetpacktest.data.dummy.BannersDummyData.banners
import com.br.jetpacktest.data.dummy.CategoriesDummyData.categories
import com.br.jetpacktest.data.dummy.NavigationDrawerData
import com.br.jetpacktest.data.dummy.NotificationsData.items
import com.br.jetpacktest.data.dummy.ProductsData.products
import com.br.jetpacktest.ui.components.BottomSheetModalFilter
import com.br.jetpacktest.ui.components.CategoriesButton
import com.br.jetpacktest.ui.components.ConfirmDialog
import com.br.jetpacktest.ui.components.FilterButton
import com.br.jetpacktest.ui.components.HistoryItem
import com.br.jetpacktest.ui.components.PagerIndicator
import com.br.jetpacktest.ui.components.SegmentedButton
import com.br.jetpacktest.ui.routes.Screen
import com.br.jetpacktest.ui.viewmodel.ThemeViewModel
import com.br.jetpacktest.util.ComposeTheme
import com.br.jetpacktest.util.VoiceToTextParser
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
                            })
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
                            imageVector = Icons.Filled.Logout,
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
                    Text(
                        Screen.Products.title, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
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
                                if (cleanedSpokenText.isNotEmpty() && !items.contains(
                                        cleanedSpokenText
                                    )
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
                                        Icons.Filled.Hearing
                                    } else {
                                        Icons.Filled.KeyboardVoice
                                    }

                                    Icon(imageVector = icon,
                                        contentDescription = "Voice",
                                        modifier = Modifier
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
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        items(categories, key = { it.label }) { categories ->
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
                                })
                        }
                    }
                    HorizontalPager(
                        state = bannerPager,
                    ) { index ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .padding(start = 22.dp, end = 22.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { },
                            elevation = CardDefaults.cardElevation(3.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            val url = banners[index].image
                            val imagePainter = painterResource(url)

                            Image(
                                painter = imagePainter,
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                            )
                        }
                    }
                    PagerIndicator(bannerPager)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Produtos em destaque",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = TextUnit(16F, TextUnitType.Sp)
                        )
                        Text(
                            text = "Ver mais",
                            fontWeight = FontWeight.W400,
                            fontSize = TextUnit(13F, TextUnitType.Sp),
                            color = Color(0xFF9B9B9B)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        LazyRow(
                            contentPadding = PaddingValues(end = 64.dp),
                        ) {
                            items(products,
                                key = { item -> item.id }) { product ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .width(130.dp)
                                            .height(140.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { },
                                        elevation = CardDefaults.cardElevation(3.dp),
                                    ) {
                                        val imagePainter = painterResource(product.image)
                                        Image(
                                            painter = imagePainter,
                                            contentDescription = null,
                                            contentScale = ContentScale.FillBounds,
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = product.description,
                                        textAlign = TextAlign.Start,
                                        fontSize = TextUnit(13F, TextUnitType.Sp)
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = product.price,
                                        fontSize = TextUnit(11F, TextUnitType.Sp),
                                        textAlign = TextAlign.Start
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Produtos Recomendados",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = TextUnit(16F, TextUnitType.Sp)
                        )
                        Text(
                            text = "Ver mais",
                            fontWeight = FontWeight.W400,
                            fontSize = TextUnit(13F, TextUnitType.Sp),
                            color = Color(0xFF9B9B9B)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        LazyRow(
                            contentPadding = PaddingValues(end = 64.dp),
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            items(products, key = { item -> item.id }) { product ->
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .width(80.dp)
                                            .height(80.dp),
                                        elevation = CardDefaults.cardElevation(3.dp),
                                    ) {
                                        Image(
                                            painter = painterResource(id = product.image),
                                            contentScale = ContentScale.FillBounds,
                                            contentDescription = null
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxHeight()
                                    ) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = product.description,
                                            textAlign = TextAlign.Start,
                                            fontSize = TextUnit(13F, TextUnitType.Sp)
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = product.price,
                                            fontSize = TextUnit(11F, TextUnitType.Sp),
                                            textAlign = TextAlign.Start
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }
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
@OptIn(ExperimentalMaterial3Api::class)
private fun TopAppBarActions(navController: NavHostController) {
    BadgedBox(badge = {
        if (items.isNotEmpty()) {
            Badge()
        }
    }) {}
    IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
        Icon(
            imageVector = Icons.Default.NotificationsNone, contentDescription = "Notifications"
        )
    }
}