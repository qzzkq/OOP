package ru.nsu.kiryushin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link Game} class.
 */
public class GameTest {
    /**
     * Ensures the game can be launched and terminated immediately.
     */
    @Test
    void gameTerminatesAfterImmediateQuit() {
        ByteArrayInputStream input =
                new ByteArrayInputStream(("1\nq\n").getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PrintStream oldOut = System.out;
        java.io.InputStream oldIn = System.in;
        try {
            System.setIn(input);
            System.setOut(new PrintStream(output));
            new Game().game();
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }

        String s = output.toString();
        assertTrue(s.contains("Игра завершена."));
    }

    /**
     * Ensures the game handles multiple rounds before termination.
     */
    @Test
    void gameHandlesMultipleRoundsBeforeQuit() {
        StringBuilder sb = new StringBuilder();
        sb.append("1\n");
        sb.append("\n");
        sb.append("q\n");
        sb.append("0\n");
        sb.append("\n");
        sb.append("q\n");
        for (int i = 0; i < 15; i++) {
            sb.append("\n");
            sb.append("q\n");
        }
        sb.append("\n");

        ByteArrayInputStream input = new ByteArrayInputStream(sb.toString().getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PrintStream oldOut = System.out;
        java.io.InputStream oldIn = System.in;
        try {
            System.setIn(input);
            System.setOut(new PrintStream(output, true));
            new Game().game();
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }

        String s = output.toString();
        assertTrue(s.contains("Игра завершена."));
    }
}
