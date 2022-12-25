package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient {
    private static final String PATH = "/api/orders/";

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutLogin(Order order) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(order)
                .post(PATH);
    }

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithLogin(String token, Order order) {
        return given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .when()
                .body(order)
                .post(PATH);
    }

    @Step("Получение заказов конкретного пользователя с авторизацией")
    public Response getOrderWithLogin(String token) {
        return given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .get(PATH);
    }

    @Step("Получение заказов конкретного пользователя без авторизации")
    public Response getOrderWithoutLogin() {
        return given()
                .spec(getBaseSpec())
                .get(PATH);
    }

}
