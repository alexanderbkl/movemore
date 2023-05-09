package magdalenaramirez.ioc.movemore.utiles;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rentals {
    private int id;
    private String name;
    private String message;
    private User owner;
    private Date Fecha_In;
    private Date Fecha_Fin;
    private double total_cost;
    private Car coche;
    private int score=-1;

    public boolean isReviwed(){
        return score != -1;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getFecha_In() {
        return Fecha_In;
    }

    public void setFecha_In(Date Fecha_In) {
        this.Fecha_In = Fecha_In;
    }

    public Date getFecha_Fin() {
        return Fecha_Fin;
    }

    public void setFecha_Fin(Date Fecha_Fin) {
        this.Fecha_Fin = Fecha_Fin;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public Car getCoche() {
        return coche;
    }

    public void setCoche(Car coche) {
        this.coche = coche;
    }

    public Rentals(){}

    static Rentals getAlquiler(int id){
        Rentals tmp_rental = null;
        return tmp_rental;
    }

    static Rentals jsonObjectToGetAlquiler(JSONObject obj) throws JSONException {
        Rentals tm_rental = new Rentals();

        try {  //No deberia petar but me lo piden
            tm_rental.setFecha_In(new SimpleDateFormat("yyyy-MM-dd").parse(obj.getString("start_date")));
            tm_rental.setFecha_Fin(new SimpleDateFormat("yyyy-MM-dd").parse(obj.getString("end_date")));
        } catch (JSONException | ParseException ex) {
            Logger.getLogger(Rentals.class.getName()).log(Level.SEVERE, null, ex);
        }
        tm_rental.setId(obj.getInt("rental_id"));

        if(!obj.isNull("message"))tm_rental.setMessage(obj.getString("message"));
        if(!obj.isNull("score"))tm_rental.setScore(obj.getInt("score"));
        else tm_rental.setScore(-1);
        tm_rental.setTotal_cost(obj.getDouble("total_cost"));

        //tm_rental.setCoche(jsonObjectToGetCar(obj.getJSONObject("coche")));

        //Copia y pega de jsonObjectToGetCar :D best api
        Car tmp_Car = new Car();
        tmp_Car.setID(obj.getInt("car_id"));
        tmp_Car.setMarca(obj.getString("make"));
        tmp_Car.setModelo(obj.getString("model"));
        tmp_Car.setAño(obj.getInt("year"));
        tmp_Car.setColor(obj.getString("color"));
        tmp_Car.setNumeroAsientos(obj.getInt("seats"));
        tmp_Car.setMatricula(obj.getString("plate"));
        tmp_Car.setUrlImage(obj.getString("photo_url"));
        tmp_Car.setPricePerDay(obj.getDouble("pricexday"));


        //tmp_Car.setClase(obj.getString("class_name"));
        String className = obj.getString("class_name");
        for (Car.claseEnum clase : Car.claseEnum.values()) {
            if (clase.label.equals(className)) {
                tmp_Car.setClase(clase);
                break;
            }
        }
        String engineName = obj.getString("engine");
        for (Car.combustibleEnum combustible : Car.combustibleEnum.values()) {
            if (combustible.label.equals(engineName)) {
                tmp_Car.setCombustible(combustible);
                break;
            }
        }
        tm_rental.setCoche(tmp_Car);

        return tm_rental;
    }
}
