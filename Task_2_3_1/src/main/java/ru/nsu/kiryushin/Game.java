package ru.nsu.kiryushin;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Core game model: holds the snake, food map, and win/lose logic.
 */
public class Game {
    private final int columns;
    private final int rows;
    private final int winLength;
    private final int targetFoodCount;

    /** Nanoseconds per tick at 1× speed; divide by the multiplier to get the actual interval. */
    public static final double CONST_SPEED = 150_000_000;
    private double speed = 1.0;

    private final Snake snake;
    private final Map<Point, Food> foodsOnBoard;
    private GameState state;

    private final Random random = new Random();
    private final Deque<Direction> inputQueue = new ArrayDeque<>();
    private static final int MAX_QUEUED_INPUTS = 2;

    /**
     * Creates a game with the given board dimensions and win condition.
     *
     * @param columns board width in tiles
     * @param rows board height in tiles
     * @param winLength snake length required to win
     * @param targetFoodCount number of food items kept on the board at all times
     */
    public Game(int columns, int rows, int winLength, int targetFoodCount) {
        this.columns = columns;
        this.rows = rows;
        this.winLength = winLength;
        this.state = GameState.PLAYING;
        this.targetFoodCount = targetFoodCount;
        this.foodsOnBoard = new HashMap<>();

        this.snake = new Snake(new Point(columns / 2, rows / 2));

        replenishFood();
    }

    /**
     * Multiplies the current speed by the given coefficient, clamped to [0.5, 3.0].
     *
     * @param koef speed multiplier
     */
    public void setSpeed(double koef) {
        this.speed *= koef;
        if (this.speed > 3) {
            this.speed = 3;
        } else if (this.speed < 0.5) {
            this.speed = 0.5;
        }
    }

    /**
     * Returns the timer interval in nanoseconds between game ticks.
     *
     * @return nanoseconds per tick
     */
    public double getSpeed() {
        return CONST_SPEED / this.speed;
    }

    /**
     * Returns the current speed multiplier (1.0 = normal, &gt;1 = faster, &lt;1 = slower).
     *
     * @return speed multiplier
     */
    public double getSpeedMultiplier() {
        return speed;
    }

    /**
     * Queues a direction change. Validated against the last queued direction (or the current
     * snake direction if the queue is empty) to prevent 180° turns. Excess inputs are dropped.
     *
     * @param d requested direction
     */
    public void queueDirection(Direction d) {
        Direction ref = inputQueue.isEmpty() ? snake.getDirection() : inputQueue.peekLast();
        if (!isOpposite(ref, d) && inputQueue.size() < MAX_QUEUED_INPUTS) {
            inputQueue.addLast(d);
        }
    }

    private static boolean isOpposite(Direction a, Direction b) {
        return (a == Direction.UP    && b == Direction.DOWN)
            || (a == Direction.DOWN  && b == Direction.UP)
            || (a == Direction.LEFT  && b == Direction.RIGHT)
            || (a == Direction.RIGHT && b == Direction.LEFT);
    }

    /**
     * Advances one game tick: moves the snake, checks collisions, and checks the win condition.
     *
     * @return {@code true} if the game is still running, {@code false} if it has ended
     */
    public boolean update() {
        if (state != GameState.PLAYING) return false;

        if (!inputQueue.isEmpty()) {
            snake.setDirection(inputQueue.pollFirst());
        }

        snake.move();
        checkCollisions();
        checkWinCondition();
        return true;
    }

    private void checkCollisions() {
        Point head = snake.getHead();

        if (head.x() < 0 || head.x() >= columns || head.y() < 0 || head.y() >= rows) {
            state = GameState.LOSE;
            return;
        }

        var body = snake.getBody();
        int index = 0;
        for (Point p : body) {
            if (index != 0 && p.equals(head)) {
                state = GameState.LOSE;
                return;
            }
            index++;
        }

        Food eatenFood = foodsOnBoard.get(head);

        if (eatenFood != null) {
            eatenFood.applyEffect(this);
            foodsOnBoard.remove(head);
            replenishFood();
        }
    }

    private void checkWinCondition() {
        if (snake.getLength() >= winLength) {
            state = GameState.WON;
        }
    }

    private void replenishFood() {
        while (foodsOnBoard.size() < targetFoodCount) {
            spawnSingleFood();
            if (state != GameState.PLAYING) break;
        }
    }

    private void spawnSingleFood() {
        Set<Point> occupied = new HashSet<>(snake.getBody());
        occupied.addAll(foodsOnBoard.keySet());

        List<Point> freeCells = new ArrayList<>();
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                Point p = new Point(x, y);
                if (!occupied.contains(p)) {
                    freeCells.add(p);
                }
            }
        }

        if (freeCells.isEmpty()) {
            state = GameState.WON;
            return;
        }

        Point newPoint = freeCells.get(random.nextInt(freeCells.size()));
        Food newFood = FoodRegistry.createRandom(newPoint);
        foodsOnBoard.put(newPoint, newFood);
    }

    /**
     * Returns the snake.
     *
     * @return snake instance
     */
    public Snake getSnake() {
        return this.snake;
    }

    /**
     * Returns a read-only view of the current food items on the board, keyed by position.
     *
     * @return unmodifiable food map
     */
    public Map<Point, Food> getFoodsOnBoard() {
        return Collections.unmodifiableMap(foodsOnBoard);
    }

    /**
     * Places a food item on the board at its position. Intended for testing.
     *
     * @param food food item to place
     */
    void placeFood(Food food) {
        foodsOnBoard.put(food.getPosition(), food);
    }

    /**
     * Returns the board width in tiles.
     *
     * @return column count
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Returns the board height in tiles.
     *
     * @return row count
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the current game state.
     *
     * @return PLAYING, WON, or LOSE
     */
    public GameState getState() {
        return this.state;
    }
}
