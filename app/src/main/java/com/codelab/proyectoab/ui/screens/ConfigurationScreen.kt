package com.codelab.proyectoab.ui.screens

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codelab.proyectoab.ui.resources.botonContactos
import com.codelab.proyectoab.ui.resources.ingresarNombre

@Composable
fun ConfigurationScreen(navController: NavController, prefs: SharedPreferences){

    Column(
        modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ingresarNombre(prefs)

        Spacer(modifier = Modifier.padding(16.dp))
        botonContactos(navController)
    }
}