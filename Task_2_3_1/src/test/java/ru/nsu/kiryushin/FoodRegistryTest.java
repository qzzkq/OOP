package ru.nsu.kiryushin;

import ru.nsu.kiryushin.food.Apple;
import ru.nsu.kiryushin.food.Booster;
import ru.nsu.kiryushin.food.Chili;
import ru.nsu.kiryushin.food.Ice;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FoodRegistryTest {

    @Test
    void allBuiltInFoodTypesAreDiscovered() {
        List<Class<? extends Food>> types = FoodRegistry.getAllFoodTypes();
        assertTrue(types.contains(Apple.class));
        assertTrue(types.contains(Ice.class));
        assertTrue(types.contains(Booster.class));
        assertTrue(types.contains(Chili.class));
    }

    @Test
    void createRandomReturnsNonNull() {
        Point pos = new Point(3, 7);
        Food food = FoodRegistry.createRandom(pos);
        assertNotNull(food);
        assertEquals(pos, food.getPosition());
    }

    @Test
    void createSpecificApple() {
        Point pos = new Point(1, 2);
        Food food = FoodRegistry.createSpecific(Apple.class, pos);
        assertInstanceOf(Apple.class, food);
        assertEquals(pos, food.getPosition());
    }

    @Test
    void createSpecificIce() {
        Point pos = new Point(4, 4);
        Food food = FoodRegistry.createSpecific(Ice.class, pos);
        assertInstanceOf(Ice.class, food);
    }
}
