package com.ya;

import com.github.javafaker.Faker;
import com.ya.apiclient.UserClient;
import com.ya.model.User;
import com.ya.utils.UserCredentials;
import com.ya.utils.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserLoginTest {

    UserClient userClient;
    User user;
    String accessToken;

    @Before
    public void setUp() {

        userClient = new UserClient();
        user = UserGenerator.getRandomUserData();
        accessToken = userClient.create(user).extract().path("accessToken");
    }

    @After
    public void tearDown() {

        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Login existed user with valid credentials is correct")
    public void userCanLoginWithValidCredentialsTest() {

        ValidatableResponse response = userClient.login(new UserCredentials(user.getEmail(), user.getPassword()));
        int statusCode = response.extract().statusCode();
        boolean bodyJSON = response.extract().path("success");

        assertEquals("status code is not valid", 200, statusCode);
        assertEquals("body JSON is not valid", true, bodyJSON);
    }

    @Test
    @DisplayName("Login user with wrong email is not allowed")
    public void userCanNotLoginWithWrongEmailTest() {

        ValidatableResponse response = userClient.login(new UserCredentials(new Faker().internet().emailAddress(), user.getPassword()));
        int statusCode = response.extract().statusCode();
        boolean bodyJSON = response.extract().path("success");

        assertEquals("status code is not valid", 401, statusCode);
        assertEquals("body JSON is not valid", false, bodyJSON);
    }


    @Test
    @DisplayName("Login user with wrong password is not allowed")
    public void userCanNotLoginWithWrongPasswordTest() {

        ValidatableResponse response = userClient.login(new UserCredentials(user.getEmail(), new Faker().internet().password()));
        int statusCode = response.extract().statusCode();
        boolean bodyJSON = response.extract().path("success");

        assertEquals("status code is not valid", 401, statusCode);
        assertEquals("body JSON is not valid", false, bodyJSON);
    }

}
