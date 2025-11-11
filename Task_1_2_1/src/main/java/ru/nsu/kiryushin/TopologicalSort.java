package ru.nsu.kiryushin;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Kahn's topological sort over {@link Graph} implementations.
 * Checks directedness and cycle presence and returns vertices in topological order.
 */
public final class TopologicalSort {
    /**
     * Utility class; no instances required.
     */
    private TopologicalSort() {
    }

    /**
     * Performs topological sorting using the Kahn algorithm.
     *
     * @param graph directed graph
     * @param <V>   vertex type
     * @return list of vertices in topological order
     * @throws IllegalArgumentException if graph is {@code null} or undirected
     * @throws IllegalStateException    if the graph contains a cycle
     */
    public static <V> List<V> sort(Graph<V> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("graph is null");
        }
        if (graph.isDirected()) {
            throw new IllegalArgumentException("graph must be directed");
        }

        Set<V> vertices = graph.getVertices();
        Map<V, Integer> indegree = new HashMap<>();
        for (V v : vertices) {
            indegree.put(v, 0);
        }

        for (V v : vertices) {
            for (V to : graph.getNeighbors(v)) {
                indegree.put(to, indegree.getOrDefault(to, 0) + 1);
            }
        }

        Deque<V> queue = new ArrayDeque<>();
        for (Map.Entry<V, Integer> entry : indegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<V> order = new ArrayList<>(vertices.size());
        while (!queue.isEmpty()) {
            V v = queue.remove();
            order.add(v);
            for (V to : graph.getNeighbors(v)) {
                Integer deg = indegree.get(to);
                if (deg == null) {
                    continue;
                }
                int next = deg - 1;
                indegree.put(to, next);
                if (next == 0) {
                    queue.add(to);
                }
            }
        }

        if (order.size() != indegree.size()) {
            throw new IllegalStateException("graph has cycle");
        }
        return order;
    }
}
