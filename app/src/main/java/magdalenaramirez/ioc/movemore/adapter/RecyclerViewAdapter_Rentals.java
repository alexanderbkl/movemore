package magdalenaramirez.ioc.movemore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

import magdalenaramirez.ioc.movemore.R;
import magdalenaramirez.ioc.movemore.utiles.Rentals;

public class RecyclerViewAdapter_Rentals extends RecyclerView.Adapter<RecyclerViewAdapter_Rentals.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    Context context;
    List<Rentals> listRent;

    private RecyclerViewAdapter_Rentals.OnItemClickListener listener;

    public RecyclerViewAdapter_Rentals(Context context,List<Rentals> listRent) {
        this.context = context;
        this.listRent = (ArrayList<Rentals>) listRent;
    }

    public void setOnItemClickListener(RecyclerViewAdapter_Rentals.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void agregarDatos(List<Rentals> listRent_Aux){
        //listCars = listCars;
        listRent.clear();
        listRent.addAll(listRent_Aux);
        this.notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_car, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.marca.setText(listRent.get(position).getCoche().getMarca());
        viewHolder.modelo.setText(String.valueOf("Modelo: " + listRent.get(position).getCoche().getModelo()));
        viewHolder.matricula.setText("Matrícula: " +  listRent.get(position).getCoche().getMatricula());

        Glide.with(context)
                .load(listRent.get(position).getCoche().getUrlImage())
                .into(viewHolder.imageView);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Llamar al método onItemClick de la interfaz
                if (listener != null) {
                    int pos = viewHolder.getAdapterPosition();
                    listener.onItemClick(pos);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listRent.size();
    }

    /**
     * Holder class that represents each row of data in the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView marca, modelo, matricula;

        public ViewHolder(View itemView) {
            super(itemView);
            // Elementos del Layout "layout_items_car"
            imageView = itemView.findViewById(R.id.coche_foto);
            marca = itemView.findViewById(R.id.coche_marca);
            modelo = itemView.findViewById(R.id.coche_modelo);
            matricula = itemView.findViewById(R.id.coche_matricula);
        }

    }
}
