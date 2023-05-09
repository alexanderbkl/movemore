package magdalenaramirez.ioc.movemore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import magdalenaramirez.ioc.movemore.adapter.RecyclerViewAdapter_Car;
import magdalenaramirez.ioc.movemore.adapter.RecyclerViewAdapter_Rentals;
import magdalenaramirez.ioc.movemore.utiles.Car;
import magdalenaramirez.ioc.movemore.utiles.Rentals;
import magdalenaramirez.ioc.movemore.utiles.User;


public class Tus_Alquileres extends AppCompatActivity {

    private Spinner spinner_ordenar;

    //Datos de las credenciales de inicio sesión
    private String file = "credenciales_login";
    private String fileContents;
    private Intent intent;

    private String selectedItem;

    View view;

    public static Car coche = new Car(); //Get accesible in all sites
    public static Rentals rental = new Rentals(); //Get accesible in all sites // no se usa en esta clase
    //public static User usr = new User(); //Get accesible in all sites
    User usr = new User();

    //RecyclerView para mostrar datos y List de la clase
    RecyclerView recyclerView;
    List<Rentals> listRentals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tus_alquileres);

        // Cargamos los datos de todos los coches by user
        loadRentals();

        //Instanciar Elementos
        //Spiner de "Ordenar por"
        spinner_ordenar = findViewById(R.id.spn_ordenar);
        spinner_ordenar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Maneja la selección del usuario aquí
                selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Seleccionaste " + selectedItem, Toast.LENGTH_SHORT).show();
                // Mostramos alquileres por filtro
                loadRentalsbyFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Maneja la situación en la que no se selecciona nada
                Toast.makeText(getApplicationContext(), "No seleccionaste nada", Toast.LENGTH_SHORT).show();
            }

        });

        //ImageView logo - CLICK - volver al main
        ImageView mImageVCab = findViewById(R.id.ImageVCab);
        mImageVCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tus_Alquileres.this, MenuPrincipal.class);
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

    // Cierre onCreate
    }
    private void loadRentalsbyFilter(){

        Thread thread = new Thread(() -> {
            switch (selectedItem) {
                case "Todo":
                    //http://10.2.66.56/index.php/user/rentalslist?
                    //filter=all
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadRentals();
                        }
                    });
                    break;
                case "Inactive":
                    //http://10.2.66.56/index.php/user/rentalsinactive?
                    //filter=all
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadRentals_Inactive();
                        }
                    });
                    break;
                case "Active":
                    //http://10.2.66.56/index.php/user/rentalsactive?
                    //filter=all
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadRentals_Active();
                        }
                    });
                    break;
                case "Closed":
                    //http://10.2.66.56/index.php/user/rentalsclosed?
                    //filter=all
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadRentals_Closed();
                        }
                    });
                    break;
            }
        });
        thread.start();
    // Cierre loadRentalsbyFilter
    }

    private void loadRentals_Closed(){

        listRentals=new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.recyclerJSON_Rentals);
        RecyclerViewAdapter_Rentals adapter=new RecyclerViewAdapter_Rentals(this,listRentals);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Thread thread = new Thread(() -> {

            //Rentals list (closed):
            //http://10.2.66.56/index.php/user/rentalsclosed?
            //filter=all
            while (listRentals.isEmpty() && !usr.getMessage().equals("no rows returned")) {
                listRentals = usr.getListMyRentalsByFilter("rentalsclosed");
            }

            if (listRentals.isEmpty()) {
                // Show an appropriate message if the request failed
                Toast.makeText(this, "Failed to get rental list", Toast.LENGTH_LONG).show();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.agregarDatos(listRentals);
                        adapter.setOnItemClickListener(new RecyclerViewAdapter_Rentals.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                // Detalle del coche(posición) del elemento clicado
                                if(new Date().compareTo(listRentals.get(position).getFecha_In()) > 0){
                                    Toast.makeText(Tus_Alquileres.this, "No puede modificar un coche que ya se está cerrado.", Toast.LENGTH_LONG).show();
                                    return;
                                }//err+="La fecha de inicio es anterior o a la fecha de hoy.\n";

                                Tus_Alquileres_Edit.coche = listRentals.get(position).getCoche();
                                Tus_Alquileres_Edit.rental = listRentals.get(position);
                                intent = new Intent(Tus_Alquileres.this, Tus_Alquileres_Edit.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
        thread.start();
    // Cierre loadRentals_Closed
    }


    private void loadRentals_Active(){

        listRentals=new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.recyclerJSON_Rentals);
        RecyclerViewAdapter_Rentals adapter=new RecyclerViewAdapter_Rentals(this,listRentals);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Thread thread = new Thread(() -> {

            //Rentals list (active):
            //http://10.2.66.56/index.php/user/rentalsactive?
            //filter=all
            while (listRentals.isEmpty() && !usr.getMessage().equals("no rows returned")) {
                listRentals = usr.getListMyRentalsByFilter("rentalsactive");
            }

            if (listRentals.isEmpty()) {
                // Show an appropriate message if the request failed
                Toast.makeText(this, "Failed to get rental list", Toast.LENGTH_LONG).show();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.agregarDatos(listRentals);
                        adapter.setOnItemClickListener(new RecyclerViewAdapter_Rentals.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                // Detalle del coche(posición) del elemento clicado
                                if(new Date().compareTo(listRentals.get(position).getFecha_In()) > 0){
                                    Toast.makeText(Tus_Alquileres.this, "No puede modificar un coche que ya se a activado.", Toast.LENGTH_LONG).show();
                                    return;
                                }//err+="La fecha de inicio es anterior o a la fecha de hoy.\n";

                                Tus_Alquileres_Edit.coche = listRentals.get(position).getCoche();
                                Tus_Alquileres_Edit.rental = listRentals.get(position);
                                intent = new Intent(Tus_Alquileres.this, Tus_Alquileres_Edit.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
        thread.start();
    // Cierre loadRentals_Active
    }

    private void loadRentals_Inactive(){

        listRentals=new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.recyclerJSON_Rentals);
        RecyclerViewAdapter_Rentals adapter=new RecyclerViewAdapter_Rentals(this,listRentals);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Thread thread = new Thread(() -> {

            //Rentals list (inactive):
            //http://10.2.66.56/index.php/user/rentalsinactive?
            //filter=all
            while (listRentals.isEmpty() && !usr.getMessage().equals("no rows returned")) {
                listRentals = usr.getListMyRentalsByFilter("rentalsinactive");
            }

            if (listRentals.isEmpty()) {
                // Show an appropriate message if the request failed
                Toast.makeText(this, "Failed to get rental list", Toast.LENGTH_LONG).show();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.agregarDatos(listRentals);
                        adapter.setOnItemClickListener(new RecyclerViewAdapter_Rentals.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                // Detalle del coche(posición) del elemento clicado
                                if(new Date().compareTo(listRentals.get(position).getFecha_In()) > 0){
                                    Toast.makeText(Tus_Alquileres.this, "No puede modificar un coche que ya se a activado.", Toast.LENGTH_LONG).show();
                                    return;
                                }//err+="La fecha de inicio es anterior o a la fecha de hoy.\n";

                                Tus_Alquileres_Edit.coche = listRentals.get(position).getCoche();
                                Tus_Alquileres_Edit.rental = listRentals.get(position);
                                intent = new Intent(Tus_Alquileres.this, Tus_Alquileres_Edit.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
        thread.start();
    // Cierre loadRentals_Inactive
    }


    private void loadRentals(){

        listRentals=new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.recyclerJSON_Rentals);
        RecyclerViewAdapter_Rentals adapter=new RecyclerViewAdapter_Rentals(this,listRentals);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Thread thread = new Thread(() -> {

            //Rentals list (all rentals):
            //http://10.2.66.56/index.php/user/rentalslist?
            //filter=all
            while (listRentals.isEmpty() && !usr.getMessage().equals("no rows returned")) {
                    listRentals = usr.getListMyRentals();
            }

            if (listRentals.isEmpty()) {
                // Show an appropriate message if the request failed
                Toast.makeText(this, "Failed to get rental list", Toast.LENGTH_LONG).show();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.agregarDatos(listRentals);
                        adapter.setOnItemClickListener(new RecyclerViewAdapter_Rentals.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                // Detalle del coche(posición) del elemento clicado
                                if(new Date().compareTo(listRentals.get(position).getFecha_In()) > 0){
                                    Toast.makeText(Tus_Alquileres.this, "No puede modificar un coche que ya se a activado.", Toast.LENGTH_LONG).show();
                                    return;
                                }//err+="La fecha de inicio es anterior o a la fecha de hoy.\n";

                                Tus_Alquileres_Edit.coche = listRentals.get(position).getCoche();
                                Tus_Alquileres_Edit.rental = listRentals.get(position);
                                intent = new Intent(Tus_Alquileres.this, Tus_Alquileres_Edit.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
        thread.start();
    // Cierre loadRentals
    }

    // Menu ActionBar - onCLICK()
    private void showMenu(View view){

        PopupMenu popupMenu = new PopupMenu(Tus_Alquileres.this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.popup_Tu_Perfil:
                        // load "Tu_Perfil" Fragment
                        //loadFragment(new Tu_Perfil());
                        intent = new Intent(Tus_Alquileres.this, Tu_Perfil.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Coches:
                        //loadFragment(new Tus_Coches());
                        intent = new Intent(Tus_Alquileres.this, Tus_Coches.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Alquileres:
                        //loadFragment(new Tus_Alquileres());
                        intent = new Intent(Tus_Alquileres.this, Tus_Alquileres.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Reviews:
                        //loadFragment(new Tus_Reviews());
                        intent = new Intent(Tus_Alquileres.this, Tus_Reviews.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Reviews_de_tus_Coches:
                        //loadFragment(new Tus_Reviews_Coches());
                        intent = new Intent(Tus_Alquileres.this, Tus_Reviews_Coches.class);
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

        Intent intent = new Intent(Tus_Alquileres.this,MainActivity.class);
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
