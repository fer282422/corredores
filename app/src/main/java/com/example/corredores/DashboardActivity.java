package com.example.corredores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;

public class DashboardActivity extends AppCompatActivity {

    TextView tvSaludo, tvProgreso;
    ProgressBar progressBar;
    Button btnRegistrar, btnFrases, btnHistorial, btnMetas;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvSaludo = findViewById(R.id.tvSaludo);
        tvProgreso = findViewById(R.id.tvProgresoMeta);
        progressBar = findViewById(R.id.progressMeta);
        btnRegistrar = findViewById(R.id.btnRegistrarEntrenamiento);
        btnFrases = findViewById(R.id.btnFrasesMotivadoras);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnMetas = findViewById(R.id.btnMetas);

        prefs = getSharedPreferences("MetasPrefs", Context.MODE_PRIVATE);

        // Obtener nombre guardado
        String nombre = prefs.getString("nombre", "Corredor");
        tvSaludo.setText("¡Hola " + nombre + "! 🏃‍♂️");

        mostrarProgreso();

        btnRegistrar.setOnClickListener(v ->
                startActivity(new Intent(this, RegistroActivity.class)));

        btnFrases.setOnClickListener(v ->
                startActivity(new Intent(this, FrasesActivity.class)));

        btnHistorial.setOnClickListener(v ->
                startActivity(new Intent(this, HistorialActivity.class)));

        btnMetas.setOnClickListener(v ->
                startActivity(new Intent(this, MetasActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarProgreso();
    }

    private void mostrarProgreso() {
        String metaMensual = prefs.getString("meta", "Sin meta establecida");
        float kmRecorridos = calcularKmTotales();

        if (metaMensual.equals("Sin meta establecida")) {
            tvProgreso.setText("📊 Establece tu meta mensual primero");
            progressBar.setProgress(0);
        } else {
            try {
                float metaKm = Float.parseFloat(metaMensual);
                float porcentaje = (kmRecorridos / metaKm) * 100;
                if (porcentaje > 100) porcentaje = 100;

                tvProgreso.setText(String.format(
                        "📊 Progreso: %.1f/%.0f km (%.1f%%)\nTe faltan: %.1f km para tu meta",
                        kmRecorridos, metaKm, porcentaje, Math.max(0, metaKm - kmRecorridos)
                ));
                progressBar.setProgress((int) porcentaje);
            } catch (NumberFormatException e) {
                tvProgreso.setText("📊 Error en formato de meta");
                progressBar.setProgress(0);
            }
        }
    }

    private float calcularKmTotales() {
        float total = 0;
        try {
            File file = new File(getFilesDir(), "entrenamientos.txt");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();

                String content = new String(data);
                String[] lines = content.split("\n");

                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        String[] parts = line.split(",");
                        if (parts.length >= 2) {
                            total += Float.parseFloat(parts[1].trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
}
