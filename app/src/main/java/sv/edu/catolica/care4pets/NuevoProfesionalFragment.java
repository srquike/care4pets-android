package sv.edu.catolica.care4pets;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;

public class NuevoProfesionalFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private EditText edtNombre, edtTelefono, edtCelular, edtCorreo, edtDireccion;
    private Spinner spnProfesion;
    private ControladorBD adminDB;
    private SQLiteDatabase db;
    private int profesionalId;

    public NuevoProfesionalFragment() {

    }

    public static NuevoProfesionalFragment newInstance(String param1, String param2) {
        NuevoProfesionalFragment fragment = new NuevoProfesionalFragment();
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
        //15 se crea un if para guardar o modificar el inflater
        if (getArguments() == null) {
            inflater.inflate(R.menu.save_or_cancel_menu, menu);
        } else {
            inflater.inflate(R.menu.save_changes_menu, menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btnAceptar:
                if (validarCampos()) {
                    insertToDB();
                    onBackPressed();
                }
                break;
            case R.id.btnCancelar:
                onBackPressed();
                break;
            //21 se crea el nuevo case para guardarCambios
            case R.id.btnGuardarCambios:
                if (validarCampos()) {
                    editarProfesional(profesionalId);
                    onBackPressed();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adminDB = new ControladorBD(getContext(), "DBCare4Pets", null, 1);
        db = adminDB.getWritableDatabase();
        View view = inflater.inflate(R.layout.fragment_nuevo_profesional, container, false);
        //16 se crea bundle getArguments;
        Bundle bundle = getArguments();
        edtNombre = view.findViewById(R.id.txtNombre);
        edtTelefono = view.findViewById(R.id.txtTelefono);
        edtCelular = view.findViewById(R.id.txtCelular);
        edtCorreo = view.findViewById(R.id.txtCorreo);
        edtDireccion = view.findViewById(R.id.txtDireccion);
        spnProfesion = view.findViewById(R.id.spnProfesion);

        //17 se llama y crea el metodo llenarCampos(bundle);
        llenarCampos(bundle);

        return view;
    }

    private void llenarCampos(Bundle bundle) {
        //18 se crea el if(bundle)
        if (bundle != null) {
            profesionalId = bundle.getInt("Id");
            ProfesionalModel profesionalModel = obtenerAlimentoPorId(profesionalId);

            if (profesionalModel != null) {
                edtNombre.setText(profesionalModel.getNombre());
                spnProfesion.setSelection(Arrays.asList(getResources().getStringArray(R.array.profesiones)).indexOf(profesionalModel.getProfesion()));
                edtTelefono.setText(profesionalModel.getTelefono());
                edtCelular.setText(profesionalModel.getCelular());
                edtCorreo.setText(profesionalModel.getCorreo());
                edtDireccion.setText(profesionalModel.getDireccion());

                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.getSupportActionBar().setTitle(profesionalModel.getNombre());

            }
        }
    }

    //19 se crea el metodo obtenerPorId
    private ProfesionalModel obtenerAlimentoPorId(int id) {
        db = adminDB.getReadableDatabase();
        ProfesionalModel profesionalModel = null;
        Cursor cursor = db.rawQuery("SELECT * FROM Profesionales WHERE ID_Profesionales = " + id + " LIMIT 1", null);

        if (cursor.moveToFirst()) {
            profesionalModel = new ProfesionalModel();
            profesionalModel.setId(cursor.getInt(0));
            profesionalModel.setNombre(cursor.getString(1));
            profesionalModel.setCorreo(cursor.getString(2));
            profesionalModel.setTelefono(cursor.getString(3));
            profesionalModel.setProfesion(cursor.getString(4));
            profesionalModel.setCelular(cursor.getString(5));
            profesionalModel.setDireccion(cursor.getString(6));
            profesionalModel.setDescripcion(profesionalModel.getProfesion() + " - " + profesionalModel.getTelefono() + " - " + profesionalModel.getDireccion());
            profesionalModel.setIcono(R.drawable.businessman);
        }

        return profesionalModel;
    }

    //20 crear metodo editar(int id)
    private void editarProfesional(int id) {
        db = adminDB.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Nombre", edtNombre.getText().toString());
        values.put("Profesion", spnProfesion.getSelectedItem().toString());
        values.put("Telefono", edtTelefono.getText().toString());
        values.put("Celular", edtCelular.getText().toString());
        values.put("Correo", edtCorreo.getText().toString());
        values.put("Direccion", edtDireccion.getText().toString());

        //21 modificamos la variable id por result y la consulta a update
        long result = db.update("Profesionales", values, "ID_Profesionales = " + id, null);
        if (id > 0) {
            MostrarMensaje("Profesional Modificado");
        } else {
            MostrarMensaje("Error al Modificar Datos");
        }
        db.close();

    }

    private void insertToDB() {

        ContentValues values = new ContentValues();
        values.put("Nombre", edtNombre.getText().toString());
        values.put("Profesion", spnProfesion.getSelectedItem().toString());
        values.put("Telefono", edtTelefono.getText().toString());
        values.put("Celular", edtCelular.getText().toString());
        values.put("Correo", edtCorreo.getText().toString());
        values.put("Direccion", edtDireccion.getText().toString());


        long id = db.insert("Profesionales", null, values);
        if (id > 0) {
            MostrarMensaje("Profesional Agregado");
        } else {
            MostrarMensaje("Error al Ingresar Datos");
        }
        db.close();
    }

    private void MostrarMensaje(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

    public boolean validarCampos() {
        boolean esValidado = true;

        if (edtNombre.getText().toString().isEmpty()) {
            esValidado = false;
            MostrarMensaje("El nombre del profesional es requerido");
        } else if (edtCelular.getText().toString().isEmpty()) {
            esValidado = false;
            MostrarMensaje("El número de celular del profesional es requerido");
        } else if (edtDireccion.getText().toString().isEmpty()) {
            esValidado = false;
            MostrarMensaje("La dirección del profesional es requerida");
        }

        return esValidado;
    }
}