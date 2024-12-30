package org.example.java5;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ship {
    private int size;
    private Point2D startPoint;
    private boolean isHorizontal;
    private Rectangle[] shipParts;  // Reprezentacja części statku na planszy

    public Ship(int size, Point2D startPoint, boolean isHorizontal) {
        this.size = size;
        this.startPoint = startPoint;
        this.isHorizontal = isHorizontal;
        this.shipParts = new Rectangle[size];

        // Tworzymy części statku
        for (int i = 0; i < size; i++) {
            int x = (int) startPoint.getX() + (isHorizontal ? i : 0);
            int y = (int) startPoint.getY() + (isHorizontal ? 0 : i);
            shipParts[i] = new Rectangle(x * 40, y * 40, 40, 40);
            shipParts[i].setFill(Color.BLUE);
        }
    }

    // Metoda, która ustawia statek na planszy
    public void setPosition(Point2D newStartPoint) {
        this.startPoint = newStartPoint;
        for (int i = 0; i < size; i++) {
            int x = (int) newStartPoint.getX() + (isHorizontal ? i : 0);
            int y = (int) newStartPoint.getY() + (isHorizontal ? 0 : i);
            shipParts[i].setX(x * 40);
            shipParts[i].setY(y * 40);
        }
    }

    public Rectangle[] getShipParts() {
        return shipParts;
    }

    public boolean checkHit(Point2D point) {
        for (Rectangle part : shipParts) {
            if (part.getBoundsInParent().contains(point.getX(), point.getY())) {
                part.setFill(Color.RED);  // Zmiana koloru, gdy trafimy statek
                return true;
            }
        }
        return false;
    }

    public boolean isSunk() {
        for (Rectangle part : shipParts) {
            if (part.getFill() != Color.RED) {
                return false;
            }
        }
        return true;  // Jeśli wszystkie części statku zostały trafione
    }
}
