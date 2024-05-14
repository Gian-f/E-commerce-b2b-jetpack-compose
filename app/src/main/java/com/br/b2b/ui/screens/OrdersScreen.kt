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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.br.b2b.data.dummy.OrderData
import com.br.b2b.domain.model.ChipState
import com.br.b2b.domain.model.Order
import com.br.b2b.domain.routes.Screen
import com.br.b2b.ui.components.ElevatedFilterChip
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController) {
    val items = OrderData.items
    val chipStates = remember { mutableStateOf(ChipState()) }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                Screen.Orders.title, maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }, navigationIcon = {
            IconButton(onClick = { navController.navigate(Screen.Products.route) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Comeback"
                )
            }
        })
    }, content = { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            FilterChips(chipStates)
            val filteredItems = filterItemsByStatus(items, chipStates)
            OrderList(filteredItems)
        }
    })
}

@Composable
fun FilterChips(chipStates: MutableState<ChipState>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        ElevatedFilterChip(label = "Pendentes",
            selected = remember {
                mutableStateOf(chipStates.value.inProgress)
            },
            onSelected = {
                chipStates.value =
                    chipStates.value.copy(
                        inProgress = true,
                        delivered = false,
                        cancelled = false
                    )
            }
        )
        ElevatedFilterChip(label = "Entregues",
            selected = remember {
                mutableStateOf(chipStates.value.delivered)
            },
            onSelected = {
                chipStates.value =
                    chipStates.value.copy(
                        inProgress = false,
                        delivered = true,
                        cancelled = false
                    )
            }
        )
        ElevatedFilterChip(label = "Cancelados",
            selected = remember {
                mutableStateOf(chipStates.value.cancelled)
            },
            onSelected = {
                chipStates.value =
                    chipStates.value.copy(
                        inProgress = false,
                        delivered = false,
                        cancelled = true
                    )
            }
        )
        Spacer(modifier = Modifier.width(4.dp))
    }
}

fun filterItemsByStatus(items: List<Order>, chipStates: MutableState<ChipState>): List<Order> {
    return items.filter { item ->
        when {
            chipStates.value.inProgress -> item.status == "Pendente"
            chipStates.value.delivered -> item.status == "Entregue"
            chipStates.value.cancelled -> item.status == "Cancelado"
            else -> true
        }
    }
}

@Composable
fun OrderList(items: List<Order>) {
    LazyColumn {
        items(items) { item ->
            OrderCard(item)
        }
    }
}

@Composable
fun OrderCard(item: Order) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(170.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Pedido #${item.orderId}",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )

                Text(
                    text = item.requestedAt, modifier = Modifier.padding(end = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Número de rastreio: ", fontFamily = FontFamily.SansSerif
                )

                Text(
                    text = item.trackingNumber,
                    color = Color.Black,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Subtotal: ", fontFamily = FontFamily.SansSerif
                )

                Text(
                    text = item.total,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.status.uppercase(Locale.ROOT),
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    color = when {
                        item.status.contains("Pendente") -> Color(0xFFCF6212)
                        item.status.contains("Cancelado") -> Color(0xFFC50000)
                        else -> Color(0xFF009254)
                    }
                )

                OutlinedButton(
                    onClick = { /* TODO: Adicione a lógica de clique aqui */ },
                    modifier = Modifier
                        .padding(8.dp)
                        .width(140.dp)
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