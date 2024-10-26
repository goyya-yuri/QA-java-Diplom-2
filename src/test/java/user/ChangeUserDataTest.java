package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class ChangeUserDataTest {
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
    @DisplayName("Изменение данных, пользователь авторизован")
    public void changeUserDataWithAuthTest(){
        User user = User.random();
        ValidatableResponse createResponse = client.create(user);
        accessToken = check.checkCreated(createResponse);

        UserSettings settings = UserSettings.fromUser(User.random());
        ValidatableResponse response = client.changeData(accessToken, settings);
        check.checkSettingsApplied(response, settings);
    }

    @Test
    @DisplayName("Изменение данных, пользователь не авторизован")
    public void changeUserDataWithoutAuthTest(){
        User user = User.random();
        ValidatableResponse createResponse = client.create(user);
        accessToken = check.checkCreated(createResponse);

        UserSettings settings = UserSettings.fromUser(User.random());
        ValidatableResponse response = client.changeData("", settings);
        check.checkSettingsAppliedFail(response);
    }
}
