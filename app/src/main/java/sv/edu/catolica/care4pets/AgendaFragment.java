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
import android.support.v7.widget.AlertDialogLayout;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AgendaFragment extends Fragment implements EventosAdapter.OnEventosListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView rcvEventos;
    private NavController navController;
    private ControladorBD adminDB;
    private SQLiteDatabase db;
    private EventosAdapter adapter;

    public AgendaFragment() {

    }

    public static AgendaFragment newInstance(String param1, String param2) {
        AgendaFragment fragment = new AgendaFragment();
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

        View vista = inflater.inflate(R.layout.fragment_agenda, container, false);
        rcvEventos = (RecyclerView) vista.findViewById(R.id.rcvEventos);
        rcvEventos.setLayoutManager(new LinearLayoutManager(getContext()));
        FloatingActionButton floatingActionButton = (FloatingActionButton) vista.findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nuevoEventoFragment);
            }
        });
//10paso put this
        adapter = new EventosAdapter(obtenerListaEventos(), this);
        rcvEventos.setAdapter(adapter);

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private ArrayList<EventoModel> obtenerListaEventos() {
        db = adminDB.getReadableDatabase();
        ArrayList<EventoModel> lstEventos = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Evento", null);
        EventoModel eventoModel = null;

        if (cursor.moveToFirst()) {
            do {
                eventoModel = new EventoModel();
                eventoModel.setId(cursor.getInt(0));
                eventoModel.setNombre(cursor.getString(1));
                eventoModel.setFecha(LocalDate.parse(cursor.getString(2), DateTimeFormatter.ofPattern("d/M/yyyy")));
                eventoModel.setHora(LocalTime.parse(cursor.getString(3), DateTimeFormatter.ofPattern("HH:mm a")));
                eventoModel.setTipoEvento(cursor.getString(4));
                eventoModel.setDescripcion(cursor.getString(5));
                eventoModel.setIcono(R.drawable.calendar);

                lstEventos.add(eventoModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lstEventos;
    }

    private void eliminarEvento(int mascotaId, int posicion) {
        db = adminDB.getWritableDatabase();
        int registrosEliminados = db.delete("Evento", "ID_Evento = " + mascotaId, null);

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
                //MensajeConfirmacion();
                EventoModel eventoModel = adapter.lstEventos.get(item.getGroupId());

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirmar eliminación");
                builder.setMessage("¿Desea eliminar a " + eventoModel.getNombre() + " de la lista de Eventos?");

                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarEvento(eventoModel.getId(), item.getGroupId());
                    }
                });

                builder.setNegativeButton("No", null);
                builder.create();
                builder.show();

                break;
        }

        return super.onContextItemSelected(item);

    }
//2do paso
    @Override
    public void onEventosClick(int posicion) {
        //11 paso
        Bundle bundle = new Bundle();
        bundle.putInt("Id",adapter.lstEventos.get(posicion).getId());
        navController.navigate(R.id.nuevoEventoFragment, bundle);

    }

   /* public void MensajeConfirmacion(){
        AlertDialog.Builder window= new AlertDialog.Builder(getContext());
        window.setCancelable(false);
        window.setMessage("¿Estas Seguro de eliminar este registro?");
        window.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        window.setNegativeButton("No",null);
        window.create();
        window.show();

    }*/

}