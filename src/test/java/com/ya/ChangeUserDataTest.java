package com.ya;

import com.ya.apiclient.UserClient;
import com.ya.model.User;
import com.ya.utils.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChangeUserDataTest {

    UserClient userClient;
    User user;
    String accessToken;

    @Before
    public void setUp() {

        userClient = new UserClient();
        user = UserGenerator.getRandomUserData();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Change user data with authorization")
    public void canChangeUserDataWithAuthorizationTest() {

        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");

        User patchedUser = UserGenerator.getRandomUserData();
        ValidatableResponse response = userClient.changeData(patchedUser, accessToken);

        int statusCode = response.extract().statusCode();
        boolean bodyJSON = response.extract().path("success");

        assertEquals("status code is not correct", 200, statusCode);
        assertEquals("body JSON is ton correct", true, bodyJSON);
    }


    @Test
    @DisplayName("Change user data with authorization")
    public void canChangeUserDataWithoutAuthorizationTest() {

        User patchedUser = UserGenerator.getRandomUserData();
        ValidatableResponse response = userClient.changeDataNoAuth(patchedUser);

        int statusCode = response.extract().statusCode();
        boolean bodyJSON = response.extract().path("success");

        assertEquals("status code is not correct", 401, statusCode);
        assertEquals("body JSON is ton correct", false, bodyJSON);
    }
}
