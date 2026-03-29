package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class PizzeriaConfigParserTest {
    @TempDir
    Path tempDir;

    @Test
    void testParse() throws IOException {
        Path configFile = tempDir.resolve("config.json");
        Files.writeString(configFile, """
                {
                  "workDurationMs": 1000,
                  "warehouseCapacity": 2,
                  "bakers": [
                    {"speedMs": 300},
                    {"speedMs": 500}
                  ],
                  "couriers": [
                    {"bagCapacity": 1},
                    {"bagCapacity": 2}
                  ],
                  "orders": [
                    {"id": 1, "arrivalTimeMs": 0, "deliveryTimeMs": 100},
                    {"id": 2, "arrivalTimeMs": 250, "deliveryTimeMs": 200}
                  ]
                }
                """);

        PizzeriaConfig config = PizzeriaConfigParser.parse(configFile.toString());

        assertEquals(1000, config.getWorkDurationMs());
        assertEquals(2, config.getWarehouseCapacity());
        assertEquals(List.of(300L, 500L), config.getBakerSpeeds());
        assertEquals(List.of(1, 2), config.getCourierCapacities());
        assertEquals(2, config.getOrders().size());
        assertEquals(2, config.getOrders().get(1).getId());
    }

    @Test
    void testNoOrdersField() throws IOException {
        Path configFile = tempDir.resolve("missing-orders.json");
        Files.writeString(configFile, """
                {
                  "workDurationMs": 1000,
                  "warehouseCapacity": 2,
                  "bakers": [{"speedMs": 300}],
                  "couriers": [{"bagCapacity": 1}]
                }
                """);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PizzeriaConfigParser.parse(configFile.toString())
        );

        assertTrue(exception.getMessage().contains("Missing required field: orders"));
    }

    @Test
    void testFractionalNumber() throws IOException {
        Path configFile = tempDir.resolve("fractional.json");
        Files.writeString(configFile, """
                {
                  "workDurationMs": 1000,
                  "warehouseCapacity": 2,
                  "bakers": [{"speedMs": 300.5}],
                  "couriers": [{"bagCapacity": 1}],
                  "orders": [{"id": 1, "arrivalTimeMs": 0, "deliveryTimeMs": 100}]
                }
                """);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PizzeriaConfigParser.parse(configFile.toString())
        );

        assertTrue(exception.getMessage().contains("Invalid config format"));
    }
}
