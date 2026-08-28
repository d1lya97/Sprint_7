package com.example.generators;

import com.example.models.Courier;
import com.example.models.Order;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DataGenerator {

    private static final Random random = new Random();

    public static Courier generateRandomCourier() {

        String login =
                "courier_" +
                        UUID.randomUUID()
                                .toString()
                                .substring(0, 8);

        String password =
                "pass_" +
                        UUID.randomUUID()
                                .toString()
                                .substring(0, 6);

        String firstName =
                "Name_" +
                        random.nextInt(1000);

        return new Courier(
                login,
                password,
                firstName
        );
    }

    public static Order generateRandomOrder() {

        Order order = new Order();

        order.setFirstName("Иван");
        order.setLastName("Иванов");
        order.setAddress("Москва, ул. Пушкина, д. 1");
        order.setMetroStation("5");
        order.setPhone("+79261234567");
        order.setRentTime(random.nextInt(7) + 1);
        order.setDeliveryDate("2026-09-01");
        order.setComment("Тестовый заказ");

        return order;
    }

    public static Order generateOrderWithColors(
            List<String> colors
    ) {

        Order order = generateRandomOrder();

        order.setColor(colors);

        return order;
    }
}