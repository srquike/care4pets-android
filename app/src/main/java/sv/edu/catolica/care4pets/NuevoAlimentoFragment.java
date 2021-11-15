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

public class NuevoAlimentoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText spnNombre,spnNotas;
    private Spinner spnTipoAlimento,spnUnidad;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ControladorBD adminDB;
    SQLiteDatabase db;

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

        adminDB = new ControladorBD(getContext(),"DBCare4Pets",null,1);
        db = adminDB.getWritableDatabase();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_nuevo_alimento, container, false);

        spnNombre = vista.findViewById(R.id.edtNombre);
        spnNotas = vista.findViewById(R.id.edtNotas);
        spnTipoAlimento = vista.findViewById(R.id.spnTipoAlimento);
        spnUnidad = vista.findViewById(R.id.spnUnidad);



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
        //values.put("ID_comida",1);
        values.put("Nombre", spnNombre.getText().toString());
        values.put("Unidad",spnUnidad.getSelectedItem().toString());
        values.put("TipoComida",spnTipoAlimento.getSelectedItem().toString());
        values.put("Notas", spnNotas.getText().toString());

        long id = db.insert("Alimentos",null,values);
        if (id>0){
            MostrarMensaje("Alimento Agregado");
        }else{
            MostrarMensaje("Error al Ingresar Datos");
        }
        LimpiarCasillas();
        db.close();
    }

    private void LimpiarCasillas() {
        spnNombre.setText("");
        spnNotas.setText("");

    }

    private void MostrarMensaje(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
    public void onBackPressed() {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

}