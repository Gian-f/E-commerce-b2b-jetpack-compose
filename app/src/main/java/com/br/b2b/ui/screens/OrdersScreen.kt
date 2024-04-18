package com.br.b2b.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.CardDefaults.elevatedShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.br.b2b.data.dummy.OrderData
import com.br.b2b.ui.components.ElevatedFilterChip
import com.br.b2b.domain.routes.Screen
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController) {
    val items = OrderData.items
    val chipStateInProgress = remember { mutableStateOf(true) }
    val chipStateDelivered = remember { mutableStateOf(false) }
    val chipStateCancelled = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        Screen.Orders.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Products.route) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIos,
                            contentDescription = "Comeback"
                        )
                    }
                },
                actions = {

                }
            )
        },
        content = { innerPadding ->
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.width(4.dp))
                    ElevatedFilterChip(
                        label = "Pendentes",
                        selected = chipStateInProgress,
                        onSelected = {
                            chipStateInProgress.value = true
                            chipStateDelivered.value = false
                            chipStateCancelled.value = false
                        }
                    )
                    ElevatedFilterChip(
                        label = "Entregues",
                        selected = chipStateDelivered,
                        onSelected = {
                            chipStateInProgress.value = false
                            chipStateDelivered.value = true
                            chipStateCancelled.value = false
                        }
                    )
                    ElevatedFilterChip(
                        label = "Cancelados",
                        selected = chipStateCancelled,
                        onSelected = {
                            chipStateInProgress.value = false
                            chipStateDelivered.value = false
                            chipStateCancelled.value = true
                        }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                val filteredItems = items.filter { item ->
                    (chipStateInProgress.value && item.status == "Pendente") ||
                            (chipStateDelivered.value && item.status == "Entregue") ||
                            (chipStateCancelled.value && item.status == "Cancelado")
                }
                LazyColumn {
                    items(filteredItems) {
                        ElevatedCard(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth()
                                .height(170.dp),
                            shape = elevatedShape
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            ) {
                                Text(
                                    text = "Pedido #" + it.orderId.toString(),
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.padding(start = 12.dp)
                                )

                                Text(
                                    text = it.requestedAt,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            ) {
                                Text(
                                    text = "Número de rastreio: ",
                                    fontFamily = FontFamily.SansSerif,
                                    modifier = Modifier.padding(start = 12.dp)
                                )

                                Text(
                                    text = it.trackingNumber,
                                    color = Color.Black,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            ) {
                                Text(
                                    text = "Subtotal: ",
                                    fontFamily = FontFamily.SansSerif,
                                    modifier = Modifier.padding(start = 12.dp)
                                )

                                Text(
                                    text = it.total,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp, bottom = 8.dp)
                            ) {
                                Text(
                                    text = it.status.uppercase(Locale.ROOT),
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = TextUnit(14F, TextUnitType.Sp),
                                    modifier = Modifier.padding(start = 12.dp, top = 8.dp),
                                    color =
                                    if (it.status.contains("Pendente")) Color(0xFFCF6212)
                                    else if (it.status.contains("Cancelado")) Color(0xFFC50000)
                                    else Color(0xFF009254)
                                )

                                OutlinedButton(
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier
                                        .width(140.dp)
                                        .height(80.dp)
                                        .padding(end = 12.dp),
                                ) {
                                    Text(
                                        text = "Detalhes",
                                        color = Color.Black,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}