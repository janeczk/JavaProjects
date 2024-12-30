package org.example.java5;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.util.Optional;
import java.net.*;
import javafx.concurrent.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class UdpServer extends Thread {
    private DatagramSocket socket;

    public UdpServer(int port) throws Exception {
        socket = new DatagramSocket(port);  // Inicjalizujemy gniazdo do odbierania danych
        System.out.println("Server started on port " + port);
    }

    @Override
    public void run() {
        byte[] buffer = new byte[256];  // Bufor do przechowywania danych otrzymanych
        try {
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);  // Przygotowujemy pakiet do odbioru
                socket.receive(packet);  // Odbieramy dane

                String received = new String(packet.getData(), 0, packet.getLength());  // Przekształcamy dane na tekst
                System.out.println("Server received: " + received);  // Wyświetlamy otrzymaną wiadomość

                // Odesłanie odpowiedzi do klienta
                String response = "Server got: " + received;
                packet = new DatagramPacket(
                        response.getBytes(),
                        response.getBytes().length,
                        packet.getAddress(),
                        packet.getPort()
                );
                socket.send(packet);  // Wysyłamy odpowiedź do klienta
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class UdpClient {

    private DatagramSocket socket;
    private InetAddress address;
    private int port;

    public UdpClient(String hostname, int port) throws Exception {
        socket = new DatagramSocket();
        address = InetAddress.getByName(hostname);
        this.port = port;
    }

    public void send(String message) throws Exception {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
        System.out.println("Message sent: " + message);
    }
}

class P_move
 {
  int x, y;
  
  public P_move()
   {
    x = 10;
    y = 10;
   }
 }
 
class G_task extends Task<P_move>
 {
  P_move p_move;
    
  public G_task()
   {   
    this.p_move = new P_move();     
   }
  
  @Override
  protected P_move call() throws Exception
   {
    //int i = 0;
    UdpClient client = new UdpClient("localhost", 12345);
    client.send("Player moved to x=" + p_move.x + ", y=" + p_move.y);
    while(true)
     {
      /*System.out.println("Task's call method");
   
      p_move.x = 10 + i;
      p_move.y = 10 + i;
            
      updateValue(null); 
      updateValue(p_move);  

      System.out.println("i=" + i);
      
      i++;
      
      System.out.println("x = " +  p_move.x + "y = " +  p_move.y); 
      
      
      if(i == 10) 
       {
        updateValue(null); 
        break;
       }
     
            
      try { Thread.sleep(1000);  System.out.println("sleep method");    }
      catch (InterruptedException ex) 
       {
        System.out.println("catch method");
        break; 
       }
     }
    
    
     return p_move;*/
         }
    }
 }

class Game_service {

    private UdpClient udpClient;

    public Game_service(String serverHost, int serverPort) {
        try {
            udpClient = new UdpClient(serverHost, serverPort);  // Inicjalizacja klienta z danym IP i portem
        } catch (Exception e) {
            e.printStackTrace();  // Obsługa wyjątku w przypadku błędu
        }
    }

    public void sendBoardData(String boardData) {
        try {
            udpClient.send(boardData);  // Wysyłamy dane planszy do klienta
            System.out.println("Board data sent to opponent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





public class StatkiSerwer extends Application implements ChangeListener<P_move> {

    Stage stage;
    private GameBoard gameBoard;
    Game_service g_s;
    private UdpClient udpClient;
    private boolean isHorizontal = false;
    private int[] shipsSizes = {1, 1, 1, 2, 2, 3, 3, 4, 5};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Battleship");

        stage = primaryStage;
        try {
            udpClient = new UdpClient("192.168.1.36", 12345);  // Adres IP i port przeciwnika
        } catch (Exception e) {
            e.printStackTrace();
        }
        g_s = new Game_service("192.168.1.36", 12345);
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

        // Tworzenie planszy gry
        gameBoard = new GameBoard();

        GridPane gridPane = new GridPane();
        gameBoard.drawBoard(gridPane);

        // Reakcja na kliknięcia myszką
        gridPane.setOnMouseClicked(event -> {
            Point2D clickLocation = new Point2D(event.getX() / 40, event.getY() / 40);

            // Jeśli lewy przycisk myszy - ustawiamy statek
            if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                int size = shipsSizes[0]; // Przykładowo bierzemy pierwszy statek z tablicy
                if (gameBoard.placeShip(size, clickLocation, isHorizontal)) {
                    System.out.println("Ship placed at: " + clickLocation);
                    shipsSizes = removeElementFromArray(shipsSizes, 0);  // Usuwamy użyty statek
                }
            }
            // Jeśli prawy przycisk myszy - zmieniamy orientację
            if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                isHorizontal = !isHorizontal;  // Zmiana orientacji statku
                System.out.println("Orientation changed. Horizontal: " + isHorizontal);
            }
        });

        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> startGame());

        VBox vBox = new VBox(menuBar, gridPane, startButton);
        Scene scene = new Scene(vBox, 960, 600);
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            exit_dialog();
        });

        primaryStage.show();
    }
    private int[] removeElementFromArray(int[] array, int index) {
        int[] newArray = new int[array.length - 1];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + 1, newArray, index, array.length - index - 1);
        return newArray;
    }
    private void startGame() {
        String boardData = gameBoard.getBoardData();  // Pobieramy dane o planszy
        g_s.sendBoardData(boardData);  // Wysyłamy dane planszy do drugiego gracza
    }

    public void changed(ObservableValue<? extends P_move> observable,
                        P_move oldValue,
                        P_move newValue) {
        if (newValue != null) {
            System.out.println("changed method called, x = " + newValue.x + " y = " + newValue.y);
        }
    }

    public void item_1() {
        System.out.println("item 1");
    }

    public void exit_dialog() {
        System.out.println("exit dialog");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Do you really want to exit the program?",
                ButtonType.YES, ButtonType.NO);

        alert.setResizable(true);
        alert.onShownProperty().addListener(e -> {
            Platform.runLater(() -> alert.setResizable(false));
        });

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            Platform.exit();
        }
    }

}
