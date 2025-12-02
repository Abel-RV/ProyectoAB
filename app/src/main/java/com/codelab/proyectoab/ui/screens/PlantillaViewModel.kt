package com.codelab.proyectoab.ui.screens
// PlantillaViewModel.kt

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.codelab.proyectoab.RepositorioJugadores


// Clase envoltorio para el estado de la tarjeta
// Puede estar aquí o en otro archivo, como en la Solución 1
// @Immutable // Opcional

class PlantillaViewModel : ViewModel() {
    private val _jugadoresUI = RepositorioJugadores.getJugadores()
        .map { jugador -> JugadorUI(jugador = jugador) }
        .toMutableStateList()

    val jugadoresUI: List<JugadorUI>
        get() = _jugadoresUI

    fun cambiaExpansion(jugadorId: Int) {
        val jugadorUI = _jugadoresUI.find { it.jugador.id == jugadorId }
        jugadorUI?.let {
            it.isExpanded.value = !(it.isExpanded.value)
        }
    }
}