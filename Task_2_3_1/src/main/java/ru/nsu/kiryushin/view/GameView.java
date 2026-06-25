package ru.nsu.kiryushin.view;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ru.nsu.kiryushin.controller.GameController;
import ru.nsu.kiryushin.model.Direction;
import ru.nsu.kiryushin.model.GameState;
import ru.nsu.kiryushin.model.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * JavaFX FXML controller: renders the board on a {@link Canvas} and routes input through
 * {@link GameController}. Reads game state only via the controller and basic value types.
 */
public class GameView {
    @FXML private Canvas gameCanvas;
    @FXML private VBox gameOverBox;
    @FXML private Label gameOverText;

    private Consumer<String> titleUpdater;

    private static final int TILE_SIZE = 30;

    /** Field is a neutral dark slate so the green snake textures stand out. */
    private static final Color TILE_LIGHT = Color.web("#3F4A55");
    private static final Color TILE_DARK  = Color.web("#374049");

    private final GameController controller = new GameController();
    private final Map<String, Image> foodTextures = new HashMap<>();
    private Image headImg;
    private Image bodyImg;
    private Image tailImg;
    private Image angleImg;
    private AnimationTimer gameTimer;

    /**
     * FXML lifecycle hook: loads food and snake textures, then starts the first game.
     */
    @FXML
    public void initialize() {
        for (String name : controller.getFoodTypeNames()) {
            String path = "/" + name + ".png";
            try {
                foodTextures.put(name, new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
            } catch (Exception e) {
                System.err.println("Нет текстуры: " + path);
            }
        }

        try {
            headImg  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/head.png")));
            bodyImg  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/body.png")));
            tailImg  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/tail.png")));
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
        if (gameTimer != null) {
            gameTimer.stop();
        }
        controller.restart();
        startGame();
    }

    private void startGame() {
        gameTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= controller.getTickInterval()) {
                    if (!controller.update()) {
                        showGameOver(controller.getState());
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

        controller.getFoodsOnBoard().forEach((p, name) -> {
            Image tex = foodTextures.get(name);
            if (tex != null) {
                gc.drawImage(tex, p.x() * TILE_SIZE, p.y() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        });

        List<Point> body = controller.getSnakeBody();
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
        updateHud();
    }

    private void drawBodySegment(GraphicsContext gc, Point c, Point p, Point n, double x, double y) {
        int dx1 = p.x() - c.x();
        int dy1 = p.y() - c.y();
        int dx2 = n.x() - c.x();
        int dy2 = n.y() - c.y();

        if (dx1 == dx2 || dy1 == dy2) {
            drawRotated(gc, bodyImg, (dx1 != 0) ? 0 : 90, x, y);
        } else {
            int sumX = dx1 + dx2;
            int sumY = dy1 + dy2;
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
        return switch (controller.getSnakeDirection()) {
            case RIGHT -> 0;
            case DOWN  -> 270;
            case LEFT  -> 180;
            case UP    -> 90;
        };
    }

    private double getTailAngle(Point t, Point p) {
        if (p.x() > t.x()) return 0;
        if (p.y() > t.y()) return 90;
        if (p.x() < t.x()) return 180;
        return 270;
    }

    private void drawBackground(GraphicsContext gc) {
        for (int x = 0; x < controller.getColumns(); x++) {
            for (int y = 0; y < controller.getRows(); y++) {
                gc.setFill((x + y) % 2 == 0 ? TILE_LIGHT : TILE_DARK);
                gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void updateHud() {
        if (titleUpdater != null) {
            titleUpdater.accept(String.format("Snake Game — Счет: %d | Скорость: %.1fx",
                    controller.getScore(), controller.getSpeedMultiplier()));
        }
    }

    /**
     * Sets a callback the view uses to publish the score / speed HUD (typically wired to
     * the primary {@link javafx.stage.Stage}'s title). Called once per tick.
     *
     * @param titleUpdater consumer that receives the formatted HUD string
     */
    public void setTitleUpdater(Consumer<String> titleUpdater) {
        this.titleUpdater = titleUpdater;
    }

    private void showGameOver(GameState state) {
        gameOverBox.setVisible(true);
        gameOverText.setText(state == GameState.WON ? "ПОБЕДА!" : "ПРОИГРЫШ");
        gameOverText.setStyle("-fx-text-fill: " + (state == GameState.WON ? "#FFD700" : "#ff4c4c")
                + "; -fx-font-size: 50px; -fx-font-weight: bold;");
    }

    /**
     * Routes a direction command from external input (keyboard, Arduino) to the controller.
     *
     * @param direction requested direction
     */
    public void handleInput(Direction direction) {
        controller.handleInput(direction);
    }
}
