package sv.edu.catolica.care4pets;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class MascotasFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private NavController navController;
    private ControladorBD adminDB;
    private SQLiteDatabase db;
    private MascotasAdapter adapter;

    public MascotasFragment() {

    }

    public static MascotasFragment newInstance(String param1, String param2) {
        MascotasFragment fragment = new MascotasFragment();
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

        View vistaMascotas = inflater.inflate(R.layout.fragment_mascotas, container, false);
        recyclerView = (RecyclerView) vistaMascotas.findViewById(R.id.rcrMascotas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FloatingActionButton floatingActionButton = (FloatingActionButton) vistaMascotas.findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nuevaMascotaFragment);
            }
        });

        adapter = new MascotasAdapter(obtenerListaMascotas());
        recyclerView.setAdapter(adapter);

        return vistaMascotas;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private ArrayList<MascotaModel> obtenerListaMascotas() {
        db = adminDB.getReadableDatabase();
        ArrayList<MascotaModel> lstMascotas = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Mascotas", null);
        MascotaModel mascotaModel = null;

        if (cursor.moveToFirst()) {
            do {

                mascotaModel = new MascotaModel();
                mascotaModel.setId(cursor.getInt(0));
                mascotaModel.setNombre(cursor.getString(1));
                mascotaModel.setRaza(cursor.getString(2));
                mascotaModel.setSexo(cursor.getString(3));
                mascotaModel.setEspecie(cursor.getString(4));
                mascotaModel.setColor(cursor.getString(5));
                mascotaModel.setFechaNacimiento(LocalDate.parse(cursor.getString(6), DateTimeFormatter.ofPattern("d/M/yyyy")));
                mascotaModel.setEsterilizacion(Boolean.parseBoolean(cursor.getString(7)));
                mascotaModel.setFechaEsterilizacion(LocalDate.parse(cursor.getString(8), DateTimeFormatter.ofPattern("d/M/yyyy")));
                mascotaModel.setFoto(R.drawable.pet);
                mascotaModel.setDescripcion(mascotaModel.getRaza() + " - " + mascotaModel.getEspecie() + " - " + mascotaModel.getColor() + " - " + mascotaModel.getSexo());

                lstMascotas.add(mascotaModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return  lstMascotas;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 121:
                MascotaModel mascotaModel = adapter.lstMasctoas.get(item.getGroupId());

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirmar eliminación");
                builder.setMessage("¿Desea eliminar a " + mascotaModel.getNombre() + " de la lista de mascotas?");

                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarMascota(mascotaModel.getId(), item.getGroupId());
                    }
                });

                builder.setNegativeButton("No", null);
                builder.create();
                builder.show();

                break;
        }

        return super.onContextItemSelected(item);
    }

    private void eliminarMascota(int mascotaId, int posicion) {
        db = adminDB.getWritableDatabase();
        int registrosEliminados = db.delete("Mascotas", "ID_pet = " + mascotaId, null);

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
}