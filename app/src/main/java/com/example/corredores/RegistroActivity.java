package com.example.corredores;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistroActivity extends AppCompatActivity {

    TextView tvFecha;
    EditText etDistancia, etTiempo;
    Spinner spTipo;
    Button btnSeleccionarFecha, btnGuardar, btnVolver;

    Calendar calendar;
    String fechaSeleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Conectar elementos del XML
        tvFecha = findViewById(R.id.tvFecha);
        etDistancia = findViewById(R.id.etDistancia);
        etTiempo = findViewById(R.id.etTiempo);
        spTipo = findViewById(R.id.spTipo);
        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVolver = findViewById(R.id.btnVolver);

        calendar = Calendar.getInstance();

        // Configurar Spinner con tipos de entrenamiento
        String[] tiposEntrenamiento = {
                "Trote suave", "Fondo", "Series", "Intervalos",
                "Tempo", "Carrera larga", "Recuperación"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tiposEntrenamiento);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapter);

        // Configurar fecha actual por defecto
        actualizarFecha();

        // Configurar botón de fecha
        btnSeleccionarFecha.setOnClickListener(v -> mostrarDatePicker());

        // Configurar botón guardar
        btnGuardar.setOnClickListener(v -> guardarEntrenamiento());

        // Configurar botón volver
        btnVolver.setOnClickListener(v -> finish());
    }

    private void mostrarDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    actualizarFecha();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void actualizarFecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fechaSeleccionada = sdf.format(calendar.getTime());
        tvFecha.setText("Fecha: " + fechaSeleccionada);
    }

    private void guardarEntrenamiento() {
        String distanciaStr = etDistancia.getText().toString().trim();
        String tiempoStr = etTiempo.getText().toString().trim();
        String tipo = spTipo.getSelectedItem().toString();

        // Validar campos
        if (distanciaStr.isEmpty() || tiempoStr.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float distancia = Float.parseFloat(distanciaStr);
            int tiempo = Integer.parseInt(tiempoStr);

            // Calcular ritmo promedio (min/km)
            float ritmo = tiempo / distancia;

            // Crear línea de datos para guardar
            String linea = fechaSeleccionada + "," +
                    distancia + "," +
                    tiempo + "," +
                    tipo + "," +
                    String.format("%.2f", ritmo) + "\n";

            // Guardar en archivo
            FileOutputStream fos = openFileOutput("entrenamientos.txt", MODE_APPEND);
            fos.write(linea.getBytes());
            fos.close();

            Toast.makeText(this, "¡Entrenamiento guardado! 🎉", Toast.LENGTH_LONG).show();

            // Limpiar campos
            etDistancia.setText("");
            etTiempo.setText("");
            spTipo.setSelection(0);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor ingresa números válidos", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}