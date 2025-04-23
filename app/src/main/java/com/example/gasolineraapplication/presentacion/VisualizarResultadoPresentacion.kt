package com.example.gasolineraapplication.presentacion

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.gasolineraapplication.R
import com.example.gasolineraapplication.negocio.VisualizarResultadoNegocio

class VisualizarResultadoPresentacion : BaseActivity() {

    private lateinit var txtResultado: TextView
    private lateinit var negocio: VisualizarResultadoNegocio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_resultado)

        txtResultado = findViewById(R.id.txtResultado)
        negocio = VisualizarResultadoNegocio(this)

        val idSucursalCombustible = intent.getIntExtra("idSucursalCombustible", -1)

        if (idSucursalCombustible != -1) {
            val resultado = negocio.obtenerUltimoResultadoCalculado(idSucursalCombustible)

            if (resultado != null) {
                val (tiempo, litros, alcanza) = resultado
                txtResultado.text = """
                    ⏱ Tiempo estimado: $tiempo min
                    🛢️ Litros restantes: $litros L
                    ✅ ¿Alcanza el combustible?: ${if (alcanza) "Sí" else "No"}
                """.trimIndent()
            } else {
                Toast.makeText(this, "No hay resultados disponibles", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Error de datos", Toast.LENGTH_SHORT).show()
        }
    }
}
