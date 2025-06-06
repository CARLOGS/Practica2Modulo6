package com.dgtic.practica2modulo6.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dgtic.practica2modulo6.R
import com.dgtic.practica2modulo6.databinding.ActivitySplash2Binding

class Splash2Activity : AppCompatActivity() {
    private lateinit var binding : ActivitySplash2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen: SplashScreen? = installSplashScreen()
        // Mostrar splash screen antes de super.onCreate()
        //val splashScreen: SplashScreen? = SplashScreen.installSplashScreen()

        // Se puede mantener la splash screen para cargar datos
        splashScreen?.setKeepOnScreenCondition {
            // Thread.sleep(1000)

            // Aquí puedes poner una condición como un booleano cargandoDatos
            false // true para mantenerla, false para quitarla
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySplash2Binding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Matrix
        Glide.with(this)
            .load(R.drawable.matrix)
            .into(binding.imgMatrix)

        // Delay de 2 segundo
        Handler().postDelayed({
            // Lanza el MainActivity
            val intent = Intent(this@Splash2Activity, LoginActivity::class.java)
            startActivity(intent)

            // Destruye el SplashActivity
            finish()
        }, 2000)
    }
}