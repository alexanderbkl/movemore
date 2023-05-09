package magdalenaramirez.ioc.movemore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import magdalenaramirez.ioc.movemore.adapter.RecyclerViewAdapter_Car;
import magdalenaramirez.ioc.movemore.utiles.Car;
import magdalenaramirez.ioc.movemore.utiles.User;

public class MenuPrincipal_admin extends AppCompatActivity {

    //Datos de las credenciales de inicio sesión
    private String file = "credenciales_login";
    private String fileContents;

    private Intent intent;

    //RecyclerView
    RecyclerView recyclerView;
    List<Car> listCars;

    public static User usr = new User(); //Get accesible in all sites
    //public static Car coche = new Car(); //Get accesible in all sites

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //Toolbar - ActionBar - Logout
        ImageView mIM_Menu = findViewById(R.id.icon_menu);
        mIM_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });

        //Button Filtros
        Button mbtn_Filtros = (Button) findViewById(R.id.btn_Filtros);
        mbtn_Filtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MenuPrincipal_admin.this,MenuFiltros.class);
                startActivity(intent);
                finish();
            }
        });

        //Load coches
        loadCars();
        // Cierre onCreate
    }

    private void loadCars(){
        listCars=new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.recyclerJSON_Cars);
        RecyclerViewAdapter_Car adapter=new RecyclerViewAdapter_Car(this,listCars);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //User usr = new User();
        Car car = new Car();
        Thread thread = new Thread(() -> {

            //cars list (all):
            //http://10.2.66.56/index.php/user/carsall
            while (listCars.size() == 0) {
                listCars = car.getListPublic();
            }

            if (listCars.isEmpty()) {
                // Show an appropriate message if the request failed
                Toast.makeText(this,"Failed to get car list", Toast.LENGTH_LONG).show();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.agregarDatos(listCars);
                        adapter.setOnItemClickListener(new RecyclerViewAdapter_Car.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                // Detalle del coche(posición) del elemento clicado
                                //Intent intent;
                                Menu_Coche_Detalle.coche = listCars.get(position);
                                intent = new Intent(MenuPrincipal_admin.this, Menu_Coche_Detalle.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
        thread.start();
    }

    // Menu ActionBar - onCLICK()
    private void showMenu(View view){

        PopupMenu popupMenu = new PopupMenu(MenuPrincipal_admin.this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.popup_Tu_Perfil:
                        // load "Tu_Perfil" Fragment
                        //loadFragment(new Tu_Perfil());
                        intent = new Intent(MenuPrincipal_admin.this, Tu_Perfil.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Coches:
                        //loadFragment(new Tus_Coches());
                        intent = new Intent(MenuPrincipal_admin.this, Tus_Coches.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Alquileres:
                        //loadFragment(new Tus_Alquileres());
                        intent = new Intent(MenuPrincipal_admin.this, Tus_Alquileres.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Reviews:
                        //loadFragment(new Tus_Reviews());
                        intent = new Intent(MenuPrincipal_admin.this, Tus_Reviews.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Reviews_de_tus_Coches:
                        //loadFragment(new Tus_Reviews_Coches());
                        intent = new Intent(MenuPrincipal_admin.this, Tus_Reviews_Coches.class);
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

    // Cargar los Fragments (NO UTILIZADO)
    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayoutMenuPrincipal, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    private void logoutSes(){
        //Borramos las credenciales
        deleteFile_UserLogin();

        SharedPreferences SM = getSharedPreferences("userrecord",0);
        SharedPreferences.Editor edit = SM.edit();
        edit.putBoolean("userlogin",false);
        edit.commit();

        intent = new Intent(MenuPrincipal_admin.this,MainActivity.class);
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