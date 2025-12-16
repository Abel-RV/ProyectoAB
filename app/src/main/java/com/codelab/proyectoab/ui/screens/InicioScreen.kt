package com.codelab.proyectoab.ui.screens

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codelab.proyectoab.MainActivity
import com.codelab.proyectoab.R
import com.codelab.proyectoab.ui.resources.botonContactos
import com.codelab.proyectoab.ui.resources.ingresarNombre
import com.codelab.proyectoab.ui.segundoActivity.SeleccionaJugadorActivity

@Composable
fun InicioScreen(
    navController: NavController,
    prefs: SharedPreferences
) {
    val temaOscuro = prefs.getBoolean(MainActivity.CLAVE_TEMA_OSCURO, false)
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ingresarNombre(prefs, soloSaludo = true)
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

        Switch(
            checked = temaOscuro,
            onCheckedChange = { isChecked ->
                prefs.edit().putBoolean(MainActivity.CLAVE_TEMA_OSCURO, isChecked).apply()
                (context as MainActivity).recreate()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                prefs.edit().clear().apply()
                (navController.context as MainActivity).recreate()
            },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Borrar datos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+34642636747"))
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "No hay app de teléfono", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "Llamar a Jarún",
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val uri = Uri.parse("geo:38.9986,-1.8672?q=Estadio+Carlos+Belmonte")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    // Fallback a navegador
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://maps.google.com/?q=38.9986,-1.8672")
                        )
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "Ver estadio en el mapa",
                textAlign = TextAlign.Center
            )
        }
    }
}