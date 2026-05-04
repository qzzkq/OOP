package ru.nsu.kiryushin.food;

import ru.nsu.kiryushin.Food;
import ru.nsu.kiryushin.Game;
import ru.nsu.kiryushin.Point;
import ru.nsu.kiryushin.SpawnWeight;

/**
 * Shrinking food: removes the last body segment of the snake when eaten.
 */
@SpawnWeight(15)
public class Chili implements Food {
    private final Point position;

    /**
     * Creates a chili pepper at the given position.
     *
     * @param position board cell
     */
    public Chili(Point position) {
        this.position = position;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void applyEffect(Game model) {
        model.getSnake().shrink();
    }
}