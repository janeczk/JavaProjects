package org.example.demo1;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;


public class Chart_1 extends Application
 {
  TextField t1, t2, t3;
  LineChart l;
  XYChart.Series<Double, Double> series;
 
 @Override
  
  public void start(Stage primaryStage) throws Exception 
   {
    AnchorPane ap = new AnchorPane();

    Label l1,l2,l3;

    Button action = new Button("Display");
    
    action.setOnAction(this::display);
        
    double a, b, c;
    
    VBox parameters = new VBox();
    parameters.setSpacing( 5.0d );
    parameters.setAlignment(Pos.TOP_LEFT);
    
    HBox line_1, line_2, line_3;
    
    line_1 = new HBox();
    line_1.setSpacing( 4.0d );
    
    l1 = new Label("a:");
    t1 = new TextField("0.0");
    
    line_1.getChildren().addAll(l1, t1);


    line_2 = new HBox();
    line_2.setSpacing( 4.0d );    

    l2 = new Label("b:");
    t2 = new TextField("0.0");
    
    line_2.getChildren().addAll(l2, t2);


    line_3 = new HBox();
    line_3.setSpacing( 4.0d );    
    
    l3 = new Label("c:");
    t3 = new TextField("0.0");
    
    line_3.getChildren().addAll(l3, t3);


    parameters.getChildren().addAll(line_1, line_2, line_3, action);

    
    ap.getChildren().add( parameters );
    
    l = new LineChart(new NumberAxis("X", -1.0, 1.0, 0.1), new NumberAxis("Y", -1.0, 1.0, 0.1));

    l.setLegendVisible(false);
    l.setCreateSymbols(false);
    
    series = new XYChart.Series<>();

    l.getData().add(series);

    
    l.setTitle("Some function");
    l.setStyle("-fx-background-color: white");
    ap.getChildren().add( l );
    AnchorPane.setTopAnchor( l, 120.0d );
    AnchorPane.setBottomAnchor( l, 40.0d );
    AnchorPane.setRightAnchor( l, 10.0d );
    AnchorPane.setLeftAnchor( l, 10.0d );
    Scene scene = new Scene(ap);
    primaryStage.setTitle("Simple chart plotting");
    primaryStage.setScene( scene );
    primaryStage.setWidth(568);
    primaryStage.setHeight(420);
    primaryStage.show();   
  }

  private double delta(double a, double b, double c){
   return (b*b - 4*a*c);
  }

  private void display(ActionEvent e)
   {
    System.out.println("Display pressed");

    System.out.println("a=" + t1.getText());
    System.out.println("b=" + t2.getText());
    System.out.println("c=" + t3.getText());
    double a = Double.parseDouble(t1.getText());
    double b = Double.parseDouble(t2.getText());
    double c = Double.parseDouble(t3.getText());

    for(double i = -1; i<=1;i+=0.2){
     double trojmian = a*i*i + b*i + c;
     series.getData().add( new XYChart.Data<>(i,trojmian));
    }
    double delta = delta(a,b,c);
    if(delta < 0) {
     System.out.println("Delta ujemna, nie ma miejsc zerowych!");
    }
    else {
     double x1 = (-b + Math.sqrt(delta)) / 2 * a;
     double x2 = (-b - Math.sqrt(delta)) / 2 * a;
     System.out.println("x1 = " + x1);
     System.out.println("x2 = " + x2);
    }

    series.getData().remove(0,11);


    
   }  

   
  public static void main(String[] args) 
   {
    launch(args);
   }
 }  
  