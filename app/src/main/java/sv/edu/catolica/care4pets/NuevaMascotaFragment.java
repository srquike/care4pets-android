package sv.edu.catolica.care4pets;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;

public class NuevaMascotaFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private EditText edtFechaEsterilizacion, edtNombre, edtRaza, edtColor;
    private EditText edtFechaNacimiento;
    private ImageView imvFechaNacimiento, imvFechaEsterilizacion, imvFoto;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private Spinner spnSexo, spnEspecie;
    private RadioButton rbSi, rbNo;
    private ControladorBD adminDB;
    private SQLiteDatabase db;
    private int mascotaId;
    private static final int REQUEST_PERMISSION_CAM = 100, REQUEST_PERMISSION_STORAGE = 200, REQUEST_CAPTURE = 3;
    Bitmap fotoCapturada;


    public NuevaMascotaFragment() {


    }

    public static NuevaMascotaFragment newInstance(String param1, String param2) {
        NuevaMascotaFragment fragment = new NuevaMascotaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getArguments() == null) {
            inflater.inflate(R.menu.save_or_cancel_menu, menu);
        } else {
            inflater.inflate(R.menu.save_changes_menu, menu);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_nueva_mascota, container, false);
        Bundle bundle = getArguments();
        adminDB = new ControladorBD(getContext(), "DBCare4Pets", null, 1);
        edtFechaNacimiento = vista.findViewById(R.id.edtFechaNacimiento);
        edtFechaEsterilizacion = vista.findViewById(R.id.edtFechaEsterilizacion);
        imvFechaNacimiento = vista.findViewById(R.id.imVFechaNacimiento);
        imvFechaEsterilizacion = vista.findViewById(R.id.imVFechaEsterilizacion);
        edtNombre = vista.findViewById(R.id.txtNombre);
        spnSexo = vista.findViewById(R.id.spnSexo);
        spnEspecie = vista.findViewById(R.id.spnEspecie);
        edtRaza = vista.findViewById(R.id.txtRaza);
        edtColor = vista.findViewById(R.id.txtColor);
        rbSi = vista.findViewById(R.id.rbSi);
        rbNo = vista.findViewById(R.id.rbNo);
        imvFoto = vista.findViewById(R.id.imvFoto);

        imvFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procesoCapturar();
            }
        });

        rbSi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtFechaEsterilizacion.setEnabled(true);
                    imvFechaEsterilizacion.setVisibility(View.VISIBLE);
                }
            }
        });

        rbNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtFechaEsterilizacion.setEnabled(false);
                    imvFechaEsterilizacion.setVisibility(View.INVISIBLE);
                }
            }
        });

        imvFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendario(edtFechaNacimiento);
            }
        });

        imvFechaEsterilizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendario(edtFechaEsterilizacion);
            }
        });

        llenarCampos(bundle);

        return vista;
    }

    private void llenarCampos(Bundle bundle) {
        if (bundle != null) {
            mascotaId = bundle.getInt("Id");
            MascotaModel mascotaModel = obtenerMascotaPorId(mascotaId);

            if (mascotaModel != null) {
                edtNombre.setText(mascotaModel.getNombre());
                spnSexo.setSelection(Arrays.asList(getResources().getStringArray(R.array.sexos)).indexOf(mascotaModel.getSexo()));
                spnEspecie.setSelection(Arrays.asList(getResources().getStringArray(R.array.especies)).indexOf(mascotaModel.getEspecie()));
                edtRaza.setText(mascotaModel.getRaza());
                edtColor.setText(mascotaModel.getColor());
                edtFechaNacimiento.setText(mascotaModel.getFechaNacimiento().format(DateTimeFormatter.ofPattern("d/M/yyyy")));

                if (mascotaModel.getEsterilizacion().equals("true")) {
                    rbSi.setChecked(true);
                } else {
                    rbNo.setChecked(true);
                }

                edtFechaEsterilizacion.setText(mascotaModel.getFechaEsterilizacion().format(DateTimeFormatter.ofPattern("d/M/yyyy")));

                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.getSupportActionBar().setTitle(mascotaModel.getNombre());
            }
        }
    }

    private MascotaModel obtenerMascotaPorId(int id) {
        db = adminDB.getReadableDatabase();
        MascotaModel mascotaModel = null;
        Cursor cursor = db.rawQuery("SELECT * FROM Mascotas WHERE ID_pet = " + id + " LIMIT 1", null);

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

    private void abrirCalendario(EditText editText) {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (editText.equals(edtFechaNacimiento)) {
                    edtFechaNacimiento.setText(dayOfMonth + "/" + month + "/" + year);
                } else if (editText.equals((edtFechaEsterilizacion))) {
                    edtFechaEsterilizacion.setText(dayOfMonth + "/" + month + "/" + year);
                }
            }
        }, year, mes, dia);

        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btnAceptar:
                insertToDB();
                onBackPressed();

                break;
            case R.id.btnCancelar:
                onBackPressed();
                break;
            case R.id.btnGuardarCambios:
                editarMascota(mascotaId);
                onBackPressed();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

    private void insertToDB() {
        String nombre = edtNombre.getText().toString();
        db = adminDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Nombre", nombre);
        values.put("Raza", edtRaza.getText().toString());
        values.put("Sexo", spnSexo.getSelectedItem().toString());
        values.put("Especie", spnEspecie.getSelectedItem().toString());
        values.put("Color", edtColor.getText().toString());
        values.put("FechaNaci", edtFechaNacimiento.getText().toString());
        values.put("FechaEsterilizacion", edtFechaEsterilizacion.getText().toString());

        if (rbSi.isChecked()) {
            values.put("Esterilizacion", "true");

        } else if (rbNo.isChecked()) {
            values.put("Esterilizacion", "false");
        }

        long id = db.insert("Mascotas", null, values);

        if (id > 0) {
            MostrarMensaje("Mascota Agregada");
        } else {
            MostrarMensaje("Error al Ingresar Datos");
        }

        db.close();
        verificarPermisoAlmacenamieto();
        guardarFoto(nombre);
    }

    private void MostrarMensaje(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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


        if (rbSi.isChecked()) {
            values.put("Esterilizacion", "true");

        } else if (rbNo.isChecked()) {
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

    public void procesoCapturar() {
        VerificarPermisoCamara();

    }

    private void VerificarPermisoCamara() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                tomarFoto();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAM);
            }
        } else {
            tomarFoto();
        }
    }

    private void tomarFoto() {
        Intent captura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captura.resolveActivity(getActivity().getPackageManager()) != null) ;
        {
            startActivityForResult(captura, REQUEST_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE) {
            if (resultCode == RESULT_OK && data != null) {
                fotoCapturada = (Bitmap) data.getExtras().get("data");
                imvFoto.setImageBitmap(fotoCapturada);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void verificarPermisoAlmacenamieto() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
                }
            }
        }
    }

    private void guardarFoto(String nombreFoto) {

        OutputStream fos = null;
        File miArchivo = null;
        String nombreArchivo = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContext().getContentResolver();

            ContentValues valores = new ContentValues();

            nombreArchivo = nombreFoto;

            valores.put(MediaStore.Images.Media.DISPLAY_NAME, nombreArchivo);
            valores.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            valores.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyAppCare4Pets/Mascotas");
            valores.put(MediaStore.Images.Media.IS_PENDING, 1);

            Uri coleccion = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri imagenUri = resolver.insert(coleccion, valores);


            try {
                fos = resolver.openOutputStream(imagenUri);
            } catch (FileNotFoundException e) {
                Toast.makeText(getContext(), "Error de Archivo", Toast.LENGTH_SHORT).show();
            }

            valores.clear();
            valores.put(MediaStore.Images.Media.IS_PENDING, 0);

            resolver.update(imagenUri, valores, null, null);

        } else {
            String Directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

            miArchivo = new File(Directorio, nombreArchivo);
            try {
                fos = new FileOutputStream(miArchivo);
            } catch (FileNotFoundException e) {
                Toast.makeText(getContext(), "Error de creacion de archivo", Toast.LENGTH_SHORT).show();
            }
        }

        boolean guardado = fotoCapturada.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        if (guardado == false) {
            Toast.makeText(getContext(), "La foto no se guardo correctamente", Toast.LENGTH_LONG).show();
        }

        if (fos != null) {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        if (miArchivo != null) {
            MediaScannerConnection.scanFile(getContext(), new String[]{
                    miArchivo.toString()
            }, null, null);
        }
    }

    public void ValidarCampos(){
        if (edtNombre.equals("")){
            edtNombre.setText("Ingrese un Nombre");
        }else if (edtRaza.equals("")){
            edtRaza.setText("Ingrese una raza");
        }else if (edtColor.equals("")){
            edtColor.setText("Ingrese un color");
        }else if (edtFechaNacimiento.equals("")){
            edtFechaNacimiento.setText("Ingrese una fecha");
        }
        else if (edtFechaEsterilizacion.equals("")){
            edtFechaEsterilizacion.setText("Ingrese una fecha");
        }
    }

}