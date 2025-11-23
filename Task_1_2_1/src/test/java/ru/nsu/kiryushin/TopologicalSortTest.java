package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

/** Tests for the topological sort utility. */
class TopologicalSortTest {

    /** Checks ordering on a simple DAG. */
    @Test
    void order() {
        Graph<Integer> g = new AdjListGraph<>(true);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 4);
        g.addEdge(3, 4);

        List<Integer> order = TopologicalSort.sort(g);
        assertEquals(List.of(1, 2, 3, 4), order);
    }

    /** Throws when cycles are present. */
    @Test
    void cycle() {
        Graph<Integer> g = new AdjListGraph<>(true);
        g.addEdge(1, 2);
        g.addEdge(2, 1);
        assertThrows(IllegalStateException.class, () -> TopologicalSort.sort(g));
    }

    /** Rejects undirected graphs. */
    @Test
    void undirected() {
        Graph<Integer> g = new AdjListGraph<>(false);
        assertThrows(IllegalArgumentException.class, () -> TopologicalSort.sort(g));
    }

    /** Returns empty order for empty graphs. */
    @Test
    void empty() {
        Graph<Integer> g = new AdjListGraph<>(true);

        assertEquals(List.of(), TopologicalSort.sort(g));
    }
}