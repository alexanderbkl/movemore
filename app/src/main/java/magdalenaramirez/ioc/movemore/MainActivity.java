package magdalenaramirez.ioc.movemore;

import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import magdalenaramirez.ioc.movemore.utiles.User;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    TextView mTV_Main_Email;
    TextView mTV_Main_Passw;
    TextView mTV_Main_Registro;
    TextView mTV_Main_RecContra;

    SharedPreferences SM;
    RequestQueue requestQueue;
    //http://10.2.66.56/index.php/user/login?username=user_user1&password=11&email=asdgaehg@gaega
    private static final String URl_login="https://10.2.66.56/index.php/user/login";

    //Datos del txt de credenciales de login
    private String file = "credenciales_login";
    private String fileContents;
    private boolean isAdminLogin = false;
    private String emailString;
    private String passwString;

    User usr = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Comunicación respuesta JSON con Volley
        //Volley uso de redes, solicitudes o cargar datos en el servidor
        requestQueue = Volley.newRequestQueue(this);

        //Instancia de elementos
        mTV_Main_Email = (TextView) findViewById(R.id.TextViewEmail);
        mTV_Main_Passw = (TextView) findViewById(R.id.TextViewPassw);
        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);

        //Comprobar si tu aplicación tiene el permiso para acceder al almacenamiento externo,
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        //Leer fichero credenciales al arrancar app
        readFile_UserLogin();

        //Botón_CLICK LOGIN - Verificar login (email/contraseña)
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verificar email y contraseña de la respuesta JSON
                if (mTV_Main_Email.getText().toString().isEmpty()|| mTV_Main_Passw.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrectos!",Toast.LENGTH_LONG).show();
                }else {
                    loginRequest();
                }
            }
        });

        //TextView_CLICK - Registrar un nuevo Usuario
        mTV_Main_Registro = findViewById(R.id.TextViewRegistro);
        mTV_Main_Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Registro.class);
                startActivity(intent);
            }
        });

        //TextView_CLICK -Recuperar contraseña
        mTV_Main_RecContra = findViewById(R.id.TextViewRecContra);
        mTV_Main_RecContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    // Cierre del onCreate
    }

    //
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido, continuar con la operación que necesita este permiso
                } else {
                    // Permiso denegado, no se puede continuar con la operación que necesita este permiso
                }
                return;
            }
        }
    }


    private void writeFile_UserLogin(){
        fileContents = mTV_Main_Email.getText().toString() + "-" + mTV_Main_Passw.getText().toString();

        try{
            FileOutputStream fOut = openFileOutput(file, MODE_PRIVATE);
            fOut.write(fileContents.getBytes());
            fOut.close();
            File fileDir = new File(getFilesDir(),file);
            //Toast.makeText(getApplicationContext(),"File Saved at" + fileDir,Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readFile_UserLogin(){

        try{
            FileInputStream fIn = openFileInput(file);
            int c;
            String temp = "";
            while ((c = fIn.read())!= -1)
            {
                temp = temp + Character.toString((char)c);
            }
            //Evaluar si el fichero contiene usuario creado o está vacío el fichero
            if(temp != ""){
                Toast.makeText(getApplicationContext(),"usuario conectado: " + temp,Toast.LENGTH_LONG).show();

                //Formateamos credenciales del fichero temp
                String[] partes = temp.split("-");
                emailString = partes[0];
                passwString = partes[1];

                //Con SharedPreference recuperamos valores guardados al cerrar app
                SharedPreferences prefs = getSharedPreferences("sharedPrefName", Context.MODE_PRIVATE);
                boolean bool= prefs.getBoolean("isAdminLogin",isAdminLogin);

                loadTemp();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Request del LOGIN - AUTO
    private void loadTemp(){

        System.out.println(".......... LOAD AUTOMATICO............");
        //User usr = new User();
        new Thread(() -> {
            try {
                System.out.println("emailString:" + emailString);
                System.out.println("passString :" + passwString);
                if (usr.loadUser(emailString, passwString)) {
                    MenuPrincipal.usr = usr;

                    if (usr.isAdmin()) {
                        isAdminLogin = false;
                        Intent intent_admin = new Intent(getApplicationContext(), MenuPrincipal_admin.class);
                        startActivity(intent_admin);
                        finish();
                    } else {
                        isAdminLogin = true;
                        Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Credenciales inválidas!", Toast.LENGTH_LONG).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
        sharedPreference_login();
    }


    // Request del LOGIN - NORMAL
    private void loginRequest(){

        //User usr = new User();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (usr.loadUser(mTV_Main_Email.getText().toString(), mTV_Main_Passw.getText().toString())) {
                        //Guardar datos del usuario conectado - credenciales
                        writeFile_UserLogin();
                        // Instanciar objeto usr de la clase User que es static
                        MenuPrincipal.usr = usr;
                        //MenuPrincipal_admin.usr=usr;

                        if (usr.isAdmin()) {
                            isAdminLogin = false;
                            Intent intent_admin = new Intent(getApplicationContext(), MenuPrincipal_admin.class);
                            startActivity(intent_admin);
                            finish();
                        } else {
                            isAdminLogin = true;
                            Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Credenciales inválidas!", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        sharedPreference_login();
    }

    // Se guarda en fichero - Este punto no se utiliza
    private void sharedPreference_login(){
        SharedPreferences prefs= getSharedPreferences("sharedPrefName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= prefs.edit();
        editor.putBoolean("isAdminLogin", isAdminLogin);
        editor.apply();
    }

// Cierre principal
}