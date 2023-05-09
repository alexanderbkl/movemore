package magdalenaramirez.ioc.movemore;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

public class MenuFiltros extends AppCompatActivity {

    private Spinner spinner_ordenar;
    private Spinner spinner_ciudad;
    private Spinner spinner_carburante;
    private Spinner spinner_modelo;

    private SeekBar sb_precio_min;
    private SeekBar sb_precio_max;
    private SeekBar sb_ano;

    // Variables para mostrar valores seekBar
    private TextView mtxt_pmin;
    private TextView mtxt_pmax;
    private EditText metxt_ano;

    private EditText metxt_fecha_ini;
    private EditText metxt_fecha_fin;

    private Button mbtn_aplica_filtros;

    private String file = "credenciales_login";
    private String fileContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_filtros);

        //Toolbar - ActionBar - Logout
        ImageView mIM_Menu = findViewById(R.id.icon_menu);
        mIM_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });

        // Spiner de "Ordenar por"
        spinner_ordenar = (Spinner) findViewById(R.id.spn_ordenar);
        String []opc1={"Todas las ciudades","Todos los carburantes","Todos los modelos","Todos los precios","Todos los años"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, opc1);
        spinner_ordenar.setAdapter(adapter1);

        // Spiner de "Ciurdad"
        spinner_ciudad = (Spinner) findViewById(R.id.spn_ciudad);
        String []opc2={"Barcelona","Tarragona","Madrid","Sevilla","Valencia"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, opc2);
        spinner_ciudad.setAdapter(adapter2);

        // Spiner de "Carburante"
        spinner_carburante = (Spinner) findViewById(R.id.spn_carburante);
        String []opc3={"Diesel","Gasolina","Eléctrico","Híbrido"};
        ArrayAdapter<String> adapter3= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, opc3);
        spinner_carburante.setAdapter(adapter3);

        // Spiner de "Modelo"
        spinner_modelo = (Spinner) findViewById(R.id.spn_modelo);
        String []opc4={"Seat","Nissan","BMW","Mercedes","Mazda"};
        ArrayAdapter<String> adapter4= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, opc4);
        spinner_modelo.setAdapter(adapter4);

        // SeekBar de "Precio Mínimo"
        mtxt_pmin = (TextView) findViewById(R.id.txt_pmin);
        sb_precio_min = (SeekBar) findViewById(R.id.seekB_precio_min);
        sb_precio_min.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mtxt_pmin.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // SeekBar de "Precio Máximo"
        mtxt_pmax = (TextView) findViewById(R.id.txt_pmax);
        sb_precio_max = (SeekBar) findViewById(R.id.seekB_precio_max);
        sb_precio_max.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mtxt_pmax.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // EditText de "Fecha inicio" - onCLICK
        metxt_fecha_ini = (EditText) findViewById(R.id.etxt_fecha_ini);
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

        // EditText de "Año"
        metxt_ano = (EditText) findViewById(R.id.etxt_ano);

        // Botón "Aplicar filtros" - onCLICK
        mbtn_aplica_filtros = (Button) findViewById(R.id.btn_aplica_filtros);
        mbtn_aplica_filtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sp1_Valor = spinner_ordenar.getSelectedItem().toString();
                Toast.makeText(getBaseContext(),"boton spiner 1: " + sp1_Valor,Toast.LENGTH_LONG).show();

                String sp2_Valor = spinner_ciudad.getSelectedItem().toString();
                Toast.makeText(getBaseContext(),"boton spiner 2: " + sp2_Valor,Toast.LENGTH_LONG).show();

                String sp3_Valor = spinner_carburante.getSelectedItem().toString();
                Toast.makeText(getBaseContext(),"boton spiner 3: " + sp3_Valor,Toast.LENGTH_LONG).show();

                String sp4_Valor = spinner_modelo.getSelectedItem().toString();
                Toast.makeText(getBaseContext(),"boton spiner 4: " + sp4_Valor,Toast.LENGTH_LONG).show();

                Toast.makeText(getBaseContext(),"boton precio min: " + mtxt_pmin.getText(),Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(),"boton precio max: " + mtxt_pmax.getText(),Toast.LENGTH_LONG).show();

                Toast.makeText(getBaseContext(),"boton fecha ini: " + metxt_fecha_ini.getText(),Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(),"boton fecha fin: " + metxt_fecha_fin.getText(),Toast.LENGTH_LONG).show();

                Toast.makeText(getBaseContext(),"boton año: " + metxt_ano.getText(),Toast.LENGTH_LONG).show();
            }
        });



    // Cierre onCreate
    }

    // Calendario - PickerDialog para obtener fechas
    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                editText.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {

        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    // Menu ActionBar - onCLICK()
    private void showMenu(View view){

        PopupMenu popupMenu = new PopupMenu(MenuFiltros.this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.popup_Tu_Perfil:
                        // load "Tu_Perfil" Fragment
                        //loadFragment(new Tu_Perfil());
                        intent = new Intent(MenuFiltros.this, Tu_Perfil.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Coches:
                        //loadFragment(new Tus_Coches());
                        intent = new Intent(MenuFiltros.this, Tus_Coches.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Alquileres:
                        //loadFragment(new Tus_Alquileres());
                        intent = new Intent(MenuFiltros.this, Tus_Alquileres.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Tus_Reviews:
                        //loadFragment(new Tus_Reviews());
                        intent = new Intent(MenuFiltros.this, Tus_Reviews.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_Reviews_de_tus_Coches:
                        //loadFragment(new Tus_Reviews_Coches());
                        intent = new Intent(MenuFiltros.this, Tus_Reviews_Coches.class);
                        startActivity(intent);
                        break;
                    case R.id.popup_logout:
                        //logoutSes();
                        break;
                }
                return true;
            }
        });

        popupMenu.show();
    // Cierre showMenu
    }

    // Cargar los Fragments
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

        Intent intent = new Intent(MenuFiltros.this,MainActivity.class);
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
