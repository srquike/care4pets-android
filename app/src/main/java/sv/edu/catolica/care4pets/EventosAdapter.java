package sv.edu.catolica.care4pets;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.EventosViewHolder> {

    ArrayList<EventoModel> lstEventos;
    //7to paso
    private OnEventosListener onEventosListener;

    public EventosAdapter(ArrayList<EventoModel> lstEventos, OnEventosListener onEventosListener) {
        //8otavo paso
        this.lstEventos = lstEventos;
        this.onEventosListener = onEventosListener;
    }

    @NonNull
    @Override
    public EventosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.evento_item, null, false);

        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
//9no paso
        return new EventosViewHolder(view, onEventosListener);
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

    public class EventosViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {

        ImageView icono;
        TextView nombre, descripcion;
        LinearLayout lytEvento;
        //4to paso
        OnEventosListener onEventosListener;

        public EventosViewHolder(@NonNull View itemView, OnEventosListener onEventosListener) {
            super(itemView);
//5to paso
            this.onEventosListener = onEventosListener;
            icono = itemView.findViewById(R.id.imvIcono);
            nombre = itemView.findViewById(R.id.txvNombre);
            descripcion = itemView.findViewById(R.id.txvDescripcion);
            lytEvento = itemView.findViewById(R.id.lytEvento);
            lytEvento.setOnCreateContextMenuListener(this);
            //ultimo paso
            lytEvento.setOnClickListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Opciones");
            menu.add(this.getAdapterPosition(), 121,0,"Eliminar");
        }
//3er paso
        @Override
        public void onClick(View v) {
            //6to paso
            onEventosListener.onEventosClick(getAdapterPosition());

        }
    }

    public void eliminarElementoSeleccionado(int posicion) {
        lstEventos.remove(posicion);
        notifyDataSetChanged();
    }
//1er paso
    public interface OnEventosListener{
        void onEventosClick(int posicion);
    }

}
