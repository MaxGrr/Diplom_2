package com.ya;

import com.ya.apiclient.UserClient;
import com.ya.model.User;
import com.ya.utils.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParametrizedUserRegistrationTest {

    User user;
    int expectedStatusCode;
    boolean expectedBodyJson;

    public ParametrizedUserRegistrationTest(User user, int expectedStatusCode, boolean expectedBodyJson) {
        this.user = user;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedBodyJson = expectedBodyJson;
    }

    @Parameterized.Parameters(name = "Test data: {0} {1} {2}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {UserGenerator.getRandomEmailAndPasswordOnly(), 403, false},
                {UserGenerator.getRandomEmailAndNameOnly(), 403, false},
                {UserGenerator.getRandomPasswordAndNameOnly(), 403, false}
        };
    }

    @Test
    @DisplayName("Create user without required fields")
    public void requestIsNotAllowedWithoutAnyRequiredFields() {

        ValidatableResponse response = new UserClient().create(user);
        int actualStatusCode = response.extract().statusCode();
        boolean actualBodyJson = response.extract().path("success");

        assertEquals("status code is not valid", expectedStatusCode, actualStatusCode);
        assertEquals("body json is not valid", expectedBodyJson, actualBodyJson);
    }

}
