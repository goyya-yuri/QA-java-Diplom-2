package order;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class Ingredients {
    private List<String> ingredients;

    public Ingredients() {}

    public Ingredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public static Ingredients randomIngredients(int count, List<Ingredient> ingredientsData) {
        List<String> ingredientsHashList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Ingredient newIngredient = ingredientsData.get((int) (Math.random() * ingredientsData.size()));
            ingredientsHashList.add(newIngredient.getId());
        }
        return new Ingredients(ingredientsHashList);
    }

    public static Ingredients randomWrongIngredients(int count) {
        List<String> ingredientsHashList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            ingredientsHashList.add(RandomStringUtils.randomAlphanumeric(24));
        }
        return new Ingredients(ingredientsHashList);
    }
}
