package com.example;

import com.example.clients.OrderClient;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class OrderListTest {

    @Test
    @Description("Проверка получения списка заказов")
    public void testGetOrdersList() {

        OrderClient orderClient =
                new OrderClient();

        Response response =
                orderClient.getOrders();

        response.then()
                .statusCode(SC_OK)
                .body(
                        "orders",
                        is(not(empty()))
                );
    }
}