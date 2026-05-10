package ru.nsu.kiryushin;

/**
 * Represents a customer order with arrival and delivery timings.
 */
public class Order {
    private final int id;
    private final long arrivalTimeMs;
    private final long deliveryTimeMs;

    /**
     * Creates an order description.
     *
     * @param id unique order identifier
     * @param arrivalTimeMs time from opening when the order appears, in milliseconds
     * @param deliveryTimeMs time required to deliver the order, in milliseconds
     */
    public Order(int id, long arrivalTimeMs, long deliveryTimeMs) {
        this.id = id;
        this.arrivalTimeMs = arrivalTimeMs;
        this.deliveryTimeMs = deliveryTimeMs;
    }

    /**
     * Returns the order identifier.
     *
     * @return order identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the planned order arrival time.
     *
     * @return time from opening in milliseconds
     */
    public long getArrivalTimeMs() {
        return arrivalTimeMs;
    }

    /**
     * Returns the delivery duration for this order.
     *
     * @return delivery duration in milliseconds
     */
    public long getDeliveryTimeMs() {
        return deliveryTimeMs;
    }
}
