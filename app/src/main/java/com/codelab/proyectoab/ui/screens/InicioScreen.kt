package com.codelab.proyectoab.ui.screens

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codelab.proyectoab.MainActivity
import com.codelab.proyectoab.R

@Composable
fun InicioScreen(
    navController: NavController,
    prefs: SharedPreferences
) {
    val temaOscuro = prefs.getBoolean(MainActivity.CLAVE_TEMA_OSCURO, false)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.height(120.dp).width(120.dp),
            painter = painterResource(R.drawable.albacete_balompie),
            contentDescription = "Imagen del Albacete Balompié"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.titulo_inicio),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.descripcion_inicio),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("plantilla") },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(text = stringResource(R.string.boton_ver_plantilla))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Modo claro
        Button(
            onClick = {
                prefs.edit().putBoolean(MainActivity.CLAVE_TEMA_OSCURO, false).apply()
                (navController.context as MainActivity).recreate()
            },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Modo Claro")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Modo oscuro
        Button(
            onClick = {
                prefs.edit().putBoolean(MainActivity.CLAVE_TEMA_OSCURO, true).apply()
                (navController.context as MainActivity).recreate()
            },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Modo Oscuro")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (temaOscuro) "Modo: OSCURO" else "Modo: CLARO",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
