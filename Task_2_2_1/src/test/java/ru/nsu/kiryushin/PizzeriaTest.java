package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PizzeriaTest {
    @Test
    void testRunDayProcessesOrdersInArrivalOrderAndSkipsLateOrders() throws InterruptedException {
        PizzeriaConfig config = new PizzeriaConfig(
                20,
                2,
                List.of(0L),
                List.of(1),
                List.of(
                        new Order(2, 10, 0),
                        new Order(3, 30, 0),
                        new Order(1, 0, 0)
                )
        );

        String output = captureOutput(() -> new Pizzeria(config).runDay());

        assertTrue(output.contains("[1] [RECEIVED]"));
        assertTrue(output.contains("[2] [RECEIVED]"));
        assertFalse(output.contains("[3] [RECEIVED]"));
        assertTrue(output.indexOf("[1] [RECEIVED]") < output.indexOf("[2] [RECEIVED]"));
        assertTrue(output.contains("[1] [DELIVERED]"));
        assertTrue(output.contains("[2] [DELIVERED]"));
    }

    @Test
    void testRunDayCompletesWhenThereAreNoOrders() {
        PizzeriaConfig config = new PizzeriaConfig(
                10,
                1,
                List.of(0L),
                List.of(1),
                List.of()
        );

        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> new Pizzeria(config).runDay());
    }

    private static String captureOutput(CheckedRunnable action) throws InterruptedException {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream captureStream = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        try {
            System.setOut(captureStream);
            action.run();
        } finally {
            System.setOut(originalOut);
            captureStream.close();
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @FunctionalInterface
    private interface CheckedRunnable {
        void run() throws InterruptedException;
    }
}
