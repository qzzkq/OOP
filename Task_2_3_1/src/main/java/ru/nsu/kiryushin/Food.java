package ru.nsu.kiryushin;

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
     * Applies the food's effect to the game model.
     *
     * @param model game instance to modify
     */
    void applyEffect(Game model);

}
