package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private DataGenerator() {
    }

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void setUp(UserInfo user) {
        given()
            .spec(requestSpec)
            .body(user)
        .when()
            .post("/api/system/users")
        .then()
            .statusCode(200);
    }

    public static String getUserName(String Locale) {
        Faker faker = new Faker(new Locale(Locale));
        return faker.name().username();
    }

    public static String getUserPassword(String Locale) {
        Faker faker = new Faker(new Locale(Locale));
        return faker.internet().password();
    }

    public static UserInfo generateUser(String locale, boolean isBlocked) {
        return new UserInfo(
            getUserName(locale),
            getUserPassword(locale),
            (isBlocked) ? "blocked" : "active");
    }

    public static UserInfo generateValidUser(String locale, boolean isBlocked) {
        UserInfo user = generateUser(locale, isBlocked);
        setUp(user);
        return user;
    }

    public static UserInfo generateInvalidLogin(String locale, boolean isBlocked) {
        String password = getUserPassword(locale);
        UserInfo user = new UserInfo(
            "vasya",
            password,
            (isBlocked) ? "blocked" : "active");
        setUp(user);
        return new UserInfo(
            "aysav",
            password,
            (isBlocked) ? "blocked" : "active");
    }

    public static UserInfo generateInvalidPassword(String locale, boolean isBlocked) {
        String login = getUserName(locale);
        UserInfo user = new UserInfo(
            login,
            "password",
            (isBlocked) ? "blocked" : "active");
        setUp(user);
        return new UserInfo(
            login,
            "wordpass",
            (isBlocked) ? "blocked" : "active");
    }
}
