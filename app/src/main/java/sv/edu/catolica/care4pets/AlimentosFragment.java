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

public class AlimentosFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private ArrayList<AlimentoModel> lstAlimentos;
    private NavController navController;

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

        View view = inflater.inflate(R.layout.fragment_alimentos, container, false);

        lstAlimentos = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.rcrMascotas);

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nuevoAlimentoFragment);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LlenarLista();

        AlimentosAdapter adapter = new AlimentosAdapter(lstAlimentos);

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private void LlenarLista() {

        lstAlimentos.add(new AlimentoModel("Alimento 1", "Descripcion de alimento 1", R.drawable.petfood));
        lstAlimentos.add(new AlimentoModel("Alimento 2", "Descripcion de alimento 2", R.drawable.petfood));
        lstAlimentos.add(new AlimentoModel("Alimento 3", "Descripcion de alimento 3", R.drawable.petfood));
        lstAlimentos.add(new AlimentoModel("Alimento 4", "Descripcion de alimento 4", R.drawable.petfood));
        lstAlimentos.add(new AlimentoModel("Alimento 5", "Descripcion de alimento 5", R.drawable.petfood));
        lstAlimentos.add(new AlimentoModel("Alimento 6", "Descripcion de alimento 6", R.drawable.petfood));
        lstAlimentos.add(new AlimentoModel("Alimento 7", "Descripcion de alimento 7", R.drawable.petfood));
    }
}