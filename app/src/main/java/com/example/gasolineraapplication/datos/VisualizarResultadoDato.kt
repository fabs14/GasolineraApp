package com.example.gasolineraapplication.datos

import android.content.Context
import android.database.Cursor
import com.gasolinera.gasolineraapplication.datos.BaseDeDatosHelper

class VisualizarResultadoDato(context: Context) {

    private val helper = BaseDeDatosHelper(context)

    fun obtenerUltimoResultado(idSucursalCombustible: Int): Cursor {
        val db = helper.readableDatabase
        return db.rawQuery(
            """
                SELECT tiempo_estimado_min, litros_restantes, alcanza_combustible
                FROM Resultado
                WHERE id_sucursalcombustible = ?
                ORDER BY id DESC LIMIT 1
            """.trimIndent(),
            arrayOf(idSucursalCombustible.toString())
        )
    }
}
