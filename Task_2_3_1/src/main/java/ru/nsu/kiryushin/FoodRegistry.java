package ru.nsu.kiryushin;

import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Discovers all concrete {@link Food} implementations at runtime and creates instances by weighted random selection.
 */
public class FoodRegistry {
    private static final List<Class<? extends Food>> foodClasses = new ArrayList<>();
    private static final Random random = new Random();

    static {
        Reflections reflections = new Reflections("ru.nsu.kiryushin.food");
        for (Class<? extends Food> clazz : reflections.getSubTypesOf(Food.class)) {
            if (!Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {
                foodClasses.add(clazz);
            }
        }
    }

    /**
     * Creates a random food item at the given position, respecting each type's {@link SpawnWeight}.
     *
     * @param position board cell where the food will be placed
     * @return new food instance
     */
    public static Food createRandom(Point position) {
        if (foodClasses.isEmpty()) {
            throw new IllegalStateException("No food types registered");
        }
        try {
            int totalWeight = 0;
            for (Class<? extends Food> clazz : foodClasses) {
                SpawnWeight annotation = clazz.getAnnotation(SpawnWeight.class);
                totalWeight += (annotation != null) ? annotation.value() : 10;
            }

            int randomValue = random.nextInt(totalWeight);
            int currentSum = 0;

            Class<? extends Food> selectedClass = foodClasses.get(0);
            for (Class<? extends Food> clazz : foodClasses) {
                SpawnWeight annotation = clazz.getAnnotation(SpawnWeight.class);
                int weight = (annotation != null) ? annotation.value() : 10;
                currentSum += weight;

                if (randomValue < currentSum) {
                    selectedClass = clazz;
                    break;
                }
            }

            return selectedClass.getConstructor(Point.class).newInstance(position);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates an instance of a specific food type at the given position.
     *
     * @param clazz food class to instantiate
     * @param position board cell where the food will be placed
     * @return new food instance
     */
    public static Food createSpecific(Class<? extends Food> clazz, Point position) {
        try {
            return clazz.getConstructor(Point.class).newInstance(position);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns an unmodifiable view of all discovered concrete food types.
     *
     * @return unmodifiable list of food classes
     */
    public static List<Class<? extends Food>> getAllFoodTypes() {
        return Collections.unmodifiableList(foodClasses);
    }
}
