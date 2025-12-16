package com.codelab.proyectoab

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import androidx.compose.material3.Text
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codelab.proyectoab.ui.screens.AppRoot
import com.codelab.proyectoab.ui.screens.ConfigurationScreen
import com.codelab.proyectoab.ui.screens.DetalleJugadorScreen
import com.codelab.proyectoab.ui.screens.InicioScreen
import com.codelab.proyectoab.ui.screens.Jugador
import com.codelab.proyectoab.ui.screens.PlantillaScreen
import com.codelab.proyectoab.ui.theme.ProyectoABTheme

class MainActivity : ComponentActivity() {
    companion object {
        const val CLAVE_NOMBRE_USUARIO = "nombre_usuario"
        const val CLAVE_TEMA_OSCURO = "tema_oscuro"
        const val CLAVE_JUGADORES_EXPANDIDOS = "jugadores_expandidos"
        const val EXTRA_JUGADOR = "jugador_seleccionado"
        const val EXTRA_JUGADOR_ID ="jugador_id"
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
            Manifest.permission.READ_CONTACTS
        )== PackageManager.PERMISSION_GRANTED
    }
    private val notificacionPermissionLauncher= registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        isGranted->
        if(isGranted){
            Toast.makeText(this,"Notificaciones activadas",Toast.LENGTH_SHORT).show()
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                    Manifest.permission.POST_NOTIFICATIONS
            )){
                Toast.makeText(this,"Las notificaciones permiten avistart de eventos del equipo",
                    Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Activa las notificaciones en Ajustes", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package",packageName,null)
                }
                startActivity(intent)
            }
        }
    }
    private fun crearCanalNotificaciones(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val canal = NotificationChannel(
                "CANAL_JUGADORES",
                "Eventos del Albacete Balompié",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description="Notificaciones sobre goles, lesiones y logros de los jugadores"
            }
            val manager= getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }
    private var jugadorIdParaRestaurar: Int = -1
    fun solicitarPermisoContactos(){
        requestContactPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
    }

    fun usuarioTienePermisoContactos():Boolean{
        return tienePermisoContactos()
    }
    fun solicitarPermisosNotificaciones(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )!= PackageManager.PERMISSION_GRANTED
            ){
                notificacionPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    fun tienePermisoNotificaciones():Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Permiso implícito en versiones anteriores
        }
    }

    @SuppressLint("MissingPermission")
    fun mostrarNotificacionJugador(jugador: Jugador, titulo:String, mensaje:String){
        if(!tienePermisoNotificaciones()){
            Toast.makeText(this,"Permiso de notificaciones requeridas", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, MainActivity::class.java).apply {
            flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(MainActivity.EXTRA_JUGADOR_ID,jugador.id)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            jugador.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(this,"CANAL_JUGADORES")
            .setSmallIcon(R.drawable.albacete_balompie)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        NotificationManagerCompat.from(this).notify(jugador.id,builder.build())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        jugadorIdParaRestaurar = savedInstanceState?.getInt("jugador_id", -1) ?: -1
        if (jugadorIdParaRestaurar == -1) {
            jugadorIdParaRestaurar = intent.getIntExtra(EXTRA_JUGADOR_ID, -1)
        }

        super.onCreate(savedInstanceState)
        crearCanalNotificaciones()
        val jugadorIdDesdeNotificacion = intent?.getIntExtra(EXTRA_JUGADOR_ID, -1) ?: -1
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
            val navController = rememberNavController()
            ProyectoABTheme(darkTheme = temaOscuro) {
                /*Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //MainScreen(prefs = prefs,initialJugadorId=jugadorIdDesdeNotificacion)
                    NavHost(
                        navController = navController,
                        startDestination = "inicio"
                    ) {
                        composable("inicio") {
                            InicioScreen(navController = navController, prefs = prefs)
                        }
                        composable("plantilla") {
                            PlantillaScreen(navController = navController,prefs=prefs)
                        }
                        /*composable(
                            route = "detail_screen/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id") ?: 1
                            val jugador = RepositorioJugadores.getJugadorPorId(id)
                            if (jugador != null) {
                                DetalleJugadorScreen(jugador = jugador)
                            } else {
                                Text("Jugador no encontrado")
                            }
                        }*/
                        composable("configuracion") {
                            ConfigurationScreen(navController=navController,prefs=prefs)
                        }
                    }
                }*/
                AppRoot(prefs = prefs, initialJugadorId = jugadorIdParaRestaurar)
            }
        }
    }
}