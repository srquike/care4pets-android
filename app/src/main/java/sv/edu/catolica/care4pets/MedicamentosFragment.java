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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MedicamentosFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView rcvMedicamentos;
    private NavController navController;
    private ControladorBD adminDB;
    private SQLiteDatabase db;

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
        adminDB = new ControladorBD(getContext(),"DBCare4Pets",null,1);
        db = adminDB.getReadableDatabase();
        View view = inflater.inflate(R.layout.fragment_medicamentos, container, false);
        rcvMedicamentos = (RecyclerView) view.findViewById(R.id.rcvMedicamentos);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nuevoMedicamentoFragment);
            }
        });

        rcvMedicamentos.setLayoutManager(new LinearLayoutManager(getContext()));
        MedicamentosAdapter adapter = new MedicamentosAdapter(obtenerListaMedicamentos());
        rcvMedicamentos.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    private ArrayList<MedicamentoModel> obtenerListaMedicamentos() {
        ArrayList<MedicamentoModel> lstMedicamentos = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Medicamentos", null);
        MedicamentoModel medicamentoModel = null;

        if (cursor.moveToFirst()) {
            do {
                medicamentoModel = new MedicamentoModel();
                medicamentoModel.setId(cursor.getInt(0));
                medicamentoModel.setNombre(cursor.getString(1));
                medicamentoModel.setNotas(cursor.getString(2));
                medicamentoModel.setPresentacion(cursor.getString(3));
                medicamentoModel.setCantidad(Double.parseDouble(cursor.getString(4)));
                medicamentoModel.setUnidad(cursor.getString(5));
                medicamentoModel.setFechaVencimiento(LocalDate.parse(cursor.getString(6), DateTimeFormatter.ofPattern("d/M/yyyy")));
                medicamentoModel.setLaboratorio(cursor.getString(7));
                medicamentoModel.setIcono(R.drawable.first_aid);
                medicamentoModel.setDescripcion(medicamentoModel.getPresentacion() + " - " + medicamentoModel.getCantidad() + " " + medicamentoModel.getUnidad() + " - " + medicamentoModel.getFechaVencimiento());

                lstMedicamentos.add(medicamentoModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return  lstMedicamentos;
    }
}