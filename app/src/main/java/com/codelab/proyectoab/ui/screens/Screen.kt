package com.example.navegacion

// Screen.kt
// Usamos una sealed class para definir los destinos de navegación
sealed class Screen(val route: String) {
    // Cada objeto representa una pantalla con su ruta específica
    object InicioScreen : Screen("Inicio_screen")
    object DetalleJugadorScreen : Screen("detail_screen/{id}") // Ruta con un argumento {id}
}