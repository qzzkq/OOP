package ru.nsu.kiryushin.model;

/**
 * A food item that can be placed on the board and applies an effect when eaten.
 */
public interface Food {
    /**
     * Returns the board position of this food item.
     *
     * @return grid position
     */
    Point getPosition();

    /**
     * Returns the relative spawn weight for this food type. Higher values make this
     * food appear more often relative to others.
     *
     * @return spawn weight
     */
    int getSpawnWeight();

    /**
     * Applies the food's effect to the game model.
     *
     * @param model game instance to modify
     */
    void applyEffect(Game model);
}
