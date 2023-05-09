package magdalenaramirez.ioc.movemore;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import magdalenaramirez.ioc.movemore.utiles.Car;
import magdalenaramirez.ioc.movemore.utiles.Rentals;
import magdalenaramirez.ioc.movemore.utiles.User;

public class Menu_Coche_Detalle extends AppCompatActivity {

    //Datos de las credenciales de inicio sesión
    private String file = "credenciales_login";
    private String fileContents;

    View view;
    public static Car coche = new Car(); //Get accesible in all sites

    Calendar calendar = Calendar.getInstance();

    private ImageView mimageView_Coche;
    private TextView mEdit_Marca;
    private TextView mEdit_Modelo;
    private TextView mEdit_Año;
    private TextView mEdit_Num_Asientos;
    private TextView mEdit_Color;
    private TextView mEdit_Combustible;
    private TextView mEdit_Precio_Dia;
    private TextView mEdit_Total;

    private EditText metxt_fecha_ini;
    private EditText metxt_fecha_fin;

    private double doublePriceD;
    private double doublePriceT;
    private LocalDate fecha_ini;
    private LocalDate fecha_fin;

    private Date dateIni = null;
    private Date dateFin = null;

    private String urlValid = "";
    private String message="ERROR";

    User usr = new User();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_coche_detalle);

        //ImageView logo - CLICK - volver al main
        ImageView mImageVCab = findViewById(R.id.ImageVCab);
        mImageVCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu_Coche_Detalle.this, MenuPrincipal.class);
                startActivity(intent);
            }
        });

        //Instanciar elementos del Item Layout
        mimageView_Coche = (ImageView) findViewById(R.id.imageView_Coche);
        mEdit_Marca = (TextView) findViewById(R.id.edit_Marca);
        mEdit_Modelo = (TextView) findViewById(R.id.edit_Modelo);
        mEdit_Año = (TextView) findViewById(R.id.edit_Año);
        mEdit_Num_Asientos = (TextView) findViewById(R.id.edit_Num_Asientos);
        mEdit_Color = (TextView) findViewById(R.id.edit_Color);
        mEdit_Combustible = (TextView) findViewById(R.id.edit_Combustible);
        mEdit_Precio_Dia = (TextView) findViewById(R.id.edit_Precio_Dia);
        mEdit_Total = (TextView) findViewById(R.id.edit_Total);

        // EditText de "Fecha inicio" - onCLICK
        metxt_fecha_ini = (EditText) findViewById(R.id.etxt_fecha_ini);
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        metxt_fecha_ini.setText(twoDigits(dayOfMonth) + "/" + twoDigits(month) + "/" + year);
        metxt_fecha_ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.etxt_fecha_ini:
                        showDatePickerDialog(metxt_fecha_ini);
                        break;
                }
            }
        });

        // EditText de "Fecha fin" - onCLICK
        metxt_fecha_fin = (EditText) findViewById(R.id.etxt_fecha_fin);
        metxt_fecha_fin.setText(twoDigits(dayOfMonth) + "/" + twoDigits(month) + "/" + year);
        metxt_fecha_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.etxt_fecha_fin:
                        showDatePickerDialog(metxt_fecha_fin);
                        break;
                }
            }
        });

        refreshTotalPrice();

        //Botón CANCELAR - onCLICK
        Button mbtn_cancelar = (Button) findViewById(R.id.btn_Cancelar);
        mbtn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu_Coche_Detalle.this, MenuPrincipal.class);
                startActivity(intent);
            }
        // Cierra OnClickListener - mbtn_cancelar
        });

        //Botón ALQUILER - onCLICK
        Button mbtn_alquilar = (Button) findViewById(R.id.btn_Alquilar);
        mbtn_alquilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validaciones
                if(coche.getID_OWNER() == usr.getId()){
                    Toast.makeText(Menu_Coche_Detalle.this,"No puedes alquilar un coche que es de tu posesion", Toast.LENGTH_LONG).show();
                    return;
                }
                String err="";
                System.out.println();
                //if(metxt_fecha_ini.toString() == null) err+="Seleccione una fecha de inicio\n";
                //if(metxt_fecha_fin.toString() == null) err+="Seleccione una fecha de finalización\n";
                if (fecha_ini.equals(fecha_fin)){
                    err+="Las fechas deben ser diferente\n";
                }
                if(metxt_fecha_ini.toString() != null && metxt_fecha_fin.toString()!= null) {
                    //err+="Fechas informadas\n";
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date fechaIni = dateFormat.parse(metxt_fecha_ini.getText().toString());
                        Date fechaFin = dateFormat.parse(metxt_fecha_fin.getText().toString());

                        if (fechaIni.compareTo(fechaFin) > 0) {
                            // metxt_fecha_ini es mayor que metxt_fecha_fin
                            err+="La fecha de inicio es mayor a la fecha de finalización.\n";
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (!err.equals("")) {
                    Toast.makeText(Menu_Coche_Detalle.this, err, Toast.LENGTH_LONG).show();
                    return;
                }

                // Código del método onClick
                // Create and start the thread
                Thread thread = new Thread(() -> {
                    dateIni = Date.from(fecha_ini.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    dateFin = Date.from(fecha_fin.atStartOfDay(ZoneId.systemDefault()).toInstant());

                    Rentals rentals = new Rentals();
                    rentals.setCoche(coche);
                    rentals.setFecha_Fin(dateFin);
                    rentals.setFecha_In(dateIni);
                    rentals.setTotal_cost(doublePriceT);
                    if (usr.addNewAlquiler(rentals)) {
                        Menu_Coche_Detalle.coche = coche;
                        Intent intent = new Intent(Menu_Coche_Detalle.this, Tus_Alquileres.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Menu_Coche_Detalle.this, "No se ha podido realizar el alquiler", Toast.LENGTH_LONG).show();
                    }
                    //coche.setClase();
                    mbtn_alquilar.setEnabled(true);
                });
                thread.start();
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
        showDetailCar();
    // Cierre onCreate
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refreshTotalPrice(){
        fecha_ini = LocalDate.parse(metxt_fecha_ini.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        fecha_fin = LocalDate.parse(metxt_fecha_fin.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        doublePriceT = (double) (ChronoUnit.DAYS.between(fecha_ini, fecha_fin) * coche.getPricePerDay());
        mEdit_Total.setText(String.valueOf(doublePriceT));
    }

    // Calendario - PickerDialog para obtener fechas
    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                editText.setText(selectedDate);
                System.out.println("showDatePickerDialog:" + selectedDate);

                fecha_ini = LocalDate.parse(metxt_fecha_ini.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                fecha_fin = LocalDate.parse(metxt_fecha_fin.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                doublePriceT = (double) (ChronoUnit.DAYS.between(fecha_ini, fecha_fin) * coche.getPricePerDay());
                mEdit_Total.setText(String.valueOf(doublePriceT));
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }


    // Mostrar datos de coche
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

        // Obtener los valores de tipo String
        mEdit_Marca.setText(coche.getMarca());
        mEdit_Modelo.setText(coche.getModelo());
        mEdit_Color.setText(coche.getColor());
        mEdit_Combustible.setText(coche.getCombustible().name());

        // Obtener el valor int de getPricePerDay
        doublePriceD = coche.getPricePerDay();
        // Convertir el valor int en una cadena de texto
        String priceDString = String.valueOf(doublePriceD);
        // Establecer el texto en el TextView
        mEdit_Precio_Dia.setText(priceDString);

        // Obtener la imagen
        Glide.with(this)
                .load(coche.getUrlImage())
                .into(mimageView_Coche);
    }

    // Menu ActionBar - onCLICK()
    private void showMenu(View view){

        PopupMenu popupMenu = new PopupMenu(Menu_Coche_Detalle.this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.popup_Tu_Perfil:
                        // load "Tu_Perfil" Fragment
                        //loadFragment(new Tu_Perfil());
                        intent = new Intent(Menu_Coche_Detalle.this, Tu_Perfil.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Coches:
                        //loadFragment(new Tus_Coches());
                        intent = new Intent(Menu_Coche_Detalle.this, Tus_Coches.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Alquileres:
                        //loadFragment(new Tus_Alquileres());
                        intent = new Intent(Menu_Coche_Detalle.this, Tus_Alquileres.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Reviews:
                        //loadFragment(new Tus_Reviews());
                        intent = new Intent(Menu_Coche_Detalle.this, Tus_Reviews.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Reviews_de_tus_Coches:
                        //loadFragment(new Tus_Reviews_Coches());
                        intent = new Intent(Menu_Coche_Detalle.this, Tus_Reviews_Coches.class);
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

        Intent intent = new Intent(Menu_Coche_Detalle.this,MainActivity.class);
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

