package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

/** Tests for the adjacency-matrix graph implementation. */
class AdjMatrGraphTest {

    /** Checks directed edges and deletions. */
    @Test
    void basic() {
        Graph<Integer> g = new AdjMatrGraph<>(true);
        g.addEdge(1, 2);
        g.addEdge(1, 2);
        g.addEdge(2, 3);

        assertEquals(Set.of(1, 2, 3), g.getVertices());
        assertEquals(Set.of(2), g.getNeighbors(1));
        assertEquals(Set.of(3), g.getNeighbors(2));
        assertTrue(g.getNeighbors(3).isEmpty());

        g.removeEdge(1, 2);
        assertEquals(Set.of(2), g.getNeighbors(1));

        g.removeVertex(2);
        assertEquals(Set.of(1, 3), g.getVertices());
        assertTrue(g.getNeighbors(1).isEmpty());
    }

    /** Ensures undirected graphs add both directions. */
    @Test
    void undirected() {
        Graph<Integer> g = new AdjMatrGraph<>(false);
        g.addEdge(4, 5);

        assertEquals(Set.of(5), g.getNeighbors(4));
        assertEquals(Set.of(4), g.getNeighbors(5));
    }

    /** Verifies clear removes all data. */
    @Test
    void clearAll() {
        Graph<Integer> g = new AdjMatrGraph<>(true);
        g.addEdge(7, 8);
        g.addEdge(8, 9);

        g.clear();
        assertTrue(g.getVertices().isEmpty());
        assertTrue(g.getNeighbors(7).isEmpty());
    }

    /** Exercises equality, hash code, and textual reporting. */
    @Test
    void equality() {
        AdjMatrGraph<String> g = new AdjMatrGraph<>(false);
        g.addEdge("a", "b");
        g.addEdge("a", "b");

        AdjMatrGraph<String> copy = new AdjMatrGraph<>(false);
        copy.addEdge("a", "b");
        copy.addEdge("a", "b");

        assertEquals(g, copy);
        assertEquals(g.hashCode(), copy.hashCode());
        assertTrue(copy.toString().contains("AdjMatrGraph"));

        copy.removeEdge("a", "b");
        assertNotEquals(g, copy);
    }
}