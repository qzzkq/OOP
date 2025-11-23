package ru.nsu.kiryushin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Graph implementation backed by an adjacency matrix.
 * Supports directed graphs and edge multiplicity.
 */
public class AdjMatrGraph<V> implements Graph<V> {
    private final List<V> vertices = new ArrayList<>();
    private final Map<V, Integer> index = new HashMap<>();
    private final boolean directed;

    private int[][] m = new int[0][0];

    /**
     * Creates an adjacency-matrix graph.
     *
     * @param directed true for directed graphs, false otherwise
     */
    public AdjMatrGraph(boolean directed) {
        this.directed = directed;
    }

    /**
     * @return true if the graph is directed
     */
    @Override
    public boolean isDirected() {
        return !directed;
    }

    /**
     * Adds a vertex if it does not exist yet.
     *
     * @param v vertex value
     */
    @Override
    public void addVertex(V v) {
        if (index.containsKey(v)) return;
        index.put(v, vertices.size());
        vertices.add(v);
        resize(vertices.size());
    }

    /**
     * Removes a vertex together with all incident edges.
     *
     * @param v vertex to remove
     */
    @Override
    public void removeVertex(V v) {
        Integer i = index.remove(v);
        if (i == null) return;

        int last = vertices.size() - 1;

        if (i != last) {
            V mv = vertices.get(last);
            vertices.set(i, mv);
            index.put(mv, i);
            for (int k = 0; k < last; k++) {
                m[i][k] = m[last][k];
                m[k][i] = m[k][last];
            }
        }
        vertices.remove(last);
        shrink(last);
    }

    /**
     * Adds a single edge, creating missing vertices on demand.
     *
     * @param from source vertex
     * @param to   destination vertex
     */
    @Override
    public void addEdge(V from, V to) {
        addVertex(from);
        addVertex(to);
        int i = index.get(from);
        int j = index.get(to);
        m[i][j]++;
        if (!directed) m[j][i]++;
    }

    /**
     * Removes exactly one parallel edge if it exists.
     *
     * @param from source vertex
     * @param to   destination vertex
     */
    @Override
    public void removeEdge(V from, V to) {
        Integer i = index.get(from), j = index.get(to);
        if (i == null || j == null) return;
        if (m[i][j] > 0) m[i][j]--;
        if (!directed && m[j][i] > 0) m[j][i]--;
    }

    /**
     * Returns a set of neighbors for the given vertex.
     *
     * @param v vertex value
     * @return neighbors with multiplicity ignored
     */
    @Override
    public Set<V> getNeighbors(V v) {
        Integer i = index.get(v);
        if (i == null) return Collections.emptySet();

        LinkedHashSet<V> res = new LinkedHashSet<>();

        for (int j = 0; j < vertices.size(); j++) {
            if (m[i][j] > 0) res.add(vertices.get(j));
        }
        return res;
    }

    /**
     * @return a defensive copy of the vertex set
     */
    @Override
    public Set<V> getVertices() {
        return new LinkedHashSet<>(vertices);
    }

    /**
     * Removes all vertices and edges.
     */
    @Override
    public void clear() {
        vertices.clear();
        index.clear();
        m = new int[0][0];
    }

    /**
     * Builds a multimap view used for equality comparisons.
     *
     * @return vertex → neighbor map annotated with arc counts
     */
    private Map<V, Map<V, Integer>> asMultimap() {
        Map<V, Map<V, Integer>> res = new HashMap<>();
        for (int i = 0; i < vertices.size(); i++) {
            Map<V, Integer> counts = res.computeIfAbsent(vertices.get(i), k -> new HashMap<>());
            for (int j = 0; j < vertices.size(); j++) {
                int c = m[i][j];
                if (c > 0) {
                    counts.put(vertices.get(j), c);
                }
            }
        }
        return res;
    }

    /**
     * Compares graphs by direction flag, vertex set, and edge multiplicities.
     *
     * @param o other object
     * @return true when the structures match
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdjMatrGraph<?> that)) return false;
        return directed == that.directed && Objects.equals(new HashSet<>(vertices), new HashSet<>(that.vertices))
                && Objects.equals(asMultimap(), that.asMultimap());
    }

    /**
     * Computes a hash code consistent with {@link #equals(Object)}.
     *
     * @return graph hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(new HashSet<>(vertices), asMultimap(), directed);
    }

    /**
     * @return a string representation of the internal state
     */
    @Override
    public String toString() {
        return "AdjMatrGraph{" +
                "directed=" + directed +
                ", vertices=" + vertices +
                ", matrix=" + Arrays.deepToString(m) +
                '}';
    }

    /**
     * Grows the matrix to size {@code n x n}.
     *
     * @param n new size
     */
    private void resize(int n) {
        int[][] nm = new int[n][n];
        for (int r = 0; r < m.length; r++) {
            System.arraycopy(m[r], 0, nm[r], 0, m.length);
        }
        m = nm;
    }

    /**
     * Shrinks the matrix to size {@code n x n}.
     *
     * @param n new size
     */
    private void shrink(int n) {
        int[][] nm = new int[n][n];
        for (int r = 0; r < n; r++) {
            System.arraycopy(m[r], 0, nm[r], 0, n);
        }
        m = nm;
    }
}
