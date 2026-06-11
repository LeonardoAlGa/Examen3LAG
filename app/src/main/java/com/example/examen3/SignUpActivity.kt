package com.example.examen3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.examen3.databinding.ActivitySignUpBinding
import com.example.examen3.model.Usuario
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.signUpMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnDoRegister.setOnClickListener {
            val username = binding.etSignUpUsername.text.toString().trim()
            val password = binding.etSignUpPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                registrarUsuario(username, password)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarUsuario(nombre: String, contrasena: String) {
        val database = MyApplication.getDatabase(this)
        val usuarioDao = database.usuarioDao()

        lifecycleScope.launch {
            try {
                val usuarioExistente = usuarioDao.getUsuarioByNombre(nombre)
                if (usuarioExistente == null) {
                    val nuevoUsuario = Usuario(nombre = nombre, password = contrasena)
                    usuarioDao.insert(nuevoUsuario)
                    Toast.makeText(this@SignUpActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    finish() // Regresa a la pantalla de login
                } else {
                    Toast.makeText(this@SignUpActivity, "El nombre de usuario ya existe", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SignUpActivity, "Error al registrar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
