package com.codelab.proyectoab.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PlantillaScreen(
    navController: NavController,
    viewModel: PlantillaViewModel = viewModel()
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
