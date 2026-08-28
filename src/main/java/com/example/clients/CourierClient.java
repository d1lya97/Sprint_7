package com.example.clients;

import com.example.models.Courier;
import com.example.models.CourierCredentials;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {


    private static final String BASE_URL =
            "https://qa-scooter.praktikum-services.ru";

    private static final String COURIER_PATH =
            "/api/v1/courier";

    public Response createCourier(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .post(BASE_URL + COURIER_PATH);
    }

    public Response loginCourier(CourierCredentials credentials) {
        return given()
                .header("Content-Type", "application/json")
                .body(credentials)
                .post(BASE_URL + COURIER_PATH + "/login");
    }

    public Response deleteCourier(int courierId) {
        return given()
                .delete(BASE_URL + COURIER_PATH + "/" + courierId);
    }

}
