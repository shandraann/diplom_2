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

public class GetOrderTest {
    public User user;
    public UserClient userClient;
    public OrderClient orderClient;
    public Order order;
    public Ingredient ingredient;
    public IngredientClient ingredientClient;
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
        token = tokenResponse.then().extract().path("accessToken");
        order = new Order(ingredients);
        orderClient = new OrderClient();
        orderClient.createOrderWithLogin(token, order);
    }

    @After
    public void delete(){
        userClient.delete(token);
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя без авторизации")
    public void getOrderWithoutAuthorizationTest() {
        response = orderClient.getOrderWithoutLogin();
        response.then().assertThat().statusCode(401);
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя без авторизации")
    public void getOrderWithAuthorizationTest() {
        response = orderClient.getOrderWithLogin(token);
        response.then().assertThat().statusCode(200);
    }

}
