package ru.nsu.kiryushin.model;

/**
 * Base implementation of {@link Food} that stores position and spawn weight.
 * Subclasses only need to implement {@link #applyEffect(Game)}.
 */
public abstract class AbstractFood implements Food {
    private final Point position;
    private final int spawnWeight;

    /**
     * Creates a food item with the given position and spawn weight.
     *
     * @param position board cell
     * @param spawnWeight relative spawn weight
     */
    protected AbstractFood(Point position, int spawnWeight) {
        this.position = position;
        this.spawnWeight = spawnWeight;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public int getSpawnWeight() {
        return spawnWeight;
    }
}
