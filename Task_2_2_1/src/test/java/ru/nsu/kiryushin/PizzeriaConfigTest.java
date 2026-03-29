package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PizzeriaConfigTest {
    @Test
    void testWrongWarehouseCapacity() {
        assertThrows(IllegalArgumentException.class,
                () -> new PizzeriaConfig(1000, 0, List.of(100L), List.of(1), List.of()));
    }

    @Test
    void testNoBakers() {
        assertThrows(IllegalArgumentException.class,
                () -> new PizzeriaConfig(1000, 1, List.of(), List.of(1), List.of()));
    }

    @Test
    void testNoCouriers() {
        assertThrows(IllegalArgumentException.class,
                () -> new PizzeriaConfig(1000, 1, List.of(100L), List.of(), List.of()));
    }

    @Test
    void testNullOrders() {
        assertThrows(IllegalArgumentException.class,
                () -> new PizzeriaConfig(1000, 1, List.of(100L), List.of(1), null));
    }

    @Test
    void testCopiesLists() {
        List<Long> bakerSpeeds = new ArrayList<>(List.of(100L));
        List<Integer> courierCapacities = new ArrayList<>(List.of(2));
        List<Order> orders = new ArrayList<>(List.of(new Order(7, 50, 120)));

        PizzeriaConfig config = new PizzeriaConfig(1000, 3, bakerSpeeds, courierCapacities, orders);

        bakerSpeeds.add(200L);
        courierCapacities.add(4);
        orders.add(new Order(8, 60, 140));

        assertEquals(List.of(100L), config.getBakerSpeeds());
        assertEquals(List.of(2), config.getCourierCapacities());
        assertEquals(1, config.getOrders().size());
        assertThrows(UnsupportedOperationException.class, () -> config.getOrders().add(new Order(9, 0, 0)));
    }
}
