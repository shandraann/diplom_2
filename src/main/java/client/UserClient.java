package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.jetbrains.annotations.NotNull;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;

public class UserClient extends RestAssuredClient {

    private static final String PATH = "/api/auth/";

    @Step("Создание уникального пользователя")
    public Response create(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(PATH + "register");
    }

    @Step("Логин под существующим пользователем")
    public Response login(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(PATH + "login");
    }

    @Step("Получение информации о пользователе")
    public ValidatableResponse getDataUser(String token) {

        return given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .when()
                .get(PATH + "user")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Step("Изменение данных пользователя с авторизацией")
    public Response updateUserDataWithToken(String token, User user) {
        return given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .when()
                .body(user)
                .patch(PATH + "user");
    }

    @Step("Изменение данных пользователя без авторизации")
    public Response updateUserDataWithoutToken(User user) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(user)
                .patch(PATH + "user");
    }

    @Step("Удаление пользователя")
    public Response delete(String token) {
         if (token != null) {
             return given()
                     .spec(getBaseSpec())
                     .auth().oauth2(token)
                     .when()
                     .delete("/api/auth/user" + "user");
         } else {
             return null;
         }

    }
}
