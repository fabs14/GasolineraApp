package com.example.gasolineraapplication.negocio

import android.content.Context
import android.util.Log
import com.example.gasolineraapplication.datos.ResultadoDato

class ResultadoNegocio(context: Context) {

    private val resultadoDato = ResultadoDato(context)
    //cu3
    fun calcularYGuardarResultado(
       // idSucursal: Int,
        idSucursalCombustible: Int,
        litrosIngresados: Double,
        metrosFila: Double,
        cantidadBombas: Int
    ): Boolean {
        val tiempoCargaPorVehiculo = 2.0
        val metrosPorVehiculo = 5.0
        val litrosPorVehiculo = 10.0

        val cantidadVehiculos = (metrosFila / metrosPorVehiculo).toInt()
        val tiempoTotalMin = (cantidadVehiculos * tiempoCargaPorVehiculo / cantidadBombas).toInt()

        val litrosCalculados = litrosIngresados - (cantidadVehiculos * litrosPorVehiculo)
        val litrosRestantes = String.format("%.2f", if (litrosCalculados < 0) 0.0 else litrosCalculados).toDouble()

        val alcanza = litrosRestantes >= litrosPorVehiculo

        return resultadoDato.insertarResultado(
            idSucursalCombustible,
            litrosIngresados,
            metrosFila,
            tiempoTotalMin,
            litrosRestantes,
            alcanza
        )
    }

    // para el mapa
    fun obtenerUbicacionSucursal(idSucursal: Int): Pair<Double, Double>? {
        return resultadoDato.obtenerUbicacionSucursal(idSucursal)
    }
}
