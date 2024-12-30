package org.example.java3;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.shape.Polygon;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;



public class Map_1 extends Application
 {
  private static final int FRAME_WIDTH  = 640;
  private static final int FRAME_HEIGHT = 480;

  int x, y;

  GraphicsContext gc;
  Canvas canvas;


  public static void main(String[] args)
   {
    launch(args);
   }



    @Override
    public void start(Stage primaryStage)
     {


      AnchorPane root = new AnchorPane();
      primaryStage.setTitle("Map");

      canvas = new Canvas(FRAME_WIDTH, FRAME_HEIGHT);
      canvas.setOnMousePressed(this::mouse);

      gc = canvas.getGraphicsContext2D();
      // Parse XML to get coordinates
      Parser_1 parser = new Parser_1();
      parser.parseXML(); // Ensure this method calls saxParser to fill x_p and y_p in Handler_1

      // Print the coordinates
      Handler_1 handler = parser.getHandler();
      ArrayList<Double> xCoordinates = handler.getX_p();
      ArrayList<Double> yCoordinates = handler.getY_p();

      System.out.println("X Coordinates: " + xCoordinates);
      System.out.println("Y Coordinates: " + yCoordinates);


      // A
      Image image = new Image("file:/D:/Java/HamudaJava/Java3/map.jpg");

      if (image.isError()) {
       System.out.println("Error loading image.");
      } else {
       gc.drawImage(image, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
      }



     drawPolygon(xCoordinates,yCoordinates);


      root.getChildren().add(canvas);

      RadioButton rbtn1 = new RadioButton();
      rbtn1.setText("Woods");
      rbtn1.setSelected(true);
      rbtn1.setOnAction(this::woods);

      root.getChildren().add(rbtn1);
      AnchorPane.setBottomAnchor( rbtn1, 5.0d );
      AnchorPane.setLeftAnchor( rbtn1, 50.0d );


      RadioButton rbtn2 = new RadioButton();
      rbtn2.setText("Rocks");
      rbtn2.setSelected(true);
      rbtn2.setOnAction(this::rocks);

      root.getChildren().add(rbtn2);
      AnchorPane.setBottomAnchor( rbtn2, 5.0d );
      AnchorPane.setLeftAnchor( rbtn2, 200.0d );




      Scene scene = new Scene(root);
      primaryStage.setTitle("Dolina Bedkowska");
      primaryStage.setScene( scene );
      primaryStage.setWidth(FRAME_WIDTH + 10);
      primaryStage.setHeight(FRAME_HEIGHT+ 80);
      primaryStage.show();
    }

   private void woods(ActionEvent e)
    {
     System.out.println("woods");
    }

   private void rocks(ActionEvent e)
    {
     System.out.println("rocks");
    }

  public void drawPolygon(ArrayList<Double> xCoordinates, ArrayList<Double> yCoordinates) {
   ArrayList<Double> currentX = new ArrayList<>();
   ArrayList<Double> currentY = new ArrayList<>();
   int j = 0;
   for (int i = 0; i < xCoordinates.size(); i++) {
    double x = xCoordinates.get(i);
    double y = yCoordinates.get(i);

    // Sprawdź, czy dotarliśmy do separatora
    if (x == 999.0) {
     j = j+1;
     if (!currentX.isEmpty()) {
      if (j >= 17 && j <= 30) {
       // Wywołanie metody drawBuilding, jeśli indeks mieści się w zakresie
       draw_building(currentX, currentY); // Możesz zmienić kolor
      } else {
       drawSinglePolygon(currentX, currentY);
      }
      currentX.clear();
      currentY.clear();
     }
    } else {
     // Dodaj współrzędne do bieżącej listy
     currentX.add(x);
     currentY.add(y);
    }
   }
   if (!currentX.isEmpty()) {
    if (xCoordinates.size() >= 16 && xCoordinates.size() <= 3000) {
     draw_building(currentX, currentY); // Możesz zmienić kolor
    } else {
     drawSinglePolygon(currentX, currentY);
    }
   }
  }
private void draw_building(ArrayList<Double> xCoordinates, ArrayList<Double> yCoordinates){
 gc.setFill(Color.RED);
 gc.fillPolygon(xCoordinates.stream().mapToDouble(Double::doubleValue).toArray(),
         yCoordinates.stream().mapToDouble(Double::doubleValue).toArray(), xCoordinates.size());


 gc.setStroke(Color.BLACK);
 gc.setLineWidth(2);
 gc.strokePolygon(xCoordinates.stream().mapToDouble(Double::doubleValue).toArray(),
         yCoordinates.stream().mapToDouble(Double::doubleValue).toArray(), xCoordinates.size());


}
  private void drawSinglePolygon(ArrayList<Double> xCoordinates, ArrayList<Double> yCoordinates) {
   int size = xCoordinates.size();
   gc.setStroke(Color.BLACK);
   gc.setLineWidth(2);
   gc.moveTo(xCoordinates.getFirst(), yCoordinates.getFirst());
   for (int i = 1; i < size; i++) {
    gc.lineTo(xCoordinates.get(i), yCoordinates.get(i));
   }
   gc.stroke();
  }


   private void mouse(MouseEvent e)
    {
     System.out.println("X=" + e.getX());
     System.out.println("Y=" + e.getY());
    }
}
