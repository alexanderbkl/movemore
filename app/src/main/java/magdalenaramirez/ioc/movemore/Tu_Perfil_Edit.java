package magdalenaramirez.ioc.movemore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;

import magdalenaramirez.ioc.movemore.utiles.User;

public class Tu_Perfil_Edit extends AppCompatActivity {
    //Datos de las credenciales de inicio sesión
    private String file = "credenciales_login";
    private String fileContents;

    public static User user = new User(); //Get accesible in all sites - Mod
    //public static User usr = new User(); //Get accesible in all sites
    //User usr = new User();

    View view;
    // Elementos para mostrar valores
    private TextView mtxtV_nombre;
    private TextView mtxtV_apellidos;
    private TextView mtxtV_telefono;
    private TextView mtxtV_direccion;
    private TextView mtxtV_usuario;
    private TextView mtxtV_email;
    private TextView mtxtV_role;

    private String error = "";
    private String message="ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_perfil_edit);

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
                Intent intent = new Intent(Tu_Perfil_Edit.this, MenuPrincipal.class);
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

        // Botón Cancelar - CLICK
        Button mbtn_Cancelar = findViewById(R.id.btn_Cancelar);
        mbtn_Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tu_Perfil_Edit.this, MenuPrincipal.class);
                startActivity(intent);
            }
        });


        // Botón Guadar - CLICK
        Button mbtn_Guardar = findViewById(R.id.btn_Guardar);
        if (mbtn_Guardar != null) {
            mbtn_Guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mbtn_Guardar.setEnabled(false);

                    if(mtxtV_nombre.getText().toString().isEmpty()){
                        error+="Rellena el campo de nombre \n";
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }
                    if(mtxtV_apellidos.getText().toString().isEmpty()){
                        error+="Rellena el campo de apellidos \n";
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }
                    //if(intNasientos==0)error+="No pueden haber 0 asientos en un coche\n"; // me da error de null
                    if(mtxtV_telefono.getText().toString().isEmpty()){
                        error+="Rellena el campo de teléfono \n";
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }
                    if(mtxtV_direccion.getText().toString().isEmpty()){
                        error+="Rellena el campo de dirección \n";
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }
                    if(mtxtV_usuario.getText().toString().isEmpty()){
                        error+="Rellena el campo de usuario \n";
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }
                    if(mtxtV_email.getText().toString().isEmpty()){
                        error+="Rellena el campo del email \n";
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }
                    //Thread thread = new Thread(() -> {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //User usr = MenuPrincipal.usr;
                            //user.setId(Tu_Perfil.usr.getId());
                            System.out.println(user.getId());
                            //System.out.println(usr.getId());
                            user.setFirstname(mtxtV_nombre.getText().toString());
                            user.setLastname(mtxtV_apellidos.getText().toString());
                            user.setPhone(mtxtV_telefono.getText().toString());
                            user.setAddr(mtxtV_direccion.getText().toString());
                            user.setUsername(mtxtV_usuario.getText().toString());
                            user.setEmail(mtxtV_email.getText().toString());

                            try {
                                if (user.modifyUser(user)) {
                                    Tu_Perfil.usr = user;
                                    System.out.println("NUEVO:" + user.getFirstname());
                                    //System.out.println("ACTUALIZADO:" + usr.getFirstname());
                                    System.out.println("NUEVO:" + user.getId());
                                    //System.out.println("ACTUALIZADO:" + usr.getId());
                                    System.out.println("PERFIL MODIFICADO");
                                    Intent intent = new Intent(Tu_Perfil_Edit.this, MenuPrincipal.class);
                                    startActivity(intent);
                                } else {
                                    //jOptionPane.showMessageDialog(null, usr.getMessage());
                                    jOptionPane pane = jOptionPane.newInstance(user.getMessage());
                                    pane.show(getFragmentManager(), "error_dialog");
                                    mbtn_Guardar.setEnabled(true);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mbtn_Guardar.setEnabled(true);
                        }
                    }).start();
                    System.out.println("");
                }
            });
        }



        // Cierre del onCreate
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

        PopupMenu popupMenu = new PopupMenu(Tu_Perfil_Edit.this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.popup_Tu_Perfil:
                        // load "Tu_Perfil" Fragment
                        //loadFragment(new Tu_Perfil());
                        intent = new Intent(Tu_Perfil_Edit.this, Tu_Perfil.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Coches:
                        //loadFragment(new Tus_Coches());
                        intent = new Intent(Tu_Perfil_Edit.this, Tus_Coches.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Alquileres:
                        //loadFragment(new Tus_Alquileres());
                        intent = new Intent(Tu_Perfil_Edit.this, Tus_Alquileres.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Reviews:
                        //loadFragment(new Tus_Reviews());
                        intent = new Intent(Tu_Perfil_Edit.this, Tus_Reviews.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Reviews_de_tus_Coches:
                        //loadFragment(new Tus_Reviews_Coches());
                        intent = new Intent(Tu_Perfil_Edit.this, Tus_Reviews_Coches.class);
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

        Intent intent = new Intent(Tu_Perfil_Edit.this,MainActivity.class);
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

// Cierre Principal
}
