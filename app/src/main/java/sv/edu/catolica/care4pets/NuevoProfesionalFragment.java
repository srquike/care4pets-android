package sv.edu.catolica.care4pets;

import android.content.ContentValues;
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

public class NuevoProfesionalFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private EditText edtNombre, edtTelefono, edtCelular, edtCorreo, edtDireccion;
    private Spinner spnProfesion;

    ControladorBD adminDB;
    SQLiteDatabase db;

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

        adminDB = new ControladorBD(getContext(),"DBCare4Pets",null,1);
        db = adminDB.getWritableDatabase();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_or_cancel_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.btnAceptar:
                insertToDB();
                onBackPressed();
                break;
            case R.id.btnCancelar:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nuevo_profesional, container, false);

        edtNombre = view.findViewById(R.id.txtNombre);
        edtTelefono = view.findViewById(R.id.txtTelefono);
        edtCelular = view.findViewById(R.id.txtCelular);
        edtCorreo = view.findViewById(R.id.txtCorreo);
        edtDireccion = view.findViewById(R.id.txtDireccion);
        spnProfesion = view.findViewById(R.id.spnProfesion);


        return view;
    }

    private void insertToDB(){

        ContentValues values = new ContentValues();
        values.put("ID_Profesionales",1);
        values.put("Nombre", edtNombre.getText().toString());
        values.put("Profesion", spnProfesion.getSelectedItem().toString());
        values.put("Telefono",edtTelefono.getText().toString());
        values.put("Celular",edtCelular.getText().toString());
        values.put("Correo",edtCorreo.getText().toString());
        values.put("Direccion",edtDireccion.getText().toString());


        long id = db.insert("Profesionales",null,values);
        if (id>0){
            MostrarMensaje("Profesional Agregado");
        }else{
            MostrarMensaje("Error al Ingresar Datos");
        }
        LimpiarCasillas();
        db.close();
    }

    private void LimpiarCasillas() {
        edtNombre.setText("");
        edtTelefono.setText("");
        edtCorreo.setText("");
        edtCelular.setText("");
        edtDireccion.setText("");
    }

    private void MostrarMensaje(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

}