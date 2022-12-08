import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UpdateDataTest {
    public UserClient userClient;
    public User user;
    private String token;

    @Before
    public void setup() {
        userClient = new UserClient();
        user = User.getRandomUser();
        Response response = userClient.create(user);
        token = response.then().extract().path("accessToken");
    }

    @After
    public void delete() {
        userClient.delete();
    }

    @Test
    @DisplayName("Изменение Email пользователя с авторизацией")
    public void updateUserEmailWithAuthTest() {
        userClient.getDataUser(token);
        user.setEmail(User.getRandomEmail());
        Response response = userClient.updateUserDataWithToken(token, new User(user.getEmail(), user.getName()));
        response.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Изменение Имени пользователя с авторизацией")
    public void updateUserNameWithAuthTest() {
        userClient.getDataUser(token);
        user.setName(User.getRandomName());
        Response response = userClient.updateUserDataWithToken(token, new User(user.getEmail(), user.getName()));
        response.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Изменение Email пользователя без авторизации")
    public void updateUserEmailWithoutAuthTest() {
        userClient.getDataUser(token);
        user.setEmail(User.getRandomEmail());
        Response response = userClient.updateUserDataWithoutToken(new User(user.getEmail(), user.getName()));
        response.then().assertThat().statusCode(401);
    }

    @Test
    @DisplayName("Изменение Имени пользователя без авторизации")
    public void updateUserNameWithoutAuthTest() {
        userClient.getDataUser(token);
        user.setName(User.getRandomName());
        Response response = userClient.updateUserDataWithoutToken(new User(user.getEmail(), user.getName()));
        response.then().assertThat().statusCode(401);
    }

}
