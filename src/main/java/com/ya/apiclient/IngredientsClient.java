package com.ya.apiclient;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class IngredientsClient extends BurgerRestClient{

    private static final String INGREDIENTS_PATH = "ingredients";


    @Step("Get ingredients list")
    public ValidatableResponse getIngredients() {

        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then();
    }

}
