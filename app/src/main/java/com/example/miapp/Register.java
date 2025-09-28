package com.example.miapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Register extends AppCompatActivity {

    TextInputEditText txtRegisterName, txtRegisterEmail, registerPass1, registerPass2;

    MaterialButton btnRegister;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        txtRegisterName = findViewById(R.id.txtRegisterName);
        txtRegisterEmail = findViewById(R.id.txtRegisterEmail);
        registerPass1 = findViewById(R.id.registerPass1);
        registerPass2 = findViewById(R.id.registerPass2);
        btnRegister = findViewById(R.id.btnRegister);

        db = new DBHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = txtRegisterEmail.getText().toString();
                String pass = registerPass1.getText().toString();
                String pass2 = registerPass2.getText().toString();

                if (user.isEmpty() || pass.isEmpty() || !pass.equals(pass2)){
                    Toast.makeText(Register.this, "Completa todos los campos de forma correcta",Toast.LENGTH_SHORT).show();
                } else {
                    boolean inserted = db.registerUser(user, pass);

                    if (inserted){{
                        Toast.makeText(Register.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, MainActivity.class));
                        finish();
                    }} else {
                        Toast.makeText(Register.this, "Error: Usuario existente", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView urllogin = findViewById(R.id.lblLogin);
        urllogin.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
        });
    }
}