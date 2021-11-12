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

public class MedicamentosFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ArrayList<MedicamentoModel> lstMedicamento;
    private RecyclerView rcvMedicamentos;
    private NavController navController;

    public MedicamentosFragment() {

    }

    public static MedicamentosFragment newInstance(String param1, String param2) {
        MedicamentosFragment fragment = new MedicamentosFragment();
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

        View view = inflater.inflate(R.layout.fragment_medicamentos, container, false);

        lstMedicamento = new ArrayList<>();
        rcvMedicamentos = (RecyclerView) view.findViewById(R.id.rcvMedicamentos);

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nuevoMedicamentoFragment);
            }
        });

        rcvMedicamentos.setLayoutManager(new LinearLayoutManager(getContext()));

        LlenarLista();

        MedicamentosAdapter adapter = new MedicamentosAdapter(lstMedicamento);

        rcvMedicamentos.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    private void LlenarLista() {

        lstMedicamento.add(new MedicamentoModel("Medicamento 1", "Descripcion de medicamento 1", R.drawable.first_aid));
        lstMedicamento.add(new MedicamentoModel("Medicamento 5", "Descripcion de medicamento 5", R.drawable.first_aid));
        lstMedicamento.add(new MedicamentoModel("Medicamento 6", "Descripcion de medicamento 6", R.drawable.first_aid));
        lstMedicamento.add(new MedicamentoModel("Medicamento 7", "Descripcion de medicamento 7", R.drawable.first_aid));
        lstMedicamento.add(new MedicamentoModel("Medicamento 3", "Descripcion de medicamento 3", R.drawable.first_aid));
        lstMedicamento.add(new MedicamentoModel("Medicamento 4", "Descripcion de medicamento 4", R.drawable.first_aid));
        lstMedicamento.add(new MedicamentoModel("Medicamento 2", "Descripcion de medicamento 2", R.drawable.first_aid));
    }
}