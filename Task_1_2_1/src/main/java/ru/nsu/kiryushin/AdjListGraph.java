package ru.nsu.kiryushin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Graph implementation backed by adjacency lists.
 * Vertices are stored in a set, each vertex keeps a list of outgoing neighbours.
 * Supports directed/undirected graphs and multiedges via duplicated entries.
 */
public class AdjListGraph<V> implements Graph<V> {
    private final Set<V> verticles;
    private final Map<V, List<V>> adjList;
    private final boolean directed;

    /**
     * Creates an adjacency-list graph.
     *
     * @param directed true for directed graph, false for undirected
     */
    public AdjListGraph(boolean directed) {
        this.verticles = new HashSet<>();
        this.adjList = new HashMap<>();
        this.directed = directed;
    }

    /**
     * @return whether the graph is directed
     */
    @Override
    public boolean isDirected() {
        return !this.directed;
    }

    /**
     * Adds a vertex if it is missing.
     *
     * @param v vertex value
     */
    @Override
    public void addVertex(V v) {
        if (verticles.add(v)) {
            adjList.put(v, new ArrayList<>());
        }
    }

    /**
     * Removes vertex and all adjacent edges.
     *
     * @param v vertex value
     */
    @Override
    public void removeVertex(V v) {
        if (verticles.remove(v)) {
            adjList.remove(v);
            for (List<V> neighbors : adjList.values()) {
                neighbors.removeIf(v::equals);
            }
        }
    }

    /**
     * Returns outgoing neighbours of the vertex (set view).
     *
     * @param v vertex value
     * @return set of neighbours or empty set for unknown vertex
     */
    @Override
    public Set<V> getNeighbors(V v) {
        List<V> vertexOut = adjList.get(v);
        if (vertexOut == null) {
            return Collections.emptySet();
        }
        return new LinkedHashSet<>(vertexOut);
    }

    /**
     * Adds edge between vertices, creating vertices on demand.
     * For undirected graphs stores both directions.
     *
     * @param from source vertex
     * @param to   destination vertex
     */
    @Override
    public void addEdge(V from, V to) {
        addVertex(from);
        addVertex(to);
        adjList.get(from).add(to);
        if (isDirected()) {
            adjList.get(to).add(from);
        }
    }

    /**
     * Removes one edge.
     *
     * @param from source vertex
     * @param to   destination vertex
     */
    @Override
    public void removeEdge(V from, V to) {
        List<V> a = adjList.get(from);
        if (a != null && a.remove(to) && isDirected()) {
            List<V> b = adjList.get(to);
            if (b != null) b.remove(from);
        }
    }

    /**
     * @return snapshot set of vertices
     */
    @Override
    public Set<V> getVertices() {
        return new HashSet<>(verticles);
    }

    /**
     * Clears all vertices and edges.
     */
    @Override
    public void clear() {
        verticles.clear();
        adjList.clear();
    }

    /**
     * Builds a multiset-like representation of outgoing edges for comparison helpers.
     *
     * @return map from vertex to neighbours with multiplicities
     */
    private Map<V, Map<V, Integer>> asMultimap() {
        Map<V, Map<V, Integer>> result = new HashMap<>();
        for (Map.Entry<V, List<V>> entry : adjList.entrySet()) {
            Map<V, Integer> counts = result.computeIfAbsent(entry.getKey(), k -> new HashMap<>());
            for (V neighbour : entry.getValue()) {
                counts.merge(neighbour, 1, Integer::sum);
            }
        }
        return result;
    }

    /**
     * Compares graphs by directed flag, vertex set, and edge multiplicities.
     *
     * @param o other object
     * @return true when structures are equivalent
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdjListGraph<?> that)) return false;
        return directed == that.directed && Objects.equals(verticles, that.verticles)
                && Objects.equals(asMultimap(), that.asMultimap());
    }

    /**
     * Computes hash code of the graph.
     *
     * @return hash code of the graph
     */
    @Override
    public int hashCode() {
        return Objects.hash(verticles, asMultimap(), directed);
    }

    /**
     * @return readable representation of the graph internals
     */
    @Override
    public String toString() {
        return "AdjListGraph{" +
                "directed=" + directed +
                ", verticles=" + verticles +
                ", edges=" + adjList +
                '}';
    }
}
