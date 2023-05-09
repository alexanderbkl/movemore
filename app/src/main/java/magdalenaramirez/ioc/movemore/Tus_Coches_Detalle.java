package magdalenaramirez.ioc.movemore;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import magdalenaramirez.ioc.movemore.utiles.Car;
import magdalenaramirez.ioc.movemore.utiles.User;

public class Tus_Coches_Detalle extends AppCompatActivity {

    //Datos de las credenciales de inicio sesión
    private String file = "credenciales_login";
    private String fileContents;

    View view;
    //public Car coche;
    public static Car coche = new Car(); //Get accesible in all sites

    private ImageView mimageView_Coche;
    private TextView mEdit_Marca;
    private TextView mEdit_Modelo;
    private TextView mEdit_Año;
    private TextView mEdit_Num_Asientos;
    private TextView mEdit_Color;
    private TextView mEdit_Matricula;
    private Spinner mEdit_Combustible;
    private TextView mEdit_Precio_Dia;

    private String urlValid = "";
    private String message="ERROR";
    private Intent intent = null;

    User usr = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tus_coches_detalle);

        //ImageView logo - CLICK - volver al main
        ImageView mImageVCab = findViewById(R.id.ImageVCab);
        mImageVCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tus_Coches_Detalle.this, MenuPrincipal.class);
                startActivity(intent);
            }
        });

        //Instanciar elementos del Item Layout
        mimageView_Coche = (ImageView) findViewById(R.id.imageView_Coche);

        mEdit_Marca = findViewById(R.id.edit_Marca);
        mEdit_Modelo = findViewById(R.id.edit_Modelo);
        mEdit_Año = findViewById(R.id.edit_Año);
        mEdit_Num_Asientos = findViewById(R.id.edit_Num_Asientos);
        mEdit_Color = findViewById(R.id.edit_Color);
        mEdit_Matricula =  findViewById(R.id.edit_Matricula);
        mEdit_Combustible = findViewById(R.id.edit_Combustible);
        mEdit_Precio_Dia =  findViewById(R.id.edit_Precio_Dia);

        //Toolbar - ActionBar - Logout
        ImageView mIM_Menu = findViewById(R.id.icon_menu);
        mIM_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });

        //Mostrar datos del profile user
        showDetailCar();

        // Botón Modificar - CLICK
        Button mbtn_Modificar = findViewById(R.id.btn_Modificar);
        if (mbtn_Modificar != null) {
            mbtn_Modificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Tus_Coches_Detalle_Edit.coche = coche;
                    intent = new Intent(Tus_Coches_Detalle.this, Tus_Coches_Detalle_Edit.class);
                    startActivity(intent);
                }
            });
        }

        // Botón Eliminar - CLICK
        Button mbtn_Eliminar = findViewById(R.id.btn_Eliminar);
        mbtn_Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo(view);
            }
        });


    // Cierre onCreate
    }

    public void mostrarDialogo(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro/a?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Si el usuario hace clic en "Sí", se ejecutará el código aquí
                Log.i("Eliminar", "Datos borrados");
                Toast.makeText(Tus_Coches_Detalle.this, "Datos borrados", Toast.LENGTH_LONG).show();

                Thread thread = new Thread(() -> {
                    if(usr.deleteCar(coche)){
                        Tus_Coches.car = coche;
                        System.out.println("REGISTRO ELIMINADO");
                        Intent intent = new Intent(Tus_Coches_Detalle.this, Tus_Coches.class);
                        startActivity(intent);

                    }
                });
                thread.start();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Si el usuario hace clic en "No", se cerrará la ventana emergente
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    // Cierre del mostrarDialogo
    }

    private void showDetailCar(){

        // Obtener el valor int de getAño
        int intAño = coche.getAño();
        // Convertir el valor int en una cadena de texto
        String añoString = String.valueOf(intAño);
        // Establecer el texto en el TextView
        mEdit_Año.setText(añoString);

        // Obtener el valor int de getNumeroAsientos
        int intNasientos = coche.getNumeroAsientos();
        // Convertir el valor int en una cadena de texto
        String nAsientosString = String.valueOf(intNasientos);
        // Establecer el texto en el TextView
        mEdit_Num_Asientos.setText(nAsientosString);

        // Obtener el valor int de getPricePerDay
        Double intPriceD = coche.getPricePerDay();
        // Convertir el valor int en una cadena de texto
        String priceDString = String.valueOf(intPriceD);
        // Establecer el texto en el TextView
        mEdit_Precio_Dia.setText(priceDString);
        //coche.setPricePerDay(Integer.valueOf(mEdit_Precio_Dia.getText().toString()));

        // Obtener los valores de tipo String
        mEdit_Color.setText(coche.getColor());
        mEdit_Marca.setText(coche.getMarca());
        mEdit_Modelo.setText(coche.getModelo());
        mEdit_Matricula.setText(coche.getMatricula());
       //mEdit_Combustible.setText(coche.getCombustible().name());

        int index = mEdit_Combustible.getSelectedItemPosition()+1;
        for (Car.claseEnum clase : Car.claseEnum.values()) {
            if (clase.id == index) {
                coche.setClase(clase);
                break;
            }
        }

        // Obtener la imagen
        Glide.with(this)
                .load(coche.getUrlImage())
                .into(mimageView_Coche);
    }


    // Menu ActionBar - onCLICK()
    private void showMenu(View view){

        PopupMenu popupMenu = new PopupMenu(Tus_Coches_Detalle.this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.popup_Tu_Perfil:
                        // load "Tu_Perfil" Fragment
                        //loadFragment(new Tu_Perfil());
                        intent = new Intent(Tus_Coches_Detalle.this, Tu_Perfil.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Coches:
                        //loadFragment(new Tus_Coches());
                        intent = new Intent(Tus_Coches_Detalle.this, Tus_Coches.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Alquileres:
                        //loadFragment(new Tus_Alquileres());
                        intent = new Intent(Tus_Coches_Detalle.this, Tus_Alquileres.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Reviews:
                        //loadFragment(new Tus_Reviews());
                        intent = new Intent(Tus_Coches_Detalle.this, Tus_Reviews.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Reviews_de_tus_Coches:
                        //loadFragment(new Tus_Reviews_Coches());
                        intent = new Intent(Tus_Coches_Detalle.this, Tus_Reviews_Coches.class);
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

        Intent intent = new Intent(Tus_Coches_Detalle.this,MainActivity.class);
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

