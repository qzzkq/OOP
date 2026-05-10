package ru.nsu.kiryushin.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SnakeTest {

    @Test
    void initialStateIsCorrect() {
        Snake snake = new Snake(new Point(5, 5));
        assertEquals(1, snake.getLength());
        assertEquals(Direction.RIGHT, snake.getDirection());
        assertEquals(new Point(5, 5), snake.getHead());
    }

    @Test
    void moveRightAdvancesHead() {
        Snake snake = new Snake(new Point(5, 5));
        snake.move();
        assertEquals(new Point(6, 5), snake.getHead());
        assertEquals(1, snake.getLength());
    }

    @Test
    void moveDownAdvancesHead() {
        Snake snake = new Snake(new Point(5, 5));
        snake.setDirection(Direction.DOWN);
        snake.move();
        assertEquals(new Point(5, 6), snake.getHead());
    }

    @Test
    void growIncreasesLength() {
        Snake snake = new Snake(new Point(5, 5));
        snake.grow();
        snake.move();
        assertEquals(2, snake.getLength());
    }

    @Test
    void shrinkDecreasesLength() {
        Snake snake = new Snake(new Point(5, 5));
        snake.grow();
        snake.move();
        assertEquals(2, snake.getLength());
        snake.shrink();
        assertEquals(1, snake.getLength());
    }

    @Test
    void shrinkDoesNotGoBelowOne() {
        Snake snake = new Snake(new Point(5, 5));
        snake.shrink();
        assertEquals(1, snake.getLength());
    }

    @Test
    void reverseDirectionIsIgnored() {
        Snake snake = new Snake(new Point(5, 5));
        snake.setDirection(Direction.UP);
        snake.setDirection(Direction.DOWN);
        assertEquals(Direction.UP, snake.getDirection());
    }

    @Test
    void rapidLeftAfterUpWhileGoingRightIsBlocked() {
        Snake snake = new Snake(new Point(5, 5));
        snake.setDirection(Direction.UP);
        snake.setDirection(Direction.LEFT);
        assertEquals(Direction.UP, snake.getDirection());
    }

    @Test
    void validDirectionChangeIsAccepted() {
        Snake snake = new Snake(new Point(5, 5));
        snake.setDirection(Direction.UP);
        assertEquals(Direction.UP, snake.getDirection());
    }

    @Test
    void bodyFirstElementIsHead() {
        Snake snake = new Snake(new Point(3, 3));
        assertEquals(snake.getHead(), snake.getBody().iterator().next());
    }
}
