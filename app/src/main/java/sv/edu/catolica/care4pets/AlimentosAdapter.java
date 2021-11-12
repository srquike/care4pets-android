package sv.edu.catolica.care4pets;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AlimentosAdapter extends RecyclerView.Adapter<AlimentosAdapter.AlimentoViewHolder> {

    ArrayList<AlimentoModel> lstAlimentos;

    public AlimentosAdapter(ArrayList<AlimentoModel> lstAlimentos) {
        this.lstAlimentos = lstAlimentos;
    }

    @NonNull
    @Override
    public AlimentosAdapter.AlimentoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alimento_item, null, false);

        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        return new AlimentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlimentosAdapter.AlimentoViewHolder alimentoViewHolder, int i) {

        alimentoViewHolder.nombre.setText(lstAlimentos.get(i).getNombre());
        alimentoViewHolder.descripcion.setText(lstAlimentos.get(i).getDescripcion());
        alimentoViewHolder.icono.setImageResource((lstAlimentos.get(i).getIcono()));
    }

    @Override
    public int getItemCount() {
        return lstAlimentos.size();
    }

    public class AlimentoViewHolder extends RecyclerView.ViewHolder {

        ImageView icono;
        TextView nombre, descripcion;

        public AlimentoViewHolder(@NonNull View itemView) {
            super(itemView);

            icono = itemView.findViewById(R.id.imvIcono);
            nombre = itemView.findViewById(R.id.txvNombre);
            descripcion = itemView.findViewById(R.id.txvDescripcion);
        }
    }
}
