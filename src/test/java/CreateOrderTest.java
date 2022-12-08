import client.IngredientClient;
import client.OrderClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Ingredient;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderTest {
    public IngredientClient ingredientClient;
    public Ingredient ingredient;
    public UserClient userClient;
    public OrderClient orderClient;
    public User user;
    public Order order;
    private String token;
    public List<String> ingredients;
    Response response;

    @Before
    public void setup() {
        ingredientClient = new IngredientClient();
        ingredient = ingredientClient.getIngredient();
        ingredients = new ArrayList<>();
        ingredients.add(ingredient.data.get(0).get_id());
        ingredients.add(ingredient.data.get(1).get_id());
        ingredients.add(ingredient.data.get(2).get_id());
        user = User.getRandomUser();
        userClient = new UserClient();
        userClient.create(user);
        Response tokenResponse = userClient.login(user);
        tokenResponse.then().assertThat().statusCode(200);
        token = tokenResponse.then().extract().path("accessToken");
        order = new Order(ingredients);
        orderClient = new OrderClient();
    }

    @After
    public void delete(){
        userClient.delete();
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthorizationTest() {
        response = orderClient.createOrderWithoutLogin(order);
        response.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа c авторизацией")
    public void createOrderWithAuthorizationTest() {
        response = orderClient.createOrderWithLogin(token, order);
        response.then().assertThat().statusCode(200);

    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithIncorrectHash() {
        ingredients.add("someIngredient");
        response = orderClient.createOrderWithLogin(token, order);
        response.then().assertThat().statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithNullIngredient() {
        ingredients.clear();
        response = orderClient.createOrderWithLogin(token, order);
        response.then().assertThat().statusCode(400);
    }

}
