package sv.edu.catolica.care4pets;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MascotasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MascotasFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private ArrayList<MascotaViewHolder> lstMascotas;
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

        FloatingActionButton floatingActionButton = (FloatingActionButton) vistaMascotas.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nuevaMascotaFragment);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LlenarLista();

        MascotasAdapter adapter = new MascotasAdapter(lstMascotas);

        recyclerView.setAdapter(adapter);
        return vistaMascotas;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private void LlenarLista() {

        lstMascotas.add(new MascotaViewHolder("Mascota 1", "Descripcion de mascota 1", R.drawable.pet, false));
        lstMascotas.add(new MascotaViewHolder("Mascota 2", "Descripcion de mascota 2", R.drawable.pet, true));
        lstMascotas.add(new MascotaViewHolder("Mascota 3", "Descripcion de mascota 3", R.drawable.pet, true));
        lstMascotas.add(new MascotaViewHolder("Mascota 4", "Descripcion de mascota 4", R.drawable.pet, false));
        lstMascotas.add(new MascotaViewHolder("Mascota 5", "Descripcion de mascota 5", R.drawable.pet, false));
        lstMascotas.add(new MascotaViewHolder("Mascota 6", "Descripcion de mascota 6", R.drawable.pet, true));
        lstMascotas.add(new MascotaViewHolder("Mascota 7", "Descripcion de mascota 7", R.drawable.pet, true));
    }
}