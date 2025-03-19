package com.example.sqllite

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var resultTextView: TextView
    private lateinit var insertButton: Button
    private lateinit var getButton: Button
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar el helper
        sqliteHelper = SQLiteHelper(this)

        // Referencias a los elementos de la interfaz
        resultTextView = findViewById(R.id.resultTextView)
        insertButton = findViewById(R.id.insertButton)
        getButton = findViewById(R.id.getButton)
        updateButton = findViewById(R.id.updateButton)
        deleteButton = findViewById(R.id.deleteButton)

        // Insertar un usuario
        insertButton.setOnClickListener {
            sqliteHelper.insertUser("Juan Pérez", "juan@example.com")
            Toast.makeText(this, "Usuario insertado", Toast.LENGTH_SHORT).show()
        }

        // Obtener todos los usuarios
        getButton.setOnClickListener {
            val users = sqliteHelper.getAllUsers()
            resultTextView.text = users.joinToString("\n")
        }

        // Actualizar el último usuario insertado
        updateButton.setOnClickListener {
            val lastId = sqliteHelper.getLastInsertedId()
            if (lastId > 0) {
                sqliteHelper.updateUser(lastId, "Juan Actualizado", "juan.actualizado@example.com")
                Toast.makeText(this, "Último usuario actualizado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No hay usuarios para actualizar", Toast.LENGTH_SHORT).show()
            }
        }

        // Eliminar el último usuario insertado
        deleteButton.setOnClickListener {
            val lastId = sqliteHelper.getLastInsertedId()
            if (lastId > 0) {
                sqliteHelper.deleteUser(lastId)
                Toast.makeText(this, "Último usuario eliminado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No hay usuarios para eliminar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
