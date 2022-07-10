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

public class UserRegistrationTest {

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

        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Register new valid user")
    public void userCanRegisterWithValidDataTest() {

        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        boolean bodyJson = response.extract().path("success");
        int statusCode = response.extract().statusCode();

        assertEquals("body json is not valid", true, bodyJson);
        assertEquals("status cod is not valid", 200, statusCode);
    }

    @Test
    @DisplayName("Register user with existed email")
    public void userCannotRegisterWithExistedEmailTest() {

        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");

        ValidatableResponse secondResponse = userClient.create(user);
        boolean bodyJson = secondResponse.extract().path("success");
        int statusCode = secondResponse.extract().statusCode();
        assertEquals("body json is not valid", false, bodyJson);
        assertEquals("status cod is not valid", 403, statusCode);
    }

}
