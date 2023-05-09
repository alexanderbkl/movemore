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

public class RecyclerViewAdapter_Reviews extends RecyclerView.Adapter<RecyclerViewAdapter_Reviews.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    Context context;
    List<Rentals> listRent;

    private RecyclerViewAdapter_Reviews.OnItemClickListener listener;

    public RecyclerViewAdapter_Reviews(Context context,List<Rentals> listRent) {
        this.context = context;
        this.listRent = (ArrayList<Rentals>) listRent;
    }

    public void setOnItemClickListener(RecyclerViewAdapter_Reviews.OnItemClickListener listener) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_car_reviews, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.marca.setText(listRent.get(position).getCoche().getMarca());
        viewHolder.modelo.setText("Modelo: " + String.valueOf(listRent.get(position).getCoche().getModelo()));
        viewHolder.matricula.setText("Matrícula: " + listRent.get(position).getCoche().getMatricula());
        viewHolder.comentario.setText("Comentario: " + listRent.get(position).getMessage());

        // Obtener el valor del campo double de la lista de datos
        double doubleField = listRent.get(position).getScore();
        // Convertir el valor del campo double a una cadena de caracteres con dos decimales
        String doubleAsString = String.format("%.2f", doubleField);
        if (doubleAsString.equals("-1")){
            doubleAsString = "0";
            System.out.println("vale -1");
        }
        // Asignar la cadena de caracteres al TextView correspondiente
        //viewHolder.score.setText("Score: " + doubleAsString);

        // Mostramos el Score con *
        StringBuilder starsBuilder = new StringBuilder();
        for (int i = 0; i < Math.min(doubleField, 10); i++) {
            starsBuilder.append("✳");
        }
        StringBuilder emptyStarsBuilder = new StringBuilder();
        for (int i = 0; i < Math.max(10 - doubleField, 0); i++) {
            emptyStarsBuilder.append("⭕");
        }
        // Asignar la cadena de caracteres al TextView correspondiente
        viewHolder.score.setText(starsBuilder.toString() + emptyStarsBuilder.toString());

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
        TextView marca, modelo, matricula, comentario, score;

        public ViewHolder(View itemView) {
            super(itemView);
            // Elementos del Layout "layout_items_car_reviews"
            imageView = itemView.findViewById(R.id.coche_foto);
            marca = itemView.findViewById(R.id.coche_marca);
            modelo = itemView.findViewById(R.id.coche_modelo);
            matricula = itemView.findViewById(R.id.coche_matricula);
            comentario = itemView.findViewById(R.id.coche_comentario);
            score = itemView.findViewById(R.id.coche_score);
        }

    }
}

