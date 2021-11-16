package sv.edu.catolica.care4pets;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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


        return vista;
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
}