package com.example.examen3

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.examen3.databinding.ActivityProfileBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.profileMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = intent.getStringExtra("USERNAME")
        if (username != null) {
            gestionarConexion(username)
        }
    }

    private fun gestionarConexion(username: String) {
        val database = MyApplication.getDatabase(this)
        val usuarioDao = database.usuarioDao()

        lifecycleScope.launch {
            val usuario = usuarioDao.getUsuarioByNombre(username)
            if (usuario != null) {
                // 1. Mostrar la última conexión guardada (la anterior a esta)
                val ultimaConexion = usuario.lastConnection
                if (ultimaConexion != null) {
                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                    val fechaFormateada = sdf.format(Date(ultimaConexion))
                    binding.tvLastConnectionValue.text = fechaFormateada
                } else {
                    binding.tvLastConnectionValue.text = "Primera conexión"
                }

                // 2. Actualizar con la fecha y hora actual para la próxima vez
                val usuarioActualizado = usuario.copy(lastConnection = System.currentTimeMillis())
                usuarioDao.update(usuarioActualizado)
            }
        }
    }
}
