package ru.nsu.kiryushin.model.food;

import ru.nsu.kiryushin.model.AbstractFood;
import ru.nsu.kiryushin.model.Game;
import ru.nsu.kiryushin.model.Point;

/**
 * Standard food: grows the snake by one segment when eaten.
 */
public class Apple extends AbstractFood {
    private static final int WEIGHT = 70;

    /**
     * Creates an apple at the given position.
     *
     * @param position board cell
     */
    public Apple(Point position) {
        super(position, WEIGHT);
    }

    @Override
    public void applyEffect(Game model) {
        model.getSnake().grow();
    }
}
