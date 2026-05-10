package ru.nsu.kiryushin.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Snake entity: body segments, movement, and growth logic.
 */
public class Snake {
    private final Deque<Point> body;
    private Direction currentDirection;
    private Direction lastMoveDirection;
    private boolean shouldGrow;

    /**
     * Creates a snake with a single segment at the given start position, facing right.
     *
     * @param startPosition initial head position
     */
    public Snake(Point startPosition) {
        this.body = new LinkedList<>();
        this.body.addFirst(startPosition);
        this.currentDirection = Direction.RIGHT;
        this.lastMoveDirection = Direction.RIGHT;
        this.shouldGrow = false;
    }

    /**
     * Advances the snake one cell in the current direction.
     * If {@code grow()} was called beforehand, the tail is not removed.
     */
    public void move() {
        lastMoveDirection = currentDirection;
        Point head = body.getFirst();
        int newX = head.x();
        int newY = head.y();

        switch (currentDirection) {
            case UP -> newY--;
            case DOWN -> newY++;
            case LEFT -> newX--;
            case RIGHT -> newX++;
        }

        body.addFirst(new Point(newX, newY));

        if (shouldGrow) {
            shouldGrow = false;
        } else {
            body.removeLast();
        }
    }

    /**
     * Changes the movement direction, ignoring reversal into the opposite direction.
     *
     * @param newDirection requested direction
     */
    public void setDirection(Direction newDirection) {
        if (isOpposite(lastMoveDirection, newDirection)) return;
        if (isOpposite(currentDirection, newDirection)) return;
        this.currentDirection = newDirection;
    }

    private static boolean isOpposite(Direction a, Direction b) {
        return (a == Direction.UP    && b == Direction.DOWN)
            || (a == Direction.DOWN  && b == Direction.UP)
            || (a == Direction.LEFT  && b == Direction.RIGHT)
            || (a == Direction.RIGHT && b == Direction.LEFT);
    }

    /**
     * Schedules the snake to grow by one segment on the next {@link #move()} call.
     */
    public void grow() {
        this.shouldGrow = true;
    }

    /**
     * Removes the tail segment immediately; does nothing when the snake is only one cell long.
     */
    public void shrink() {
        if (body.size() > 1) {
            body.removeLast();
        }
    }

    /**
     * Returns the current movement direction.
     *
     * @return current direction
     */
    public Direction getDirection() {
        return currentDirection;
    }

    /**
     * Returns the head position.
     *
     * @return head cell
     */
    public Point getHead() {
        return body.getFirst();
    }

    /**
     * Returns a read-only view of the body ordered head-to-tail.
     *
     * @return unmodifiable body collection
     */
    public Collection<Point> getBody() {
        return Collections.unmodifiableCollection(body);
    }

    /**
     * Returns the number of body segments.
     *
     * @return body length
     */
    public int getLength() {
        return body.size();
    }
}
