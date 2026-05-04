package ru.nsu.kiryushin.food;

import ru.nsu.kiryushin.Food;
import ru.nsu.kiryushin.Game;
import ru.nsu.kiryushin.Point;
import ru.nsu.kiryushin.SpawnWeight;

/**
 * Speed-reducing food: slows the snake by ~33% when eaten.
 */
@SpawnWeight(15)
public class Ice implements Food {
    private final Point position;

    /**
     * Creates an ice tile at the given position.
     *
     * @param position board cell
     */
    public Ice(Point position) {
        this.position = position;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void applyEffect(Game model) {
        model.setSpeed(0.67);
    }
}
