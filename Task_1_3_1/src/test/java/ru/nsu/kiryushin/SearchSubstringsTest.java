package ru.nsu.kiryushin;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubstringSearcherTest {

    private final SubstringSearcher searcher = new SubstringSearcher();

    @Test
    void testExample() {
        String text = "абракадабра";
        List<Integer> result = searcher.search(new StringReader(text), "бра");
        assertEquals(List.of(1, 8), result);
    }

    @Test
    void testOverlap() {
        String text = "aaaaa";
        List<Integer> result = searcher.search(new StringReader(text), "aa");
        assertEquals(List.of(0, 1, 2, 3), result);
    }

    @Test
    void testNoMatch() {
        String text = "abcdefg";
        List<Integer> result = searcher.search(new StringReader(text), "xyz");
        assertTrue(result.isEmpty());
    }

    @Test
    void testWholeString() {
        String text = "hello";
        List<Integer> result = searcher.search(new StringReader(text), "hello");
        assertEquals(List.of(0), result);
    }

    @Test
    void testOneChar() {
        String text = "abcabc";
        List<Integer> result = searcher.search(new StringReader(text), "a");
        assertEquals(List.of(0, 3), result);
    }

    @Test
    void testCyrillic() {
        String text = "Привет, ФИТ!";
        List<Integer> result = searcher.search(new StringReader(text), "Привет");
        assertEquals(List.of(0), result);
    }

    @Test
    void testEmoji() {
        String text = "😀x😀😀";
        List<Integer> result = searcher.search(new StringReader(text), "😀😀");
        assertEquals(List.of(2), result);
    }

    @Test
    void testLargeInput() {
        int prefixLength = 20_000;
        String marker = "XYZPATTERNXYZ";
        String text = "a".repeat(prefixLength) +
                marker +
                "bbbbbbbbbb";

        List<Integer> result = searcher.search(new StringReader(text), "PATTERN");

        assertEquals(List.of(prefixLength + 3), result);
    }

    @Test
    void testNullReader() {
        assertThrows(IllegalArgumentException.class,
                () -> searcher.search(null, "test"));
    }

    @Test
    void testNullSubstring() {
        StringReader reader = new StringReader("abc");
        assertThrows(IllegalArgumentException.class,
                () -> searcher.search(reader, null));
    }

    @Test
    void testEmptySubstring() {
        StringReader reader = new StringReader("abc");
        assertThrows(IllegalArgumentException.class,
                () -> searcher.search(reader, ""));
    }

    @Test
    void testManyEmoji() {
        int emojiCount = 10_000;
        int half = emojiCount / 2;
        String emoji = "😀";

        String text = emoji.repeat(half) +
                'x' +
                emoji.repeat(half);

        List<Integer> result = searcher.search(new StringReader(text), "x");

        assertEquals(List.of(half), result);
    }
}