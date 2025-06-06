package com.dgtic.practica2modulo6.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dgtic.practica2modulo6.R
import com.dgtic.practica2modulo6.databinding.ActivityMainBinding
import com.dgtic.practica2modulo6.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding
    private var name: String = ""
    private var developer: String = ""
    private var lat: Double = 0.0
    private var long: Double = 0.0

    // GoogleMaps
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Recupera parámetros
        name = intent.getStringExtra("language") ?: ""
        developer = intent.getStringExtra("developer") ?: ""
        lat = intent.getDoubleExtra("lat", 0.0)
        long = intent.getDoubleExtra("long", 0.0)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicia GoogleMaps
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Localiza un punto y hace zoom en el mapa
        createMarker()
    }

    private fun createMarker() {
        val coordinates = LatLng(lat, long)
        val  marker = MarkerOptions()
            .position(coordinates)
            .title(developer)
            .snippet(name)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.gmapin))

        googleMap.addMarker(marker)
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4_000,
            null
        )
    }
}