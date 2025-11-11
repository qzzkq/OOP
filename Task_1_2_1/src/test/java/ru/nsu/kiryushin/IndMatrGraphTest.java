package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

/** Tests for the incidence-matrix graph implementation. */
class IndMatrGraphTest {

    /** Checks equality and hash codes for identical graphs. */
    @Test
    void basic() {
        IndMatrGraph<String> g = new IndMatrGraph<>(true);
        g.addEdge("a", "b");
        g.addEdge("b", "c");
        g.addEdge("c", "a");

        assertEquals(Set.of("a", "b", "c"), g.getVertices());
        assertEquals(Set.of("b"), g.getNeighbors("a"));

        IndMatrGraph<String> copy = new IndMatrGraph<>(true);
        copy.addEdge("a", "b");
        copy.addEdge("b", "c");
        copy.addEdge("c", "a");
        assertEquals(g, copy);
        assertEquals(g.hashCode(), copy.hashCode());

        copy.removeEdge("c", "a");
        assertNotEquals(g, copy);
        assertTrue(copy.getNeighbors("c").isEmpty());
    }

    /** Verifies undirected edges add both directions. */
    @Test
    void undirected() {
        IndMatrGraph<Integer> g = new IndMatrGraph<>(false);
        g.addEdge(1, 2);

        assertEquals(Set.of(2), g.getNeighbors(1));
        assertEquals(Set.of(1), g.getNeighbors(2));
    }

    /** Ensures removing vertices clears incident edges. */
    @Test
    void removeVertex() {
        IndMatrGraph<String> g = new IndMatrGraph<>(true);
        g.addEdge("m", "n");
        g.addEdge("n", "o");

        g.removeVertex("n");
        assertFalse(g.getVertices().contains("n"));
        assertTrue(g.getNeighbors("m").isEmpty());
    }
}
