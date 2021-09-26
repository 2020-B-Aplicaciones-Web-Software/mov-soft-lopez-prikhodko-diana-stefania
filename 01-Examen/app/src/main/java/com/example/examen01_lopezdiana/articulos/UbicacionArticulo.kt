package com.example.examen01_lopezdiana.articulos

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.examen01_lopezdiana.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.examen01_lopezdiana.databinding.ActivityUbicacionProductoBinding
import com.example.examen01_lopezdiana.entities.Articulo

class UbicacionArticulo : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityUbicacionProductoBinding
    var permisos = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUbicacionProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val fragmentoMapa = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync(this)

        // Obtener ubicación
        val articulo = intent.getParcelableExtra<Articulo>("articulo")

        fragmentoMapa.getMapAsync{ googleMap ->
            if(googleMap != null){
                mMap = googleMap
                establecerConfiguracionMapa()
                val quicentro = LatLng(articulo?.ubicacionLatArticulo!!, articulo?.ubicacionLngArticulo!!)
                val titulo = articulo?.nombreArticulo.toString()
                val zoom = 17f
                anadirMarcador(quicentro,titulo)
                moverCamaraConZoom(quicentro,zoom)
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


    fun anadirMarcador(latLng: LatLng, title: String){
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
        )
    }

    fun moverCamaraConZoom(latLng: LatLng, zoom: Float = 10f){
        mMap.moveCamera(
            CameraUpdateFactory
                .newLatLngZoom(latLng,zoom)
        )
    }

    fun solicitarPermisos(){
        val contexto = this.applicationContext

        val permisosFineLocation = ContextCompat
            .checkSelfPermission(
                contexto,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        val tienePermiso = permisosFineLocation == PackageManager.PERMISSION_GRANTED
        if(tienePermiso){
            permisos = true
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf( //Arreglo permisos
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1//Código de peticion de los permisos
            )
        }

    }

    fun establecerConfiguracionMapa(){

        val contexto = this.applicationContext
        // Verifica si el mapa no es nulo

        with(mMap){
            val permisosFineLocation = androidx.core.content.ContextCompat
                .checkSelfPermission(
                    contexto,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            val tienePermiso = permisosFineLocation == android.content.pm.PackageManager.PERMISSION_GRANTED
            if(tienePermiso){
                mMap.isMyLocationEnabled = true //no tenemos aun permisos
            }
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = true // no tenemos aun permisos
        }

    }
}