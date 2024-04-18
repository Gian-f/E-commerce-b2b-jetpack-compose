package com.br.b2b.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.br.b2b.data.dummy.CardsData.cards
import com.br.b2b.ui.components.CreditCard
import com.br.b2b.domain.routes.Screen
import com.br.b2b.ui.viewmodel.CardViewModel
import com.br.b2b.util.CardNumberFilter
import com.br.b2b.util.formattedDate
import com.br.jetpacktest.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(Screen.MyCards.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Products.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_ios),
                            contentDescription = "Go back"
                        )
                    }
                },
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth(),
                    Arrangement.Center,
                    CenterVertically
                ) {
                    Text(text = "Gerenciamento de cartões")
                    Spacer(modifier = Modifier.width(40.dp))
                    Text(text = "Adicionar", color = MaterialTheme.colorScheme.secondary)

                }
                Spacer(modifier = Modifier.height(24.dp))
                LazyRow(
                    contentPadding = PaddingValues(end = 64.dp)
                ) {
                    items(cards) {
                        androidx.compose.material3.Card(
                            modifier = Modifier
                                .width(300.dp)
                                .height(200.dp)
                                .padding(12.dp),
                            elevation = CardDefaults.cardElevation(3.dp),
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = CenterVertically
                                ) {
                                    Text(text = it.cardType.name)
                                }
                                Text(text = it.cardNumber)
                                Text(text = it.holderName)
                                Text(text = formattedDate(it.expiryDate))
                            }
                        }
                    }
                }
                ExtendedFloatingActionButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_card_24),
                        contentDescription = "fab"
                    )
                }
            }
        }
    )
}


@Composable
fun CardDetails() {

    val viewModel = remember { CardViewModel() }

    Column(modifier = Modifier.fillMaxSize()) {

        CreditCard(
            cardNumber = viewModel.cardNumber,
            holderName = viewModel.cardHolderName,
            expiryDate = viewModel.expiryDate,
            cardCVV = viewModel.cardCVV
        )

        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            item {
                OutlinedTextField(
                    value = viewModel.cardNumber.text,
                    onValueChange = { viewModel.cardNumber = TextFieldValue(it) },
                    label = { Text("Seu número de cartão") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    visualTransformation = CardNumberFilter
                )
            }

            item {
                OutlinedTextField(
                    value = viewModel.cardHolderName.text,
                    onValueChange = { viewModel.cardHolderName = TextFieldValue(it) },
                    label = { Text("Seu nome no cartão") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    verticalAlignment = CenterVertically
                ) {
                    OutlinedTextField(
                        value = viewModel.expiryDate.text,
                        onValueChange = { viewModel.expiryDate = TextFieldValue(it) },
                        label = { Text("Validade") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    OutlinedTextField(
                        value = viewModel.cardCVV.text,
                        onValueChange = { viewModel.cardCVV = TextFieldValue(it) },
                        label = { Text("CVV") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    )
                }
            }

            item {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        text = "Salvar",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 30.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true, backgroundColor = 0xFFFFFEFE,
    device = "id:pixel_6_pro"
)
@Composable
fun Card() {
    CardDetails()
}
