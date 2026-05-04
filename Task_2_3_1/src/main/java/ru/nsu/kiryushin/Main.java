package ru.nsu.kiryushin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import com.fazecast.jSerialComm.SerialPort;

import java.util.Optional;

/**
 * JavaFX entry point: loads the FXML scene, detects Arduino, and lets the user choose the input method.
 */
public class Main extends Application {

    private SerialPort arduinoPort;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scene.fxml"));
        Parent root = loader.load();

        GameController controller = loader.getController();
        Scene scene = new Scene(root);

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> closeSerialPort());
        primaryStage.show();

        SerialPort[] ports = SerialPort.getCommPorts();
        boolean useArduino = ports.length > 0 && askInputChoice(primaryStage, ports[0]);

        if (useArduino) {
            setupArduinoInput(controller, ports[0]);
        } else {
            setupKeyboardInput(scene, controller);
        }
    }

    /**
     * Shows a dialog asking the user to choose between keyboard and Arduino.
     * Called only when at least one serial port is detected.
     *
     * @param owner the owner stage for the dialog
     * @param port the detected Arduino port
     * @return true if the user chose Arduino, false if keyboard
     */
    private boolean askInputChoice(Stage owner, SerialPort port) {
        ButtonType keyboardBtn = new ButtonType("Клавиатура");
        ButtonType arduinoBtn  = new ButtonType("Ардуино");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(owner);
        alert.setTitle("Выбор управления");
        alert.setHeaderText("Обнаружено устройство: " + port.getSystemPortName());
        alert.setContentText("Выберите способ управления змейкой:");
        alert.getButtonTypes().setAll(keyboardBtn, arduinoBtn);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == arduinoBtn;
    }

    /**
     * Wires WASD / arrow keys to the game controller.
     *
     * @param scene scene to attach the key listener to
     * @param controller game controller to receive direction events
     */
    private void setupKeyboardInput(Scene scene, GameController controller) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W, UP    -> controller.handleInput(Direction.UP);
                case S, DOWN  -> controller.handleInput(Direction.DOWN);
                case A, LEFT  -> controller.handleInput(Direction.LEFT);
                case D, RIGHT -> controller.handleInput(Direction.RIGHT);
            }
        });
        System.out.println("Управление: клавиатура");
    }

    /**
     * Opens the given serial port and starts a daemon thread that forwards joystick commands to the controller.
     *
     * @param controller game controller to receive direction events
     * @param port serial port to open
     */
    private void setupArduinoInput(GameController controller, SerialPort port) {
        arduinoPort = port;
        arduinoPort.setBaudRate(9600);

        if (!arduinoPort.openPort()) {
            System.out.println("Не удалось открыть порт " + arduinoPort.getSystemPortName() + ". Переключаемся на клавиатуру.");
            return;
        }

        System.out.println("Управление: Arduino (" + arduinoPort.getSystemPortName() + ")");

        Thread serialThread = new Thread(() -> {
            while (arduinoPort.isOpen()) {
                if (arduinoPort.bytesAvailable() > 0) {
                    byte[] readBuffer = new byte[1];
                    arduinoPort.readBytes(readBuffer, readBuffer.length);
                    char command = (char) readBuffer[0];

                    Platform.runLater(() -> {
                        switch (command) {
                            case 'U' -> controller.handleInput(Direction.UP);
                            case 'D' -> controller.handleInput(Direction.DOWN);
                            case 'L' -> controller.handleInput(Direction.LEFT);
                            case 'R' -> controller.handleInput(Direction.RIGHT);
                        }
                    });
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        serialThread.setDaemon(true);
        serialThread.start();
    }

    /**
     * Closes the Arduino serial port if it was opened.
     */
    private void closeSerialPort() {
        if (arduinoPort != null && arduinoPort.isOpen()) {
            arduinoPort.closePort();
            System.out.println("COM порт закрыт.");
        }
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments forwarded to {@link Application#launch}
     */
    public static void main(String[] args) {
        launch(args);
    }
}
