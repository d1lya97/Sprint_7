package com.example;

import com.example.clients.CourierClient;
import com.example.generators.DataGenerator;
import com.example.models.Courier;
import com.example.models.CourierCredentials;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;

public class CourierLoginTest {

    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {

        courierClient = new CourierClient();
        courier = DataGenerator.generateRandomCourier();

        Response response =
                courierClient.createCourier(courier);

        response.then()
                .statusCode(SC_CREATED);
    }

    @Test
    @Description("Проверка успешной авторизации курьера с правильным логином и паролем")
    public void testLoginCourierSuccess() {

        CourierCredentials credentials =
                new CourierCredentials(
                        courier.getLogin(),
                        courier.getPassword()
                );

        Response response =
                courierClient.loginCourier(credentials);

        response.then()
                .statusCode(SC_OK)
                .body("id", notNullValue());

        Integer courierId =
                response
                        .then()
                        .extract()
                        .path("id");

        assertTrue(
                "ID курьера должен быть больше 0",
                courierId > 0
        );
    }

    @Test
    @Description("Проверка ошибки авторизации с неверным паролем")
    public void testLoginWithWrongPassword() {

        CourierCredentials credentials =
                new CourierCredentials(
                        courier.getLogin(),
                        "wrong_password"
                );

        Response response =
                courierClient.loginCourier(credentials);

        response.then()
                .statusCode(SC_NOT_FOUND)
                .body(
                        "message",
                        containsString("Учетная запись не найдена")
                );
    }

    @Test
    @Description("Проверка ошибки авторизации без указания пароля")
    public void testLoginWithoutPassword() {

        CourierCredentials credentials =
                new CourierCredentials(
                        courier.getLogin(),
                        null
                );

        Response response =
                courierClient.loginCourier(credentials);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body(
                        "message",
                        containsString("Недостаточно данных")
                );
    }

    @Test
    @Description("Проверка ошибки авторизации без указания логина")
    public void testLoginWithoutLogin() {

        CourierCredentials credentials =
                new CourierCredentials(
                        null,
                        courier.getPassword()
                );

        Response response =
                courierClient.loginCourier(credentials);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body(
                        "message",
                        containsString("Недостаточно данных")
                );
    }

    @Test
    @Description("Проверка ошибки авторизации с несуществующим логином")
    public void testLoginWithNonExistentLogin() {

        CourierCredentials credentials =
                new CourierCredentials(
                        "non_existent_login_"
                                + System.currentTimeMillis(),
                        "password"
                );

        Response response =
                courierClient.loginCourier(credentials);

        response.then()
                .statusCode(SC_NOT_FOUND)
                .body(
                        "message",
                        containsString("Учетная запись не найдена")
                );
    }

    @After
    public void tearDown() {

        Response loginResponse =
                courierClient.loginCourier(
                        new CourierCredentials(
                                courier.getLogin(),
                                courier.getPassword()
                        )
                );

        if (loginResponse.statusCode() == SC_OK) {

            Integer courierId =
                    loginResponse
                            .then()
                            .extract()
                            .path("id");

            if (courierId != null && courierId > 0) {
                courierClient.deleteCourier(courierId);
            }
        }
    }
}