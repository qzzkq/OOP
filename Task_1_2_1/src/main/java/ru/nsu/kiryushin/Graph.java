package ru.nsu.kiryushin;

import java.util.Set;

/**
 * Graph interface that supports vertex/edge adding and removal, get set of vertices, get vertex neighbors.
 * Implementations can be directed or undirected and may support multiple edges.
 *
 * @param <V> vertex value type
 */
public interface Graph<V> {

    /**
     * Adds vertex if it is not present.
     *
     * @param v vertex value
     */
    void addVertex(V v);

    /**
     * Removes vertex and incident edges if present.
     *
     * @param v vertex value
     */
    void removeVertex(V v);

    /**
     * Returns outgoing neighbours of the vertex.
     *
     * @param v vertex value
     * @return neighbours as a set (empty if vertex missing)
     */
    Set<V> getNeighbors(V v);

    /**
     * Adds edge from {@code from} to {@code to}, creating vertices if needed.
     *
     * @param from source vertex
     * @param to   destination vertex
     */
    void addEdge(V from, V to);

    /**
     * Removes one edge from {@code from} to {@code to} if it exists.
     *
     * @param from source vertex
     * @param to   destination vertex
     */
    void removeEdge(V from, V to);

    /**
     * @return true if the graph is directed
     */
    boolean isDirected();

    /**
     * @return snapshot set of vertices
     */
    Set<V> getVertices();

    /**
     * Removes all vertices and edges from the graph.
     */
    void clear();

}
