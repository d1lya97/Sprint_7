package com.example;

import com.example.clients.OrderClient;
import com.example.generators.DataGenerator;
import com.example.models.Order;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final List<String> colors;
    private final String testDescription;

    private final OrderClient orderClient = new OrderClient();

    private Integer track;

    public OrderCreateTest(
            List<String> colors,
            String testDescription
    ) {
        this.colors = colors;
        this.testDescription = testDescription;
    }

    @Parameterized.Parameters(name = "{1}")
    public static Object[][] getOrderData() {

        return new Object[][]{
                {
                        Arrays.asList("BLACK"),
                        "Только BLACK"
                },
                {
                        Arrays.asList("GREY"),
                        "Только GREY"
                },
                {
                        Arrays.asList("BLACK", "GREY"),
                        "BLACK и GREY"
                },
                {
                        null,
                        "Без цвета"
                }
        };
    }

    @Test
    @Description("Проверка успешного создания заказа с разными вариантами цвета")
    public void testCreateOrderWithColors() {

        Order order =
                DataGenerator.generateOrderWithColors(colors);

        Response response =
                orderClient.createOrder(order);

        response.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());

        track = response
                .then()
                .extract()
                .path("track");
    }

    @After
    public void tearDown() {

        if (track != null) {
            orderClient.cancelOrder(String.valueOf(track));
        }
    }
}