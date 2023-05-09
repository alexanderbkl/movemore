package magdalenaramirez.ioc.movemore;

import android.content.Context;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

import magdalenaramirez.ioc.movemore.utiles.User;

public class Tu_Perfil extends AppCompatActivity {

    //Datos de las credenciales de inicio sesión
    private String file = "credenciales_login";
    private String fileContents;

    public static User usr = new User(); //Get accesible in all sites

    View view;
    // Elementos para mostrar valores
    private TextView mtxtV_nombre;
    private TextView mtxtV_apellidos;
    private TextView mtxtV_telefono;
    private TextView mtxtV_direccion;
    private TextView mtxtV_usuario;
    private TextView mtxtV_email;
    private TextView mtxtV_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_perfil);

        //Instanciar Elementos
        mtxtV_nombre = (TextView) findViewById(R.id.txtV_Nombre);
        mtxtV_apellidos = (TextView) findViewById(R.id.txtV_Apellidos);
        mtxtV_telefono = (TextView) findViewById(R.id.txtV_Telefono);
        mtxtV_direccion = (TextView) findViewById(R.id.txtV_Direccion);
        mtxtV_usuario = (TextView) findViewById(R.id.txtV_Usuario);
        mtxtV_email = (TextView) findViewById(R.id.txtV_Email);
        mtxtV_role = (TextView) findViewById(R.id.txtV_Role);

        //ImageView logo - CLICK - volver al main
        ImageView mImageVCab = findViewById(R.id.ImageVCab);
        mImageVCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tu_Perfil.this, MenuPrincipal.class);
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

        //Mostrar datos del profile user
        showDetailUser();

        // Botón Modificar - CLICK
        Button mbtn_Modificar = findViewById(R.id.btn_Modificar);
        mbtn_Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tu_Perfil_Edit.user = usr;
                Intent intent = new Intent(Tu_Perfil.this, Tu_Perfil_Edit.class);
                startActivity(intent);
            }
        });

        // Botón Eliminar - CLICK
        Button mbtn_Eliminar = findViewById(R.id.btn_Eliminar);
        mbtn_Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo(view);
            }
        });

    // Cierre del onCreate
    }

    public void mostrarDialogo(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro/a?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Si el usuario hace clic en "Sí", se ejecutará el código aquí
                Log.i("Eliminar", "Datos borrados");
                Toast.makeText(Tu_Perfil.this, "Datos borrados", Toast.LENGTH_LONG).show();

                Thread thread = new Thread(() -> {
                    try {
                        if(usr.delUser(usr)){
                            Tu_Perfil.usr = usr;
                            System.out.println(" TUS PERFIL A BORRAR ------: " + usr.getId());
                            System.out.println("PERFIL ELIMINADO");
                            Intent intent = new Intent(Tu_Perfil.this, MainActivity.class);
                            startActivity(intent);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

    private void showDetailUser(){

        SharedPreferences prefs = getSharedPreferences("sharedPrefName", Context.MODE_PRIVATE);

        //Obtenemos el usuario de MenuPrincipal
        User usr = MenuPrincipal.usr;
        System.out.println(" TUS PERFIL CLASE ------: " + usr.getId());
        System.out.println(usr.getFirstname());

        mtxtV_nombre.setText(usr.getFirstname());
        mtxtV_apellidos.setText(usr.getLastname());
        mtxtV_direccion.setText(usr.getAddr());
        mtxtV_telefono.setText(usr.getPhone());
        mtxtV_usuario.setText(usr.getUsername());
        mtxtV_email.setText(usr.getEmail());
        //locLabel.setText(usr.getLocation());
        //bioLabel.setText(usr.getBio());
        mtxtV_role.setText(usr.isAdmin()?"Admin":"User");

    }

    // Menu ActionBar - onCLICK()
    private void showMenu(View view){

        PopupMenu popupMenu = new PopupMenu(Tu_Perfil.this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.popup_Tu_Perfil:
                        // load "Tu_Perfil" Fragment
                        //loadFragment(new Tu_Perfil());
                        intent = new Intent(Tu_Perfil.this, Tu_Perfil.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Coches:
                        //loadFragment(new Tus_Coches());
                        intent = new Intent(Tu_Perfil.this, Tus_Coches.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Alquileres:
                        //loadFragment(new Tus_Alquileres());
                        intent = new Intent(Tu_Perfil.this, Tus_Alquileres.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Reviews:
                        //loadFragment(new Tus_Reviews());
                        intent = new Intent(Tu_Perfil.this, Tus_Reviews.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Reviews_de_tus_Coches:
                        //loadFragment(new Tus_Reviews_Coches());
                        intent = new Intent(Tu_Perfil.this, Tus_Reviews_Coches.class);
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

        Intent intent = new Intent(Tu_Perfil.this,MainActivity.class);
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
