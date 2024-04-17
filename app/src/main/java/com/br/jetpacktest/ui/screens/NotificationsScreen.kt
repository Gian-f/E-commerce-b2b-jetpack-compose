package com.br.jetpacktest.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastDistinctBy
import androidx.compose.ui.util.fastForEach
import androidx.navigation.NavHostController
import com.br.jetpacktest.R
import com.br.jetpacktest.data.dummy.NotificationsData
import com.br.jetpacktest.ui.components.ConfirmDialog
import com.br.jetpacktest.domain.routes.Screen
import com.br.jetpacktest.util.formatDateAgo
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(navController: NavHostController) {
    val formattedDateAgo = formatDateAgo(Date())
    val openDialog = remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(NotificationsData.items) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(Screen.Notifications.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Products.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_ios),
                            contentDescription = "Go back"
                        )
                    }
                },
                actions = {
                    if (notifications.isNotEmpty()) {
                        notifications.fastDistinctBy { }
                        IconButton(
                            onClick = {
                                openDialog.value = true
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        )
                    }
                }
            )
            ConfirmDialog(
                onConfirm = {
                    notifications = emptyList()
                },
                dialogState = openDialog,
                message = "Você deseja excluir todas as notificações?"
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(count = notifications.size) { index ->

                    Column(modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp)) {
                        Text(
                            text = "Oferta imperdível $index",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Olha só o que você está perdendo..."
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = formattedDateAgo,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, end = 4.dp),
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    )
}