package sv.edu.catolica.care4pets;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;

public class NuevaMascotaFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private EditText edtFechaEsterilizacion;
    private EditText edtFechaNacimiento;
    private ImageView imvFechaNacimiento, imvFechaEsterilizacion;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private String fecha;

    public NuevaMascotaFragment() {

    }

    public static NuevaMascotaFragment newInstance(String param1, String param2) {
        NuevaMascotaFragment fragment = new NuevaMascotaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_or_cancel_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_nueva_mascota, container, false);

        edtFechaNacimiento = vista.findViewById(R.id.edtFechaNacimiento);
        edtFechaEsterilizacion = vista.findViewById(R.id.edtFechaEsterilizacion);
        imvFechaNacimiento = vista.findViewById(R.id.imVFechaNacimiento);
        imvFechaEsterilizacion = vista.findViewById(R.id.imVFechaEsterilizacion);

        imvFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendario(edtFechaNacimiento);
            }
        });

        imvFechaEsterilizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendario(edtFechaEsterilizacion);
            }
        });

        return vista;
    }

    private void abrirCalendario(EditText editText) {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (editText.equals(edtFechaNacimiento)) {
                    edtFechaNacimiento.setText(dayOfMonth + "/" + month + "/" + year);
                } else if (editText.equals((edtFechaEsterilizacion))){
                    edtFechaEsterilizacion.setText(dayOfMonth + "/" + month + "/" + year);
                }
            }
        }, year, mes, dia);

        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.btnAceptar:

                break;
            case R.id.btnCancelar:
                break;
        }

        return super.onOptionsItemSelected(item);



    }
}