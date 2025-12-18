package ru.nsu.kiryushin;

import org.junit.jupiter.api.Test;
import java.io.StringReader;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubstringSearcherTest {

    @Test
    void testExample() {
        String text = "абракадабра";
        List<Integer> result = new SubstringSearcher(new StringReader(text), "бра").search();
        assertEquals(List.of(1, 8), result);
    }

    @Test
    void testOverlap() {
        String text = "aaaaa";
        List<Integer> result = new SubstringSearcher(new StringReader(text), "aa").search();
        assertEquals(List.of(0, 1, 2, 3), result);
    }

    @Test
    void testNoMatch() {
        String text = "abcdefg";
        List<Integer> result = new SubstringSearcher(new StringReader(text), "xyz").search();
        assertTrue(result.isEmpty());
    }

    @Test
    void testWholeString() {
        String text = "hello";
        List<Integer> result = new SubstringSearcher(new StringReader(text), "hello").search();
        assertEquals(List.of(0), result);
    }

    @Test
    void testOneChar() {
        String text = "abcabc";
        List<Integer> result = new SubstringSearcher(new StringReader(text), "a").search();
        assertEquals(List.of(0, 3), result);
    }

    @Test
    void testCyrillic() {
        String text = "Привет, ФИТ!";
        List<Integer> result = new SubstringSearcher(new StringReader(text), "Привет").search();
        assertEquals(List.of(0), result);
    }

    @Test
    void testEmoji() {
        String text = "😀x😀😀";
        List<Integer> result = new SubstringSearcher(new StringReader(text), "😀😀").search();
        assertEquals(List.of(2), result);
    }

    @Test
    void testLargeInput() {
        int prefixLength = 20_000;
        String marker = "XYZPATTERNXYZ";
        String text = "a".repeat(prefixLength) + marker + "bbbbbbbbbb";
        List<Integer> result = new SubstringSearcher(new StringReader(text), "PATTERN").search();
        assertEquals(List.of(prefixLength + 3), result);
    }

    @Test
    void testNullReader() {
        assertThrows(IllegalArgumentException.class,
                () -> new SubstringSearcher(null, "test"));
    }

    @Test
    void testNullSubstring() {
        assertThrows(IllegalArgumentException.class,
                () -> new SubstringSearcher(new StringReader("abc"), null));
    }

    @Test
    void testEmptySubstring() {
        assertThrows(IllegalArgumentException.class,
                () -> new SubstringSearcher(new StringReader("abc"), ""));
    }

    @Test
    void testManyEmoji() {
        int emojiCount = 10_000;
        int half = emojiCount / 2;
        String emoji = "😀";
        String text = emoji.repeat(half) + 'x' + emoji.repeat(half);
        List<Integer> result = new SubstringSearcher(new StringReader(text), "x").search();
        assertEquals(List.of(half), result);
    }
}