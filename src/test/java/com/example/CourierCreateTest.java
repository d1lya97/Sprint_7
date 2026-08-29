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
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest {

    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = DataGenerator.generateRandomCourier();
    }

    @Test
    @Description("Проверка успешного создания курьера с валидными данными")
    public void testCreateCourierSuccess() {

        Response response =
                courierClient.createCourier(courier);

        response.then()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @Description("Проверка невозможности создать двух курьеров с одинаковым логином")
    public void testCreateDuplicateCourier() {

        Response firstResponse =
                courierClient.createCourier(courier);

        firstResponse.then()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));

        Response secondResponse =
                courierClient.createCourier(courier);

        secondResponse.then()
                .statusCode(SC_CONFLICT)
                .body(
                        "message",
                        containsString("Этот логин уже используется")
                );
    }

    @Test
    @Description("Проверка ошибки при создании курьера без логина")
    public void testCreateCourierWithoutLogin() {

        Courier courierWithoutLogin =
                new Courier(
                        null,
                        courier.getPassword(),
                        courier.getFirstName()
                );

        Response response =
                courierClient.createCourier(courierWithoutLogin);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body(
                        "message",
                        containsString(
                                "Недостаточно данных для создания учетной записи"
                        )
                );
    }

    @Test
    @Description("Проверка ошибки при создании курьера без пароля")
    public void testCreateCourierWithoutPassword() {

        Courier courierWithoutPassword =
                new Courier(
                        courier.getLogin(),
                        null,
                        courier.getFirstName()
                );

        Response response =
                courierClient.createCourier(courierWithoutPassword);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body(
                        "message",
                        containsString(
                                "Недостаточно данных для создания учетной записи"
                        )
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