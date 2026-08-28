package com.example;

import com.example.clients.OrderClient;
import com.example.generators.DataGenerator;
import com.example.models.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final List<String> colors;
    private final String testDescription;

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
    @Step("Создание заказа: {1}")
    public void testCreateOrderWithColors() {

        OrderClient orderClient = new OrderClient();

        Order order =
                DataGenerator.generateOrderWithColors(colors);

        Response response =
                orderClient.createOrder(order);

        System.out.println(
                "Тест: "
                        + testDescription
                        + ". Статус: "
                        + response.statusCode()
        );

        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}