package com.br.b2b.data.dummy

import com.br.b2b.domain.model.Card
import com.br.b2b.domain.model.CardType
import java.util.Date

object CardsData {
    val cards = listOf(
        Card(
            cardNumber = "1555 2345 5346 5124",
            holderName = "Gian Felipe da Silva",
            cardType = CardType.Maestro,
            cvv = "***",
            expiryDate = Date()
        ),
        Card(
            cardNumber = "1555 2345 5346 5124",
            holderName = "Gian Felipe da Silva",
            cardType = CardType.Maestro,
            cvv = "***",
            expiryDate = Date()
        ),
        Card(
            cardNumber = "1555 2345 5346 5124",
            holderName = "Gian Felipe da Silva",
            cardType = CardType.Maestro,
            cvv = "***",
            expiryDate = Date()
        ),
        Card(
            cardNumber = "1555 2345 5346 5124",
            holderName = "Gian Felipe da Silva",
            cardType = CardType.Maestro,
            cvv = "***",
            expiryDate = Date()
        ),
        Card(
            cardNumber = "1555 2345 5346 5124",
            holderName = "Gian Felipe da Silva",
            cardType = CardType.Mastercard,
            cvv = "***",
            expiryDate = Date()
        ),
        Card(
            cardNumber = "1555 2345 5346 5124",
            holderName = "Gian Felipe da Silva",
            cardType = CardType.AmericanExpress,
            cvv = "***",
            expiryDate = Date()
        ),
        Card(
            cardNumber = "1555 2345 5346 5124",
            holderName = "Gian Felipe da Silva",
            cardType = CardType.DinersClub,
            cvv = "***",
            expiryDate = Date()
        ),
        Card(
            cardNumber = "1555 2345 5346 5124",
            holderName = "Gian Felipe da Silva",
            cardType = CardType.RuPay,
            cvv = "***",
            expiryDate = Date()
        ),
        Card(
            cardNumber = "1555 2345 5346 5124",
            holderName = "Gian Felipe da Silva",
            cardType = CardType.Visa,
            cvv = "***",
            expiryDate = Date()
        ),
    )
}