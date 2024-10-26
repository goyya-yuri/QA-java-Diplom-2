package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import user.User;
import user.UserChecks;
import user.UserClient;

import java.util.List;

public class CreateOrderTest {
    private static final UserClient userClient = new UserClient();
    private static final UserChecks userChecks = new UserChecks();
    private static String accessToken;

    private final OrderClient orderClient = new OrderClient();
    private final OrderChecks orderChecks = new OrderChecks();

    @BeforeClass
    @Description("Подготовка данных")
    public static void initData(){
        User user = User.random();
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = userChecks.checkCreated(createResponse);
    }

    @AfterClass
    @Description("Удаление созданных пользователей")
    public static void removeUser(){
        if(accessToken == null) return;
        ValidatableResponse deleteResponse = userClient.delete(accessToken);
        userChecks.checkDeleted(deleteResponse);
    }

    @Test
    @DisplayName("Создание заказа, клиент авторизован, корректные ингредиенты")
    public void createOrderWithUserLogInWithIngredientsTest() {
        ValidatableResponse ingredientsResponse = orderClient.getIngredientsHashList();
        List<Ingredient> ingredientsList = orderChecks.ingredientsListReceived(ingredientsResponse);
        Ingredients ingredients = Ingredients.randomIngredients(4, ingredientsList);

        ValidatableResponse response = orderClient.create(accessToken, ingredients);
        orderChecks.createdSuccessful(response);
    }

    @Test
    @DisplayName("Создание заказа, клиент не авторизован, корректные ингредиенты")
    public void createOrderWithUserLogOutTest() {
        ValidatableResponse ingredientsResponse = orderClient.getIngredientsHashList();
        List<Ingredient> ingredientsList = orderChecks.ingredientsListReceived(ingredientsResponse);
        Ingredients ingredients = Ingredients.randomIngredients(4, ingredientsList);

        ValidatableResponse response = orderClient.create("", ingredients);
        orderChecks.createdFailWithUserLogOut(response);
    }

    @Test
    @DisplayName("Создание заказа, клиент авторизован, без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Ingredients ingredients = new Ingredients();

        ValidatableResponse response = orderClient.create(accessToken, ingredients);
        orderChecks.createdFailWithoutIngredients(response);
    }

    @Test
    @DisplayName("Создание заказа, клиент авторизован, некорректные ингредиенты")
    public void createOrderWithInvalidIngredientsTest() {
        Ingredients ingredients = Ingredients.randomWrongIngredients(4);

        ValidatableResponse response = orderClient.create(accessToken, ingredients);
        orderChecks.createdFailWithInvalidIngredients(response);
    }
}
