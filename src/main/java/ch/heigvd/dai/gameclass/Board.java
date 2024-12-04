package ch.heigvd.dai.gameclass;

import java.util.ArrayList;

public class Board {
  final int MAX_DIM = 26;
  private final Cell[][] cells;
  private final ArrayList<Cell> boats = new ArrayList<>();

  Board(int width, int height) {
    if (width < 0 || height < 0 || width > MAX_DIM || height > MAX_DIM) {
      throw new IllegalArgumentException(
          "Dimensions cannot be smaller than 0 or higher than " + MAX_DIM + ".");
    }
    this.cells = new Cell[width][height];
    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < height; ++y) {
        cells[x][y] = new Cell();
      }
    }
  }

  public Board() {
    this(10, 10);
  }

  public boolean place(ShipType ship, char x, int y, Orientation orientation) {
    for (int i = 0; i < ship.getSize(); ++i) {
      char newX = x;
      int newY = y;
      Cell cell;
      try {
        switch (orientation) {
          case RIGHT -> newY = y + i;
          case LEFT -> newY = y - i;
          case BOTTOM -> newX = (char) (x + i);
          case TOP -> newX = (char) (x - i);
        }
        cell = getCell(newX, newY);
      } catch (Exception ignore) {
        return false;
      }
      if (cell.getShipType() != ShipType.NONE) {
        return false;
      }
    }
    for (int i = 0; i < ship.getSize(); ++i) {
      Cell c = new Cell(ship);
      char newX = x;
      int newY = y;
      switch (orientation) {
        case RIGHT -> newY = y + i;
        case LEFT -> newY = y - i;
        case BOTTOM -> newX = (char) (x + i);
        case TOP -> newX = (char) (x - i);
      }
      setCell(newX, newY, c);
      boats.add(c);
    }
    return true;
  }

  private int letterToOrdinal(char letter) {
    char upper = Character.toUpperCase(letter);
    if (upper < 'A' || upper > 'Z') {
      throw new IllegalArgumentException("First coordinate must be a letter (case-insensitive)");
    }
    return upper - 'A';
  }

  private boolean isInBounds(int x, int y) {
    return (x < 0 || y < 0 || x > cells.length || y > cells[0].length);
  }

  public Cell getCell(char letter, int y) {
    int x = letterToOrdinal(letter);
    y = y - 1;
    if (isInBounds(x, y)) {
      throw new IndexOutOfBoundsException();
    }
    return cells[x][y];
  }

  public void setCell(char letter, int y, Cell c) {
    int x = letterToOrdinal(letter);
    y = y - 1;
    if (isInBounds(x, y)) {
      throw new IndexOutOfBoundsException();
    }
    cells[x][y] = c;
  }

  boolean allShipsSank() {
    for (Cell c : boats) {
      if (!c.isHit()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    // number header
    str.append(' '); // space for letters
    for (int y = 1; y <= cells[0].length; ++y) {
      str.append(' ').append(y);
    }
    str.append('\n');

    // content
    for (int i = 0; i < cells.length; ++i) {
      char x = (char) ('A' + i);
      str.append(x);
      for (int y = 1; y <= cells[0].length; ++y) {
        str.append(' ').append(getCell(x, y).toString());
      }
      str.append('\n');
    }
    return str.toString();
  }
}
