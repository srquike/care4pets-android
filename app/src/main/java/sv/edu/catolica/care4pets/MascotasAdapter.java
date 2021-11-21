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

public class MascotasAdapter extends RecyclerView.Adapter<MascotasAdapter.ViewHolderMascotas> {

    public ArrayList<MascotaModel> lstMascota;
    private OnMascotaListener onMascotaListener;

    public MascotasAdapter(ArrayList<MascotaModel> lstMasctoas, OnMascotaListener onMascotaListener) {
        this.lstMascota = lstMasctoas;
        this.onMascotaListener = onMascotaListener;
    }

    @Override
    public ViewHolderMascotas onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mascota_item, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        return new ViewHolderMascotas(view, onMascotaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMascotas viewHolderMascotas, int i) {

        viewHolderMascotas.nombre.setText(lstMascota.get(i).getNombre());
        viewHolderMascotas.descripcion.setText(lstMascota.get(i).getDescripcion());
        viewHolderMascotas.foto.setImageResource((lstMascota.get(i).getFoto()));

        if (lstMascota.get(i).isTieneNotificacines()) {

            viewHolderMascotas.notificacion.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return lstMascota.size();
    }

    public class ViewHolderMascotas
            extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener, View.OnClickListener {

        ImageView foto, notificacion;
        TextView nombre, descripcion;
        LinearLayout lytMascota;
        OnMascotaListener onMascotaListener;

        public ViewHolderMascotas(@NonNull View itemView, OnMascotaListener onMascotaListener) {
            super(itemView);

            this.onMascotaListener = onMascotaListener;
            foto = itemView.findViewById(R.id.imvFoto);
            notificacion = itemView.findViewById(R.id.imvNotificacion);
            nombre = itemView.findViewById(R.id.txvNombre);
            descripcion = itemView.findViewById(R.id.txvDescripcion);
            lytMascota = itemView.findViewById(R.id.lytMascota);
            lytMascota.setOnCreateContextMenuListener(this);
            lytMascota.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Opciones");
            menu.add(this.getAdapterPosition(), 121,0,"Eliminar");
        }

        @Override
        public void onClick(View v) {
            onMascotaListener.onMascotaClick(getAdapterPosition());
        }
    }

    public interface OnMascotaListener {
        void onMascotaClick(int posicion);
    }

    public void eliminarElementoSeleccionado(int posicion) {
        lstMascota.remove(posicion);
        notifyDataSetChanged();
    }
}