package com.example.clients;

import com.example.models.Order;
import io.qameta.allure.Step;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String BASE_URL =
            "https://qa-scooter.praktikum-services.ru";

    private static final String ORDER_PATH =
            "/api/v1/orders";

    private static final RestAssuredConfig config =
            RestAssuredConfig.config()
                    .httpClient(
                            HttpClientConfig.httpClientConfig()
                                    .setParam(
                                            "http.connection.timeout",
                                            30000
                                    )
                                    .setParam(
                                            "http.socket.timeout",
                                            30000
                                    )
                                    .setParam(
                                            "http.connection-manager.timeout",
                                            30000
                                    )
                    );

    @Step("Создать заказ")
    public Response createOrder(Order order) {
        return given()
                .config(config)
                .header("Content-Type", "application/json")
                .body(order)
                .post(BASE_URL + ORDER_PATH);
    }

    @Step("Получить список заказов")
    public Response getOrders() {
        return given()
                .config(config)
                .get(BASE_URL + ORDER_PATH);
    }

    @Step("Отменить заказ с номером {track}")
    public Response cancelOrder(String track) {
        return given()
                .config(config)
                .put(BASE_URL + ORDER_PATH + "/cancel?track=" + track);
    }
}

