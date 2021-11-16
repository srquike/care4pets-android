package sv.edu.catolica.care4pets;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class ControladorBD extends SQLiteOpenHelper {


    public ControladorBD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Roles(ID_Rol integer primary key autoincrement, Nombre text)");
        db.execSQL("CREATE TABLE Usuario(ID_User integer primary key autoincrement, Nombre text, Apellido text, correo text, contraseña text)");
        db.execSQL("CREATE TABLE Mascotas(ID_pet integer primary key autoincrement,Nombre text, Raza text,Sexo text,Especie text, Color text,FechaNaci date,Esterilizacion boolean,FechaEsterilizacion date)");
        db.execSQL("CREATE TABLE Evento(ID_Evento integer primary key autoincrement,Nombre text, Fecha date,Hora datetime, TipoEvento text,Descripcion text)");
        db.execSQL("CREATE TABLE Medicamentos(ID_Medicamento integer primary key autoincrement,Nombre text,dosis integer,hora_ini datetime,notas text, Presentacion text,Cantidad text,Unidad text,FechaVencimiento date,Laboratorio text)");
        db.execSQL("CREATE TABLE Alimentos(ID_comida integer primary key autoincrement,Nombre text, cantidad integer,fecha_ini date,Tomas integer,Notas text, TipoComida text, Unidad text)");
        db.execSQL("CREATE TABLE Recordatorio(ID_Recordatorio integer primary key autoincrement,Nombre text,Fecha date,notas text) ");
        db.execSQL("CREATE TABLE Profesionales(ID_Profesionales integer primary key autoincrement," +
                "Nombre text, Correo text,Telefono text,Profesion text,Celular text, Direccion text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }
}
