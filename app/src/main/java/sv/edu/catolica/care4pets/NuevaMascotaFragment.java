package sv.edu.catolica.care4pets;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class NuevaMascotaFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private EditText edtFechaEsterilizacion, edtNombre, edtRaza, edtColor;
    private EditText edtFechaNacimiento;
    private ImageView imvFechaNacimiento, imvFechaEsterilizacion;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    //private String fecha;
    private Spinner spnSexo, spnEspecie;
    private RadioButton rbSi,rbNo;




    ControladorBD adminDB;
    SQLiteDatabase db;

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

        adminDB = new ControladorBD(getContext(),"DBCare4Pets",null,1);
        db = adminDB.getWritableDatabase();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_or_cancel_menu, menu);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_nueva_mascota, container, false);

        edtFechaNacimiento = vista.findViewById(R.id.edtFechaNacimiento);
        edtFechaEsterilizacion = vista.findViewById(R.id.edtFechaEsterilizacion);
        imvFechaNacimiento = vista.findViewById(R.id.imVFechaNacimiento);
        imvFechaEsterilizacion = vista.findViewById(R.id.imVFechaEsterilizacion);

        //aqui se inician las variables
        edtNombre = vista.findViewById(R.id.txtNombre);
        spnSexo = vista.findViewById(R.id.spnSexo);
        spnEspecie = vista.findViewById(R.id.spnEspecie);
        edtRaza = vista.findViewById(R.id.txtRaza);
        edtColor = vista.findViewById(R.id.txtColor);
        rbSi = vista.findViewById(R.id.rbSi);
        rbNo = vista.findViewById(R.id.rbNo);


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

        return vista;
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
                } else if (editText.equals((edtFechaEsterilizacion))){
                    edtFechaEsterilizacion.setText(dayOfMonth + "/" + month + "/" + year);
                }
            }
        }, year, mes, dia);

        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.btnAceptar:
                insertToDB();

                break;
            case R.id.btnCancelar:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }


    private void insertToDB(){

        ContentValues values = new ContentValues();
        //values.put("ID_pet",5);
        values.put("Nombre", edtNombre.getText().toString());
        values.put("Raza",edtRaza.getText().toString());
        values.put("Sexo",spnSexo.getSelectedItem().toString());
        values.put("Especie",spnEspecie.getSelectedItem().toString());
        values.put("Color",edtColor.getText().toString());
        values.put("FechaNaci",edtFechaNacimiento.getText().toString());
        values.put("FechaEsterilizacion",edtFechaEsterilizacion.getText().toString());
        if (rbSi.isChecked()){
            values.put("Esterilizacion",true);
        }else if(rbNo.isChecked()){
            values.put("Esterilizacion",false);
        }


        long id = db.insert("Mascotas",null,values);
        if (id>0){
            MostrarMensaje("Mascota Agregada");
        }else{
            MostrarMensaje("Error al Ingresar Datos");
        }
        LimpiarCasillas();
        db.close();
    }

    private void LimpiarCasillas() {
        edtNombre.setText("");
        edtRaza.setText("");
        edtColor.setText("");
        edtFechaNacimiento.setText("");
        edtFechaEsterilizacion.setText("");

        
    }

    private void MostrarMensaje(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /*public void BuscarMascota(){
        db = adminDB.getWritableDatabase();

        //String codMascota = edtNombre.getText().toString();
        String sql ="SELECT * FROM Mascotas";
        Cursor fila = db.rawQuery(sql,null);
        if (fila.moveToFirst()){
            edtNombre.setText(fila.getString(0));
        }else {
            MostrarMensaje("Error");
        }
    }*/



}