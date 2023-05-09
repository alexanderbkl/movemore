package magdalenaramirez.ioc.movemore.utiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Car {
    public enum combustibleEnum {
        petrol("petrol",1),
        gasoil("gas-oil",2),
        gasoilhybrid("gas-oil/hybrid",3),
        eletric("electric",4);

        public final String label;
        public final int id;

        private combustibleEnum(String label, int id) {this.label = label; this.id = id;}
    }
    public enum claseEnum {
        Economy("Economy",1),
        Compact("Compact",2),
        Midsize("Mid-size",3),
        Fullsize("Full-size",4),
        SUV("SUV",5),
        Luxury("Luxury",6),
        Electric("Electric",7),
        Sedan("Sedan",8),
        CCompact("Compact\n",9),//Fix shit database
        CCCompact("ompact\n\n",10),
        MMidsize("Mid-size\n",11),
        LLuxury("Luxury\n",12),
        EElectric("Electric\n",13),
        HHybrid("Hybrid\n",14);


        public final String label;
        public final int id;

        private claseEnum(String label, int id) {this.label = label; this.id = id;}
    }
    private int ID;
    private int ID_OWNER;
    private int numeroAsientos;
    private String name;
    private String marca;
    private String modelo;
    private int año;
    private String color;
    private String localidad;
    private String matricula;
    private claseEnum clase;
    private double pricePerDay;
    private combustibleEnum combustible;
    private String urlImage;// = "https://static.wikia.nocookie.net/forzamotorsport/images/7/76/HOR_XB1_Toyota_Yaris.png/revision/latest/scale-to-width-down/1000?cb=20191031202319";

    public int getID_OWNER() {
        return ID_OWNER;
    }

    public void setID_OWNER(int ID_OWNER) {
        this.ID_OWNER = ID_OWNER;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }


    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNumeroAsientos(int numeroAsientos) {
        this.numeroAsientos = numeroAsientos;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public void setClase(claseEnum clase) {
        this.clase = clase;
    }

    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay;  }

    public void setCombustible(combustibleEnum combustible) {
        this.combustible = combustible;
    }

    public int getID() {
        return ID;
    }

    public int getNumeroAsientos() {
        return numeroAsientos;
    }

    public String getName() {
        return name;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAño() {
        return año;
    }

    public String getColor() {
        return color;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getMatricula() {
        return matricula;
    }

    public claseEnum getClase() {
        return clase;
    }

    public double getPricePerDay() { return pricePerDay; }

    public combustibleEnum getCombustible() {
        return combustible;
    }
    public Car() {}

    public static Car getCar(int id){
        Car tm_car = null; // crea una lista vacía de objetos Car
        try {
            //añadir el parametro id
            Map<String, String> parameters = new HashMap<>();
            parameters.put("car_id", String.valueOf(id));

            //hacer la request
            JSONObject carsMesage = Util.getResponse("user/carinfo");
            //Si es valido conviertelo a coche
            if(carsMesage.getBoolean("status")){tm_car = jsonObjectToGetCar(carsMesage);}

        } catch (IOException | JSONException ex) {
            Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tm_car;
    }


    public static Car jsonObjectToGetCar(JSONObject obj) throws JSONException {
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
        tmp_Car.setID_OWNER(obj.getInt("owner_id"));

        //tmp_Car.setClase(obj.getString("class_name"));
        String className = obj.getString("class_name");
        for (claseEnum clase : claseEnum.values()) {
            if (clase.label.equals(className)) {
                tmp_Car.setClase(clase);
                break;
            }
        }
        String engineName = obj.getString("engine");
        for (combustibleEnum combustible : combustibleEnum.values()) {
            if (combustible.label.equals(engineName)) {
                tmp_Car.setCombustible(combustible);
                break;
            }
        }

        return tmp_Car;
    }

    public static List<Car> getListPublic(){
        List<Car> tm_car = new ArrayList<>(); // crea una lista vacía de objetos Car
        try {
            JSONObject carsMesage = Util.getResponse("user/carsall");

            if(carsMesage.getBoolean("status")){
                JSONArray carsObjects = carsMesage.getJSONArray("cars");

                for(int i=0; i<carsObjects.length(); i++){
                    tm_car.add(jsonObjectToGetCar(carsObjects.getJSONObject(i)));
                }
            }
            System.out.println(carsMesage);
        } catch (IOException | JSONException ex) {
            Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tm_car;
    }
}
