package ru.nsu.kiryushin;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchSubstringsTest {

    private final SearchSubstrings searcher = new SearchSubstrings();

    private Path writeTempFile(String content) throws IOException {
        Path file = Files.createTempFile("search_substrings_test_", ".txt");
        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file;
    }

    @Test
    void testExample() throws IOException {
        String text = "абракадабра";
        Path file = writeTempFile(text);

        List<Integer> result = searcher.search(file.toString(), "бра");

        assertEquals(List.of(1, 8), result);
    }

    @Test
    void testOverlap() throws IOException {
        String text = "aaaaa";
        Path file = writeTempFile(text);

        List<Integer> result = searcher.search(file.toString(), "aa");

        assertEquals(List.of(0, 1, 2, 3), result);
    }

    @Test
    void testNoMatch() throws IOException {
        String text = "abcdefg";
        Path file = writeTempFile(text);

        List<Integer> result = searcher.search(file.toString(), "xyz");

        assertTrue(result.isEmpty());
    }

    @Test
    void testWholeString() throws IOException {
        String text = "hello";
        Path file = writeTempFile(text);

        List<Integer> result = searcher.search(file.toString(), "hello");

        assertEquals(List.of(0), result);
    }

    @Test
    void testOneChar() throws IOException {
        String text = "abcabc";
        Path file = writeTempFile(text);

        List<Integer> result = searcher.search(file.toString(), "a");

        assertEquals(List.of(0, 3), result);
    }

    @Test
    void testCyrillic() throws IOException {
        String text = "Привет, ФИТ!";
        Path file = writeTempFile(text);

        List<Integer> result = searcher.search(file.toString(), "Привет");

        assertEquals(List.of(0), result);
    }

    @Test
    void testEmoji() throws IOException {
        String text = "😀x😀😀";
        Path file = writeTempFile(text);

        List<Integer> result = searcher.search(file.toString(), "😀😀");

        assertEquals(List.of(2), result);
    }

    @Test
    void testLargeFile() throws IOException {
        int prefixLength = 20_000;

        String marker = "XYZPATTERNXYZ";

        String text = "a".repeat(prefixLength) +
                marker +
                "bbbbbbbbbb";
        Path file = writeTempFile(text);

        List<Integer> result = searcher.search(file.toString(), "PATTERN");

        assertEquals(List.of(prefixLength + 3), result);
    }

    @Test
    void testNullFile() {
        assertThrows(IllegalArgumentException.class,
                () -> searcher.search(null, "test"));
    }

    @Test
    void testBlankFile() {
        assertThrows(IllegalArgumentException.class,
                () -> searcher.search("   ", "test"));
    }

    @Test
    void testNullSubstring() throws IOException {
        Path file = writeTempFile("abc");
        assertThrows(IllegalArgumentException.class,
                () -> searcher.search(file.toString(), null));
    }

    @Test
    void testEmptySubstring() throws IOException {
        Path file = writeTempFile("abc");
        assertThrows(IllegalArgumentException.class,
                () -> searcher.search(file.toString(), ""));
    }

    @Test
    void testFileNotExists() {
        String path = "this_file_definitely_does_not_exist_" + System.nanoTime();
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> searcher.search(path, "test"));

        assertTrue(ex.getMessage().contains("File do not exists"));
    }

    @Test
    void testManyEmoji() throws IOException {
        int emojiCount = 10_000;
        int half = emojiCount / 2;
        String emoji = "😀";

        String text = emoji.repeat(half) +
                'x' +
                emoji.repeat(half);
        Path file = writeTempFile(text);

        List<Integer> result = searcher.search(file.toString(), "x");

        assertEquals(List.of(half), result);
    }
}
