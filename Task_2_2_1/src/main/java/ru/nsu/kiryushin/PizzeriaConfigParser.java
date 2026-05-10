package ru.nsu.kiryushin;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Parses a pizzeria configuration from a JSON file.
 */
public final class PizzeriaConfigParser {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);

    private record RawConfig(
            Long workDurationMs,
            Integer warehouseCapacity,
            List<RawBaker> bakers,
            List<RawCourier> couriers,
            List<RawOrder> orders
    ) {
    }

    private record RawBaker(Long speedMs) {
    }

    private record RawCourier(Integer bagCapacity) {
    }

    private record RawOrder(Integer id, Long arrivalTimeMs, Long deliveryTimeMs) {
    }

    /**
     * Prevents instantiation of this utility class.
     */
    private PizzeriaConfigParser() {
    }

    /**
     * Reads and parses a configuration file.
     *
     * @param path path to the JSON configuration file
     * @return parsed simulation configuration
     * @throws IOException if the file cannot be read
     */
    public static PizzeriaConfig parse(String path) throws IOException {
        try {
            RawConfig raw = OBJECT_MAPPER.readValue(Path.of(path).toFile(), RawConfig.class);
            return new PizzeriaConfig(
                    require(raw.workDurationMs(), "workDurationMs"),
                    require(raw.warehouseCapacity(), "warehouseCapacity"),
                    parseBakers(require(raw.bakers(), "bakers")),
                    parseCouriers(require(raw.couriers(), "couriers")),
                    parseOrders(require(raw.orders(), "orders"))
            );
        } catch (DatabindException e) {
            throw new IllegalArgumentException("Invalid config format", e);
        }
    }

    /**
     * Parses baker definitions.
     *
     * @param rawBakers parsed baker list
     * @return baker cooking times in milliseconds
     */
    private static List<Long> parseBakers(List<RawBaker> rawBakers) {
        return rawBakers.stream()
                .map(baker -> require(require(baker, "baker").speedMs(), "baker.speedMs"))
                .toList();
    }

    /**
     * Parses courier definitions.
     *
     * @param rawCouriers parsed courier list
     * @return courier bag capacities
     */
    private static List<Integer> parseCouriers(List<RawCourier> rawCouriers) {
        return rawCouriers.stream()
                .map(courier -> require(require(courier, "courier").bagCapacity(), "courier.bagCapacity"))
                .toList();
    }

    /**
     * Parses order definitions.
     *
     * @param rawOrders parsed order list
     * @return parsed order descriptions
     */
    private static List<Order> parseOrders(List<RawOrder> rawOrders) {
        return rawOrders.stream()
                .map(order -> {
                    RawOrder safeOrder = require(order, "order");
                    return new Order(
                            require(safeOrder.id(), "order.id"),
                            require(safeOrder.arrivalTimeMs(), "order.arrivalTimeMs"),
                            require(safeOrder.deliveryTimeMs(), "order.deliveryTimeMs")
                    );
                })
                .toList();
    }

    /**
     * Checks that the provided value is not {@code null}.
     *
     * @param value value to validate
     * @param fieldName field name used in error messages
     * @return original non-null value
     * @param <T> value type
     */
    private static <T> T require(T value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException("Missing required field: " + fieldName);
        }
        return value;
    }
}
