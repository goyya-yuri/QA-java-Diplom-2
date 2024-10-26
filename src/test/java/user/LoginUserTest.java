package user;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class LoginUserTest {
    private final UserClient client = new UserClient();
    private final UserChecks check = new UserChecks();

    private static User user;
    private final String accessToken;

    boolean wrongEmail;
    boolean wrongPassword;
    boolean expectedResult;

    public LoginUserTest(boolean wrongEmail, boolean wrongPassword, boolean expectedResult) {
        this.wrongEmail = wrongEmail;
        this.wrongPassword = wrongPassword;
        this.expectedResult = expectedResult;
        user = User.random();
        ValidatableResponse createResponse = client.create(user);
        accessToken = check.checkCreated(createResponse);
    }

    @After
    public void removeUser(){
        if(accessToken == null) return;
        ValidatableResponse deleteResponse = client.delete(accessToken);
        check.checkDeleted(deleteResponse);
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][] {
                {false,false,true},
                {false,true,false},
                {true,false,false},
                {true,true,false},
        };
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Авторизация пользователя")
    @Step("Авторизация пользователя")
    public void loginTest() {
        UserCredentials credentials = UserCredentials.fromUser(user);
        if(wrongEmail){
            credentials.setEmail(User.random().getEmail());}
        if(wrongPassword){
            credentials.setPassword(User.random().getPassword());}
        ValidatableResponse loginResponse = client.logIn(credentials);

        if (expectedResult) {
            check.checkAuth(loginResponse);
        }else{
            check.checkAuthFail(loginResponse);
        }
    }
}
