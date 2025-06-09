package com.example.corredores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etNombre;
    Button btnIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre = findViewById(R.id.etNombre);
        btnIniciar = findViewById(R.id.btnIniciar);

        btnIniciar.setOnClickListener(v -> {
            String nombreIngresado = etNombre.getText().toString().trim();
            if (nombreIngresado.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa tu nombre", Toast.LENGTH_SHORT).show();
            } else {
                // Guardar nombre en SharedPreferences
                SharedPreferences prefs = getSharedPreferences("MetasPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nombre", nombreIngresado);
                editor.apply();

                // Abrir DashboardActivity
                Intent intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
                finish(); // Opcional: cerrar esta actividad para no volver atrás
            }
        });
    }
}
