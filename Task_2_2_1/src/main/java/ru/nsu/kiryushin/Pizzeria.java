package ru.nsu.kiryushin;

/**
 * Coordinates a full pizzeria workday simulation.
 */
public class Pizzeria {
    private final WorkerDepartment workerDepartment;
    private final OrderDepartment orderDepartment;

    /**
     * Creates a simulation instance from an immutable configuration.
     *
     * @param config simulation settings
     */
    public Pizzeria(PizzeriaConfig config) {
        this.orderDepartment = new OrderDepartment(config);
        this.workerDepartment = new WorkerDepartment(config);
    }

    /**
     * Runs the pizzeria for one simulated workday.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public void runDay() throws InterruptedException {
        workerDepartment.startWorkers(orderDepartment);

        orderDepartment.acceptOrdersDuringWorkingHours();

        workerDepartment.waitForBakers();
        orderDepartment.closeStorage();
        workerDepartment.waitForCoriers();
    }
}
