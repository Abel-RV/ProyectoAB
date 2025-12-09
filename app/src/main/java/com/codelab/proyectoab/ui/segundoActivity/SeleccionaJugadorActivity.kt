package com.codelab.proyectoab.ui.segundoActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.codelab.proyectoab.MainActivity
import com.codelab.proyectoab.RepositorioJugadores
import com.codelab.proyectoab.ui.screens.SeleccionaJugadorScreen
import com.codelab.proyectoab.ui.segundoActivity.ui.theme.ProyectoABTheme

class SeleccionaJugadorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoABTheme {
                SeleccionaJugadorScreen(
                    jugadores = RepositorioJugadores.getJugadores()
                ) { jugadorSeleccionado ->
                    val intent = Intent().apply {
                        putExtra(MainActivity.EXTRA_JUGADOR, jugadorSeleccionado.id)
                    }
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }
}