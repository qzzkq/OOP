package ru.nsu.kiryushin.food;

import ru.nsu.kiryushin.Food;
import ru.nsu.kiryushin.Game;
import ru.nsu.kiryushin.Point;
import ru.nsu.kiryushin.SpawnWeight;

/**
 * Standard food: grows the snake by one segment when eaten.
 */
@SpawnWeight(70)
public class Apple implements Food {
    private final Point position;

    /**
     * Creates an apple at the given position.
     *
     * @param position board cell
     */
    public Apple(Point position) {
        this.position = position;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void applyEffect(Game model) {
        model.getSnake().grow();
    }
}