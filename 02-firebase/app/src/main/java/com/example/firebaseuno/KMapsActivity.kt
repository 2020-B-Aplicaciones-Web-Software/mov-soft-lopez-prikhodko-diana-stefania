package com.example.firebaseuno

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions

class KMapsActivity : AppCompatActivity() {

    private lateinit var mapa: GoogleMap

    var permisos = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kmaps)

        solicitarPermisos()

        val botonCarolina = findViewById<Button>(R.id.btn_ir_carolina)
        botonCarolina.setOnClickListener{
            val carolina = LatLng(-0.18288452555103193, -78.48449971346241)
            val zoom = 17f
            moverCamaraConZoom(carolina,zoom)
        }

        val fragmentoMapa = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        fragmentoMapa.getMapAsync{ googleMap ->
            if(googleMap != null){
                mapa = googleMap
                establecerConfiguracionMapa()
                val quicentro = LatLng(-0.295268, -78.542193)
                val titulo = "Quicentro"
                val zoom = 17f
                anadirMarcador(quicentro,titulo)
                moverCamaraConZoom(quicentro,zoom)

                //Línea

                val poliLineaUno = googleMap
                    .addPolyline(
                        PolylineOptions()
                            .add(
                                LatLng(-0.175327,-78.488211),
                                LatLng(-0.17158712906364446,-78.48483202848371),
                                LatLng(-0.17680657057716478,-78.47801941206454)
                            )
                    )
                poliLineaUno.tag = "Linea 1"

                //Polígono

                val poligonoUno = googleMap
                    .addPolygon(
                        PolygonOptions()
                            .add(
                                LatLng(-0.1771546902239471,-78.48344981495214),
                                LatLng(-0.1796981486125768,-78.48269198043828),
                                LatLng(-0.1771895812414777,-78.48142892291516)
                            )
                    )
                poligonoUno.fillColor = -0xc771c4
                poligonoUno.tag = "poligono 2"
                escucharListeners()
            }
        }
    }

    fun escucharListeners(){
        mapa.setOnPolygonClickListener {
            Log.i("mapa","setOnPolygonClickListener ${it}")
        }
        mapa.setOnPolylineClickListener {
            Log.i("mapa","setOnPolylineClickListener ${it}")
        }
        mapa.setOnMarkerClickListener {
            Log.i("mapa","setOnMarkerClickListener ${it}")
            return@setOnMarkerClickListener true
        }
        mapa.setOnCameraMoveListener {
            Log.i("mapa","setOnCameraMoveListener")
        }
        mapa.setOnCameraMoveStartedListener {
            Log.i("mapa","setOnCameraMoveStartedListener")
        }
        mapa.setOnCameraIdleListener {
            Log.i("mapa","setOnCameraIdleListener")
        }
    }

    fun anadirMarcador(latLng: LatLng, title: String){
        mapa.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
        )
    }

    fun moverCamaraConZoom(latLng: LatLng, zoom: Float = 10f){
        mapa.moveCamera(
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

        with(mapa){
            val permisosFineLocation = ContextCompat
                .checkSelfPermission(
                    contexto,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            val tienePermiso = permisosFineLocation == PackageManager.PERMISSION_GRANTED
            if(tienePermiso){
                mapa.isMyLocationEnabled = true //no tenemos aun permisos
            }
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = true // no tenemos aun permisos
        }

    }

}