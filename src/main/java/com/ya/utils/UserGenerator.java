package com.ya.utils;

import com.github.javafaker.Faker;
import com.ya.model.User;
import io.qameta.allure.Step;

public class UserGenerator {

    @Step("Create random user Data")
    public static User getRandomUserData() {

        Faker faker = new Faker();
        final String email = faker.internet().emailAddress();
        final String password = faker.internet().password();
        final String name = faker.name().firstName();
        return new User(email, password, name);
    }

    @Step("Create random user email and password")
    public static User getRandomEmailAndPasswordOnly() {

        Faker faker = new Faker();
        return new User().setEmailAndPassword(faker.internet().emailAddress(), faker.internet().password());
    }

    @Step("Create random user email and name")
    public static User getRandomEmailAndNameOnly() {

        Faker faker = new Faker();
        return new User().setEmailAndName(faker.internet().emailAddress(), faker.name().firstName());
    }

    @Step("Create random user password and name")
    public static User getRandomPasswordAndNameOnly() {

        Faker faker = new Faker();
        return new User().setPasswordAndName(faker.internet().password(), faker.name().firstName());
    }

}
