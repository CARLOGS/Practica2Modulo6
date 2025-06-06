package com.dgtic.practica2modulo6.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dgtic.practica2modulo6.R
import com.dgtic.practica2modulo6.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    // Propiedades
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Bloquea orientación en modo retrato
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Inicia FirebasAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Le permite ingresar si ya estaba logueado
        if ( firebaseAuth.currentUser != null ) {
            actionLoginSeccessful()
        }

        binding.btnLogin.setOnClickListener {
            if (validateFields()) {
                binding.progressBar.visibility = View.VISIBLE
                authenticateUser(email, password)
            } else {
                message(getString(R.string.invalid_data))
                return@setOnClickListener
            }
        }

        binding.btnRegistrarse.setOnClickListener {
            if (validateFields()) {
                binding.progressBar.visibility = View.VISIBLE
                createUser(email, password)
            } else {
                message(getString(R.string.invalid_data))
                return@setOnClickListener
            }
        }

        binding.tvRestablecerPassword.setOnClickListener {
            resetPassword()
        }
    }

    /**
     * Valida que los campos contengan datos correctos
     */
    private fun validateFields(): Boolean{
        email = binding.tietEmail.text.toString().trim()  // Elimina los espacios en blanco
        password = binding.tietContrasena.text.toString().trim()

        //Verifica que el campo de correo no esté vacío
        if (email.isEmpty()) {
            binding.tietEmail.error = getString(R.string.email_required)
            binding.tietEmail.requestFocus()
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tietEmail.error = getString(R.string.email_invalid)
            binding.tietEmail.requestFocus()
            return false
        }

        //Verifica que el campo de la contraseña no esté vacía y tenga al menos 6 caracteres
        if (password.isEmpty()) {
            binding.tietContrasena.error = getString(R.string.empty_email)
            binding.tietContrasena.requestFocus()
            return false
        } else if (password.length < 6) {
            binding.tietContrasena.error = getString(R.string.pass_invalid_size)
            binding.tietContrasena.requestFocus()
            return false
        }
        return true
    }

    private fun handleErrors(task: Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            e.printStackTrace()
        }

        when( errorCode ){
            "ERROR_INVALID_EMAIL" -> {
                message(getString(R.string.invalid_email))
                binding.tietEmail.error = getString(R.string.invalid_email)
                binding.tietEmail.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                message(getString(R.string.invalid_pass))
                binding.tietContrasena.error = getString(R.string.invalid_pass)
                binding.tietContrasena.requestFocus()
                binding.tietContrasena.setText("")

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                message(getString(R.string.already_exist_account))
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                message(getString(R.string.email_used))
                binding.tietEmail.error = getString(R.string.email_used)
                binding.tietEmail.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                message(getString(R.string.expired_token))
            }
            "ERROR_USER_NOT_FOUND" -> {
                message(getString(R.string.user_not_found))
            }
            "ERROR_WEAK_PASSWORD" -> {
                message(getString(R.string.weak_pass))
                binding.tietContrasena.error = getString(R.string.pass_invalid_size)
                binding.tietContrasena.requestFocus()
            }
            "NO_NETWORK" -> {
                message(getString(R.string.not_network))
            }
            else -> {
                message(getString(R.string.not_authentication))
            }
        }
    }

    private fun actionLoginSeccessful() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    private fun authenticateUser(usr: String, psw: String) {
        firebaseAuth.signInWithEmailAndPassword(usr, psw).addOnCompleteListener { task ->
            if ( task.isSuccessful ) {
                message(getString(R.string.welcome, usr))
                actionLoginSeccessful()
            } else {
                // Oculta el progress
                binding.progressBar.visibility = View.GONE
                handleErrors(task)
            }
        }
    }

    private fun createUser(usr: String, psw: String) {
        firebaseAuth.createUserWithEmailAndPassword(usr, psw).addOnCompleteListener { task ->
            if ( task.isSuccessful ) {
                // Correo de veririficación
                firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                    message(getString(R.string.verification_sent))
                }?.addOnFailureListener {
                    message(getString(R.string.verification_not_sent))
                }

                message(getString(R.string.welcome, usr))
                actionLoginSeccessful()
            } else {
                // Oculta el progress
                binding.progressBar.visibility = View.GONE
                handleErrors(task)
            }
        }
    }

    private fun resetPassword() {
        // Texto manual por programa
        val resetMail = EditText(this)
        resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.reset_pass))
            .setMessage(getString(R.string.email_input))
            .setView(resetMail)
            .setPositiveButton(getString(R.string.send)) { _, _ ->
                val mail = resetMail.text.toString()
                if (mail.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener {
                        message(getString(R.string.email_pass_sent))
                    }.addOnFailureListener {
                        message(getString(R.string.email_pass_not_sent))
                    }
                } else {
                    message(getString(R.string.email_invalid))
                }
            }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create().show()
    }
}