package sv.edu.catolica.care4pets;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;

public class ProfesionalesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView rcvProfesionales;
    private NavController navController;
    private ControladorBD adminDB;
    private SQLiteDatabase db;
    private ProfesionalesAdapter adapter;

    public ProfesionalesFragment() {

    }

    public static ProfesionalesFragment newInstance(String param1, String param2) {
        ProfesionalesFragment fragment = new ProfesionalesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adminDB = new ControladorBD(getContext(),"DBCare4Pets",null,1);

        View view = inflater.inflate(R.layout.fragment_profesionales, container, false);
        rcvProfesionales = (RecyclerView) view.findViewById(R.id.rcrProfesionales);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nuevoProfesionalFragment);
            }
        });

        rcvProfesionales.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProfesionalesAdapter(obtenerListaProfesionales());
        rcvProfesionales.setAdapter(adapter);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    public ArrayList<ProfesionalModel> obtenerListaProfesionales() {
        db = adminDB.getReadableDatabase();
        ArrayList<ProfesionalModel> lstProfesionales = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Profesionales", null);
        ProfesionalModel profesionalModel = null;

        if (cursor.moveToFirst()) {
            do {
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

                lstProfesionales.add(profesionalModel);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return lstProfesionales;
    }

    private void eliminarProfesional(int profesionalId, int posicion) {
        db = adminDB.getWritableDatabase();
        int registrosEliminados = db.delete("Profesionales", "ID_Profesionales = " + profesionalId, null);

        if (registrosEliminados > 0) {
            adapter.eliminarElementoSeleccionado(posicion);
        } else {
            MostrarMensaje("No se pudo eliminar");
        }

        db.close();
    }

    private void MostrarMensaje(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 121:
                ProfesionalModel profesionalModel = adapter.lstProfesionales.get(item.getGroupId());
                eliminarProfesional(profesionalModel.getId(), item.getGroupId());
                break;
        }
        return super.onContextItemSelected(item);
    }
}