package ru.nsu.kiryushin;

import java.util.ArrayList;
import java.util.List;

/**
 * Owns worker threads and their lifecycle.
 */
public class WorkerDepartment {
    private final PizzeriaConfig config;
    private final OrderDepartment orderDepartment;
    private final List<Thread> bakerThreads = new ArrayList<>();
    private final List<Thread> courierThreads = new ArrayList<>();

    /**
     * Creates a worker department from the pizzeria configuration.
     *
     * @param config simulation settings
     * @param orderDepartment department that provides orders and storage
     */
    public WorkerDepartment(PizzeriaConfig config, OrderDepartment orderDepartment) {
        this.config = config;
        this.orderDepartment = orderDepartment;
    }

    /**
     * Starts all configured bakers and couriers.
     */
    public void startWorkers() {
        int bakerIndex = 1;
        for (long speedMs : config.getBakerSpeeds()) {
            Thread bakerThread = new Thread(
                    new Baker(speedMs, orderDepartment),
                    "Baker-" + bakerIndex++
            );
            bakerThreads.add(bakerThread);
            bakerThread.start();
        }

        int courierIndex = 1;
        for (int bagCapacity : config.getCourierCapacities()) {
            Thread courierThread = new Thread(
                    new Courier(bagCapacity, orderDepartment),
                    "Courier-" + courierIndex++
            );
            courierThreads.add(courierThread);
            courierThread.start();
        }
    }

    /**
     * Waits for bakers to finish, closes storage, and then waits for couriers.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public void waitForWorkers() throws InterruptedException {
        joinAll(bakerThreads);
        orderDepartment.closeStorage();
        joinAll(courierThreads);
    }

    private void joinAll(List<Thread> threads) throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
