package com.gasolinera.gasolineraapplication.datos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDeDatosHelper(context: Context) : SQLiteOpenHelper(
    context,
    "GasolineraDB.db",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase) {
        // Crear tablas
        db.execSQL("""
            CREATE TABLE Sucursal (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                direccion TEXT,
                latitud REAL,
                longitud REAL,
                cantidad_bombas INTEGER NOT NULL
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE TipoCombustible (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE SucursalCombustible (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_sucursal INTEGER NOT NULL,
                id_combustible INTEGER NOT NULL,
                FOREIGN KEY (id_sucursal) REFERENCES Sucursal(id) ON DELETE CASCADE,
                FOREIGN KEY (id_combustible) REFERENCES TipoCombustible(id) ON DELETE CASCADE
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE Resultado (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_sucursalcombustible INTEGER NOT NULL,
                litros_ingresados REAL NOT NULL,
                metros_fila REAL NOT NULL,
                tiempo_estimado_min INTEGER NOT NULL,
                litros_restantes REAL NOT NULL,
                alcanza_combustible INTEGER NOT NULL,
                fechahora_calculo TEXT DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (id_sucursalcombustible) REFERENCES SucursalCombustible(id) ON DELETE CASCADE
            );
        """.trimIndent())

        insertarDatosIniciales(db)
    }

    private fun insertarDatosIniciales(db: SQLiteDatabase) {
        // Insertar sucursales
        db.execSQL("""
            INSERT INTO Sucursal (nombre, direccion, latitud, longitud, cantidad_bombas) VALUES
            ('Sucursal Alemana', 'AV. ALEMANA, 2DO ANILLO', -17.768831074873233, -63.17102893206783, 8),
            ('Sucursal Beni', 'AV. BENI, 2DO ANILLO', -17.76924396037168, -63.17881741857332, 4),
            ('Sucursal Berea', 'DOBLE VIA LA GUARDIA KM 8', -17.83755054769819, -63.23812255905556, 6),
            ('Sucursal Equipetrol', 'V. EQUIPETROL, 4TO ANILLO AL FRENTE DE EX - BUFALO PARK', -17.754202719143098, -63.19715572041929, 6),
            ('Sucursal Gasco', 'AV. BANZER 3ER ANILLO', -17.759197267036505, -63.17936868789235, 6),
            ('Sucursal La Teca', 'CARRETERA A COTOCA, ANTES DE LA TRANCA', -17.763899293507045, -63.07134372024192, 6),
            ('Sucursal Lopez', 'AV. BANZER, 7MO ANILLO', -17.7255166935016, -63.16533261672819, 6),
            ('Sucursal Montecristo', 'AV. VIRGEN DE COTOCA 8VO ANILLO', -17.76594911343311, -63.1094592049014, 6),
            ('Sucursal Paragua', 'AV. PARAGUA, 4TO ANILLO', -17.764662060038034, -63.14938054723115, 6),
            ('Sucursal Pirai', 'AV. ROCA Y CORONADO 3ER ANILLO', -17.78571687825245, -63.204476276243085, 6),
            ('Sucursal Royal', 'AV. ROQUE AGUILERA ESQ CALLE ANGEL SANDOVAL NRO 3897 ZONA VILLA FATIMA', -17.80563552537115, -63.197974091583134, 6),
            ('Sucursal Sur Central', 'AV. SANTOS DUMONT, 2DO ANILLO', -17.799715408705506, -63.18053544740765, 6),
            ('Sucursal Viru Viru', 'KM11 AL NORTE A LADO DE PLAY LAND PARK', -17.675744031656198, -63.158993549077046, 6),
            ('Sucursal Chaco', 'AV. VIRGEN DE COTOCA, 2DO ANILLO', -17.781726618504536, -63.163653911718285, 6);
            
        """.trimIndent())

        // Insertar tipos de combustible
        db.execSQL("""
            INSERT INTO TipoCombustible (nombre) VALUES
            ('Gasolina Especial'),
            ('Diésel');
        """.trimIndent())

        // Insertar asociaciones sucursal-combustible
        db.execSQL("""
            INSERT INTO SucursalCombustible (id_sucursal, id_combustible) VALUES
            (1, 1),
            (2, 1),
            (3, 1),
            (4, 1),
            (5, 1),
            (6, 1),
            (7, 1),
            (8, 1),
            (9, 1),
            (10, 1),
            (11, 1),
            (12, 1),
            (13, 1),
            (3, 2),
            (4, 2),
            (14, 2);
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Resultado")
        db.execSQL("DROP TABLE IF EXISTS SucursalCombustible")
        db.execSQL("DROP TABLE IF EXISTS TipoCombustible")
        db.execSQL("DROP TABLE IF EXISTS Sucursal")
        onCreate(db)
    }
}
