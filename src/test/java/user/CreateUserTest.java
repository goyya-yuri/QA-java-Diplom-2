package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class CreateUserTest {
    private final UserClient client = new UserClient();
    private final UserChecks check = new UserChecks();

    private String accessToken;

    @After
    @DisplayName("Удаление пользователя")
    public void removeUser(){
        if(accessToken == null) return;
        ValidatableResponse deleteResponse = client.delete(accessToken);
        check.checkDeleted(deleteResponse);
    }

    @Test
    @DisplayName("Создание пользователя")
    public void createUserTest() {
        var user = User.random();
        ValidatableResponse createResponse = client.create(user);
        accessToken = check.checkCreated(createResponse);
    }

    @Test
    @DisplayName("Создание, уже зарегестрированного, пользователя")
    public void failCreateUserCloneTest() {
        var user = User.random();
        ValidatableResponse createResponse = client.create(user);
        accessToken = check.checkCreated(createResponse);
        ValidatableResponse createResponseDouble = client.create(user);
        check.checkCreatedFail(createResponseDouble);
    }

    @Test
    @DisplayName("Создание пользователя без почты")
    public void failCreateUserWithoutEmailTest() {
        var user = User.random();
        user.setEmail(null);
        ValidatableResponse createResponse = client.create(user);
        check.checkCreatedFail(createResponse);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void failCreateUserWithoutPasswordTest() {
        var user = User.random();
        user.setPassword(null);
        ValidatableResponse createResponse = client.create(user);
        check.checkCreatedFail(createResponse);}

    @Test
    @DisplayName("Создание пользователя без имени")
    public void failCreateUserWithoutNameTest() {
        var user = User.random();
        user.setName(null);
        ValidatableResponse createResponse = client.create(user);
        check.checkCreatedFail(createResponse);}
}
