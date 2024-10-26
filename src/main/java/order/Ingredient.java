package order;

public class Ingredient {
    String _id;
    String name;
    String type;
    String proteins;
    long fat;
    long carbohydrates;
    long calories;
    long price;
    String image;
    String image_mobile;
    String image_large;
    long __v;

    public Ingredient() {}

    public String getId() {
        return _id;
    }
}
