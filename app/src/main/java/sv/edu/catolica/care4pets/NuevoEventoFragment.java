package sv.edu.catolica.care4pets;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NuevoEventoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private EditText edtFechaEvento, edtHoraEvento;
    private ImageView imvFechaEvento, imvHoraEvento;
    private TimePickerDialog timePickerDialog;
    private final SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm a");

    public NuevoEventoFragment() {

    }

    public static NuevoEventoFragment newInstance(String param1, String param2) {
        NuevoEventoFragment fragment = new NuevoEventoFragment();
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

        View view = inflater.inflate(R.layout.fragment_nuevo_evento, container, false);

        edtFechaEvento = view.findViewById(R.id.edtFechaEvento);
        edtHoraEvento = view.findViewById(R.id.edtHoraEvento);
        imvFechaEvento = view.findViewById(R.id.imvFechaEvento);
        imvHoraEvento = view.findViewById(R.id.imvHoraEvento);

        imvFechaEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendario();
            }
        });

        imvHoraEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirReloj();
            }
        });

        return view;
    }

    private void abrirCalendario() {

        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                edtFechaEvento.setText(dayOfMonth + "/" + month + "/" + year);
            }
        }, year, mes, dia);

        datePickerDialog.show();
    }

    private void abrirReloj() {

        timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0, hourOfDay, minute);
                edtHoraEvento.setText(timeformat.format(calendar.getTime()));
            }
        }, 12, 0, false);

        timePickerDialog.show();
    }
}