package sv.edu.catolica.care4pets;

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

import java.util.ArrayList;

public class AgendaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView rcvEventos;
    private ArrayList<EventoModel> lstEventos;
    private NavController navController;

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

        View vista = inflater.inflate(R.layout.fragment_agenda, container, false);
        rcvEventos = (RecyclerView) vista.findViewById(R.id.rcvEventos);
        lstEventos = new ArrayList<>();
        rcvEventos.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton floatingActionButton = (FloatingActionButton) vista.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nuevoEventoFragment);
            }
        });

        LlenarLista();

        EventosAdapter adapter = new EventosAdapter(lstEventos);
        rcvEventos.setAdapter(adapter);

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private void LlenarLista() {

        lstEventos.add(new EventoModel("Evento 1", "Descripcion de evento 1", R.drawable.calendar));
        lstEventos.add(new EventoModel("Evento 2", "Descripcion de evento 2", R.drawable.calendar));
        lstEventos.add(new EventoModel("Evento 3", "Descripcion de evento 3", R.drawable.calendar));
        lstEventos.add(new EventoModel("Evento 4", "Descripcion de evento 4", R.drawable.calendar));
        lstEventos.add(new EventoModel("Evento 5", "Descripcion de evento 5", R.drawable.calendar));
        lstEventos.add(new EventoModel("Evento 6", "Descripcion de evento 6", R.drawable.calendar));
        lstEventos.add(new EventoModel("Evento 7", "Descripcion de evento 7", R.drawable.calendar));
    }
}