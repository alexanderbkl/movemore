package magdalenaramirez.ioc.movemore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import magdalenaramirez.ioc.movemore.utiles.Car;
import magdalenaramirez.ioc.movemore.utiles.User;

public class Tus_Coches_Detalle_Edit extends AppCompatActivity {

    //Datos de las credenciales de inicio sesión
    private String file = "credenciales_login";
    private String fileContents;

    View view;
    //public Car coche;
    public static Car coche = new Car(); //Get accesible in all sites

    private ImageView mimageView_Coche;
    private Spinner mEdit_Marca;
    private EditText mEdit_Modelo;
    private EditText mEdit_Año;
    private Spinner mEdit_Num_Asientos;
    private Spinner mEdit_Color;
    private EditText mEdit_Matricula;
    private Spinner mEdit_Combustible;
    private EditText mEdit_Precio_Dia;
    private EditText mEdit_Url;

    private String spnNasientos;
    private String spnColor,spnMarca;

    private String spnAño;
    private String urlValid = "";
    private String error = "";
    private String message="ERROR";

    private Integer intAño, intNasientos;
    User usr = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tus_coches_detalle_edit);

        //ImageView logo - CLICK - volver al main
        ImageView mImageVCab = findViewById(R.id.ImageVCab);
        mImageVCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tus_Coches_Detalle_Edit.this, MenuPrincipal.class);
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
        mEdit_Precio_Dia = findViewById(R.id.edit_Precio_Dia);
        mEdit_Url = findViewById(R.id.edit_Url);

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

        // Botón Cancelar - CLICK
        Button mbtn_Cancelar = findViewById(R.id.btn_Cancelar);
        mbtn_Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tus_Coches_Detalle_Edit.this, MenuPrincipal.class);
                startActivity(intent);
            }
        });

        // Spinner Nº Asientos
        mEdit_Num_Asientos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spnNasientos = parent.getItemAtPosition(position).toString();
                System.out.println(spnNasientos);
                // Aquí puedes usar la variable "selectedValue" para hacer lo que necesites con el valor seleccionado
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Este método se llama cuando no hay elementos seleccionados en el Spinner
            }
        });

        // Spinner Color
        mEdit_Color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spnColor = parent.getItemAtPosition(position).toString();
                System.out.println(spnColor);
                // Aquí puedes usar la variable "selectedValue" para hacer lo que necesites con el valor seleccionado
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Este método se llama cuando no hay elementos seleccionados en el Spinner
            }
        });

        // Spinner Marca
        mEdit_Marca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spnMarca = parent.getItemAtPosition(position).toString();
                System.out.println(spnMarca);
                // Aquí puedes usar la variable "selectedValue" para hacer lo que necesites con el valor seleccionado
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Este método se llama cuando no hay elementos seleccionados en el Spinner
            }
        });

        // Spinner Año
        /*
        List<String> years = new ArrayList<String>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1900; i <= currentYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEdit_Año.setAdapter(adapter);
        mEdit_Num_Asientos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spnAño = parent.getItemAtPosition(position).toString();
                System.out.println(spnAño);
                // Aquí puedes usar la variable "selectedValue" para hacer lo que necesites con el valor seleccionado
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Este método se llama cuando no hay elementos seleccionados en el Spinner
            }
        });

        */

        Button mbtn_Lupa = findViewById(R.id.btn_Foto);
        mbtn_Lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlValid = mEdit_Url.getText().toString();
                System.out.println(urlValid);
                // Obtener la imagen
                Glide.with(Tus_Coches_Detalle_Edit.this)
                        .load(urlValid)
                        .into(mimageView_Coche);
            }
        });


        // Botón Guadar - CLICK
        Button mbtn_Guardar = findViewById(R.id.btn_Guardar);
        if (mbtn_Guardar != null) {
            mbtn_Guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mbtn_Guardar.setEnabled(false);

                    if(urlValid.isEmpty()){
                        error+="Selecione una imagen valida \n";
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }
                    // Es un spinner
                    //if(mEdit_Marca.getText().toString().isEmpty()){
                    //    error+="Rellena el campo de Marca \n";
                    //    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    //}

                    if(mEdit_Modelo.getText().toString().isEmpty()){
                        error+="Rellena el campo de Modelo \n";
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }

                    // Es un spinner
                    //if(intNasientos==0)error+="No pueden haber 0 asientos en un coche\n";
                    // Es un spinner
                    //if(mEdit_Color.getText().toString().isEmpty()){
                    //    error+="Rellena el campo de Color \n";
                    //    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    //}
                    if(mEdit_Matricula.getText().toString().isEmpty()){
                        error+="Rellena el campo de Matricula \n";
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }

                    //Thread thread = new Thread(() -> {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int intAño = 0;
                            if (!mEdit_Año.getText().toString().isEmpty()) {
                                intAño = Integer.parseInt(mEdit_Año.getText().toString());
                            }

                            //int intNasientos = 0;
                            //if (!mEdit_Num_Asientos.getText().toString().isEmpty()) {
                            //    intNasientos = Integer.parseInt(mEdit_Num_Asientos.getText().toString());
                            //}
                            if (spnNasientos != null && !spnNasientos.isEmpty()) {
                                intNasientos = Integer.parseInt(spnNasientos);
                                // Aquí puedes hacer algo con el valor numérico
                            } else {
                                // Aquí puedes manejar el caso en el que la cadena sea nula o vacía
                                error+="Selecione el número de asientos \n";
                            }
                            coche.setAño(intAño);
                            coche.setNumeroAsientos(intNasientos);
                            coche.setPricePerDay(Double.valueOf(mEdit_Precio_Dia.getText().toString()));

                            // Tipo String
                            //coche.setColor(mEdit_Color.getText().toString()); // Es un spinner
                            coche.setColor(spnColor);
                            //coche.setMarca(mEdit_Marca.getText().toString());
                            coche.setMarca(spnMarca);

                            coche.setModelo(mEdit_Modelo.getText().toString());
                            coche.setMatricula(mEdit_Matricula.getText().toString());
                            urlValid = mEdit_Url.getText().toString();
                            coche.setUrlImage(urlValid);

                            int index = mEdit_Combustible.getSelectedItemPosition()+1;
                            for (Car.claseEnum clase : Car.claseEnum.values()) {
                                if (clase.id == index) {
                                    coche.setClase(clase);
                                    break;
                                }
                            }

                            if (usr.modifyCar(coche)) {
                                Tus_Coches.car = coche;
                                System.out.println("REGISTRO MODIFICADO");
                                Intent intent = new Intent(Tus_Coches_Detalle_Edit.this, Tus_Coches.class);
                                startActivity(intent);
                            } else {
                                //jOptionPane.showMessageDialog(null, usr.getMessage());
                                jOptionPane pane = jOptionPane.newInstance(usr.getMessage());
                                pane.show(getFragmentManager(), "error_dialog");
                                mbtn_Guardar.setEnabled(true);
                            }
                            //coche.setClase();
                            mbtn_Guardar.setEnabled(true);
                        }
                    }).start();
                    System.out.println("");
                }
            });
        }
    // Cierre onCreate
    }

    private void showDetailCar(){

        // Obtener el valor int de getAño
        int intAño = coche.getAño();
        // Convertir el valor int en una cadena de texto
        String añoString = String.valueOf(intAño);
        // Establecer el texto en el TextView
        mEdit_Año.setText(añoString);

        //Es un spinner
        // Obtener el valor int de getNumeroAsientos
        //int intNasientos = coche.getNumeroAsientos();
        // Convertir el valor int en una cadena de texto
        //String nAsientosString = String.valueOf(intNasientos);
        // Establecer el texto en el TextView
        //mEdit_Num_Asientos.setText(nAsientosString); // Es un spinner

        // Obtener el valor int de getPricePerDay
        Double intPriceD = coche.getPricePerDay();
        // Convertir el valor int en una cadena de texto
        String priceDString = String.valueOf(intPriceD);
        // Establecer el texto en el TextView
        mEdit_Precio_Dia.setText(priceDString);

        // Obtener los valores de tipo String
        //Es un spinner
        //mEdit_Color.setText(coche.getColor()); // Es un spinner
        //mEdit_Marca.setText(coche.getMarca()); //Es un spinner

        mEdit_Modelo.setText(coche.getModelo());
        mEdit_Matricula.setText(coche.getMatricula());
        //mEdit_Combustible.setText(coche.getCombustible().name()); // Es un spinner

        // Obtener la imagen
        Glide.with(this)
                .load(coche.getUrlImage())
                .into(mimageView_Coche);
    }

    // Menu ActionBar - onCLICK()
    private void showMenu(View view){

        PopupMenu popupMenu = new PopupMenu(Tus_Coches_Detalle_Edit.this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.popup_Tu_Perfil:
                        // load "Tu_Perfil" Fragment
                        //loadFragment(new Tu_Perfil());
                        intent = new Intent(Tus_Coches_Detalle_Edit.this, Tu_Perfil.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Coches:
                        //loadFragment(new Tus_Coches());
                        intent = new Intent(Tus_Coches_Detalle_Edit.this, Tus_Coches.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Alquileres:
                        //loadFragment(new Tus_Alquileres());
                        intent = new Intent(Tus_Coches_Detalle_Edit.this, Tus_Alquileres.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Reviews:
                        //loadFragment(new Tus_Reviews());
                        intent = new Intent(Tus_Coches_Detalle_Edit.this, Tus_Reviews.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Reviews_de_tus_Coches:
                        //loadFragment(new Tus_Reviews_Coches());
                        intent = new Intent(Tus_Coches_Detalle_Edit.this, Tus_Reviews_Coches.class);
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

        Intent intent = new Intent(Tus_Coches_Detalle_Edit.this,MainActivity.class);
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


