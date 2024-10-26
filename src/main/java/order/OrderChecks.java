package order;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;

import java.util.List;

import static java.net.HttpURLConnection.*;


public class OrderChecks {
    Gson gson = new Gson();

    @Step("Получен список ингредиентов")
    public List<Ingredient> ingredientsListReceived(ValidatableResponse response) {
        JsonArray returnData = response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .as(JsonObject.class)
                .getAsJsonArray("data");
        return List.of(gson.fromJson(returnData.toString(), Ingredient[].class));
    }

    @Step("Заказ сформирован")
    public void createdSuccessful(ValidatableResponse response) {
        boolean created = response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("success");
        Assert.assertTrue(created);
    }

    @Step("Заказ не сформирован")
    public void createdFailWithUserLogOut(ValidatableResponse response) {
        boolean created = response
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .extract()
                .path("success");
        Assert.assertFalse(created);
    }

    @Step("Заказ не сформирован")
    public void createdFailWithoutIngredients(ValidatableResponse response) {
        boolean created = response
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .path("success");
        Assert.assertFalse(created);
    }

    @Step("Заказ не сформирован")
    public void createdFailWithInvalidIngredients(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HTTP_INTERNAL_ERROR);
    }

    @Step("Получен список заказов")
    public void ordersListReceived(ValidatableResponse response) {
        boolean success = response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("success");
        Assert.assertTrue(success);
    }

    @Step("Список заказов не получен")
    public void ordersListReceivedFail(ValidatableResponse response) {
        boolean success = response
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .extract()
                .path("success");
        Assert.assertFalse(success);
    }
}
