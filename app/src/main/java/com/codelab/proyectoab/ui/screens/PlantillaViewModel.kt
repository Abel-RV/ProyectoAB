package com.codelab.proyectoab.ui.screens
// PlantillaViewModel.kt

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.codelab.proyectoab.MainActivity
import com.codelab.proyectoab.RepositorioJugadores


// Clase envoltorio para el estado de la tarjeta
// Puede estar aquí o en otro archivo, como en la Solución 1
// @Immutable // Opcional

class PlantillaViewModel(private val prefs: SharedPreferences) : ViewModel() {
    /*private val _jugadoresUI = RepositorioJugadores.getJugadores()
        .map { jugador -> JugadorUI(jugador = jugador) }
        .toMutableStateList()

    val jugadoresUI: List<JugadorUI>
        get() = _jugadoresUI*/

    fun cambiaExpansion(jugadorId: Int) {
        val jugadorUI = _jugadoresUI.find { it.jugador.id == jugadorId }
        jugadorUI?.let {
            it.isExpanded.value = !it.isExpanded.value
            guardarEstadoExpandido()
        }
    }
    private fun guardarEstadoExpandido() {
        val expandidos = _jugadoresUI
            .filter { it.isExpanded.value }
            .map { it.jugador.id.toString() }
            .toSet()
        prefs.edit()
            .putStringSet(MainActivity.CLAVE_JUGADORES_EXPANDIDOS, expandidos)
            .apply()
    }

    private val _jugadoresUI = mutableListOf<JugadorUI>()
    val jugadoresUI: List<JugadorUI> = _jugadoresUI
    init {
        val jugadores = RepositorioJugadores.getJugadores()
        val expandidos = prefs.getStringSet(
            MainActivity.CLAVE_JUGADORES_EXPANDIDOS,
            emptySet()
        ) ?: emptySet()

        _jugadoresUI.addAll(
            jugadores.map { jugador->
                JugadorUI(
                    jugador=jugador,
                    isExpanded = mutableStateOf(jugador.id.toString() in expandidos)
                )
            }
        )
    }
}