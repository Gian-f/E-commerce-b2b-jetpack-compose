package com.br.b2b.data.dummy


import com.br.b2b.domain.model.Banner
import com.br.jetpacktest.R
import javax.annotation.concurrent.Immutable

@Immutable
object BannersDummyData {
    val banners = listOf(
        Banner(
            image = R.drawable.banner_1,
        ),
        Banner(
            image = R.drawable.banner_2,
        ),
        Banner(
            image = R.drawable.banner_3,
        ),
        Banner(
            image = R.drawable.banner_1,
        ),
        Banner(
            image = R.drawable.banner_2,
        ),
    )
}