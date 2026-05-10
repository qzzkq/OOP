package ru.nsu.kiryushin.model.food;

import ru.nsu.kiryushin.model.AbstractFood;
import ru.nsu.kiryushin.model.Game;
import ru.nsu.kiryushin.model.Point;

/**
 * Shrinking food: removes the last body segment of the snake when eaten.
 */
public class Chili extends AbstractFood {
    private static final int WEIGHT = 15;

    /**
     * Creates a chili pepper at the given position.
     *
     * @param position board cell
     */
    public Chili(Point position) {
        super(position, WEIGHT);
    }

    @Override
    public void applyEffect(Game model) {
        model.getSnake().shrink();
    }
}
