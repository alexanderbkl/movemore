package magdalenaramirez.ioc.movemore.utiles;

public class Ratings {

    private int ID;
    private double score;
    private String message;

    Ratings(){}

    static Ratings getRating(int ID){
        return new Ratings();
    }
}
