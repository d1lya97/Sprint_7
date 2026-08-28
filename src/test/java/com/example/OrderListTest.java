package com.example;

import com.example.clients.OrderClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class OrderListTest {

    @Test
    @Step("Получение списка заказов")
    public void testGetOrdersList() {

        OrderClient orderClient = new OrderClient();

        Response response = orderClient.getOrders();

        int statusCode = response.statusCode();

        System.out.println(
                "Получение списка заказов. Статус: "
                        + statusCode
        );

        System.out.println(
                "Ответ сервера: "
                        + response.asString()
        );

        response.then()
                .statusCode(200)
                .body("orders", is(not(empty())));
    }
}

