package com.example.corredores;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HistorialActivity extends AppCompatActivity {

    ListView listView;
    TextView tvEstadisticas;
    Button btnVolver, btnLimpiarHistorial;
    ArrayList<Entrenamiento> entrenamientos;
    HistorialAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        listView = findViewById(R.id.listView);
        tvEstadisticas = findViewById(R.id.tvEstadisticas);
        btnVolver = findViewById(R.id.btnVolver);
        btnLimpiarHistorial = findViewById(R.id.btnLimpiarHistorial);

        entrenamientos = new ArrayList<>();

        cargarEntrenamientos();

        adapter = new HistorialAdapter(this, entrenamientos);
        listView.setAdapter(adapter);

        mostrarEstadisticas();

        btnVolver.setOnClickListener(v -> finish());

        btnLimpiarHistorial.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Estás seguro de que deseas eliminar todo el historial?")
                    .setPositiveButton("Sí", (dialog, which) -> eliminarHistorial())
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    private void eliminarHistorial() {
        File file = new File(getFilesDir(), "entrenamientos.txt");

        if (file.exists()) {
            boolean eliminado = file.delete();
            if (eliminado) {
                entrenamientos.clear();
                adapter.notifyDataSetChanged();
                tvEstadisticas.setText("📊 Sin datos para mostrar");
                Toast.makeText(this, "Historial eliminado correctamente.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se pudo eliminar el historial.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No hay historial para eliminar.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarEntrenamientos() {
        try {
            File file = new File(getFilesDir(), "entrenamientos.txt");
            if (!file.exists()) {
                Toast.makeText(this, "No hay entrenamientos registrados", Toast.LENGTH_SHORT).show();
                return;
            }

            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();

            String content = new String(data);
            String[] lines = content.split("\n");

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        Entrenamiento entrenamiento = new Entrenamiento(
                                parts[0].trim(),
                                Float.parseFloat(parts[1].trim()),
                                Integer.parseInt(parts[2].trim()),
                                parts[3].trim(),
                                Float.parseFloat(parts[4].trim())
                        );
                        entrenamientos.add(entrenamiento);
                    }
                }
            }

            Collections.reverse(entrenamientos);

        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar entrenamientos: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarEstadisticas() {
        if (entrenamientos.isEmpty()) {
            tvEstadisticas.setText("📊 Sin datos para mostrar");
            return;
        }

        float totalKm = 0;
        int totalMinutos = 0;
        float mejorRitmo = Float.MAX_VALUE;

        for (Entrenamiento e : entrenamientos) {
            totalKm += e.getDistancia();
            totalMinutos += e.getTiempo();
            if (e.getRitmo() < mejorRitmo) {
                mejorRitmo = e.getRitmo();
            }
        }

        int horas = totalMinutos / 60;
        int minutos = totalMinutos % 60;

        String estadisticas = String.format(
                "📊 Estadísticas Generales\n" +
                        "🏃‍♂️ Total entrenamientos: %d\n" +
                        "📏 Distancia total: %.1f km\n" +
                        "⏱️ Tiempo total: %dh %dm\n" +
                        "🚀 Mejor ritmo: %.2f min/km",
                entrenamientos.size(), totalKm, horas, minutos, mejorRitmo
        );

        tvEstadisticas.setText(estadisticas);
    }

    public static class Entrenamiento {
        private String fecha;
        private float distancia;
        private int tiempo;
        private String tipo;
        private float ritmo;

        public Entrenamiento(String fecha, float distancia, int tiempo, String tipo, float ritmo) {
            this.fecha = fecha;
            this.distancia = distancia;
            this.tiempo = tiempo;
            this.tipo = tipo;
            this.ritmo = ritmo;
        }

        public String getFecha() { return fecha; }
        public float getDistancia() { return distancia; }
        public int getTiempo() { return tiempo; }
        public String getTipo() { return tipo; }
        public float getRitmo() { return ritmo; }
    }
}
