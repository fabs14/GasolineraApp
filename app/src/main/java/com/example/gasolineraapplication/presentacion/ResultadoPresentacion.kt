package com.example.gasolineraapplication.presentacion

import android.os.Bundle
import android.content.Intent
import android.widget.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import com.example.gasolineraapplication.R
import com.example.gasolineraapplication.negocio.ResultadoNegocio

class ResultadoPresentacion : BaseActivity(), OnMapReadyCallback {

    private lateinit var editLitros: EditText
    private lateinit var editMetros: EditText
    private lateinit var btnCalcular: Button
    private lateinit var btnDibujar: Button
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var resultadoNegocio: ResultadoNegocio
    private lateinit var googleMap: GoogleMap

    private var idSucursal: Int = -1
    private var idSucursalCombustible: Int = -1
    private var cantidadBombas: Int = -1
    private var puntosDibujo = mutableListOf<LatLng>()
    private var polyline: Polyline? = null
    private var modoDibujo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        // Inicializar vistas
        editLitros = findViewById(R.id.editLitros)
        editMetros = findViewById(R.id.editMetros)
        btnCalcular = findViewById(R.id.btnCalcular)
        btnDibujar = findViewById(R.id.btnDibujar)
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment

        resultadoNegocio = ResultadoNegocio(this)
        idSucursal = intent.getIntExtra("idSucursal", -1)
        idSucursalCombustible = intent.getIntExtra("idSucursalCombustible", -1)
        cantidadBombas = intent.getIntExtra("cantidadBombas", -1)

        editMetros.isFocusable = false
        editMetros.isClickable = false

        mapFragment.getMapAsync(this)

        // Asociar listeners a funciones
        btnDibujar.setOnClickListener { onBtnDibujarClick() }
        btnCalcular.setOnClickListener { onBtnCalcularClick() }
    }

    private fun onBtnDibujarClick() {
        modoDibujo = !modoDibujo
        btnDibujar.text = if (modoDibujo) "Confirmar dibujo" else "Dibujar fila en el mapa"
        if (!modoDibujo) {
            calcularDistanciaYMostrar()
        } else {
            Toast.makeText(this, "Toca puntos en el mapa", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onBtnCalcularClick() {
        val litros = editLitros.text.toString().toDoubleOrNull()
        val metros = editMetros.text.toString().toDoubleOrNull()

        if (litros == null || metros == null || idSucursal == -1 || idSucursalCombustible == -1) {
            Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        val exito = resultadoNegocio.calcularYGuardarResultado(
           // idSucursal,
            idSucursalCombustible,
            litros,
            metros,
            cantidadBombas
        )

        if (exito) {
            Toast.makeText(this, "Resultado guardado correctamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, VisualizarResultadoPresentacion::class.java)
            intent.putExtra("idSucursalCombustible", idSucursalCombustible)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Ya existe un cálculo reciente con los mismos datos", Toast.LENGTH_LONG).show()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Centrar en sucursal
        resultadoNegocio.obtenerUbicacionSucursal(idSucursal)?.let {
            val sucursalLatLng = LatLng(it.first, it.second)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sucursalLatLng, 17f))
            googleMap.addMarker(MarkerOptions().position(sucursalLatLng).title("Sucursal"))
        }

        googleMap.setOnMapClickListener { punto ->
            if (!modoDibujo) return@setOnMapClickListener
            puntosDibujo.add(punto)
            actualizarLinea()
        }
    }

    private fun actualizarLinea() {
        polyline?.remove()
        polyline = googleMap.addPolyline(
            PolylineOptions()
                .addAll(puntosDibujo)
                .color(getColor(R.color.purple_500))
                .width(10f)
        )
    }

    private fun calcularDistanciaYMostrar() {
        if (puntosDibujo.size < 2) {
            Toast.makeText(this, "Dibuja al menos dos puntos", Toast.LENGTH_SHORT).show()
            return
        }
        val distancia = SphericalUtil.computeLength(puntosDibujo)
        editMetros.setText(distancia.toInt().toString())
        Toast.makeText(this, "Distancia: ${distancia.toInt()} m", Toast.LENGTH_SHORT).show()
    }
}
