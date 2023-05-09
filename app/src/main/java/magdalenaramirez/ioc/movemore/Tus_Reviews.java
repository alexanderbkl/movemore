package magdalenaramirez.ioc.movemore;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Handler;

import magdalenaramirez.ioc.movemore.adapter.RecyclerViewAdapter_Car;
import magdalenaramirez.ioc.movemore.adapter.RecyclerViewAdapter_Rentals;
import magdalenaramirez.ioc.movemore.adapter.RecyclerViewAdapter_Reviews;
import magdalenaramirez.ioc.movemore.utiles.Car;
import magdalenaramirez.ioc.movemore.utiles.Rentals;
import magdalenaramirez.ioc.movemore.utiles.User;


public class Tus_Reviews extends AppCompatActivity {

    private Spinner spinner_ordenar;

    //Datos de las credenciales de inicio sesión
    private String file = "credenciales_login";
    private String fileContents;

    private String selectedItem;
    private int posFilter;
    private Intent intent;
    private String comment;
    private float rating;
    private EditText editTextComment;
    private RatingBar ratingBar;
    private boolean dialogClosed;

    View view;
    User usr = new User();
    public static Rentals coche = new Rentals(); //Get accesible in all sites

    //RecyclerView para mostrar datos y List de la clase
    RecyclerView recyclerView;
    List<Rentals> listRentals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tus_reviews);

        //ImageView logo - CLICK - volver al main
        ImageView mImageVCab = findViewById(R.id.ImageVCab);
        mImageVCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tus_Reviews.this, MenuPrincipal.class);
                startActivity(intent);
            }
        });

        //Toolbar - ActionBar - Logout
        ImageView mIM_Menu = findViewById(R.id.icon_menu);
        mIM_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });

        //Instanciar Elementos
        //Spiner de "Ordenar por"
        spinner_ordenar = findViewById(R.id.spn_ordenar);
        spinner_ordenar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Maneja la selección del usuario aquí
                selectedItem = parent.getItemAtPosition(position).toString();
                //pos = Integer.parseInt(selectedItem);
                Toast.makeText(getApplicationContext(), "Seleccionaste " + selectedItem, Toast.LENGTH_SHORT).show();
                System.out.println("DENTRO DEL SPINNER!!!!!");

                if (selectedItem.equals("Todo")) {
                    posFilter = 0;
                } else if (selectedItem.equals("Comentado")) {
                    posFilter = 1;
                } else if (selectedItem.equals("Sin Comentar")) {
                    posFilter = 2;
                } else {
                   // Si la opción seleccionada no es válida, establecer un valor por defecto o mostrar un mensaje de error
                   Toast.makeText(Tus_Reviews.this, "Opción no válida", Toast.LENGTH_SHORT).show();
                }
                //Cargar vista aplicando filtros
                loadReviewbyFilter();
            // Cierre del onItemSelected - spinner_ordenar
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               // Maneja la situación en la que no se selecciona nada
               Toast.makeText(getApplicationContext(), "No seleccionaste nada", Toast.LENGTH_SHORT).show();
            }

        });

        // Cargamos los datos de todos mis reviews
        loadReviews();
     // Cierre onCreate
    }

    private void showDialogComment() {
        dialogClosed = false; // Variable para indicar si el diálogo se ha cerrado o no

        // Crear el AlertDialog y establecer sus elementos
        AlertDialog.Builder builder = new AlertDialog.Builder(Tus_Reviews.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_comment, null);
        builder.setView(dialogView);

        // Obtener los elementos de la vista inflada
        editTextComment = dialogView.findViewById(R.id.edit_comment);
        ratingBar = dialogView.findViewById(R.id.rating_bar);

        // Establecer los valores predeterminados para los elementos de la vista inflada
        editTextComment.setText("");
        ratingBar.setRating(0.0f);

        // Agregar los botones de Aceptar y Cancelar al AlertDialog
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Obtener los valores ingresados por el usuario
                comment = editTextComment.getText().toString();
                rating = ratingBar.getRating();
                dialogClosed = true;
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // No hacer nada
            }
        });

        // Mostrar el AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    // Cierre del showDialogComment
    }

    RecyclerViewAdapter_Reviews adapter;
    private void loadReviews() {
        listRentals = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerJSON_Rentals);
        adapter = new RecyclerViewAdapter_Reviews(this, listRentals);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Thread thread = new Thread(() -> {
            while (listRentals.isEmpty() && !usr.getMessage().equals("no rows returned")) {
                listRentals = usr.getListMyRentalsByFilter("rentalsclosed");
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.agregarDatos(listRentals);
                    adapter.setOnItemClickListener(new RecyclerViewAdapter_Reviews.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Tus_Reviews.coche = listRentals.get(position);
                            System.out.println(coche.getId());

                            if(listRentals.get(position).isReviwed()){
                                Toast.makeText(Tus_Reviews.this, "No puedes comentar una review comentada.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            showDialogComment();

                            Thread thread = new Thread(() -> {
                                while (!dialogClosed) {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                //enviar review y re-inciar
                                listRentals.get(position).setScore((int) ratingBar.getRating());
                                listRentals.get(position).setMessage(editTextComment.getText().toString());

                                if (usr.sendReview(listRentals.get(position))) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            loadReviews();
                                        }
                                    });
                                }
                            });
                            thread.start();
                        }
                    });
                    recyclerView.setAdapter(adapter); // Actualización de la vista en el hilo principal
                }
            });
        });
        thread.start();
    // Cierre del loadReviews
    }


    private void loadReviewbyFilter(){

        List<Rentals> listRentals2 = new ArrayList<>();
        switch((String)spinner_ordenar.getSelectedItem()){
            case "Todo":
                adapter.agregarDatos(listRentals);
                break;
            case "Comentado":
                for (Rentals rentals : listRentals) {
                    if(rentals.isReviwed()){
                        listRentals2.add(rentals);
                    }
                }
                adapter.agregarDatos(listRentals2);
                break;
            case "Sin Comentar":
                for (Rentals rentals : listRentals) {
                    if(!rentals.isReviwed()){
                        listRentals2.add(rentals);
                    }
                }
                adapter.agregarDatos(listRentals2);
                break;
        }
    // Cierre del loadReviewbyFilter
    }



    // Menu ActionBar - onCLICK()
    private void showMenu(View view){

        PopupMenu popupMenu = new PopupMenu(Tus_Reviews.this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.popup_Tu_Perfil:
                        // load "Tu_Perfil" Fragment
                        //loadFragment(new Tu_Perfil());
                        intent = new Intent(Tus_Reviews.this, Tu_Perfil.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Coches:
                        //loadFragment(new Tus_Coches());
                        intent = new Intent(Tus_Reviews.this, Tus_Coches.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Alquileres:
                        //loadFragment(new Tus_Alquileres());
                        intent = new Intent(Tus_Reviews.this, Tus_Alquileres.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Reviews:
                        //loadFragment(new Tus_Reviews());
                        intent = new Intent(Tus_Reviews.this, Tus_Reviews.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Reviews_de_tus_Coches:
                        //loadFragment(new Tus_Reviews_Coches());
                        intent = new Intent(Tus_Reviews.this, Tus_Reviews_Coches.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_logout:
                        logoutSes();
                        break;
                }
                return true;
            }
        });

        popupMenu.show();
        // Cierre showMenu
    }

    private void logoutSes(){
        //Borramos las credenciales
        deleteFile_UserLogin();

        SharedPreferences SM = getSharedPreferences("userrecord",0);
        SharedPreferences.Editor edit = SM.edit();
        edit.putBoolean("userlogin",false);
        edit.commit();

        Intent intent = new Intent(Tus_Reviews.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void deleteFile_UserLogin(){

        try{
            FileOutputStream fOut = openFileOutput(file, MODE_PRIVATE);
            fOut.write(fileContents.getBytes());
            fOut.close();
            File fileDir = new File(getFilesDir(),file);
            Toast.makeText(getBaseContext(),"File Deleted:" + fileDir,Toast.LENGTH_LONG).show();
            fileDir.delete();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
