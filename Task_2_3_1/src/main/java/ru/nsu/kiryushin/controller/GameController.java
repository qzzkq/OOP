package ru.nsu.kiryushin.controller;

import ru.nsu.kiryushin.model.Direction;
import ru.nsu.kiryushin.model.Food;
import ru.nsu.kiryushin.model.FoodRegistry;
import ru.nsu.kiryushin.model.Game;
import ru.nsu.kiryushin.model.GameState;
import ru.nsu.kiryushin.model.Point;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mediates between the view and the {@link Game} model. Holds no JavaFX dependencies.
 * <p>
 * The view interacts with the game only through this class and a small set of basic
 * value types ({@link Point}, {@link Direction}, {@link GameState}, food type names),
 * so it never needs to import the model's own classes ({@code Game}, {@code Snake},
 * {@code Food}).
 */
public class GameController {
    private static final int DEFAULT_COLUMNS = 20;
    private static final int DEFAULT_ROWS = 20;
    private static final int DEFAULT_WIN_LENGTH = 70;
    private static final int DEFAULT_FOOD_COUNT = 15;

    private final int columns;
    private final int rows;
    private final int winLength;
    private final int targetFoodCount;

    private Game game;

    /** Creates a controller with default board parameters. */
    public GameController() {
        this(DEFAULT_COLUMNS, DEFAULT_ROWS, DEFAULT_WIN_LENGTH, DEFAULT_FOOD_COUNT);
    }

    /**
     * Creates a controller with the given board parameters.
     *
     * @param columns board width in tiles
     * @param rows board height in tiles
     * @param winLength snake length required to win
     * @param targetFoodCount number of food items kept on the board
     */
    public GameController(int columns, int rows, int winLength, int targetFoodCount) {
        this.columns = columns;
        this.rows = rows;
        this.winLength = winLength;
        this.targetFoodCount = targetFoodCount;
        this.game = new Game(columns, rows, winLength, targetFoodCount);
    }

    /** Starts a new game, replacing the current one. */
    public void restart() {
        this.game = new Game(columns, rows, winLength, targetFoodCount);
    }

    /**
     * Routes a direction command to the snake.
     *
     * @param direction requested direction
     */
    public void handleInput(Direction direction) {
        game.queueDirection(direction);
    }

    /**
     * Advances one game tick.
     *
     * @return {@code true} if the game is still running, {@code false} if it has ended
     */
    public boolean update() {
        return game.update();
    }

    /** @return board width in tiles */
    public int getColumns() {
        return columns;
    }

    /** @return board height in tiles */
    public int getRows() {
        return rows;
    }

    /** @return current game state */
    public GameState getState() {
        return game.getState();
    }

    /** @return current snake length / score */
    public int getScore() {
        return game.getSnake().getLength();
    }

    /** @return current speed multiplier (1.0 = normal) */
    public double getSpeedMultiplier() {
        return game.getSpeedMultiplier();
    }

    /** @return nanoseconds per tick at the current speed */
    public double getTickInterval() {
        return game.getSpeed();
    }

    /** @return current movement direction of the snake */
    public Direction getSnakeDirection() {
        return game.getSnake().getDirection();
    }

    /**
     * Returns the snake body ordered head-to-tail.
     *
     * @return list of body cells
     */
    public List<Point> getSnakeBody() {
        return List.copyOf(game.getSnake().getBody());
    }

    /**
     * Returns the food currently on the board, mapping each cell to a stable food type
     * name (the lowercase simple class name, e.g. {@code "apple"}). The view uses this
     * name to pick a texture without depending on {@link Food}.
     *
     * @return map of cell to food type name
     */
    public Map<Point, String> getFoodsOnBoard() {
        Map<Point, String> result = new LinkedHashMap<>();
        for (Map.Entry<Point, Food> e : game.getFoodsOnBoard().entrySet()) {
            result.put(e.getKey(), typeName(e.getValue().getClass()));
        }
        return result;
    }

    /**
     * Returns the names of all known food types (lowercase simple class names).
     * The view uses this to load the matching textures up front.
     *
     * @return list of food type names
     */
    public List<String> getFoodTypeNames() {
        return FoodRegistry.getAllFoodTypes().stream()
                .map(GameController::typeName)
                .collect(Collectors.toUnmodifiableList());
    }

    private static String typeName(Class<? extends Food> clazz) {
        return clazz.getSimpleName().toLowerCase();
    }
}
