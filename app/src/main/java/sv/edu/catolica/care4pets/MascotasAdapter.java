package sv.edu.catolica.care4pets;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MascotasAdapter extends RecyclerView.Adapter<MascotasAdapter.ViewHolderMascotas> {

    ArrayList<MascotaModel> lstMasctoas;

    public MascotasAdapter(ArrayList<MascotaModel> lstMasctoas) {
        this.lstMasctoas = lstMasctoas;
    }

    @Override
    public ViewHolderMascotas onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mascota_item, null, false);

        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        return new ViewHolderMascotas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMascotas viewHolderMascotas, int i) {

        viewHolderMascotas.nombre.setText(lstMasctoas.get(i).getNombre());
        viewHolderMascotas.descripcion.setText(lstMasctoas.get(i).getDescripcion());
        viewHolderMascotas.foto.setImageResource((lstMasctoas.get(i).getFoto()));

        if (lstMasctoas.get(i).isTieneNotificacines()) {

            viewHolderMascotas.notificacion.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return lstMasctoas.size();
    }

    public class ViewHolderMascotas extends RecyclerView.ViewHolder {

        ImageView foto, notificacion;
        TextView nombre, descripcion;

        public ViewHolderMascotas(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imvFoto);
            notificacion = itemView.findViewById(R.id.imvNotificacion);
            nombre = itemView.findViewById(R.id.txvNombre);
            descripcion = itemView.findViewById(R.id.txvDescripcion);
        }
    }
}