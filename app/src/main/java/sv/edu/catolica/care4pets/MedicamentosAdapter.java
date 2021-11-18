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

public class MedicamentosAdapter extends RecyclerView.Adapter<MedicamentosAdapter.MedicamentoViewHolder> {

    ArrayList<MedicamentoModel> lstMedicamentos;

    public MedicamentosAdapter(ArrayList<MedicamentoModel> lstMedicamentos) { this.lstMedicamentos = lstMedicamentos;
    }

    @NonNull
    @Override
    public MedicamentoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.medicamento_item, null, false);

        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        return new MedicamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentoViewHolder medicamentoViewHolder, int i) {

        medicamentoViewHolder.nombre.setText(lstMedicamentos.get(i).getNombre());
        medicamentoViewHolder.descripcion.setText(lstMedicamentos.get(i).getDescripcion());
        medicamentoViewHolder.icono.setImageResource((lstMedicamentos.get(i).getIcono()));
    }

    @Override
    public int getItemCount() {
        return lstMedicamentos.size();
    }

    public void eliminarElementoSeleccionado(int posicion) {
        lstMedicamentos.remove(posicion);
        notifyDataSetChanged();
    }

    public class MedicamentoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        ImageView icono;
        TextView nombre, descripcion;
        LinearLayout lytMedicamento;

        public MedicamentoViewHolder(@NonNull View itemView) {
            super(itemView);

            icono = itemView.findViewById(R.id.imvIcono);
            nombre = itemView.findViewById(R.id.txvNombre);
            descripcion = itemView.findViewById(R.id.txvDescripcion);
            lytMedicamento = itemView.findViewById(R.id.lytMedicamento);
            lytMedicamento.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Opciones");
            menu.add(this.getAdapterPosition(), 121,0,"Eliminar");
        }
    }
}
