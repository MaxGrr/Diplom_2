package com.ya.apiclient;

import com.ya.model.Order;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends BurgerRestClient{

    private static final String ORDER_PATH = "orders";

    @Step("Create order without authorization")
    public ValidatableResponse createOrder(Order order) {

        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Create order with authorization")
    public ValidatableResponse createOrderAuth(Order order, String accessToken) {

        return given()
                .spec(getBaseSpec())
                .header("authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Get order list with authorization")
    public ValidatableResponse getOrderList(String accessToken) {

        return given()
                .spec(getBaseSpec())
                .header("authorization", accessToken)
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Get order list without authorization")
    public ValidatableResponse getOrderListNoAuth() {

        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }
}
