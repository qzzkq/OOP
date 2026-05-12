package ru.nsu.kiryushin.model;

import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Discovers all concrete {@link Food} implementations at runtime and creates instances by weighted random selection.
 * Each food's spawn weight is read from a probe instance via {@link Food#getSpawnWeight()}.
 */
public class FoodRegistry {
    private static final List<Class<? extends Food>> foodClasses = new ArrayList<>();
    private static final Map<Class<? extends Food>, Integer> weights = new LinkedHashMap<>();
    private static final Random random = new Random();

    private static final Point PROBE_POSITION = new Point(0, 0);

    static {
        Reflections reflections = new Reflections("ru.nsu.kiryushin.model.food");
        for (Class<? extends Food> clazz : reflections.getSubTypesOf(Food.class)) {
            if (Modifier.isAbstract(clazz.getModifiers()) || clazz.isInterface()) {
                continue;
            }
            foodClasses.add(clazz);
            weights.put(clazz, instantiate(clazz, PROBE_POSITION).getSpawnWeight());
        }
    }

    /**
     * Creates a random food item at the given position, with each type chosen by its
     * {@link Food#getSpawnWeight() spawn weight}.
     *
     * @param position board cell where the food will be placed
     * @return new food instance
     */
    public static Food createRandom(Point position) {
        if (foodClasses.isEmpty()) {
            throw new IllegalStateException("No food types registered");
        }

        int totalWeight = 0;
        for (Class<? extends Food> clazz : foodClasses) {
            totalWeight += weights.get(clazz);
        }

        int roll = random.nextInt(totalWeight);
        int running = 0;
        for (Class<? extends Food> clazz : foodClasses) {
            running += weights.get(clazz);
            if (roll < running) {
                return instantiate(clazz, position);
            }
        }
        // Unreachable: roll < totalWeight by construction.
        return instantiate(foodClasses.get(foodClasses.size() - 1), position);
    }

    /**
     * Creates an instance of a specific food type at the given position.
     *
     * @param clazz food class to instantiate
     * @param position board cell where the food will be placed
     * @return new food instance
     */
    public static Food createSpecific(Class<? extends Food> clazz, Point position) {
        return instantiate(clazz, position);
    }

    /**
     * Returns an unmodifiable view of all discovered concrete food types.
     *
     * @return unmodifiable list of food classes
     */
    public static List<Class<? extends Food>> getAllFoodTypes() {
        return Collections.unmodifiableList(foodClasses);
    }

    private static Food instantiate(Class<? extends Food> clazz, Point position) {
        try {
            return clazz.getConstructor(Point.class).newInstance(position);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to instantiate " + clazz.getName(), e);
        }
    }
}
