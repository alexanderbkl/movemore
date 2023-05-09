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
import magdalenaramirez.ioc.movemore.utiles.Car;


public class RecyclerViewAdapter_Car extends RecyclerView.Adapter<RecyclerViewAdapter_Car.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    Context context;
    List<Car> listCars;

    private OnItemClickListener listener;

    public RecyclerViewAdapter_Car(Context context,List<Car> listCars) {
        this.context = context;
        this.listCars = (ArrayList<Car>) listCars;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void agregarDatos(List<Car> listCars_Aux){
        //listCars = listCars;
        listCars.clear();
        listCars.addAll(listCars_Aux);
        this.notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_car, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.marca.setText(listCars.get(position).getMarca());
        viewHolder.modelo.setText("Modelo: " + String.valueOf(listCars.get(position).getModelo()));
        viewHolder.matricula.setText("Matrícula: " + listCars.get(position).getMatricula());

        Glide.with(context)
                .load(listCars.get(position).getUrlImage())
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
        return listCars.size();
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