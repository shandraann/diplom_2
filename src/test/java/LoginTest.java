import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginTest {
    public UserClient userClient;
    public User user;
    Response response;
    private String token;

    @Before
    public void setup() {
        userClient = new UserClient();
        user = User.getRandomUser();
        userClient.create(user);
        Response tokenResponse = userClient.login(user);
        tokenResponse.then().assertThat().statusCode(200);
        token = tokenResponse.then().extract().path("accessToken");
    }

    @After
    public void delete() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void userLoginTest() {
        response = userClient.login(user);
        response.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Логин с некорректным паролем")
    public void userLoginWithIncorrectPassword() {
        user.setPassword("Password");
        response = userClient.login(user);
        response.then().assertThat().statusCode(401);
    }

    @Test
    @DisplayName("Логин с некорретным логином")
    public void userLoginWithIncorrectLogin() {
        user.setEmail("Email");
        response = userClient.login(user);
        response.then().assertThat().statusCode(401);
    }

}
