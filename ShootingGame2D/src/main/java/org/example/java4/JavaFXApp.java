package org.example.java4;
import javafx.application.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import java.util.Optional;
import java.util.Iterator;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/*
import gp.*;

class GamepadController{
    private final Gp gamepad;
    private final Position position;
    private final List<Bullet> bullets;
    private String lastDirection = "N";

    public GamepadController(Position position,List<Bullet> bullets){
        this.position = position;
        this.bullets = bullets;
        this.gamepad = new Gp();
        final  int VENDOR_ID   = 0x0810;
        int result = gamepad.gp_open(VENDOR_ID, 0x0001);
        if(result == -1) System.out.println("gamepad not opened");

    }
    public void start() {
        Thread gamepadThread = new Thread(() -> {
            while(true){
                Gp_values curr_v = gamepad.get_values();
                if(curr_v.buttons[11]) position.set_Position_y(Math.max(0,position.y - 10));
                if(curr_v.buttons[12]) position.set_Position_x(Math.min(940,position.x + 10));
                if(curr_v.buttons[13]) position.set_Position_y(Math.min(580,position.y + 10));
                if(curr_v.buttons[14]) position.set_Position_x(Math.max(0,position.x - 10));
                if(curr_v.buttons[9]) bullets.add(new Bullet(position.x, position.y,lastDirection));
                try{
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        gamepadThread.setDaemon(true);
        gamepadThread.start();
    }
}

 */
class Keyboard {
    private final Position position;
    private final List<Bullet> bullets;
    private String lastDirection = "S";
    public Keyboard(Position position,List<Bullet> bullets) {
        this.position = position;
        this.bullets = bullets;
    }
    public void addKeyboardControls(Scene scene, Runnable onPositionChange) {
        scene.setOnKeyPressed(event -> {
            boolean moved = false;
            if (event.getCode() == KeyCode.UP && position.y>0) {
                position.set_Position_y(position.y - 10);
                lastDirection = "N";
                moved = true;
            } else if (event.getCode() == KeyCode.DOWN && position.y<580) {
                position.set_Position_y(position.y + 10);
                lastDirection = "S";
                moved = true;
            } else if (event.getCode() == KeyCode.LEFT && position.x >0) {
                position.set_Position_x(position.x - 10);
                lastDirection = "W";
                moved = true;
            } else if (event.getCode() == KeyCode.RIGHT&& position.x <940) {
                position.set_Position_x(position.x + 10);
                lastDirection = "E";
                moved = true;
            }else if (event.getCode() == KeyCode.SPACE) {
                bullets.add(new Bullet(position.x, position.y,lastDirection));
            }
            if (moved) {
                onPositionChange.run();
            }
        });
    }
}


class Position {
    int x, y;
    public Position() {
        x = 300;
        y = 300;
    }
    public void set_Position_x(int newX) {
        x = newX;
    }
    public void set_Position_y(int newY) {
        y = newY;
    }
}
class Bullet {
    private int x, y;
    private int previousX, previousY;
    private double offsetX, offsetY;
    private double previousOffsetX, previousOffsetY;
    private final String direction;

    public Bullet(int startX, int startY, String direction) {
        this.x = startX;
        this.y = startY;
        this.previousX = startX;
        this.previousY = startY;
        this.direction = direction;
        setOffsets(direction);
    }
    public void update() {
        previousX = x;
        previousY = y;
        previousOffsetX = offsetX;
        previousOffsetY = offsetY;
        int speed = 15;
        switch (direction) {
            case "N":
                y -= speed;
                break;
            case "E":
                x += speed;
                break;
            case "S":
                y += speed;
                break;
            case "W":
                x -= speed;
                break;
        }
        setOffsets(direction);
    }
    private void setOffsets(String direction) {
        switch (direction) {
            case "N":
                offsetX = 7.5;
                offsetY = -5;
                break;
            case "E":
                offsetX = 20;
                offsetY = 7.5;
                break;
            case "S":
                offsetX = 7.5;
                offsetY = 20;
                break;
            case "W":
                offsetX = -5;
                offsetY = 7.5;
                break;
        }
    }
    public void draw(GraphicsContext gc) {
        gc.clearRect(previousX + previousOffsetX, previousY + previousOffsetY, 5, 5);
        gc.setFill(Color.BLACK);
        gc.fillOval(x + offsetX, y + offsetY, 5, 5);
    }
    public boolean isOutOfBounds(int width, int height) {
        int posX = (int) (x + offsetX);
        int posY = (int) (y + offsetY);
        return (posX < -25 || posX > width || posY < -25 || posY > height);
    }
    public double getBulletX() {
        return x + offsetX;
    }
    public double getBulletY() {
        return y + offsetY;
    }
}

class Mushroom {
    private final List<int[]> positions;
    private final int mushroomSize = 20;
    private final int numMushrooms = 20;

    public Mushroom() {
        positions = new ArrayList<>();
        generateMushrooms();
    }
    private void generateMushrooms() {
        Random rand = new Random();
        for (int i = 0; i < numMushrooms; i++) {
            int x = rand.nextInt(960 - mushroomSize);
            int y = rand.nextInt(600 - mushroomSize);
            positions.add(new int[]{x, y});
        }
    }
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BROWN);
        for (int[] pos : positions) {
            gc.fillOval(pos[0], pos[1], mushroomSize, mushroomSize);
        }
    }
    public List<int[]> getPositions() {
        return positions;
    }
}
class Player {
    private final Position position;
    private int previousX;
    private int previousY;

    public Player(Position position) {
        this.position = position;
        this.previousX = position.x;
        this.previousY = position.y;
    }
    public void drawPlayer(GraphicsContext gc) {
        gc.clearRect(previousX, previousY, 20, 20);
        gc.setFill(Color.BLUE);
        gc.fillRect(position.x, position.y, 20, 20);
        previousX = position.x;
        previousY = position.y;
        //System.out.println("x = "+position.x +" y = "+ position.y);
    }
}


public class JavaFXApp extends Application {
    private Position position;
    private int score = 0;
    private Player player;
    private Mushroom mushrooms;
    private Canvas canvas;
    //private GamepadController gamepadController;
    private List<Bullet> bullets;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX App");
        position = new Position();
        player = new Player(position);
        mushrooms = new Mushroom();
        bullets = new ArrayList<>();

        //gamepadController = new GamepadController(position,bullets);
        //gamepadController.start();

        canvas = new Canvas(960, 625);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Menu menu1 = new Menu("File");
        MenuItem menuItem1 = new MenuItem("Item 1");
        MenuItem menuItem2 = new MenuItem("Exit");

        menuItem2.setOnAction(e -> {
            System.out.println("Exit Selected");
            exit_dialog();
        });

        menu1.getItems().add(menuItem1);
        menu1.getItems().add(menuItem2);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu1);

        VBox vBox = new VBox(menuBar, canvas);
        Scene scene = new Scene(vBox, 960, 625);
        primaryStage.setScene(scene);

        Keyboard keyboard = new Keyboard(position,bullets);
        keyboard.addKeyboardControls(scene, () -> {
            player.drawPlayer(gc);
            mushrooms.draw(gc);
            drawBullets(gc);
        });

        player.drawPlayer(gc);
        mushrooms.draw(gc);
        Thread renderThread = new Thread(() -> {
            while (true) {
                player.drawPlayer(gc);
                mushrooms.draw(gc);
                drawBullets(gc);
                checkCollisions(gc);
                drawScore(gc);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        renderThread.setDaemon(true);
        renderThread.start();
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            exit_dialog();
        });
        primaryStage.show();
    }
    private void drawBullets(GraphicsContext gc){
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while(bulletIterator.hasNext()){
            Bullet bullet = bulletIterator.next();
            bullet.update();
            if(bullet.isOutOfBounds(985,625)) {
                bulletIterator.remove();
            }
            else {
                bullet.draw(gc);
            }
        }
    }

    private void checkCollisions(GraphicsContext gc) {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            double bulletX = bullet.getBulletX();
            double bulletY = bullet.getBulletY();
            Iterator<int[]> mushroomIterator = mushrooms.getPositions().iterator();
            while (mushroomIterator.hasNext()) {
                int[] mushroom = mushroomIterator.next();
                int mushroomX = mushroom[0];
                int mushroomY = mushroom[1];
                if (bulletX >= mushroomX && bulletX <= mushroomX + 20
                        && bulletY >= mushroomY && bulletY <= mushroomY + 20) {
                    gc.clearRect(mushroomX, mushroomY,20, 20);
                    gc.clearRect(bulletX, bulletY,5, 5);
                    mushroomIterator.remove();
                    bulletIterator.remove();
                    score++;
                    //System.out.println("Collision detected! Mushroom removed.");
                    break;
                }
            }
        }
    }
    private void drawScore(GraphicsContext gc) {
        gc.clearRect(0, 0, 960, 30);
        gc.setFill(Color.BLACK);
        gc.fillText("Score: " + score, 10, 20);
    }
    public void exit_dialog() {
        System.out.println("exit dialog");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to exit the program?", ButtonType.YES, ButtonType.NO);
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            Platform.exit();
        }
    }
}
