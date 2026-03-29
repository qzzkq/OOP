package ru.nsu.kiryushin;
import java.util.ArrayList;
import java.util.List;

/**
 * Immutable configuration of a pizzeria workday simulation.
 */
public class PizzeriaConfig {
    private final long workDurationMs;
    private final int warehouseCapacity;
    private final List<Long> bakerSpeeds;
    private final List<Integer> courierCapacities;
    private final List<Order> orders;

    /**
     * Creates a validated simulation configuration.
     *
     * @param workDurationMs workday duration in milliseconds
     * @param warehouseCapacity maximum number of finished orders in storage
     * @param bakerSpeeds baking times for each baker
     * @param courierCapacities bag capacities for each courier
     * @param orders orders planned for the day
     */
    public PizzeriaConfig(
            long workDurationMs,
            int warehouseCapacity,
            List<Long> bakerSpeeds,
            List<Integer> courierCapacities,
            List<Order> orders
    ) {
        if (warehouseCapacity <= 0) {
            throw new IllegalArgumentException("warehouseCapacity must be positive");
        }
        if (bakerSpeeds == null || bakerSpeeds.isEmpty()) {
            throw new IllegalArgumentException("No bakers");
        }
        if (courierCapacities == null || courierCapacities.isEmpty()) {
            throw new IllegalArgumentException("No couriers");
        }
        if (orders == null) {
            throw new IllegalArgumentException("orders is null");
        }
        this.workDurationMs = workDurationMs;
        this.warehouseCapacity = warehouseCapacity;
        this.bakerSpeeds = List.copyOf(bakerSpeeds);
        this.courierCapacities = List.copyOf(courierCapacities);
        this.orders = List.copyOf(new ArrayList<>(orders));
    }

    /**
     * Returns the workday duration.
     *
     * @return workday duration in milliseconds
     */
    public long getWorkDurationMs() {
        return workDurationMs;
    }

    /**
     * Returns the storage capacity.
     *
     * @return maximum number of finished orders in storage
     */
    public int getWarehouseCapacity() {
        return warehouseCapacity;
    }

    /**
     * Returns configured baker speeds.
     *
     * @return immutable list of baking times in milliseconds
     */
    public List<Long> getBakerSpeeds() {
        return bakerSpeeds;
    }

    /**
     * Returns configured courier bag capacities.
     *
     * @return immutable list of courier capacities
     */
    public List<Integer> getCourierCapacities() {
        return courierCapacities;
    }

    /**
     * Returns configured orders.
     *
     * @return immutable list of orders
     */
    public List<Order> getOrders() {
        return orders;
    }
}
