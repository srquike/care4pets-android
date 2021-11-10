package sv.edu.catolica.care4pets;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfesionalesAdapter extends RecyclerView.Adapter<ProfesionalesAdapter.ProfesionalesViewHolder>  {

    private ArrayList<ProfesionalModel> lstProfesionales;

    public ProfesionalesAdapter(ArrayList<ProfesionalModel> lstProfesionales) {
        this.lstProfesionales = lstProfesionales;
    }

    @NonNull
    @Override
    public ProfesionalesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profesinal_item, null, false);

        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        return new ProfesionalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfesionalesViewHolder profesionalesViewHolder, int i) {

        profesionalesViewHolder.nombre.setText(lstProfesionales.get(i).getNombre());
        profesionalesViewHolder.descripcion.setText(lstProfesionales.get(i).getDescripcion());
        profesionalesViewHolder.icono.setImageResource((lstProfesionales.get(i).getIcono()));
    }

    @Override
    public int getItemCount() {
        return lstProfesionales.size();
    }

    public class ProfesionalesViewHolder extends RecyclerView.ViewHolder {

        ImageView icono;
        TextView nombre, descripcion;

        public ProfesionalesViewHolder(@NonNull View itemView) {
            super(itemView);

            icono = itemView.findViewById(R.id.imvIcono);
            nombre = itemView.findViewById(R.id.txvNombre);
            descripcion = itemView.findViewById(R.id.txvDescripcion);
        }
    }
}
