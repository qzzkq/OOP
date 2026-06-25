package ru.nsu.kiryushin.model.food;

import ru.nsu.kiryushin.model.AbstractFood;
import ru.nsu.kiryushin.model.Game;
import ru.nsu.kiryushin.model.Point;

/**
 * Speed-reducing food: slows the snake by ~33% when eaten.
 */
public class Ice extends AbstractFood {
    private static final int WEIGHT = 15;

    /**
     * Creates an ice tile at the given position.
     *
     * @param position board cell
     */
    public Ice(Point position) {
        super(position, WEIGHT);
    }

    @Override
    public void applyEffect(Game model) {
        model.setSpeed(0.67);
    }
}
