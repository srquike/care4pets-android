package sv.edu.catolica.care4pets;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NuevoMedicamentoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    private EditText edtNombre,edtCantidad, edtLaboratorio,edtNotas, edtFechaVencimiento;
    private Spinner spnPresentacion, spnUnidad;


    ControladorBD adminDB;
    SQLiteDatabase db;

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

        View view = inflater.inflate(R.layout.fragment_nuevo_medicamento, container, false);

        edtNombre = view.findViewById(R.id.txtNombre);
        edtCantidad = view.findViewById(R.id.txtCantidad);
        edtLaboratorio = view.findViewById(R.id.txtLaboratorio);
        edtFechaVencimiento = view.findViewById(R.id.edtFechaVencimiento);


        return view;
    }

    private void insertToDB(){

        ContentValues values = new ContentValues();
        values.put("ID_Medicamento",1);
        values.put("Nombre", edtNombre.getText().toString());
        values.put("Presentacion",spnPresentacion.getSelectedItem().toString());
        values.put("Cantidad",edtCantidad.getText().toString());
        values.put("Unidad",spnUnidad.getSelectedItem().toString());
        values.put("FechaVencimiento",edtFechaVencimiento.getText().toString());
        values.put("Laboratorio",edtLaboratorio.getText().toString());
        values.put("notas",edtNotas.getText().toString());



        long id = db.insert("Medicamentos",null,values);
        if (id>0){
            MostrarMensaje("Medicamento Agregado");
        }else{
            MostrarMensaje("Error al Ingresar Datos");
        }
        LimpiarCasillas();
        db.close();
    }

    private void LimpiarCasillas() {
        edtNombre.setText("");
        edtCantidad.setText("");
        edtFechaVencimiento.setText("");
        edtLaboratorio.setText("");
        edtNotas.setText("");


    }

    private void MostrarMensaje(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


}