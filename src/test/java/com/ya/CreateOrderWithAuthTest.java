package com.ya;

import com.ya.apiclient.IngredientsClient;
import com.ya.apiclient.OrderClient;
import com.ya.apiclient.UserClient;
import com.ya.model.Order;
import com.ya.model.User;
import com.ya.utils.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateOrderWithAuthTest {

    IngredientsClient ingredientsClient;
    OrderClient orderClient;
    Order order;
    UserClient userClient;
    User user;
    String accessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        ingredientsClient = new IngredientsClient();
        userClient = new UserClient();
        user = UserGenerator.getRandomUserData();
        accessToken = userClient.create(user).extract().path("accessToken");
    }

    @After
    public void tearDown() {

        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Create order by authorized user with valid ingredients")
    public void createOrderWithAuthWithValidIngredientsIsAllowedTest() {

        ValidatableResponse getResponse = ingredientsClient.getIngredients();
        String[] ingredients = new String[2];
        ingredients[0] = getResponse.extract().path("data._id[0]");
        ingredients[1] = getResponse.extract().path("data._id[1]");

        order = new Order(ingredients);

        ValidatableResponse orderResponse = orderClient.createOrderAuth(order, accessToken);
        int statusCode = orderResponse.extract().statusCode();
        boolean bodyJSON = orderResponse.extract().path("success");
        assertEquals("status cod is not correct", 200, statusCode);
        assertEquals("body JSON is not correct", true, bodyJSON);

    }

    @Test
    @DisplayName("Create order by authorized user without ingredients")
    public void createOrderWithAuthWithoutIngredientsIsNotAllowedTest() {

        String[] ingredients = {null};
        order = new Order(ingredients);

        ValidatableResponse orderResponse = orderClient.createOrderAuth(order, accessToken);
        int statusCode = orderResponse.extract().statusCode();
        boolean bodyJSON = orderResponse.extract().path("success");
        assertEquals("status cod is not correct", 400, statusCode);
        assertEquals("body JSON is not correct", false, bodyJSON);

    }

    @Test
    @DisplayName("Create order by authorized user with not valid ingredients")
    public void createOrderWithAuthWithNotValidIngredientsIsNotAllowedTest() {

        String[] ingredients = new String[2];
        ingredients[0] = RandomStringUtils.randomAlphabetic(21);
        ingredients[1] = RandomStringUtils.randomAlphabetic(21);
        order = new Order(ingredients);

        ValidatableResponse orderResponse = orderClient.createOrderAuth(order, accessToken);
        int statusCode = orderResponse.extract().statusCode();
        String statusMessage = orderResponse.extract().statusLine();
        assertEquals("status cod is not correct", 500, statusCode);
        assertEquals("body JSON is not correct", "HTTP/1.1 500 Internal Server Error", statusMessage);

    }
}
