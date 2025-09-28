package com.example.miapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    DBHelper db;
    ListView listUsers;
    Button btnAddUSer;
    ArrayList<String> userList;
    ArrayList<Integer> userIds;
    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        db = new DBHelper(this);
        listUsers = findViewById(R.id.listUsers);
        btnAddUSer = findViewById(R.id.btnAddUser);

        loadUsers();

        //Crear un nuevo usuario
        btnAddUSer.setOnClickListener(v -> showUserDialog(-1));

        //Click para editar o borrar
        listUsers.setOnItemClickListener((parent, view, position, id) -> {
            int userId = userIds.get(position);
            showOptionsDialog(userId);
        });

    }

    //Cargar usuarios en el listView
    private void loadUsers(){
        Cursor cursor = db.getAllUsers();
        userList = new ArrayList<>();
        userIds = new ArrayList<>();

        if (cursor != null){
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));

                userIds.add(id);
                userList.add(id + " - " + username);
            }
             cursor.close();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,userList);
        listUsers.setAdapter(adapter);
    }

    //Mostrar opciones al seleccionar el usuario
    private void showUserDialog(int userId){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(userId == -1 ? "Nuevo Usuario" : "Editar Usuario");

        View view = getLayoutInflater().inflate(R.layout.dialog_user,null);
        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);

        if (userId != -1) {
            Cursor c = db.getUserById(userId);
            if (c != null && c.moveToFirst()){
                etUsername.setText(c.getString(c.getColumnIndexOrThrow("username")));
                etPassword.setText(c.getString(c.getColumnIndexOrThrow("password")));
            }
            if (c != null) c.close();
        }

        builder.setView(view);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String user = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userId == -1){
                boolean inserted = db.registerUser(user, pass);
                if (inserted) Toast.makeText(this, "Usuario Agregado", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "ERROR al agregar",  Toast.LENGTH_SHORT).show();
            } else {
                boolean updated = db.updateUser(userId, user, pass);
                if (updated) Toast.makeText(this, "Usuario Actualizado", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "Error al Actualizar", Toast.LENGTH_SHORT).show();
            }
            loadUsers();
        });

        builder.setNegativeButton("Cacelar", null);
        builder.show();

    }


    private void showOptionsDialog(int userId){

        Cursor cursor = db.getUserById(userId);
        String username = "";
        if (cursor != null){
            if (cursor.moveToFirst()){
                username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            }
            cursor.close();
        }

        String[] options = {"Editar", "Eliminar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acciones para " + username);
        builder.setItems(options, (dialog, wich) -> {
            if (wich == 0){
                //Editar
                showUserDialog(userId);
            } else {
                //Eliminar
                if (db.deleteUser(userId)){
                    Toast.makeText(this, "Usuario Eliminado", Toast.LENGTH_SHORT).show();
                    loadUsers();
                } else {
                    Toast.makeText(this, "Error al Eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }
}