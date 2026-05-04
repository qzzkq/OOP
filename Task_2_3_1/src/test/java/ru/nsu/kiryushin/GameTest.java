package ru.nsu.kiryushin;

import ru.nsu.kiryushin.food.Apple;
import ru.nsu.kiryushin.food.Booster;
import ru.nsu.kiryushin.food.Chili;
import ru.nsu.kiryushin.food.Ice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    @Test
    void initialStateIsPlaying() {
        Game game = new Game(10, 10, 50, 1);
        assertEquals(GameState.PLAYING, game.getState());
    }

    @Test
    void updateReturnsTrueWhilePlaying() {
        Game game = new Game(10, 10, 50, 1);
        assertTrue(game.update());
    }

    @Test
    void snakeHitsLeftWallLoses() {
        Game game = new Game(10, 10, 50, 0);
        game.getSnake().setDirection(Direction.LEFT);
        for (int i = 0; i < 15; i++) {
            game.update();
        }
        assertEquals(GameState.LOSE, game.getState());
    }

    @Test
    void snakeHitsTopWallLoses() {
        Game game = new Game(10, 10, 50, 0);
        game.getSnake().setDirection(Direction.UP);
        for (int i = 0; i < 15; i++) {
            game.update();
        }
        assertEquals(GameState.LOSE, game.getState());
    }

    @Test
    void snakeHitsRightWallLoses() {
        Game game = new Game(10, 10, 50, 0);
        for (int i = 0; i < 15; i++) {
            game.update();
        }
        assertEquals(GameState.LOSE, game.getState());
    }

    @Test
    void updateReturnsFalseAfterGameEnds() {
        Game game = new Game(10, 10, 50, 0);
        game.getSnake().setDirection(Direction.LEFT);
        for (int i = 0; i < 15; i++) {
            game.update();
        }
        assertFalse(game.update());
        assertEquals(GameState.LOSE, game.getState());
    }

    @Test
    void reachingWinLengthTriggersWon() {
        Game game = new Game(20, 20, 2, 0);
        Point head = game.getSnake().getHead();
        Apple apple = new Apple(new Point(head.x() + 1, head.y()));
        game.placeFood(apple);
        game.update();
        game.update();
        assertEquals(GameState.WON, game.getState());
    }

    @Test
    void initialFoodCountMatchesTarget() {
        int target = 5;
        Game game = new Game(20, 20, 50, target);
        assertEquals(target, game.getFoodsOnBoard().size());
    }

    @Test
    void appleGrowsSnake() {
        Snake snake = new Snake(new Point(5, 5));
        int before = snake.getLength();
        new Apple(new Point(5, 5)).applyEffect(createGameWithSnake(snake));
        snake.move();
        assertEquals(before + 1, snake.getLength());
    }

    @Test
    void chiliShrinksSnake() {
        Snake snake = new Snake(new Point(5, 5));
        snake.grow();
        snake.move();
        int before = snake.getLength();
        new Chili(new Point(5, 5)).applyEffect(createGameWithSnake(snake));
        assertEquals(before - 1, snake.getLength());
    }

    @Test
    void iceDecreasesSpeed() {
        Game game = new Game(20, 20, 50, 0);
        double before = game.getSpeed();
        new Ice(new Point(0, 0)).applyEffect(game);
        assertTrue(game.getSpeed() > before);
    }

    @Test
    void boosterIncreasesSpeed() {
        Game game = new Game(20, 20, 50, 0);
        double before = game.getSpeed();
        new Booster(new Point(0, 0)).applyEffect(game);
        assertTrue(game.getSpeed() < before);
    }

    @Test
    void speedIsClampedAboveMinimum() {
        Game game = new Game(20, 20, 50, 0);
        for (int i = 0; i < 20; i++) {
            new Booster(new Point(0, 0)).applyEffect(game);
        }
        assertTrue(game.getSpeed() > 0);
    }

    @Test
    void speedIsClampedBelowMaximum() {
        Game game = new Game(20, 20, 50, 0);
        double baseline = game.getSpeed();
        for (int i = 0; i < 20; i++) {
            new Ice(new Point(0, 0)).applyEffect(game);
        }
        assertTrue(game.getSpeed() <= baseline * 4);
    }

    private Game createGameWithSnake(Snake snake) {
        return new Game(20, 20, 50, 0) {
            @Override
            public Snake getSnake() {
                return snake;
            }
        };
    }
}
