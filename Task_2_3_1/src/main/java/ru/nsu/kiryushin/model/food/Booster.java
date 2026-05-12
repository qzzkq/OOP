package ru.nsu.kiryushin.model.food;

import ru.nsu.kiryushin.model.AbstractFood;
import ru.nsu.kiryushin.model.Game;
import ru.nsu.kiryushin.model.Point;

/**
 * Speed-increasing food: accelerates the snake by ~33% when eaten.
 */
public class Booster extends AbstractFood {
    private static final int WEIGHT = 15;

    /**
     * Creates a booster at the given position.
     *
     * @param position board cell
     */
    public Booster(Point position) {
        super(position, WEIGHT);
    }

    @Override
    public void applyEffect(Game model) {
        model.setSpeed(1.33);
    }
}
