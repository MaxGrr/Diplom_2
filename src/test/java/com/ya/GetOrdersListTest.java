package com.ya;

import com.ya.apiclient.IngredientsClient;
import com.ya.apiclient.OrderClient;
import com.ya.apiclient.UserClient;
import com.ya.model.Order;
import com.ya.model.User;
import com.ya.utils.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetOrdersListTest {

    IngredientsClient ingredientsClient;
    OrderClient orderClient;
    Order order;
    UserClient userClient;
    User user;
    String accessToken;

    @Before
    public void setUp() {

        ingredientsClient = new IngredientsClient();
        userClient = new UserClient();
        orderClient = new OrderClient();

        ValidatableResponse getResponse = ingredientsClient.getIngredients();
        String[] ingredients = new String[2];
        ingredients[0] = getResponse.extract().path("data._id[0]");
        ingredients[1] = getResponse.extract().path("data._id[1]");

        user = UserGenerator.getRandomUserData();
        accessToken = userClient.create(user).extract().path("accessToken");

        order = new Order(ingredients);
        orderClient.createOrderAuth(order, accessToken);
    }

    @After
    public void tearDown() {
            userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Get orders list by authorized user")
    public void userCanGetOrdersListWithAuthTest() {

        ValidatableResponse response = orderClient.getOrderList(accessToken);
        int statusCode = response.extract().statusCode();
        boolean bodyJSON = response.extract().path("success");
        assertEquals("status code is not valid", 200, statusCode);
        assertEquals("body JSON is not valid", true, bodyJSON);
    }

    @Test
    @DisplayName("Get orders list by not authorized user")
    public void userCanNotGetOrdersListWithoutAuthTest() {

        ValidatableResponse response = orderClient.getOrderListNoAuth();
        int statusCode = response.extract().statusCode();
        boolean bodyJSON = response.extract().path("success");
        assertEquals("status code is not valid", 401, statusCode);
        assertEquals("body json is not valid", false, bodyJSON);
    }


}
