package ru.nsu.kiryushin;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * JavaFX controller: drives the game loop, renders the board, and routes keyboard/Arduino input.
 */
public class GameController {
    @FXML private Canvas gameCanvas;
    @FXML private Label scoreLabel;
    @FXML private Label speedLabel;
    @FXML private VBox gameOverBox;
    @FXML private Label gameOverText;

    private Game game;
    private static final int TILE_SIZE = 30;
    private AnimationTimer gameTimer;

    private final Map<Class<? extends Food>, Image> foodTextures = new HashMap<>();
    private Image headImg, bodyImg, tailImg, angleImg;

    /**
     * FXML lifecycle hook: loads food and snake textures, then starts the first game.
     */
    @FXML
    public void initialize() {
        for (Class<? extends Food> clazz : FoodRegistry.getAllFoodTypes()) {
            String path = "/" + clazz.getSimpleName().toLowerCase() + ".png";
            try {
                foodTextures.put(clazz, new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
            } catch (Exception e) { System.err.println("Нет текстуры: " + path); }
        }

        try {
            headImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/head.png")));
            bodyImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/body.png")));
            tailImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/tail.png")));
            angleImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/bodyangle.png")));
        } catch (Exception e) {
            System.err.println("Не удалось загрузить текстуры змейки: " + e.getMessage());
        }

        startGame();
    }

    /**
     * FXML button handler: hides the game-over overlay and starts a fresh game.
     */
    @FXML
    public void restartGame() {
        gameOverBox.setVisible(false);
        if (gameTimer != null) gameTimer.stop();
        startGame();
    }

    private void startGame() {
        game = new Game(20, 20, 70, 15);
        gameTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= game.getSpeed()) {
                    if (!game.update()) {
                        showGameOver(game.getState());
                        this.stop();
                    }
                    render();
                    lastUpdate = now;
                }
            }
        };
        gameTimer.start();
    }

    private void render() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        drawBackground(gc);

        game.getFoodsOnBoard().forEach((p, f) -> {
            Image tex = foodTextures.get(f.getClass());
            if (tex != null) gc.drawImage(tex, p.x() * TILE_SIZE, p.y() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        });

        List<Point> body = new ArrayList<>(game.getSnake().getBody());
        for (int i = 0; i < body.size(); i++) {
            Point curr = body.get(i);
            double x = curr.x() * TILE_SIZE;
            double y = curr.y() * TILE_SIZE;

            if (i == 0) {
                drawRotated(gc, headImg, getHeadAngle(), x, y);
            } else if (i == body.size() - 1) {
                drawRotated(gc, tailImg, getTailAngle(curr, body.get(i - 1)), x, y);
            } else {
                drawBodySegment(gc, curr, body.get(i - 1), body.get(i + 1), x, y);
            }
        }
        updateHUD();
    }

    private void drawBodySegment(GraphicsContext gc, Point c, Point p, Point n, double x, double y) {
        int dx1 = p.x() - c.x(), dy1 = p.y() - c.y();
        int dx2 = n.x() - c.x(), dy2 = n.y() - c.y();

        if (dx1 == dx2 || dy1 == dy2) {
            drawRotated(gc, bodyImg, (dx1 != 0) ? 0 : 90, x, y);
        } else {
            int sumX = dx1 + dx2, sumY = dy1 + dy2;
            double angle;
            if      (sumX ==  1 && sumY ==  1) angle = -90;
            else if (sumX == -1 && sumY ==  1) angle = 0;
            else if (sumX == -1 && sumY == -1) angle = 90;
            else if (sumX ==  1 && sumY == -1) angle = 180;
            else {
                System.err.println("Неожиданные смещения тела: sumX=" + sumX + " sumY=" + sumY);
                angle = 0;
            }
            drawRotated(gc, angleImg, angle, x, y);
        }
    }

    private void drawRotated(GraphicsContext gc, Image img, double a, double x, double y) {
        gc.save();
        gc.translate(x + TILE_SIZE / 2.0, y + TILE_SIZE / 2.0);
        gc.rotate(a);
        gc.drawImage(img, -TILE_SIZE / 2.0, -TILE_SIZE / 2.0, TILE_SIZE, TILE_SIZE);
        gc.restore();
    }

    private double getHeadAngle() {
        return switch (game.getSnake().getDirection()) {
            case RIGHT -> 0; case DOWN -> 270; case LEFT -> 180; case UP -> 90;
        };
    }

    private double getTailAngle(Point t, Point p) {
        if (p.x() > t.x()) return 0; if (p.y() > t.y()) return 90;
        if (p.x() < t.x()) return 180; return 270;
    }

    private void drawBackground(GraphicsContext gc) {
        for (int x = 0; x < game.getColumns(); x++) {
            for (int y = 0; y < game.getRows(); y++) {
                gc.setFill((x + y) % 2 == 0 ? Color.web("#AAD751") : Color.web("#A2D149"));
                gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void updateHUD() {
        scoreLabel.setText("Счет: " + game.getSnake().getLength());
        speedLabel.setText(String.format("Скорость: %.1fx", game.getSpeedMultiplier()));
    }

    private void showGameOver(GameState s) {
        gameOverBox.setVisible(true);
        gameOverText.setText(s == GameState.WON ? "ПОБЕДА!" : "ПРОИГРЫШ");
        gameOverText.setStyle("-fx-text-fill: " + (s == GameState.WON ? "#FFD700" : "#ff4c4c") + "; -fx-font-size: 50px; -fx-font-weight: bold;");
    }

    /**
     * Routes a direction command to the snake.
     *
     * @param d requested direction
     */
    public void handleInput(Direction d) { game.queueDirection(d); }
}
