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
import android.widget.Spinner;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;

public class NuevoMedicamentoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private EditText edtNombre, edtCantidad, edtLaboratorio, edtNotas, edtFechaVencimiento;
    private Spinner spnPresentacion, spnUnidad;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private ImageView imvFechaVencimiento;
    private ControladorBD adminDB;
    private SQLiteDatabase db;
    private int medicamentoId;

    public NuevoMedicamentoFragment() {

    }

    public static NuevoMedicamentoFragment newInstance(String param1, String param2) {
        NuevoMedicamentoFragment fragment = new NuevoMedicamentoFragment();
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
        if (getArguments()== null){
            inflater.inflate(R.menu.save_or_cancel_menu, menu);
        }else{
            inflater.inflate(R.menu.save_changes_menu, menu);
        }

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
                editarMedicamento(medicamentoId);
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adminDB = new ControladorBD(getContext(), "DBCare4Pets", null, 1);
        db = adminDB.getWritableDatabase();
        View view = inflater.inflate(R.layout.fragment_nuevo_medicamento, container, false);
        Bundle bundle = getArguments();
        edtNombre = view.findViewById(R.id.txtNombre);
        edtCantidad = view.findViewById(R.id.txtCantidad);
        edtLaboratorio = view.findViewById(R.id.txtLaboratorio);
        spnUnidad = view.findViewById(R.id.spnUnidad);
        edtFechaVencimiento = view.findViewById(R.id.edtFechaVencimiento);
        imvFechaVencimiento = view.findViewById(R.id.imvFechaVencimiento);
        spnPresentacion = view.findViewById(R.id.spnPresentacion);
        edtNotas = view.findViewById(R.id.txtNotas);

        imvFechaVencimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendario(edtFechaVencimiento);
            }
        });
        llenarCampos(bundle);

        return view;
    }
    private void llenarCampos(Bundle bundle) {
        //18 se crea el if(bundle)
        if (bundle != null) {
            medicamentoId = bundle.getInt("Id");
            MedicamentoModel medicamentoModel = obtenerMedicamentoPorId(medicamentoId);

            if (medicamentoModel != null){
                edtNombre.setText(medicamentoModel.getNombre());
                spnPresentacion.setSelection(Arrays.asList(getResources().getStringArray(R.array.tiposMedicamentos)).indexOf(medicamentoModel.getPresentacion()));
                edtCantidad.setText(Double.toString(medicamentoModel.getCantidad()));
                spnUnidad.setSelection(Arrays.asList(getResources().getStringArray(R.array.tiposMedicamentos)).indexOf(medicamentoModel.getUnidad()));
                edtFechaVencimiento.setText(medicamentoModel.getFechaVencimiento().format(DateTimeFormatter.ofPattern("d/M/yyyy")));
                edtLaboratorio.setText(medicamentoModel.getLaboratorio());
                edtNotas.setText(medicamentoModel.getNotas());

                MainActivity mainActivity= (MainActivity) getActivity();
                mainActivity.getSupportActionBar().setTitle(medicamentoModel.getNombre());

            }
        }
    }
    private MedicamentoModel obtenerMedicamentoPorId(int id){
        db = adminDB.getReadableDatabase();
        MedicamentoModel medicamentoModel = null;
        Cursor cursor = db.rawQuery("SELECT * FROM Medicamentos WHERE ID_Medicamento = "+ id +" LIMIT 1",null);

        if (cursor.moveToFirst()){
            medicamentoModel = new MedicamentoModel();
            medicamentoModel.setId(cursor.getInt(0));
            medicamentoModel.setNombre(cursor.getString(1));
            medicamentoModel.setNotas(cursor.getString(2));
            medicamentoModel.setPresentacion(cursor.getString(3));
            medicamentoModel.setCantidad(Double.parseDouble(cursor.getString(4)));
            medicamentoModel.setUnidad(cursor.getString(5));
            medicamentoModel.setFechaVencimiento(LocalDate.parse(cursor.getString(6), DateTimeFormatter.ofPattern("d/M/yyyy")));
            medicamentoModel.setLaboratorio(cursor.getString(7));
            medicamentoModel.setIcono(R.drawable.first_aid);
            medicamentoModel.setDescripcion(medicamentoModel.getPresentacion() + " - " + medicamentoModel.getCantidad() + " " + medicamentoModel.getUnidad() + " - " + medicamentoModel.getFechaVencimiento());
        }

        return medicamentoModel;
    }
    private void editarMedicamento(int id){
        db = adminDB.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Nombre", edtNombre.getText().toString());
        values.put("Presentacion", spnPresentacion.getSelectedItem().toString());
        values.put("Cantidad", edtCantidad.getText().toString());
        values.put("Unidad", spnUnidad.getSelectedItem().toString());
        values.put("FechaVencimiento", edtFechaVencimiento.getText().toString());
        values.put("Laboratorio", edtLaboratorio.getText().toString());
        values.put("notas", edtNotas.getText().toString());

        long result = db.update("Medicamentos",values, "ID_Medicamento = "+ id,null);
        if (id > 0) {
            MostrarMensaje("Medicamento Modificado");
        } else {
            MostrarMensaje("Error al modificar Datos");
        }

        db.close();
    }

    private void abrirCalendario(EditText editText) {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (editText.equals(edtFechaVencimiento)) {
                    edtFechaVencimiento.setText(dayOfMonth + "/" + month + "/" + year);
                }
            }
        }, year, mes, dia);

        datePickerDialog.show();
    }


    private void insertToDB() {

        ContentValues values = new ContentValues();
        values.put("Nombre", edtNombre.getText().toString());
        values.put("Presentacion", spnPresentacion.getSelectedItem().toString());
        values.put("Cantidad", edtCantidad.getText().toString());
        values.put("Unidad", spnUnidad.getSelectedItem().toString());
        values.put("FechaVencimiento", edtFechaVencimiento.getText().toString());
        values.put("Laboratorio", edtLaboratorio.getText().toString());
        values.put("notas", edtNotas.getText().toString());

        long id = db.insert("Medicamentos", null, values);
        if (id > 0) {
            MostrarMensaje("Medicamento Agregado");
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

    public void ValidarCampos(){
        if (edtNombre.equals("")){
            edtNombre.setText("Ingrese nombre");
        }else if (edtCantidad.equals(null)){
            edtCantidad.setText(0);
        }else if (edtFechaVencimiento.equals("")){
            edtFechaVencimiento.setText("Ingrese Fecha");
        }else if (edtLaboratorio.equals("")){
            edtLaboratorio.setText("Ingrese Laboratorio");
        }else {}

    }
}