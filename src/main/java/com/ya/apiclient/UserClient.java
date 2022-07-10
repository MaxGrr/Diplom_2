package com.ya.apiclient;

import com.ya.model.User;
import com.ya.utils.UserCredentials;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends BurgerRestClient {

    private static final String USER_PATH = "auth/";

    @Step("Create user")
    public ValidatableResponse create(User user) {

        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "register")
                .then();
    }

    @Step("Delete user")
    public ValidatableResponse delete(String accessToken) {

        return given()
                .spec(getBaseSpec())
                .header("authorization", accessToken)
                .when()
                .delete(USER_PATH + "user")
                .then();
    }

    @Step("Login with credentials{credentials}")
    public ValidatableResponse login(UserCredentials credentials) {

        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(USER_PATH + "login")
                .then();
    }

    @Step("Change user data with authorization")
    public ValidatableResponse changeData(User patchedUser, String accessToken) {

        return given()
                .spec(getBaseSpec())
                .header("authorization", accessToken)
                .body(patchedUser)
                .when()
                .patch(USER_PATH + "user")
                .then();
    }

    @Step("Change user data without authorization")
    public ValidatableResponse changeDataNoAuth(User patchedUser) {

        return given()
                .spec(getBaseSpec())
                .body(patchedUser)
                .when()
                .patch(USER_PATH + "user")
                .then();
    }
}
