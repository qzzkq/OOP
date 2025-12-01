package ru.nsu.kiryushin;

import java.io.Reader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility for searching all occurrences of a substring in a UTF-8 text file.
 * Uses the Knuth–Morris–Pratt (KMP) algorithm over Unicode code points
 * and streams the file in fixed-size blocks.
 */
public class SubstringSearcher {

    private static final int BLOCK_SIZE = 8192;

    private final char[] buffer = new char[BLOCK_SIZE];

    private int bufIndex = 0;
    private int read = 0;

    /**
     * Searches for all occurrences of the specified substring in the given Reader.
     * Matching is performed over Unicode code points.
     *
     * @param reader    source reader
     * @param subString substring to search for
     * @return list of zero-based start positions of all matches (in code points)
     * @throws IllegalArgumentException if reader is null or substring is null/empty
     * @throws RuntimeException         if an I/O error occurs
     */
    public List<Integer> search(Reader reader, String subString) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        }
        if (subString == null || subString.isEmpty()) {
            throw new IllegalArgumentException("Substring must not be null or empty");
        }

        this.bufIndex = 0;
        this.read = 0;

        List<Integer> indexes = new ArrayList<>();

        int[] pattern = subString.codePoints().toArray();
        int[] pi = prefixFunction(pattern);
        int c;
        int k = 0;
        int globalIndex = 0;
        int subLen = pattern.length;

        try {
            while ((c = nextCodePoint(reader)) != -1) {

                while (k > 0 && c != pattern[k]) {
                    k = pi[k - 1];
                }

                if (c == pattern[k]) {
                    k++;
                }

                if (k == subLen) {
                    indexes.add(1 - subLen + globalIndex);
                    k = pi[k - 1];
                }

                globalIndex++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from stream", e);
        }
        return indexes;
    }

    /**
     * Computes the prefix-function for the KMP algorithm.
     *
     * @param pattern pattern represented as an array of code points
     * @return prefix-function array where {@code pi[i]} is the length of the longest
     *         proper prefix of {@code pattern[0..i]} which is also a suffix
     */
    private int[] prefixFunction(int[] pattern) {
        int n = pattern.length;
        int[] pi = new int[n];
        pi[0] = 0;
        for (int i = 1; i < n; i++) {
            int k = pi[i-1];

            while (k > 0 && pattern[i] != pattern[k]) {
                k = pi[k-1];
            }

            if (pattern[i] == pattern[k]){
                k++;
            }

            pi[i] = k;
        }
        return pi;
    }

    /**
     * Reads the next Unicode code point from the stream using an internal char buffer.
     * Handles surrogate pairs explicitly.
     *
     * @param reader source reader
     * @return next code point, or {@code -1} if end of stream is reached
     * @throws IOException if an I/O error occurs or an incomplete surrogate pair is found at EOF
     */
    private int nextCodePoint(Reader reader) throws IOException {
        if (!checkForReread(reader)){
            return -1;
        };

        char firstChar = buffer[bufIndex];
        bufIndex++;

        if (!Character.isHighSurrogate(firstChar)) {
            return firstChar;
        }

        if (!checkForReread(reader)) {
            throw new IOException("Incomplete surrogate pair at EOF");
        };

        char secondChar = buffer[bufIndex];
        bufIndex++;

        return Character.toCodePoint(firstChar, secondChar);
    }

    /**
     * Ensures that the internal buffer has at least one unread character.
     * If the buffer is exhausted, reads the next block from the reader.
     *
     * @param reader source reader
     * @return {@code true} if a character is available, {@code false} if EOF is reached
     * @throws IOException if an I/O error occurs during reading
     */
    private boolean checkForReread(Reader reader) throws IOException {
        if (bufIndex >= read) {
            bufIndex = 0;
            read = reader.read(buffer, 0, BLOCK_SIZE);
            return read != -1;
        }
        return true;
    }
}

