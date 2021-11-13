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
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;

public class MascotasFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private ArrayList<MascotaModel> lstMascotas;
    private NavController navController;

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

        View vistaMascotas = inflater.inflate(R.layout.fragment_mascotas, container, false);

        lstMascotas = new ArrayList<>();
        recyclerView = (RecyclerView) vistaMascotas.findViewById(R.id.rcrMascotas);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton floatingActionButton = (FloatingActionButton) vistaMascotas.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nuevaMascotaFragment);
            }
        });

        LlenarLista();

        MascotasAdapter adapter = new MascotasAdapter(lstMascotas);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Se seleccionó: " + lstMascotas.get(recyclerView.getChildAdapterPosition(v)).getNombre(), Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(adapter);

        return vistaMascotas;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private void LlenarLista() {

        lstMascotas.add(new MascotaModel("Mascota 1", "Descripcion de mascota 1", R.drawable.pet, false));
        lstMascotas.add(new MascotaModel("Mascota 2", "Descripcion de mascota 2", R.drawable.pet, true));
        lstMascotas.add(new MascotaModel("Mascota 3", "Descripcion de mascota 3", R.drawable.pet, true));
        lstMascotas.add(new MascotaModel("Mascota 4", "Descripcion de mascota 4", R.drawable.pet, false));
        lstMascotas.add(new MascotaModel("Mascota 5", "Descripcion de mascota 5", R.drawable.pet, false));
        lstMascotas.add(new MascotaModel("Mascota 6", "Descripcion de mascota 6", R.drawable.pet, true));
        lstMascotas.add(new MascotaModel("Mascota 7", "Descripcion de mascota 7", R.drawable.pet, true));
    }
}