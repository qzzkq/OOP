package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

/** Tests for the adjacency-list graph implementation. */
class AdjListGraphTest {

    /** Checks duplicate edges and basic removals. */
    @Test
    void basic() {
        Graph<String> g = new AdjListGraph<>(false);
        g.addEdge("a", "b");
        g.addEdge("a", "b");

        assertEquals(Set.of("a", "b"), g.getVertices());
        assertEquals(Set.of("b"), g.getNeighbors("a"));
        assertEquals(Set.of("a"), g.getNeighbors("b"));

        g.removeEdge("a", "b");
        assertEquals(Set.of("b"), g.getNeighbors("a"));

        g.removeVertex("b");
        assertFalse(g.getVertices().contains("b"));
        assertTrue(g.getNeighbors("b").isEmpty());
    }

    /** Verifies directed graphs keep orientation. */
    @Test
    void directed() {
        Graph<String> g = new AdjListGraph<>(true);
        g.addEdge("x", "y");

        assertEquals(Set.of("y"), g.getNeighbors("x"));
        assertTrue(g.getNeighbors("y").isEmpty());

        g.removeVertex("x");
        assertFalse(g.getVertices().contains("x"));
    }

    /** Ensures isolated vertices stay tracked. */
    @Test
    void isolated() {
        Graph<String> g = new AdjListGraph<>(false);
        g.addVertex("solo");

        assertTrue(g.getNeighbors("solo").isEmpty());

        g.removeVertex("solo");
        assertTrue(g.getVertices().isEmpty());
    }
}
