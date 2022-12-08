package client;

import io.qameta.allure.Step;
import model.Ingredient;

import static io.restassured.RestAssured.given;

public class IngredientClient extends RestAssuredClient {
    private static final String PATH = "api/ingredients/";

    @Step("Получение данных об ингредиентах")
    public Ingredient getIngredient() {
        return given()
                .spec(getBaseSpec())
                .get(PATH)
                .as(Ingredient.class);
    }
}