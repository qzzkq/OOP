package ru.nsu.kiryushin;

import java.util.*;

/**
 * Graph implementation based on an incidence-matrix idea:
 * vertices are rows, each edge is a separate "column".
 * We store edges as pairs of vertex indices (u, v).
 * Supports directed/undirected graphs and multiedges.
 */
public class IndMatrGraph<V> implements Graph<V> {
    private final boolean directed;

    private final List<V> vertices = new ArrayList<>();
    private final Map<V, Integer> vIndex = new HashMap<>();

    private final List<int[]> edges = new ArrayList<>();

    /**
     * Creates an incidence-based graph.
     *
     * @param directed true for directed graph, false for undirected
     */
    public IndMatrGraph(boolean directed) {
        this.directed = directed;
    }

    /**
     * @return whether this graph is directed
     */
    @Override
    public boolean isDirected() {
        return !directed;
    }

    /**
     * Adds vertex if it doesn't exist.
     *
     * @param v vertex value
     */
    @Override
    public void addVertex(V v) {
        if (vIndex.containsKey(v)) {
            return;
        }
        vIndex.put(v, vertices.size());
        vertices.add(v);
    }

    /**
     * Removes vertex and all incident edges.
     * Preserves compact indices by moving the last vertex into the removed slot.
     *
     * @param v vertex to remove
     */
    @Override
    public void removeVertex(V v) {
        Integer i = vIndex.remove(v);
        if (i == null) {
            return;
        }

        int last = vertices.size() - 1;

        edges.removeIf(e -> e[0] == i || e[1] == i);

        if (i != last) {
            V moved = vertices.get(last);
            vertices.set(i, moved);
            vIndex.put(moved, i);

            for (int[] e : edges) {
                if (e[0] == last) e[0] = i;
                if (e[1] == last) e[1] = i;
            }
        }
        vertices.remove(last);
    }

    /**
     * Adds one edge (column). For undirected graphs we still store (u, v) once.
     *
     * @param from source vertex
     * @param to   destination vertex
     */
    @Override
    public void addEdge(V from, V to) {
        addVertex(from);
        addVertex(to);
        int u = vIndex.get(from);
        int w = vIndex.get(to);
        edges.add(new int[]{u, w});
    }

    /**
     * Removes exactly one parallel edge if present.
     * For undirected graphs matches (from,to) or (to,from).
     *
     * @param from source vertex
     * @param to   destination vertex
     */
    @Override
    public void removeEdge(V from, V to) {
        Integer u = vIndex.get(from), w = vIndex.get(to);
        if (u == null || w == null) {
            return;
        }
        for (int i = 0; i < edges.size(); i++) {
            int[] e = edges.get(i);
            if (directed) {
                if (e[0] == u && e[1] == w) {
                    edges.remove(i);
                    return;
                }
            } else {
                if ((e[0] == u && e[1] == w) || (e[0] == w && e[1] == u)) {
                    edges.remove(i);
                    return;
                }
            }
        }
    }

    /**
     * Returns the set of neighbors of v.
     * For directed graphs only outgoing neighbors are reported.
     * Multiplicity is ignored (set semantics).
     *
     * @param v vertex
     * @return set of neighbor vertices
     */
    @Override
    public Set<V> getNeighbors(V v) {
        Integer idx = vIndex.get(v);
        if (idx == null) {
            return Collections.emptySet();
        }
        LinkedHashSet<V> res = new LinkedHashSet<>();
        for (int[] e : edges) {
            int u = e[0], w = e[1];
            if (directed) {
                if (u == idx) res.add(vertices.get(w));
            } else {
                if (u == idx) res.add(vertices.get(w));
                else if (w == idx) res.add(vertices.get(u));
            }
        }
        return res;
    }

    @Override
    public Set<V> getVertices() {
        return new LinkedHashSet<>(vertices);
    }

    @Override
    public void clear() {
        vertices.clear();
        vIndex.clear();
        edges.clear();
    }

    /**
     * Represents the graph as a multimap for internal comparisons.
     *
     * @return vertex → neighbor map annotated with edge counts
     */
    private Map<V, Map<V, Integer>> asMultimap() {
        Map<V, Map<V, Integer>> res = new HashMap<>();
        for (int[] edge : edges) {
            V from = vertices.get(edge[0]);
            V to = vertices.get(edge[1]);
            res.computeIfAbsent(from, k -> new HashMap<>())
                    .merge(to, 1, Integer::sum);
            if (!directed) {
                res.computeIfAbsent(to, k -> new HashMap<>())
                        .merge(from, 1, Integer::sum);
            }
        }
        for (V v : vertices) {
            res.computeIfAbsent(v, k -> new HashMap<>());
        }
        return res;
    }

    /**
     * Compares graphs by vertex set, edge multiset, and direction flag.
     *
     * @param o other object
     * @return true when graphs are equivalent
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndMatrGraph<?> that)) return false;
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
        return "IndMatrGraph{" +
                "directed=" + directed +
                ", vertices=" + vertices +
                ", edges=" + edges +
                '}';
    }
}
