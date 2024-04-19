package com.br.b2b.data.dummy

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChildFriendly
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.NoteAlt
import com.br.b2b.domain.model.Categories
import javax.annotation.concurrent.Immutable

@Immutable
object CategoriesDummyData {
    val categories = listOf(
        Categories(
            label = "HomeCare",
            icon = Icons.Filled.Home
        ),
        Categories(
            label = "Automotivo",
            icon = Icons.Filled.DirectionsCar
        ),
        Categories(
            label = "Saúde",
            icon = Icons.Filled.MonitorHeart
        ),
        Categories(
            label = "Crianças",
            icon = Icons.Filled.ChildFriendly
        ),
        Categories(
            label = "Papelaria",
            icon = Icons.Filled.NoteAlt
        ),
        Categories(
            label = "Teste 1",
            icon = Icons.Filled.Home
        ),
        Categories(
            label = "Teste 2",
            icon = Icons.Filled.DirectionsCar
        ),
        Categories(
            label = "Teste 3",
            icon = Icons.Filled.MonitorHeart
        ),
        Categories(
            label = "Teste 4",
            icon = Icons.Filled.ChildFriendly
        ),
        Categories(
            label = "Teste 5",
            icon = Icons.Filled.NoteAlt
        ),
    )
}