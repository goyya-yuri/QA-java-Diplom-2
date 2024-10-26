package user;

import com.google.gson.JsonObject;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;

import static java.net.HttpURLConnection.*;

public class UserChecks {

    @Step("Успешная регистрация пользователя")
    public String checkCreated(ValidatableResponse response) {
        String accessToken = response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("accessToken");
        Assert.assertNotNull(accessToken);
        return accessToken;
    }

    @Step("Успешная авторизация пользователя")
    public void checkAuth(ValidatableResponse response) {
        boolean logged = response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("success");
        Assert.assertTrue(logged);
    }
    @Step("Успешная авторизация пользователя")
    public void checkAuthFail(ValidatableResponse response) {
        boolean logged = response
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .extract()
                .path("success");
        Assert.assertFalse(logged);
    }

    @Step("Успешное удаление пользователя")
    public void checkDeleted(ValidatableResponse response) {
        boolean removed = response
                .assertThat()
                .statusCode(HTTP_ACCEPTED)
                .extract()
                .path("success");
        Assert.assertTrue(removed);
    }

    @Step("Создать пользователя не удалось")
    public void checkCreatedFail(ValidatableResponse response) {
        boolean created = response
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .extract()
                .path("success");
        Assert.assertFalse(created);
    }

    @Step("Успешно применены новые настройки пользователя")
    public void checkSettingsApplied(ValidatableResponse response, UserSettings settings) {
        JsonObject element = response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .as(JsonObject.class);
        Assert.assertEquals(settings.getName(),
                element.getAsJsonObject("user").get("name").getAsString());
        Assert.assertEquals(settings.getEmail().toLowerCase(),
                element.getAsJsonObject("user").get("email").getAsString());
    }

    @Step("Новые настройки пользователя, не применены.")
    public void checkSettingsAppliedFail(ValidatableResponse response) {
        boolean changed = response
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .extract()
                .path("success");
        Assert.assertFalse(changed);
    }
}
