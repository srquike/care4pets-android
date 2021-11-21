package sv.edu.catolica.care4pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class MascotaActivity extends AppCompatActivity {

    private EditText edtNombre, edtRaza, edtColor, edtFechaNacimiento, edtFechaEsterilizacion;
    private Spinner spnSexo, spnEspecie;
    private RadioButton rdbSi, rdbNo;
    private ImageView imvFoto, imvFechaEsterilizacion;
    private ControladorBD adminDB;
    private SQLiteDatabase db;
    private int idMascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascota);
        edtColor = findViewById(R.id.edtColor);
        edtNombre = findViewById(R.id.edtNombre);
        edtRaza = findViewById(R.id.edtRaza);
        edtFechaNacimiento = findViewById(R.id.edtFechaNacimiento);
        edtFechaEsterilizacion = findViewById(R.id.edtFechaEsterilizacion);
        spnEspecie = findViewById(R.id.spnEspecie);
        spnSexo = findViewById(R.id.spnSexo);
        rdbNo = findViewById(R.id.rdbNo);
        rdbSi = findViewById(R.id.rdbSi);
        imvFoto = findViewById(R.id.imvFoto);
        imvFechaEsterilizacion = findViewById(R.id.imvFechaEsterilizacion);
        adminDB = new ControladorBD(this, "DBCare4Pets", null, 1);

        rdbSi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtFechaEsterilizacion.setEnabled(true);
                    imvFechaEsterilizacion.setVisibility(View.VISIBLE);
                }
            }
        });

        rdbNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtFechaEsterilizacion.setEnabled(false);
                    imvFechaEsterilizacion.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();

            if (bundle == null) {
                idMascota = Integer.parseInt(null);
            } else {
                idMascota = bundle.getInt("Id");
            }
        } else {
            idMascota = (int) savedInstanceState.getSerializable("Id");
        }

        MascotaModel mascotaModel = obtenerMascotaPorId(idMascota);

        if (mascotaModel != null) {
            edtNombre.setText(mascotaModel.getNombre());
            spnSexo.setSelection(Arrays.asList(getResources().getStringArray(R.array.sexos)).indexOf(mascotaModel.getSexo()));
            spnEspecie.setSelection(Arrays.asList(getResources().getStringArray(R.array.especies)).indexOf(mascotaModel.getEspecie()));
            edtRaza.setText(mascotaModel.getRaza());
            edtColor.setText(mascotaModel.getColor());
            edtFechaNacimiento.setText(mascotaModel.getFechaNacimiento().format(DateTimeFormatter.ofPattern("d/M/yyyy")));

            if (mascotaModel.getEsterilizacion().equals("true")) {
                rdbSi.setChecked(true);
            } else {
                rdbNo.setChecked(true);
            }

            edtFechaEsterilizacion.setText(mascotaModel.getFechaEsterilizacion().format(DateTimeFormatter.ofPattern("d/M/yyyy")));
        }

        getSupportActionBar().setTitle(mascotaModel.getNombre());
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void editarMascota(int id) {
        db = adminDB.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Nombre", edtNombre.getText().toString());
        values.put("Raza", edtRaza.getText().toString());
        values.put("Sexo", spnSexo.getSelectedItem().toString());
        values.put("Especie", spnEspecie.getSelectedItem().toString());
        values.put("Color", edtColor.getText().toString());
        values.put("FechaNaci", edtFechaNacimiento.getText().toString());
        values.put("FechaEsterilizacion", edtFechaEsterilizacion.getText().toString());

        if (rdbSi.isChecked()) {
            values.put("Esterilizacion", "true");

        } else if (rdbNo.isChecked()) {
            values.put("Esterilizacion", "false");
        }

        long resultado = db.update("Mascotas", values, "ID_pet = " + id, null);

        if (resultado > 0) {
            MostrarMensaje("Se modificarón los datos");
        } else {
            MostrarMensaje("Error al modificar los datos");
        }

        db.close();
    }

    private void MostrarMensaje(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private MascotaModel obtenerMascotaPorId(int id) {
        db = adminDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Mascotas WHERE ID_pet = " + id + " LIMIT 1", null);
        MascotaModel mascotaModel = null;

        if (cursor.moveToFirst()) {
            mascotaModel = new MascotaModel();
            mascotaModel.setId(cursor.getInt(0));
            mascotaModel.setNombre(cursor.getString(1));
            mascotaModel.setRaza(cursor.getString(2));
            mascotaModel.setSexo(cursor.getString(3));
            mascotaModel.setEspecie(cursor.getString(4));
            mascotaModel.setColor(cursor.getString(5));
            mascotaModel.setFechaNacimiento(LocalDate.parse(cursor.getString(6), DateTimeFormatter.ofPattern("d/M/yyyy")));
            mascotaModel.setEsterilizacion(cursor.getString(7));
            mascotaModel.setFechaEsterilizacion(LocalDate.parse(cursor.getString(8), DateTimeFormatter.ofPattern("d/M/yyyy")));
            mascotaModel.setFoto(R.drawable.pet);
            mascotaModel.setDescripcion(mascotaModel.getRaza() + " - " + mascotaModel.getEspecie() + " - " + mascotaModel.getColor() + " - " + mascotaModel.getSexo());
        }

        return mascotaModel;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.save_changes_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btnGuardarCambios:
                editarMascota(idMascota);
                break;
            case R.id.btnCancelar:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}