package order;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/";
    private static final String ORDERS_PATH = "orders";
    private static final String INGREDIENTS_PATH = "ingredients";

    Gson gson = new Gson();

    @Step("Создание заказа")
    public ValidatableResponse create(String accessToken,Ingredients ingredients) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .baseUri(BASE_URL)
                .body(gson.toJson(ingredients))
                .when()
                .post(ORDERS_PATH)
                .then().log().all();
    }

    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredientsHashList() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .when()
                .get(INGREDIENTS_PATH)
                .then().log().all();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrdersList(String accessToken) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .baseUri(BASE_URL)
                .when()
                .get(ORDERS_PATH)
                .then().log().all();
    }
}
