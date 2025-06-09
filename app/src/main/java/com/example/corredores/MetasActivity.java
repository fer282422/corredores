package com.example.corredores;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MetasActivity extends AppCompatActivity {

    EditText etKmMeta;
    Spinner spTipoMeta;
    Button btnGuardarMeta, btnRevisarMeta, btnEliminarMeta;

    // Para guardar en SharedPreferences
    SharedPreferences prefs;
    static final String PREFS_NAME = "MetasPrefs";
    static final String KEY_KM = "meta_km";
    static final String KEY_TIPO = "meta_tipo";
    static final String KEY_VIGENTE = "meta_vigente"; // true/false

    // Mismos tipos de entrenamiento que en RegistroActivity
    String[] tiposEntrenamiento = {
            "Trote suave", "Fondo", "Series", "Intervalos",
            "Tempo", "Carrera larga", "Recuperación"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas);

        etKmMeta = findViewById(R.id.etKmMeta);
        spTipoMeta = findViewById(R.id.spTipoMeta);
        btnGuardarMeta = findViewById(R.id.btnGuardarMeta);
        btnRevisarMeta = findViewById(R.id.btnRevisarMeta);
        btnEliminarMeta = findViewById(R.id.btnEliminarMeta);

        // Configurar spinner con tipos
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tiposEntrenamiento);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoMeta.setAdapter(adapter);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        cargarMetaGuardada();

        btnGuardarMeta.setOnClickListener(v -> guardarMeta());
        btnRevisarMeta.setOnClickListener(v -> mostrarMetaVigente());
        btnEliminarMeta.setOnClickListener(v -> eliminarMeta());
    }

    private void cargarMetaGuardada() {
        boolean vigente = prefs.getBoolean(KEY_VIGENTE, false);
        if (vigente) {
            float km = prefs.getFloat(KEY_KM, 0);
            String tipo = prefs.getString(KEY_TIPO, "");

            etKmMeta.setText(String.valueOf(km));

            // Seleccionar tipo en spinner
            int pos = 0;
            for (int i = 0; i < tiposEntrenamiento.length; i++) {
                if (tiposEntrenamiento[i].equals(tipo)) {
                    pos = i;
                    break;
                }
            }
            spTipoMeta.setSelection(pos);

            // Deshabilitar edición para evitar que se agregue otra meta
            etKmMeta.setEnabled(false);
            spTipoMeta.setEnabled(false);
            btnGuardarMeta.setEnabled(false);

            Toast.makeText(this, "Hay una meta vigente activa.", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarMeta() {
        boolean vigente = prefs.getBoolean(KEY_VIGENTE, false);
        if (vigente) {
            Toast.makeText(this, "Termina la meta vigente antes de crear una nueva.", Toast.LENGTH_LONG).show();
            return;
        }

        String kmStr = etKmMeta.getText().toString().trim();
        if (kmStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa los kilómetros de la meta.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float km = Float.parseFloat(kmStr);
            String tipo = spTipoMeta.getSelectedItem().toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putFloat(KEY_KM, km);
            editor.putString(KEY_TIPO, tipo);
            editor.putBoolean(KEY_VIGENTE, true);
            editor.apply();

            Toast.makeText(this, "Meta guardada correctamente.", Toast.LENGTH_LONG).show();

            // Deshabilitar campos
            etKmMeta.setEnabled(false);
            spTipoMeta.setEnabled(false);
            btnGuardarMeta.setEnabled(false);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese un número válido para kilómetros.", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarMetaVigente() {
        boolean vigente = prefs.getBoolean(KEY_VIGENTE, false);
        if (!vigente) {
            Toast.makeText(this, "No hay una meta vigente actualmente.", Toast.LENGTH_SHORT).show();
            return;
        }

        float km = prefs.getFloat(KEY_KM, 0);
        String tipo = prefs.getString(KEY_TIPO, "");

        String mensaje = "Meta vigente:\n\n" +
                "Kilómetros: " + km + "\n" +
                "Tipo de entrenamiento: " + tipo;

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Detalles de la Meta Vigente")
                .setMessage(mensaje)
                .setPositiveButton("Cerrar", null)
                .show();
    }

    private void eliminarMeta() {
        boolean vigente = prefs.getBoolean(KEY_VIGENTE, false);
        if (!vigente) {
            Toast.makeText(this, "No hay meta para eliminar.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Meta eliminada. Puedes crear una nueva.", Toast.LENGTH_SHORT).show();

        // Habilitar campos para nueva meta
        etKmMeta.setText("");
        etKmMeta.setEnabled(true);
        spTipoMeta.setEnabled(true);
        btnGuardarMeta.setEnabled(true);
    }
}
