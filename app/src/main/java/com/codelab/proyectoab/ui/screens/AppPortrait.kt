package com.codelab.proyectoab.ui.screens

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun AppPortrait(
    navController: NavController,
    prefs: SharedPreferences,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        bottomBar = {
            SootheBottomNavigation(
                navController = navController,
                onNavigate = { route ->
                    navController.navigate(route) {
                        // Evitar múltiples copias de la misma pantalla
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        content(Modifier.padding(paddingValues))
    }
}

@Composable
private fun SootheBottomNavigation(
    navController: NavController,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Column {
        NavigationBar(
            contentColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = modifier
        ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Inicio"
                    )
                },
                label = {
                    Text("Inicio")
                },
                selected = currentRoute == "inicio",
                onClick = { onNavigate("inicio") }
            )

            // Item 2: Plantilla
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = "Plantilla"
                    )
                },
                label = {
                    Text("Plantilla")
                },
                selected = currentRoute == "plantilla",
                onClick = { onNavigate("plantilla") }
            )

            // Item 3: Configuración (opcional)
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Configuración"
                    )
                },
                label = {
                    Text("Ajustes")
                },
                selected = currentRoute == "configuracion",
                onClick = { onNavigate("configuracion") }
            )
        }
    }

}