package magdalenaramirez.ioc.movemore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import magdalenaramirez.ioc.movemore.adapter.RecyclerViewAdapter_Car;
import magdalenaramirez.ioc.movemore.utiles.Car;
import magdalenaramirez.ioc.movemore.utiles.User;

public class Tus_Coches extends AppCompatActivity {

    //Datos de las credenciales de inicio sesión
    private String file = "credenciales_login";
    private String fileContents;

    //RecyclerView para mostrar datos y List de la clase
    RecyclerView recyclerView;
    List<Car> listCars;
    View view;

    public static Car car = new Car(); //Get accesible in all sites
    public static User usr = new User(); //Get accesible in all sites

    private String message="ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tus_coches);

        //ImageView logo - CLICK - volver al main
        ImageView mImageVCab = findViewById(R.id.ImageVCab);
        mImageVCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tus_Coches.this, MenuPrincipal.class);
                startActivity(intent);
            }
        });

        // Botón Añadir - CLICK
        Button mbtn_nuevo = findViewById(R.id.btn_nuevoCoche);
        mbtn_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tus_Coches.this, Tus_Coches_Nuevo.class);
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

        // Cargamos los datos de todos los coches by user
        loadCars();
    }

    private void loadCars(){

        listCars=new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.recyclerJSON_Cars);
        RecyclerViewAdapter_Car adapter=new RecyclerViewAdapter_Car(this,listCars);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //User usr = new User();
        //Obtenemos el usuario de MenuPrincipal
        User usr = MenuPrincipal.usr;
        System.out.println("--- TUS COCHES, USR VALE: " + usr.getId());
        Thread thread = new Thread(() -> {
            //cars list by owner_id:(owner_id is user_id from users)
            //http://10.2.66.56/index.php/user/carsbyowner?owner_id=1
            while (listCars.isEmpty() && !usr.getMessage().equals("no rows returned")) {
                System.out.println(" TUS COCHES CLASE ------: " + usr.getId());
                listCars = usr.getListMyCars();
            }

            if (listCars.isEmpty()) {
                // Show an appropriate message if the request failed
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Tus_Coches.this, "Failed to get car list", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                this.runOnUiThread(() -> {
                    adapter.agregarDatos(listCars);
                    //Nueva actividad con el detalle del coche clickado (position)
                    adapter.setOnItemClickListener(new RecyclerViewAdapter_Car.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            // Detalle del coche(posición) del elemento clicado
                            Intent intent;
                            Tus_Coches_Detalle.coche = listCars.get(position);
                            intent = new Intent(Tus_Coches.this, Tus_Coches_Detalle.class);
                            startActivity(intent);
                        }
                    });
                });
            }
        });
        thread.start();
        // Cierre loadCars
    }


    // Menu ActionBar - onCLICK()
    private void showMenu(View view){

        PopupMenu popupMenu = new PopupMenu(Tus_Coches.this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.popup_Tu_Perfil:
                        // load "Tu_Perfil" Fragment
                        //loadFragment(new Tu_Perfil());
                        intent = new Intent(Tus_Coches.this, Tu_Perfil.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Coches:
                        //loadFragment(new Tus_Coches());
                        intent = new Intent(Tus_Coches.this, Tus_Coches.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Alquileres:
                        //loadFragment(new Tus_Alquileres());
                        intent = new Intent(Tus_Coches.this, Tus_Alquileres.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Reviews:
                        //loadFragment(new Tus_Reviews());
                        intent = new Intent(Tus_Coches.this, Tus_Reviews.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Reviews_de_tus_Coches:
                        //loadFragment(new Tus_Reviews_Coches());
                        intent = new Intent(Tus_Coches.this, Tus_Reviews_Coches.class);
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

        Intent intent = new Intent(Tus_Coches.this,MainActivity.class);
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


// Cierre principal
}
