package org.example.java2;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

// Abstract class for Shape
abstract class Shape {
 protected int x, y;
 protected Color color;
 private boolean isScaled;

 public Shape(int x, int y, String color) {
  this.x = x;
  this.y = y;
  this.color = Color.valueOf(color);
  this.isScaled = false;
 }

 public abstract double calcArea();
 public abstract void draw(GraphicsContext gc);
 public abstract boolean contains(double x, double y);
 public abstract void scale(double factor);

 public boolean isScaled() {
  return isScaled;
 }

 public void setScaled(boolean scaled) {
  this.isScaled = scaled;
 }
}

// Circle class
class Circle extends Shape {
 private double radius;

 public Circle(int x, int y, Color color, double radius) {
  super(x, y, String.valueOf(color));
  this.radius = radius;
 }

 @Override
 public double calcArea() {
  return Math.PI * radius * radius;
 }

 @Override
 public void draw(GraphicsContext gc) {
  gc.setFill(color);
  gc.fillOval(x, y, radius, radius);
 }

 @Override
 public boolean contains(double mouseX, double mouseY) {
  double dx = mouseX - x;
  double dy = mouseY - y;
  return dx * dx + dy * dy <= radius * radius;
 }

 @Override
 public void scale(double factor) {
  this.radius *= factor;
 }
}

// Ellipse class
class Ellipse extends Shape {
 private double majorAxis;
 private double minorAxis;

 public Ellipse(int x, int y, Color color, double majorAxis, double minorAxis) {
  super(x, y, String.valueOf(color));
  this.majorAxis = majorAxis;
  this.minorAxis = minorAxis;
 }

 @Override
 public double calcArea() {
  return Math.PI * majorAxis * minorAxis;
 }

 @Override
 public void draw(GraphicsContext gc) {
  gc.setFill(color);
  gc.fillOval(x, y, majorAxis * 2, minorAxis * 2);
 }

 @Override
 public boolean contains(double mouseX, double mouseY) {
  double dx = (mouseX - x) / majorAxis;
  double dy = (mouseY - y) / minorAxis;
  return dx * dx + dy * dy <= 1;
 }

 @Override
 public void scale(double factor) {
  this.majorAxis *= factor;
  this.minorAxis *= factor;
 }
}

// Square class
class Square extends Shape {
 private double side;

 public Square(int x, int y, Color color, double side) {
  super(x, y, String.valueOf(color));
  this.side = side;
 }

 @Override
 public double calcArea() {
  return side * side;
 }

 @Override
 public void draw(GraphicsContext gc) {
  gc.setFill(color);
  gc.fillRect(x, y, side, side);
 }

 @Override
 public boolean contains(double mouseX, double mouseY) {
  return mouseX >= x && mouseX <= x + side && mouseY >= y && mouseY <= y + side;
 }

 @Override
 public void scale(double factor) {
  this.side *= factor;
 }
}

// Rectangle class
class Rectangle extends Shape {
 private double width;
 private double height;

 public Rectangle(int x, int y, Color color, double width, double height) {
  super(x, y, String.valueOf(color));
  this.width = width;
  this.height = height;
 }

 @Override
 public double calcArea() {
  return width * height;
 }

 @Override
 public void draw(GraphicsContext gc) {
  gc.setFill(color);
  gc.fillRect(x, y, width, height);
 }

 @Override
 public boolean contains(double mouseX, double mouseY) {
  return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
 }

 @Override
 public void scale(double factor) {
  this.width *= factor;
  this.height *= factor;
 }
}

// Main class
public class Simple_game_2 extends Application implements ChangeListener<Number> {
 private static final int FRAME_WIDTH = 640;
 private static final int FRAME_HEIGHT = 480;

 private double x0, y0;
 private double x, y;
 private GraphicsContext gc;
 private Canvas canvas;
 private Slider alpha, v;
 private double time;
 private Timeline timeline;

 public static void main(String[] args) {
  launch(args);
 }

 @Override
 public void start(Stage primaryStage) {
  AnchorPane root = new AnchorPane();
  primaryStage.setTitle("Volleyball");

  canvas = new Canvas(FRAME_WIDTH, FRAME_HEIGHT);
  gc = canvas.getGraphicsContext2D();

  x0 = 20;
  y0 = 390;

  root.getChildren().add(canvas);

  // Buttons
  Button playButton = new Button("Play");
  Button circleButton = new Button("Only Circle");

  playButton.setOnAction(this::play);

  root.getChildren().add(circleButton);
  AnchorPane.setBottomAnchor(circleButton, 5.0);

  root.getChildren().add(playButton);
  AnchorPane.setBottomAnchor(playButton, 5.0);

  // Sliders
  alpha = createSlider(30, 80, 5, "alpha");
  root.getChildren().add(alpha);
  AnchorPane.setBottomAnchor(alpha, 2.0);
  AnchorPane.setLeftAnchor(alpha, 150.0);

  v = createSlider(10, 100, 10, "v");
  root.getChildren().add(v);
  AnchorPane.setBottomAnchor(v, 2.0);
  AnchorPane.setLeftAnchor(v, 300.0);

  // Shapes and events
  Shape[] shapes = createShapes();
  drawShapes(shapes);

  circleButton.setOnAction(event -> {
   gc.clearRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
   for (Shape shape : shapes) {
    if (shape instanceof Circle) {
     shape.draw(gc);
    }
   }
  });

  canvas.setOnMouseMoved(event -> handleMouseMove(event, shapes));
  primaryStage.setScene(new Scene(root));
  primaryStage.setWidth(FRAME_WIDTH + 10);
  primaryStage.show();
 }

 private Slider createSlider(int min, int max, int value, String label) {
  Slider slider = new Slider(min, max, value);
  slider.setShowTickMarks(true);
  slider.setShowTickLabels(true);
  slider.valueProperty().addListener(this);
  return slider;
 }

 private Shape[] createShapes() {
  return new Shape[]{
          new Circle(100, 100, Color.RED, 50),
          new Ellipse(200, 150, Color.BLUE, 60, 30),
          new Square(300, 200, Color.GREEN, 80),
          new Rectangle(400, 250, Color.YELLOW, 100, 50),
          new Circle(500, 300, Color.PURPLE, 40)
  };
 }

 private void drawShapes(Shape[] shapes) {
  for (Shape shape : shapes) {
   shape.draw(gc);
  }
 }

 private void handleMouseMove(MouseEvent event, Shape[] shapes) {
  double mouseX = event.getX();
  double mouseY = event.getY();

  for (Shape shape : shapes) {
   if (shape.contains(mouseX, mouseY)) {
    if (!shape.isScaled()) {
     shape.scale(1.5);
     shape.setScaled(true);
     redrawShapes(shapes);
    }
   } else {
    if (shape.isScaled()) {
     shape.scale(1 / 1.5);
     shape.setScaled(false);
     redrawShapes(shapes);
    }
   }
  }
 }

 private void redrawShapes(Shape[] shapes) {
  gc.clearRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
  for (Shape shape : shapes) {
   shape.draw(gc);
  }
 }

 @Override
 public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
  System.out.println("Value changed to: " + new_val);
 }

 private void play(ActionEvent e) {
  timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> animateBall()));
  timeline.setCycleCount(Timeline.INDEFINITE);
  timeline.play();
 }

 private void animateBall() {
  gc.clearRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
  drawBoisko();
  gc.setFill(Color.RED);
  gc.fillOval(x, y, 40, 40);

  x = x0 + time * v.getValue() * Math.cos(Math.toRadians(alpha.getValue()));
  y = y0 - time * v.getValue() * Math.sin(Math.toRadians(alpha.getValue())) + (9.81 * time * time) / 2;
  time += 0.05;

  if (x + 40 > 317 && y - 40 > 200 && x < 323) reset();
  if (x + 40 > FRAME_WIDTH) x = 1200 - time * v.getValue() * Math.cos(Math.toRadians(alpha.getValue()));
  if (y + 40 > 430) y = 390;
 }

 private void reset() {
  time = 0;
  x0 = 20;
  y0 = 390;
  gc.clearRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
  drawBoisko();
  gc.setFill(Color.RED);
  gc.fillOval(x0, y0, 40, 40);
  if (timeline != null) timeline.stop();
 }

 private void drawBoisko() {
  gc.setFill(Color.BLACK);
  gc.fillRect(317, 200, 6, 236);
  gc.fillRect(0, 430, FRAME_WIDTH, 6);
 }
}