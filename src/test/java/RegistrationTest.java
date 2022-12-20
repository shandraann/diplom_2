import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RegistrationTest {
    public UserClient userClient;
    public User user;
    Response response;

    @Before
    public void setup() {
        userClient = new UserClient();
    }

    @After
    public void delete() {
        userClient.delete(userClient.token);
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void userCreationTest() {
        user = User.getRandomUser();
        response = userClient.create(user);
        response.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Cоздание пользователя, который уже зарегистрирован")
    public void userAlreadyCreatedTest() {
        user = User.getRandomUser();
        userClient.create(user);
        response = userClient.create(user);
        response.then().assertThat().statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем Email")
    public void userCreationWithoutLogin() {
        user = User.getRandomUser();
        user.setEmail(null);
        userClient.create(user);
        response = userClient.create(user);
        response.then().assertThat().statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем пароль")
    public void userCreationWithoutPassword() {
        user = User.getRandomUser();
        user.setPassword(null);
        userClient.create(user);
        response = userClient.create(user);
        response.then().assertThat().statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем Имя")
    public void userCreationWithoutName() {
        user = User.getRandomUser();
        user.setName(null);
        userClient.create(user);
        response = userClient.create(user);
        response.then().assertThat().statusCode(403);
    }

}
