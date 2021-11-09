package sv.edu.catolica.care4pets;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.EventosViewHolder> {

    ArrayList<EventoModel> lstEventos;

    public EventosAdapter(ArrayList<EventoModel> lstEventos) {
        this.lstEventos = lstEventos;
    }

    @NonNull
    @Override
    public EventosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.evento_item, null, false);

        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        return new EventosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventosViewHolder eventosViewHolder, int i) {
        eventosViewHolder.nombre.setText(lstEventos.get(i).getNombre());
        eventosViewHolder.descripcion.setText(lstEventos.get(i).getDescripcion());
        eventosViewHolder.icono.setImageResource((lstEventos.get(i).getIcono()));
    }

    @Override
    public int getItemCount() {
        return lstEventos.size();
    }

    public class EventosViewHolder extends RecyclerView.ViewHolder {

        ImageView icono;
        TextView nombre, descripcion;

        public EventosViewHolder(@NonNull View itemView) {
            super(itemView);

            icono = itemView.findViewById(R.id.imvIcono);
            nombre = itemView.findViewById(R.id.txvNombre);
            descripcion = itemView.findViewById(R.id.txvDescripcion);
        }
    }
}
