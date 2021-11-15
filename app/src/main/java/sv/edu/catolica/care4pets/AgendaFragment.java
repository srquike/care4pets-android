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
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AgendaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView rcvEventos;
    private NavController navController;
    private ControladorBD adminDB;
    private SQLiteDatabase db;

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
        db = adminDB.getReadableDatabase();
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

        EventosAdapter adapter = new EventosAdapter(obtenerListaEventos());
        rcvEventos.setAdapter(adapter);

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private ArrayList<EventoModel> obtenerListaEventos() {
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
}