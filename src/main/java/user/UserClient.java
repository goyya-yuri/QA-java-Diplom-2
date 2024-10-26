package user;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/auth/";
    private static final String REGISTER_PATH = "register";
    private static final String LOGIN_PATH = "login";
    private static final String USER_PATH = "user";

    Gson gson = new Gson();

    @Step("Регистрация пользователя")
    public ValidatableResponse create(User user) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(gson.toJson(user))
                .when()
                .post(REGISTER_PATH)
                .then().log().all();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse logIn(UserCredentials user) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(gson.toJson(user))
                .when()
                .post(LOGIN_PATH)
                .then().log().all();}

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String token) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .when()
                .delete(USER_PATH)
                .then().log().all();

    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse changeData(String token, UserSettings settings) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(gson.toJson(settings))
                .when()
                .patch(USER_PATH)
                .then().log().all();
    }
}
