package com.br.b2b.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
@Immutable
data class Categories(
    val label: String,
    val icon: ImageVector,
)
