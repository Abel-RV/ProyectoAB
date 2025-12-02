package com.codelab.proyectoab.ui.screens

// En ui.screens (por ejemplo, en JugadorCard.kt o en un archivo nuevo)
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
@Immutable // Opcional, pero buena práctica si los datos no cambian de forma compleja
data class JugadorUI(
    val jugador: Jugador,
    var isExpanded: MutableState<Boolean> = mutableStateOf(false)
)