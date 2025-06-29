package com.example.appesp32;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ControlActivity extends AppCompatActivity {

    TextView lblEstado, lblFecha;
    ImageView imgPuerta;
    Button btnOpen, btnClose;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("ESTADO_OBJETO");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        lblEstado = findViewById(R.id.lblEstado);
        lblFecha = findViewById(R.id.lblFecha);
        imgPuerta = findViewById(R.id.imgPuerta);
        btnOpen = findViewById(R.id.btnOpen);
        btnClose = findViewById(R.id.btnClose);

        btnOpen.setOnClickListener(v -> actualizarEstado(true));
        btnClose.setOnClickListener(v -> actualizarEstado(false));

        escucharEstado();
    }

    private void actualizarEstado(boolean abierta) {
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        dbRef.child("estado").setValue(abierta);
        dbRef.child("fecha").setValue(fecha);
    }

    private void escucharEstado() {
        dbRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Boolean estado = snapshot.child("estado").getValue(Boolean.class);
                    String fecha = snapshot.child("fecha").getValue(String.class);

                    if (estado != null) {
                        actualizarInterfaz(estado, fecha);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ControlActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarInterfaz(boolean abierta, String fecha) {
        lblFecha.setText("Fecha: " + fecha);

        if (abierta) {
            lblEstado.setText("Estado: Puerta abierta");
            imgPuerta.setImageResource(R.drawable.puerta_abierta);
        } else {
            lblEstado.setText("Estado: Puerta cerrada");
            imgPuerta.setImageResource(R.drawable.puerta_cerrada);
        }
    }
}