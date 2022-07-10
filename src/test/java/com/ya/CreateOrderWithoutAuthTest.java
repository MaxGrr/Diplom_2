package com.ya;

import com.ya.apiclient.IngredientsClient;
import com.ya.apiclient.OrderClient;
import com.ya.model.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateOrderWithoutAuthTest {

    IngredientsClient ingredientsClient;
    OrderClient orderClient;
    Order order;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        ingredientsClient = new IngredientsClient();
    }

    @Test
    @DisplayName("Create order by non authorized user with valid ingredients")
    public void createOrderWithoutAuthWithValidIngredientsIsAllowedTest() {

        ValidatableResponse getResponse = ingredientsClient.getIngredients();
        String[] ingredients = new String[2];
        ingredients[0] = getResponse.extract().path("data._id[0]");
        ingredients[1] = getResponse.extract().path("data._id[1]");

        order = new Order(ingredients);

        ValidatableResponse orderResponse = orderClient.createOrder(order);
        int statusCode = orderResponse.extract().statusCode();
        boolean bodyJSON = orderResponse.extract().path("success");
        assertEquals("status cod is not correct", 200, statusCode);
        assertEquals("body JSON is not correct", true, bodyJSON);

    }

    @Test
    @DisplayName("Create order by non authorized user without ingredients")
    public void createOrderWithoutAuthWithoutIngredientsIsNotAllowedTest() {

        String[] ingredients = {null};
        order = new Order(ingredients);

        ValidatableResponse orderResponse = orderClient.createOrder(order);
        int statusCode = orderResponse.extract().statusCode();
        boolean bodyJSON = orderResponse.extract().path("success");
        assertEquals("status cod is not correct", 400, statusCode);
        assertEquals("body JSON is not correct", false, bodyJSON);

    }

    @Test
    @DisplayName("Create order by non authorized user with not valid ingredients")
    public void createOrderWithoutAuthWithNotValidIngredientsIsNotAllowedTest() {

        String[] ingredients = new String[2];
        ingredients[0] = RandomStringUtils.randomAlphabetic(21);
        ingredients[1] = RandomStringUtils.randomAlphabetic(21);
        order = new Order(ingredients);

        ValidatableResponse orderResponse = orderClient.createOrder(order);
        int statusCode = orderResponse.extract().statusCode();
        String statusMessage = orderResponse.extract().statusLine();
        assertEquals("status cod is not correct", 500, statusCode);
        assertEquals("body JSON is not correct", "HTTP/1.1 500 Internal Server Error", statusMessage);

    }
}
