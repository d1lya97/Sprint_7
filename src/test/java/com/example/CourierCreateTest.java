package com.example;

import com.example.clients.CourierClient;
import com.example.generators.DataGenerator;
import com.example.models.Courier;
import com.example.models.CourierCredentials;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;

public class CourierCreateTest {

    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    @Step("Подготовка данных для создания курьера")
    public void setUp() {
        courierClient = new CourierClient();
        courier = DataGenerator.generateRandomCourier();
    }

    @Test
    @Step("Создание курьера с валидными данными")
    public void testCreateCourierSuccess() {

        Response response = courierClient.createCourier(courier);

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));

        Response loginResponse = courierClient.loginCourier(
                new CourierCredentials(
                        courier.getLogin(),
                        courier.getPassword()
                )
        );

        loginResponse.then()
                .statusCode(200);

        courierId = loginResponse.jsonPath().getInt("id");

        assertNotNull(
                "ID созданного курьера не должен быть null",
                courierId
        );
    }

    @Test
    @Step("Создание двух одинаковых курьеров")
    public void testCreateDuplicateCourier() {

        Response firstResponse = courierClient.createCourier(courier);

        firstResponse.then()
                .statusCode(201)
                .body("ok", equalTo(true));

        Response loginResponse = courierClient.loginCourier(
                new CourierCredentials(
                        courier.getLogin(),
                        courier.getPassword()
                )
        );

        loginResponse.then()
                .statusCode(200);

        courierId = loginResponse.jsonPath().getInt("id");

        Response secondResponse = courierClient.createCourier(courier);

        secondResponse.then()
                .statusCode(409)
                .body(
                        "message",
                        containsString("Этот логин уже используется")
                );
    }

    @Test
    @Step("Создание курьера без логина")
    public void testCreateCourierWithoutLogin() {

        Courier courierWithoutLogin = new Courier(
                null,
                courier.getPassword(),
                courier.getFirstName()
        );

        Response response = courierClient.createCourier(courierWithoutLogin);

        response.then()
                .statusCode(400)
                .body(
                        "message",
                        containsString(
                                "Недостаточно данных для создания учетной записи"
                        )
                );
    }

    @Test
    @Step("Создание курьера без пароля")
    public void testCreateCourierWithoutPassword() {

        Courier courierWithoutPassword = new Courier(
                courier.getLogin(),
                null,
                courier.getFirstName()
        );

        Response response = courierClient.createCourier(courierWithoutPassword);

        response.then()
                .statusCode(400)
                .body(
                        "message",
                        containsString(
                                "Недостаточно данных для создания учетной записи"
                        )
                );
    }

    @After
    @Step("Удаление созданного курьера")
    public void tearDown() {

        if (courierId != null && courierId > 0) {
            Response response = courierClient.deleteCourier(courierId);

            System.out.println(
                    "Удаление курьера. Статус: "
                            + response.statusCode()
            );
        }
    }
}