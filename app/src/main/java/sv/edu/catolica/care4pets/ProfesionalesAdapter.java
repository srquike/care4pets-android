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

public class ProfesionalesAdapter extends RecyclerView.Adapter<ProfesionalesAdapter.ProfesionalesViewHolder>  {

    ArrayList<ProfesionalModel> lstProfesionales;
    //8 se crea private OnListener onListener;
    private OnProfesionalesListener onProfesionalesListener;
    //9 se agrega el OnListener onListener luego de lst
    public ProfesionalesAdapter(ArrayList<ProfesionalModel> lstProfesionales, OnProfesionalesListener onProfesionalesListener) {
        this.lstProfesionales = lstProfesionales;
        //10 se agrega el this.onListener = onListener;
        this.onProfesionalesListener = onProfesionalesListener;
    }

    @NonNull
    @Override
    public ProfesionalesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profesional_item, null, false);

        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        //11 se agrega la variable creada onListener en los parentesis y regresamos a clase fragment
        return new ProfesionalesViewHolder(view, onProfesionalesListener);
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

    public void eliminarElementoSeleccionado(int posicion) {
        lstProfesionales.remove(posicion);
        notifyDataSetChanged();
    }
    //3ro se agrega el view.onclickListener y luego se implementa el metodo
    public class ProfesionalesViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {

        ImageView icono;
        TextView nombre, descripcion;
        LinearLayout lytProfesional;
        //4to se crea variable OnListener
        OnProfesionalesListener onProfesionalesListener;
        //5to se agrega OnListener  onListener
        public ProfesionalesViewHolder(@NonNull View itemView, OnProfesionalesListener onProfesionalesListener) {
            super(itemView);

            //6to se crea el this.onListener = onListener
            this.onProfesionalesListener = onProfesionalesListener;
            icono = itemView.findViewById(R.id.imvIcono);
            nombre = itemView.findViewById(R.id.txvNombre);
            descripcion = itemView.findViewById(R.id.txvDescripcion);
            lytProfesional = itemView.findViewById(R.id.lytProfesional);
            lytProfesional.setOnCreateContextMenuListener(this);
            //ultimo paso
            lytProfesional.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Opciones");
            menu.add(this.getAdapterPosition(), 121,0,"Eliminar");
        }

    @Override
    public void onClick(View v) {
            //7to onListener.onCLick(getAdapterPosition());
        onProfesionalesListener.onProfesionalesClick(getAdapterPosition());

    }
}

    //1ro se crea metodo OnListener
    public interface OnProfesionalesListener{
        void onProfesionalesClick(int posicion);
    }

}
