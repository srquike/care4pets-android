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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AlimentosFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private NavController navController;
    private ControladorBD adminDB;
    private SQLiteDatabase db;
    private AlimentosAdapter adapter;

    public AlimentosFragment() {

    }

    public static AlimentosFragment newInstance(String param1, String param2) {
        AlimentosFragment fragment = new AlimentosFragment();
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
        db = adminDB.getReadableDatabase();
        View view = inflater.inflate(R.layout.fragment_alimentos, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rcrMascotas);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nuevoAlimentoFragment);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AlimentosAdapter(obtenerListaAlimentos());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private ArrayList<AlimentoModel> obtenerListaAlimentos() {
        ArrayList<AlimentoModel> lstAlimentos = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Alimentos", null);

        if (cursor.moveToFirst()) {
            do {
                AlimentoModel alimentoModel = new AlimentoModel();
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
                lstAlimentos.add(alimentoModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lstAlimentos;
    }

    private void eliminarAlimento(int mascotaId, int posicion) {
        db = adminDB.getWritableDatabase();
        int registrosEliminados = db.delete("Alimentos", "ID_comida = " + mascotaId, null);

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
                AlimentoModel alimentoModel = adapter.lstAlimentos.get(item.getGroupId());
                eliminarAlimento(alimentoModel.getId(), item.getGroupId());
                break;
        }

        return super.onContextItemSelected(item);

    }

}