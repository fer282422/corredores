package com.example.corredores;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FrasesActivity extends AppCompatActivity {

    ImageView imgView;
    Button btnCambiar;

    int[] imagenes = {
            R.drawable.imagen1,
            R.drawable.imagen2,
            R.drawable.imagen3,
            R.drawable.imagen4
    };
    int indiceActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frases);

        imgView = findViewById(R.id.imgView);
        btnCambiar = findViewById(R.id.btnCambiar);

        btnCambiar.setOnClickListener(v -> {
            indiceActual = (indiceActual + 1) % imagenes.length;
            imgView.setImageResource(imagenes[indiceActual]);
        });
    }
}
