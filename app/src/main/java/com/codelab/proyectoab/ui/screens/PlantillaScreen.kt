package com.codelab.proyectoab.ui.screens

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController

@Composable
fun PlantillaScreen(
    navController: NavController,
    prefs: SharedPreferences
) {
    val viewModel: PlantillaViewModel = viewModel(factory = object : ViewModelProvider.Factory{
        override fun <T: ViewModel> create(modelClass: Class<T>):T{
            return PlantillaViewModel(prefs) as T
        }
    })
    Box(Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(viewModel.jugadoresUI) { jugadorUI ->
                JugadorCard(
                    jugador = jugadorUI.jugador,
                    isExpanded = jugadorUI.isExpanded.value,
                    onExpandedChange = { viewModel.cambiaExpansion(jugadorUI.jugador.id) },
                    navController = navController
                )
            }
        }
    }
}
