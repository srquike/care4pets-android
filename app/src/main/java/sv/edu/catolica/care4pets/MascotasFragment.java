package sv.edu.catolica.care4pets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MascotasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MascotasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private ArrayList<MascotaViewHolder> lstMascotas;

    public MascotasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MascotasFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        LlenarLista();

        MascotasAdapter adapter = new MascotasAdapter(lstMascotas);

        recyclerView.setAdapter(adapter);
        return vistaMascotas;
    }

    private void LlenarLista() {

        lstMascotas.add(new MascotaViewHolder("Mascota 1", "Descripcion de mascota 1", R.drawable.pet, false));
        lstMascotas.add(new MascotaViewHolder("Mascota 2", "Descripcion de mascota 2", R.drawable.pet, true));
        lstMascotas.add(new MascotaViewHolder("Mascota 3", "Descripcion de mascota 3", R.drawable.pet, true));
        lstMascotas.add(new MascotaViewHolder("Mascota 4", "Descripcion de mascota 4", R.drawable.pet, false));
        lstMascotas.add(new MascotaViewHolder("Mascota 5", "Descripcion de mascota 5", R.drawable.pet, false));
    }
}