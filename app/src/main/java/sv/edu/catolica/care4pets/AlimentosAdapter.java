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

public class AlimentosAdapter extends RecyclerView.Adapter<AlimentosAdapter.AlimentoViewHolder> {

    ArrayList<AlimentoModel> lstAlimentos;
    private OnAlimentosListener onAlimentosListener;

    public AlimentosAdapter(ArrayList<AlimentoModel> lstAlimentos, OnAlimentosListener onAlimentosListener) {
        this.lstAlimentos = lstAlimentos;
        this.onAlimentosListener = onAlimentosListener;
    }

    @NonNull
    @Override
    public AlimentosAdapter.AlimentoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alimento_item, null, false);

        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        return new AlimentoViewHolder(view, onAlimentosListener);
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

    public void eliminarElementoSeleccionado(int posicion) {
        lstAlimentos.remove(posicion);
        notifyDataSetChanged();
    }


    public class AlimentoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {

        ImageView icono;
        TextView nombre, descripcion;
        LinearLayout lytAlimento;
        OnAlimentosListener onAlimentosListener;

        public AlimentoViewHolder(@NonNull View itemView, OnAlimentosListener onAlimentosListener) {
            super(itemView);


            this.onAlimentosListener = onAlimentosListener;
            icono = itemView.findViewById(R.id.imvIcono);
            nombre = itemView.findViewById(R.id.txvNombre);
            descripcion = itemView.findViewById(R.id.txvDescripcion);
            lytAlimento = itemView.findViewById(R.id.lytAlimento);
            lytAlimento.setOnCreateContextMenuListener(this);
            lytAlimento.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Opciones");
            menu.add(this.getAdapterPosition(), 121,0,"Eliminar");

        }


        @Override
        public void onClick(View v) {
            onAlimentosListener.onAlimentosClick(getAdapterPosition());

        }
    }
    public interface OnAlimentosListener{
        void onAlimentosClick(int posicion);
    }
}
