package com.example.gasolineraapplication.negocio

import android.content.Context
import com.example.gasolineraapplication.datos.VisualizarResultadoDato

class VisualizarResultadoNegocio(context: Context) {

    private val resultadoDato = VisualizarResultadoDato(context)

    fun obtenerUltimoResultadoCalculado(idSucursalCombustible: Int): Triple<Int, Double, Boolean>? {
        val cursor = resultadoDato.obtenerUltimoResultado(idSucursalCombustible)
        if (cursor.moveToFirst()) {
            val tiempo = cursor.getInt(0)
            val litros = cursor.getDouble(1)
            val alcanza = cursor.getInt(2) == 1
            cursor.close()
            return Triple(tiempo, litros, alcanza)
        }
        cursor.close()
        return null
    }
}
