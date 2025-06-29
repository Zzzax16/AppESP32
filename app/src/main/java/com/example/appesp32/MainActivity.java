package com.example.appesp32;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText etUser, etPassword;
    Button btnIngresar;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        btnIngresar = findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(v -> validarUsuario());
    }

    private void validarUsuario() {
        String user = etUser.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.child(user).get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                if (snapshot.child("Password").exists()) {
                    String storedPass = snapshot.child("Password").getValue(String.class);
                    if (pass.equals(storedPass)) {
                        startActivity(new Intent(this, ControlActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "El usuario no tiene contraseña configurada", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Usuario no existe", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}