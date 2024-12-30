package org.example.java5;

import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameBoard {
    private static final int BOARD_SIZE = 10;  // Rozmiar planszy (10x10)
    private Rectangle[][] grid;
    private Ship[] playerShips;  // Statki gracza

    public GameBoard() {
        this.grid = new Rectangle[BOARD_SIZE][BOARD_SIZE];
        this.playerShips = new Ship[5];  // Załóżmy, że gracz ma 5 statków do ustawienia

        // Tworzymy planszę
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                grid[i][j] = new Rectangle(40, 40);
                grid[i][j].setFill(Color.LIGHTGRAY);
                grid[i][j].setStroke(Color.BLACK);
            }
        }
    }

    // Rysowanie planszy w GridPane
    public void drawBoard(GridPane pane) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                pane.add(grid[i][j], i, j);
            }
        }
    }

    // Ustawianie statku na planszy
    public boolean placeShip(int size, Point2D startPoint, boolean isHorizontal) {
        if (isValidPlacement(size, startPoint, isHorizontal)) {
            Ship newShip = new Ship(size, startPoint, isHorizontal);
            playerShips[size - 1] = newShip;  // Przechowywanie statku
            for (Rectangle part : newShip.getShipParts()) {
                grid[(int) (part.getX() / 40)][(int) (part.getY() / 40)].setFill(Color.BLUE);
            }
            return true;
        }
        return false;
    }

    // Sprawdzanie, czy statek może zostać ustawiony na tej pozycji
    private boolean isValidPlacement(int size, Point2D startPoint, boolean isHorizontal) {
        // Sprawdzamy, czy statek nie wychodzi poza planszę
        if (isHorizontal && startPoint.getX() + size > BOARD_SIZE) {
            return false;
        }
        if (!isHorizontal && startPoint.getY() + size > BOARD_SIZE) {
            return false;
        }

        // Sprawdzamy, czy w miejscu statku nie ma innych statków
        for (int i = 0; i < size; i++) {
            int x = (int) startPoint.getX() + (isHorizontal ? i : 0);
            int y = (int) startPoint.getY() + (isHorizontal ? 0 : i);
            if (grid[x][y].getFill() == Color.BLUE) {
                return false;  // Jeśli w tym miejscu jest już statek
            }
        }
        return true;
    }

    public String getBoardData() {
        StringBuilder boardData = new StringBuilder();
        for (Ship ship : playerShips) {
            if (ship != null) {
                for (Rectangle part : ship.getShipParts()) {
                    boardData.append(part.getX() / 40).append(",").append(part.getY() / 40).append(";");
                }
                boardData.append("|");
            }
        }
        return boardData.toString();  // Zwracamy dane w formacie tekstowym
    }
}
