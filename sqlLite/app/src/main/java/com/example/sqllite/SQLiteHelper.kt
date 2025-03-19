package com.example.sqllite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

// Definir el nombre y versión de la base de datos
class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "MyDatabase.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear la tabla
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_EMAIL TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Si la base de datos se actualiza, eliminar la tabla anterior y crear una nueva
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Función para insertar un nuevo usuario
    fun insertUser(name: String, email: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_EMAIL, email)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // Función para obtener todos los usuarios
    fun getAllUsers(): List<String> {
        val db = readableDatabase
        val users = mutableListOf<String>()
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_NAME, COLUMN_EMAIL), null, null, null, null, null)

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
            users.add("Name: $name, Email: $email")
        }
        cursor.close()
        db.close()
        return users
    }

    // Función para actualizar un usuario
    fun updateUser(id: Int, name: String, email: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_EMAIL, email)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    // Función para eliminar un usuario
    fun deleteUser(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }
    // Función para obtener el último ID insertado
    fun getLastInsertedId(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT MAX($COLUMN_ID) FROM $TABLE_NAME", null)
        cursor.moveToFirst()
        val lastId = cursor.getInt(0)
        cursor.close()
        db.close()
        return lastId
    }

}
