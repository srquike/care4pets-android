package sv.edu.catolica.care4pets;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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

public class NuevoAlimentoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText edtNombre, edtNotas, edtCantidad, edtPresentacion, edtMarca, edtPrecio, edtFechaVencimiento;
    private Spinner spnTipoAlimento,spnUnidad;
    private String mParam1;
    private String mParam2;
    private ControladorBD adminDB;
    private SQLiteDatabase db;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private ImageView imvFechaVencimiento;
    private int alimentoId;

    public NuevoAlimentoFragment() {

    }

    public static NuevoAlimentoFragment newInstance(String param1, String param2) {
        NuevoAlimentoFragment fragment = new NuevoAlimentoFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adminDB = new ControladorBD(getContext(),"DBCare4Pets",null,1);
        db = adminDB.getWritableDatabase();
        View vista = inflater.inflate(R.layout.fragment_nuevo_alimento, container, false);
        Bundle bundle = getArguments();
        edtNombre = vista.findViewById(R.id.edtNombre);
        edtNotas = vista.findViewById(R.id.edtNotas);
        edtPresentacion = vista.findViewById(R.id.edtPresentaciónAlimento);
        edtMarca = vista.findViewById(R.id.edtMarcaAlimento);
        edtPrecio = vista.findViewById(R.id.edtPrecioAlimento);
        edtFechaVencimiento = vista.findViewById(R.id.edtFechaVencimientoAlimento);
        edtCantidad = vista.findViewById(R.id.edtCantidadAlimento);
        spnTipoAlimento = vista.findViewById(R.id.spnTipoAlimento);
        spnUnidad = vista.findViewById(R.id.spnUnidadAlimento);
        imvFechaVencimiento = vista.findViewById(R.id.imvFechaVencimientoAlimento);

        imvFechaVencimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendario();
            }
        });

        llenarCampos(bundle);

        return vista;
    }

    private void llenarCampos(Bundle bundle) {
        if (bundle != null) {
            alimentoId = bundle.getInt("Id");
            AlimentoModel alimentoModel = obtenerAlimentoPorId(alimentoId);

            if (alimentoModel != null) {
                edtNombre.setText(alimentoModel.getNombre());
                edtCantidad.setText(Double.toString(alimentoModel.getCantidad()));
                spnUnidad.setSelection(Arrays.asList(getResources().getStringArray(R.array.unidades)).indexOf(alimentoModel.getUnidad()));
                spnTipoAlimento.setSelection(Arrays.asList(getResources().getStringArray(R.array.tiposAlimentos)).indexOf(alimentoModel.getTipoComida()));
                edtPresentacion.setText(alimentoModel.getPresentacion());
                edtMarca.setText(alimentoModel.getMarca());
                edtPrecio.setText(Double.toString(alimentoModel.getPrecio()));
                edtFechaVencimiento.setText(alimentoModel.getFechaVencimiento().format(DateTimeFormatter.ofPattern("d/M/yyyy")));
                edtNotas.setText(alimentoModel.getNotas());

                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.getSupportActionBar().setTitle(alimentoModel.getNombre());
            }
        }
    }
    private AlimentoModel obtenerAlimentoPorId(int id){
        db = adminDB.getReadableDatabase();
        AlimentoModel alimentoModel =null;
        Cursor cursor = db.rawQuery("SELECT * FROM Alimentos WHERE ID_comida = " + id + " LIMIT 1", null);

        if (cursor.moveToFirst()){
            alimentoModel  = new AlimentoModel();
            alimentoModel.setId(cursor.getInt(0));
            alimentoModel.setNombre(cursor.getString(1));
            alimentoModel.setCantidad(cursor.getDouble(2));
            alimentoModel.setFechaVencimiento(LocalDate.parse(cursor.getString(3), DateTimeFormatter.ofPattern("d/M/yyyy")));
            alimentoModel.setNotas(cursor.getString(4));
            alimentoModel.setTipoComida(cursor.getString(5));
            alimentoModel.setUnidad(cursor.getString(6));
            alimentoModel.setPresentacion(cursor.getString(7));
            alimentoModel.setMarca(cursor.getString(8));
            alimentoModel.setPrecio(cursor.getDouble(9));
            alimentoModel.setIcono(R.drawable.petfood);
            alimentoModel.setDescripcion(alimentoModel.getMarca() + " - " + alimentoModel.getTipoComida() + " - " + alimentoModel.getCantidad() + " " + alimentoModel.getUnidad());

        }

        return alimentoModel;
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

        switch (item.getItemId()){
            case R.id.btnAceptar:
                insertToDB();
                onBackPressed();
                break;
            case R.id.btnCancelar:
                onBackPressed();
                break;
            case R.id.btnGuardarCambios:
                editarAlimento(alimentoId);
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertToDB(){

        ContentValues values = new ContentValues();
        values.put("Nombre", edtNombre.getText().toString());
        values.put("Unidad", spnUnidad.getSelectedItem().toString());
        values.put("TipoComida",spnTipoAlimento.getSelectedItem().toString());
        values.put("Notas", edtNotas.getText().toString());
        values.put("Cantidad", Double.parseDouble(edtCantidad.getText().toString()));
        values.put("Fecha_vencimiento", edtFechaVencimiento.getText().toString());
        values.put("Presentacion", edtPresentacion.getText().toString());
        values.put("Marca", edtMarca.getText().toString());
        values.put("Precio", Double.parseDouble(edtPrecio.getText().toString()));

        long id = db.insert("Alimentos",null,values);
        if (id>0){
            MostrarMensaje("Alimento Agregado");
        }else{
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

    private void abrirCalendario() {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                edtFechaVencimiento.setText(dayOfMonth + "/" + month + "/" + year);
            }
        }, year, mes, dia);

        datePickerDialog.show();
    }

    private void editarAlimento(int id){
        db = adminDB.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Nombre", edtNombre.getText().toString());
        values.put("Unidad", spnUnidad.getSelectedItem().toString());
        values.put("TipoComida",spnTipoAlimento.getSelectedItem().toString());
        values.put("Notas", edtNotas.getText().toString());
        values.put("Cantidad", Double.parseDouble(edtCantidad.getText().toString()));
        values.put("Fecha_vencimiento", edtFechaVencimiento.getText().toString());
        values.put("Presentacion", edtPresentacion.getText().toString());
        values.put("Marca", edtMarca.getText().toString());
        values.put("Precio", Double.parseDouble(edtPrecio.getText().toString()));

        long result = db.update("Alimentos",values, "ID_comida = "+ id,null);
        if (id>0){
            MostrarMensaje("Alimento Modificado");
        }else{
            MostrarMensaje("Error al modificar Datos");
        }

        db.close();

    }
}