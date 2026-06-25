package ru.nsu.kiryushin.model;

/**
 * Possible states of a running game session.
 */
public enum GameState {
    /** Game is active and accepting input. */
    PLAYING,
    /** Win condition reached. */
    WON,
    /** Snake collided with a wall or itself. */
    LOSE
}
