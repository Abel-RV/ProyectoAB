package com.codelab.proyectoab

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.codelab.proyectoab.ui.screens.Jugador
import com.codelab.proyectoab.ui.theme.ProyectoABTheme
import java.util.jar.Manifest

class MainActivity : ComponentActivity() {
    companion object {
        const val CLAVE_NOMBRE_USUARIO = "nombre_usuario"
        const val CLAVE_TEMA_OSCURO = "tema_oscuro"
        const val CLAVE_JUGADORES_EXPANDIDOS = "jugadores_expandidos"
        const val EXTRA_JUGADOR = "jugador_seleccionado"
    }

    val seleccionJugadorLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Recuperar el ID del jugador en lugar del objeto completo
            val jugadorId = result.data?.getIntExtra(EXTRA_JUGADOR, -1) ?: -1
            if (jugadorId != -1) {
                val jugador = RepositorioJugadores.getJugadorPorId(jugadorId)
                jugador?.let {
                    Toast.makeText(this, "Seleccionado: ${it.nombre}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private val requestContactPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        when{
            isGranted->{
                Toast.makeText(this,"Acceso a contactos concedido", Toast.LENGTH_SHORT).show()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)->{
                Toast.makeText(this,"El permiso es necesario para esta funcion", Toast.LENGTH_LONG).show()
            }
            else->{
                Toast.makeText(this,"El permiso es necesario para esta funcion", Toast.LENGTH_SHORT).show()
                val indent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data= Uri.fromParts("package",packageName,null)
                }
                startActivity(indent)
            }
        }
    }
    private fun tienePermisoContactos():Boolean{
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_CONTACTS
        )== PackageManager.PERMISSION_GRANTED
    }
    fun solicitarPermisoContactos(){
        requestContactPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
    }

    fun usuarioTienePermisoContactos():Boolean{
        return tienePermisoContactos()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SharedPreferences
        val prefs = getSharedPreferences("ajustes_usuario", Context.MODE_PRIVATE)
        val temaOscuro = prefs.getBoolean(CLAVE_TEMA_OSCURO, false)

        // Aplicar modo de noche antes de dibujar UI
        AppCompatDelegate.setDefaultNightMode(
            if (temaOscuro) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        enableEdgeToEdge()
        setContent {
            ProyectoABTheme(darkTheme = temaOscuro) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pasamos prefs a la UI
                    MainScreen(prefs = prefs)
                }
            }
        }
    }

}