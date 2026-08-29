package com.example.clients;

import com.example.models.Courier;
import com.example.models.CourierCredentials;
import io.qameta.allure.Step;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String BASE_URL =
            "https://qa-scooter.praktikum-services.ru";

    private static final String COURIER_PATH =
            "/api/v1/courier";

    private static final RestAssuredConfig config =
            RestAssuredConfig.config()
                    .httpClient(
                            HttpClientConfig.httpClientConfig()
                                    .setParam("http.connection.timeout", 30000)
                                    .setParam("http.socket.timeout", 30000)
                                    .setParam("http.connection-manager.timeout", 30000)
                    );

    @Step("Создать курьера")
    public Response createCourier(Courier courier) {
        return given()
                .config(config)
                .header("Content-Type", "application/json")
                .body(courier)
                .post(BASE_URL + COURIER_PATH);
    }

    @Step("Авторизовать курьера")
    public Response loginCourier(CourierCredentials credentials) {
        return given()
                .config(config)
                .header("Content-Type", "application/json")
                .body(credentials)
                .post(BASE_URL + COURIER_PATH + "/login");
    }

    @Step("Удалить курьера с ID {courierId}")
    public Response deleteCourier(int courierId) {
        return given()
                .config(config)
                .delete(BASE_URL + COURIER_PATH + "/" + courierId);
    }
}