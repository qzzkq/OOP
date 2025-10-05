package ru.nsu.kiryushin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Test Game
 */
public class GameTest{
    /**
     * Test launch game
     */
    @Test
    void Test0(){
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
     * Test ending game
     */
    @Test
    void Test1(){
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