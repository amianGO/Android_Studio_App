package com.example.miapp;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Usuarios.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL("CREATE TABLE users(" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS users");
        onCreate(database);
    }


    //Insertar un Usuario
    public boolean registerUser(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", username);
        values.put("password",password);

        long result = db.insert("users",null,values);
        return  result != -1; //true si se inserto bien
    }

    //Verificar Login
    public boolean loginUser(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE username=? AND password=?",
                new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();

        return exists;
    }

    //Obtener todos los Usuarios
    public Cursor getAllUsers(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users", null);
    }

    //Obtener usuario por ID
    public Cursor getUserById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return  db.rawQuery("SELECT * FROM users WHERE id=?", new String[]{String.valueOf(id)});
    }

    //Actualizar usuario por Id
    public boolean updateUser(int id, String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", username);
        values.put("password", password);

        int result = db.update("users", values, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    //Borrar usuario por Id
    public boolean deleteUser(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("users", "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}
