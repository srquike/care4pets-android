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

public class ProfesionalesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ArrayList<ProfesionalModel> lstProfesionales;
    private RecyclerView rcvProfesionales;
    private NavController navController;

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
        View view = inflater.inflate(R.layout.fragment_profesionales, container, false);

        lstProfesionales = new ArrayList<>();
        rcvProfesionales = (RecyclerView) view.findViewById(R.id.rcrProfesionales);

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navController.navigate(R.id.nuevaMascotaFragment);
            }
        });

        rcvProfesionales.setLayoutManager(new LinearLayoutManager(getContext()));

        LlenarLista();

        ProfesionalesAdapter adapter = new ProfesionalesAdapter(lstProfesionales);

        rcvProfesionales.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private void LlenarLista() {

        lstProfesionales.add(new ProfesionalModel("Profesional 1", "Descripcion de profesional 1", R.drawable.businessman));
        lstProfesionales.add(new ProfesionalModel("Profesional 5", "Descripcion de profesional 5", R.drawable.businessman));
        lstProfesionales.add(new ProfesionalModel("Profesional 6", "Descripcion de profesional 6", R.drawable.businessman));
        lstProfesionales.add(new ProfesionalModel("Profesional 7", "Descripcion de profesional 7", R.drawable.businessman));
        lstProfesionales.add(new ProfesionalModel("Profesional 3", "Descripcion de profesional 3", R.drawable.businessman));
        lstProfesionales.add(new ProfesionalModel("Profesional 4", "Descripcion de profesional 4", R.drawable.businessman));
        lstProfesionales.add(new ProfesionalModel("Profesional 2", "Descripcion de profesional 2", R.drawable.businessman));
    }
}