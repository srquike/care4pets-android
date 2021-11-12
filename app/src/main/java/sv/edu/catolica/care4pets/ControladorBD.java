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
        db.execSQL("create table Roles(ID_Rol integer primary key, Nombre text)");
        db.execSQL("CREATE TABLE Usuario(ID_User integer primary key, Nombre text, Apellido text, correo text, contraseña text)");
        db.execSQL("CREATE TABLE Mascotas(ID_pet integer primary key,Nombre text, Raza text,Sexo text,Especie text, Color text,FechaNaci date,Esterilizacion boolean,FechaEsterilizacion date)");
        db.execSQL("CREATE TABLE Evento(ID_Evento integer primary key,Nombre text, Fecha date,Hora datetime,Mascota text,TipoEvento text,Descripcion text)");
        db.execSQL("CREATE TABLE Medicamentos(ID_Medicamento integer primary key,Nombre text,dosis integer,hora_ini datetime,notas text)");
        db.execSQL("CREATE TABLE Alimentos(ID_comida integer primary key,Nombre text, cantidad integer,fecha_ini date,Tomas integer,Notas text)");
        db.execSQL("CREATE TABLE Recordatorio(integer primary key,Nombre text,Fecha date,notas text) ");
        db.execSQL("CREATE TABLE Profesionales(ID_Profesionales integer primary key,Nombre text,Apellido text,Correo text,Telefono int)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {
        //sql scrip re-estructuracion de la bd

    }
}
