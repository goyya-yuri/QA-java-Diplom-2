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

public class ReceiveUserOrdersTest {

    private static final UserClient userClient = new UserClient();
    private static final UserChecks userChecks = new UserChecks();
    private static String accessToken;

    private static final OrderClient orderClient = new OrderClient();
    private static final OrderChecks orderChecks = new OrderChecks();

    @BeforeClass
    @Description("Подготовка данных")
    public static void initData(){
        User user = User.random();
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = userChecks.checkCreated(createResponse);

        ValidatableResponse ingredientsResponse = orderClient.getIngredientsHashList();
        List<Ingredient> ingredientsList = orderChecks.ingredientsListReceived(ingredientsResponse);
        Ingredients ingredients = Ingredients.randomIngredients(4, ingredientsList);

        ValidatableResponse response = orderClient.create(accessToken, ingredients);
        orderChecks.createdSuccessful(response);
    }

    @AfterClass
    @Description("Удаление созданных пользователей")
    public static void removeUser(){
        if(accessToken == null) return;
        ValidatableResponse deleteResponse = userClient.delete(accessToken);
        userChecks.checkDeleted(deleteResponse);
    }

    @Test
    @DisplayName("Получение заказов, пользователь авторизован")
    public void receiveOrdersWithLogInTest() {
        ValidatableResponse response = orderClient.getOrdersList(accessToken);
        orderChecks.ordersListReceived(response);
    }

    @Test
    @DisplayName("Получение заказов, пользователь не авторизован")
    public void receiveOrdersWithoutLogInTest() {
        ValidatableResponse response = orderClient.getOrdersList("");
        orderChecks.ordersListReceivedFail(response);
    }
}
