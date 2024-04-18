package com.br.b2b.domain.model

import androidx.annotation.DrawableRes
import com.br.jetpacktest.R

enum class CardType(
    @DrawableRes val image: Int,
) {
    None(R.drawable.visa),
    Visa(R.drawable.visa),
    Mastercard(R.drawable.mastercard),
    RuPay(R.drawable.rupay_logo),
    AmericanExpress(R.drawable.amex_logo),
    Maestro(R.drawable.maestro),
    DinersClub(R.drawable.diners_club)
}