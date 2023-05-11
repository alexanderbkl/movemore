package magdalenaramirez.ioc.movemore.utiles;

import android.content.Context;
import android.widget.Toast;
import magdalenaramirez.ioc.movemore.MainActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static magdalenaramirez.ioc.movemore.utiles.Car.jsonObjectToGetCar;
import static magdalenaramirez.ioc.movemore.utiles.Rentals.jsonObjectToGetAlquiler;
import static magdalenaramirez.ioc.movemore.utiles.Util.getResponse;

public class User {
    private int id=3;
    //private int id user_id;
    private String firstname;
    private String lastname;
    private String phone;
    private String addr;
    private String username;
    private String bio;
    private String location;
    private boolean admin = false;
    //-----
    private String email;
    private String password;
    private String tokken;

    //-----User Cars
    List<Car> coches = new ArrayList<>();
    ;
    List<Rentals> alquileres;

    private String message = "ERROR";

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokken() {
        return tokken;
    }

    public void setTokken(String tokken) {
        this.tokken = tokken;
    }

    public List<Car> getCoches() {
        return coches;
    }

    public void setCoches(List<Car> coches) {
        this.coches = coches;
    }

    public List<Rentals> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<Rentals> alquileres) {
        this.alquileres = alquileres;
    }

    public int getId() {
        return id;
    }

    public User() {
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getMessage() {
        return message;
    }

    public boolean loadUser(String v_email, String v_password) throws IOException {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("email", v_email);
            parameters.put("password", v_password);
            //String resposte = getResponse("http://10.2.66.56/index.php/user/login",parameters);

            //Obten la respuesta en Json
            JSONObject jsonObject = Util.getResponse("user/login", parameters);
            System.out.println(jsonObject);

            //Save message
            message = jsonObject.getString("message");

            //Check if verification it's valid
            if (jsonObject.getBoolean("status") == true) {
                this.email = v_email;
                this.password = v_password;

                //Obtengo el objeto usuario y me guardo los datos
                JSONObject user = jsonObject.getJSONObject("user");

                this.id = user.getInt("user_id");
                System.out.println("------ USER CLASE: " + id);
                this.firstname = user.getString("first_name");
                this.lastname = user.getString("last_name");
                this.phone = user.getString("phone_number");
                this.addr = user.getString("address");
                this.username = user.getString("username");
                this.email = user.getString("email");
                this.admin = user.getInt("is_admin") == 0 ? false : true;
                return true;
            } else {
                return false;
            }

        } catch (JSONException ex) {
            message = ex.getMessage();
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }

        //If something happens, it returns false.
        return false;
    }

    ;

    public boolean delUser(User v_User) {
        // parameters.put("user_id");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", String.valueOf(v_User.id));
        parameters.put("user_id", String.valueOf(v_User.getId()));

        try {
            //Obten la respuesta en Json
            JSONObject jsonObject = getResponse("user/deleteprofile");

            message = jsonObject.getString("message"); //get message

            return jsonObject.getBoolean("status");
        } catch (IOException | JSONException  ex) {
            message = ex.getMessage(); //get message
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }

        return false;

    }

    public boolean modifyUser(User v_User) {

        // parameters.put("user_id", + ALL);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", String.valueOf(v_User.id));

        //User
        parameters.put("user_id", String.valueOf(v_User.getId()));
        parameters.put("first_name", v_User.getFirstname());
        parameters.put("last_name", v_User.getLastname());
        parameters.put("phone_number", v_User.getPhone());
        parameters.put("address", v_User.getAddr());
        parameters.put("username", v_User.getUsername());
        parameters.put("email", v_User.getEmail());

        try {
            //Obten la respuesta en Json // pendiente nombre RUBEN
            JSONObject jsonObject = getResponse("user/editprofile", parameters);

            message = jsonObject.getString("message"); //get message

            return jsonObject.getBoolean("status");
        } catch (IOException | JSONException  ex) {
            message = ex.getMessage(); //get message
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    //Coches
    public List<Car> getListMyCars() {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("owner_id", String.valueOf(this.id));

            //Obten la respuesta en Json
            JSONObject jsonObject = getResponse("user/carsbyowner", parameters);
            message = jsonObject.getString("message"); //get message
            coches.clear();
            System.out.println(jsonObject);
            if (jsonObject.getBoolean("status")) {
                JSONArray carsObjects = jsonObject.getJSONArray("cars");

                for (int i = 0; i < carsObjects.length(); i++) {
                    coches.add(jsonObjectToGetCar(carsObjects.getJSONObject(i)));
                }
            }
        } catch (IOException | JSONException | KeyStoreException | CertificateException | NoSuchAlgorithmException |
                 UnrecoverableKeyException | KeyManagementException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }

        return coches;
    }

    public boolean addNewCar(Car v_Car) {
        Map<String, String> parameters = new HashMap<>();
        // parameters.put("username", "user_user1");
        parameters.put("usr_id", String.valueOf(this.id));

        //Car
        parameters.put("make", v_Car.getMarca());
        parameters.put("model", v_Car.getModelo());
        parameters.put("year", String.valueOf(v_Car.getAño()));
        parameters.put("color", v_Car.getColor());
        parameters.put("seats", String.valueOf(v_Car.getNumeroAsientos()));
        parameters.put("plate", v_Car.getMatricula());
        parameters.put("pricexday", String.valueOf(v_Car.getPricePerDay()));
        parameters.put("class_id", String.valueOf(v_Car.getClase().id));

        parameters.put("photo", v_Car.getUrlImage());

        try {
            //Obten la respuesta en Json
            JSONObject jsonObject = getResponse("user/newcar", parameters);

            message = jsonObject.getString("message"); //get message

            return jsonObject.getBoolean("status");
        } catch (IOException | JSONException | KeyStoreException | CertificateException | NoSuchAlgorithmException |
                 UnrecoverableKeyException | KeyManagementException ex) {
            message = ex.getMessage(); //get message
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteCar(Car v_Car) {
        Map<String, String> parameters = new HashMap<>();
        // parameters.put("username", "user_user1");
        parameters.put("usr_id", String.valueOf(this.id));
        parameters.put("car_id", String.valueOf(v_Car.getID()));
        try {
            //Obten la respuesta en Json
            JSONObject jsonObject = getResponse("user/delcar", parameters);

            message = jsonObject.getString("message"); //get message

            return jsonObject.getBoolean("status");
        } catch (IOException | JSONException | KeyStoreException | CertificateException | NoSuchAlgorithmException |
                 UnrecoverableKeyException | KeyManagementException ex) {
            message = ex.getMessage(); //get message
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean modifyCar(Car v_Car) {
        Map<String, String> parameters = new HashMap<>();
        // parameters.put("username", "user_user1");
        parameters.put("usr_id", String.valueOf(this.id));
        parameters.put("car_id", String.valueOf(v_Car.getID()));

        //Car
        parameters.put("make", v_Car.getMarca());
        parameters.put("model", v_Car.getModelo());
        parameters.put("year", String.valueOf(v_Car.getAño()));
        parameters.put("color", v_Car.getColor());
        parameters.put("seats", String.valueOf(v_Car.getNumeroAsientos()));
        parameters.put("plate", v_Car.getMatricula());
        parameters.put("new_plate", v_Car.getMatricula());
        parameters.put("pricexday", String.valueOf(v_Car.getPricePerDay()));
        parameters.put("class_id", String.valueOf(v_Car.getClase().id));

        parameters.put("photo", v_Car.getUrlImage());

        try {
            //Obten la respuesta en Json
            JSONObject jsonObject = getResponse("user/editcar", parameters);

            message = jsonObject.getString("message"); //get message

            return jsonObject.getBoolean("status");
        } catch (IOException | JSONException ex) {
            message = ex.getMessage(); //get message
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException | UnrecoverableKeyException | CertificateException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    //Alquileres
    public List<Rentals> getListMyRentals() {
        LinkedList<Rentals> reviewAlquilados = new LinkedList<Rentals>();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", String.valueOf(this.id));
        parameters.put("filter", "my");
        try {
            JSONObject jsonObject = getResponse("user/rentalslist", parameters);
            System.out.println(jsonObject);
            message = jsonObject.getString("message"); //get message

            if (jsonObject.getBoolean("status")) {
                JSONArray array = jsonObject.getJSONArray("rentals");
                for (int i = 0; i < array.length(); i++) {
                    reviewAlquilados.add(jsonObjectToGetAlquiler(array.getJSONObject(i)));
                }
            }
        } catch (IOException | JSONException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage(); //get message
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return reviewAlquilados;
    }

    public List<Rentals> getListMyRentalsByFilter(String filter) {
        LinkedList<Rentals> reviewAlquilados = new LinkedList<Rentals>();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", String.valueOf(this.id));
        parameters.put("filter", "my");
        try {
            JSONObject jsonObject = getResponse("user/" + filter, parameters);
            System.out.println(jsonObject);
            message = jsonObject.getString("message"); //get message

            if (jsonObject.getBoolean("status")) {
                JSONArray array = jsonObject.getJSONArray("rentals");
                for (int i = 0; i < array.length(); i++) {
                    reviewAlquilados.add(jsonObjectToGetAlquiler(array.getJSONObject(i)));
                }
            }
        } catch (IOException | JSONException | KeyStoreException | CertificateException | NoSuchAlgorithmException |
                 UnrecoverableKeyException | KeyManagementException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage(); //get message
        }
        return reviewAlquilados;
    }

    public boolean addNewAlquiler(Rentals v_Alquiler) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("rentee_id", String.valueOf(this.id));
            parameters.put("renter_id", String.valueOf(v_Alquiler.getCoche().getID_OWNER()));
            parameters.put("total_cost", String.valueOf(v_Alquiler.getTotal_cost()));
            parameters.put("car_id", String.valueOf(v_Alquiler.getCoche().getID()));

            parameters.put("start_date", new SimpleDateFormat("yyyy-MM-dd").format(v_Alquiler.getFecha_In()));
            parameters.put("end_date", new SimpleDateFormat("yyyy-MM-dd").format(v_Alquiler.getFecha_Fin()));


            JSONObject jsonObject = getResponse("user/rentnew", parameters);
            message = jsonObject.getString("message"); //get message


            return jsonObject.getBoolean("status");
        } catch (IOException | JSONException | KeyStoreException | CertificateException | NoSuchAlgorithmException |
                 UnrecoverableKeyException | KeyManagementException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage(); //get message
        }
        return false;
    }

    public boolean deleteAlquiler(Rentals v_Alquiler) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("user_id", String.valueOf(this.id));
            parameters.put("rental_id", String.valueOf(v_Alquiler.getId()));

            JSONObject jsonObject = getResponse("user/rentdel", parameters);
            message = jsonObject.getString("message"); //get message


            return jsonObject.getBoolean("status");
        } catch (IOException | JSONException | KeyStoreException | CertificateException | NoSuchAlgorithmException |
                 UnrecoverableKeyException | KeyManagementException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage(); //get message
        }
        return false;
    }

    public boolean modifyAlquiler(Rentals v_Alquiler) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("user_id", String.valueOf(this.id));
            parameters.put("rental_id", String.valueOf(v_Alquiler.getId()));
            parameters.put("total_cost", String.valueOf(v_Alquiler.getTotal_cost()));
            parameters.put("start_date", new SimpleDateFormat("yyyy-MM-dd").format(v_Alquiler.getFecha_In()));
            parameters.put("end_date", new SimpleDateFormat("yyyy-MM-dd").format(v_Alquiler.getFecha_Fin()));

            JSONObject jsonObject = getResponse("user/rentedit", parameters);
            message = jsonObject.getString("message"); //get message


            return jsonObject.getBoolean("status");
        } catch (IOException | JSONException | KeyStoreException | CertificateException | NoSuchAlgorithmException |
                 UnrecoverableKeyException | KeyManagementException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage(); //get message
        }
        return false;
    }

    //Reviews
    public List<Rentals> reviewAlquilados() {
        LinkedList<Rentals> reviewAlquilados = new LinkedList<Rentals>();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", String.valueOf(this.id));
        parameters.put("filter", "my");
        try {
            JSONObject jsonObject = getResponse("user/rentalslist", parameters);
            System.out.println(jsonObject);
            message = jsonObject.getString("message"); //get message

            if (jsonObject.getBoolean("status")) {
                JSONArray array = jsonObject.getJSONArray("rentals");
                for (int i = 0; i < array.length(); i++) {
                    reviewAlquilados.add(jsonObjectToGetAlquiler(array.getJSONObject(i)));
                }
            }
        } catch (IOException | JSONException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage(); //get message
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return reviewAlquilados;
    }

    public boolean sendReview(Rentals v_rental) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", String.valueOf(this.id));
        parameters.put("rental_id", String.valueOf(v_rental.getId()));
        parameters.put("msg", v_rental.getMessage());
        parameters.put("score", String.valueOf(v_rental.getScore()));
        try {
            JSONObject jsonObject = getResponse("user/rentalreview", parameters);
            message = jsonObject.getString("message"); //get message

            return jsonObject.getBoolean("status");
        } catch (IOException | JSONException | KeyStoreException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage(); //get message
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public List<Rentals> reviewAlquileres() {
        LinkedList<Rentals> reviewAlquilados = new LinkedList<Rentals>();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", String.valueOf(this.id));
        parameters.put("filter", "rented");
        try {
            JSONObject jsonObject = getResponse("user/rentalsfiltered", parameters);
            System.out.println(jsonObject);
            message = jsonObject.getString("message"); //get message

            if (jsonObject.getBoolean("status")) {
                JSONArray array = jsonObject.getJSONArray("rentals");
                for (int i = 0; i < array.length(); i++) {
                    reviewAlquilados.add(jsonObjectToGetAlquiler(array.getJSONObject(i)));
                }
            }
        } catch (IOException | JSONException | KeyStoreException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage(); //get message
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return reviewAlquilados;
    }
}