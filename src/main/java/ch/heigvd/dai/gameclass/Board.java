package ch.heigvd.dai.gameclass;

public class Board {
  final int MAX_DIM = 26;
  private Cell[][] cells;
  int width, height;

  Board(int width, int height) {
    if (width < 0 || height < 0 || width >= MAX_DIM || height >= MAX_DIM) {
      throw new IllegalArgumentException("Dimensions cannot be smaller than 0.");
    }
    this.cells = new Cell[width][height];
    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < height; ++y) cells[x][y] = new Cell();
    }
    this.width = width;
    this.height = height;
  }

  public Board() {
    this(10, 10);
  }

  public boolean place(ShipType ship, char x, int y, Orientation orientation) {
    for (int i = 0; i < ship.getSize(); ++i) {
      Cell cell;
      try {
        cell =
            switch (orientation) {
              case RIGHT -> getCell(x, (y + i));
              case LEFT -> getCell(x, (y - i));
              case TOP -> getCell((char) (x + i), y);
              case BOTTOM -> getCell((char) (x - i), y);
            };
      } catch (Exception ignore) {
        return false;
      }
      if (cell.getShipType() != ShipType.NONE) return false;
    }
    for (int i = 0; i < ship.getSize(); ++i) {
      Cell c = new Cell(ship);
      switch (orientation) {
        case RIGHT -> setCell(x, (y + i), c);
        case LEFT -> setCell(x, (y - i), c);
        case TOP -> setCell((char) (x + i), y, c);
        case BOTTOM -> setCell((char) (x - i), y, c);
      }
    }
    return true;
  }

  int letterToOrdinal(char letter) {
    char upper = Character.toUpperCase(letter);
    if (upper < 'A' || upper > 'Z')
      throw new IllegalArgumentException("First coordinate must be a letter (case-insensitive)");
    return upper - 'A';
  }

  public Cell getCell(char letter, int y) {
    int x = letterToOrdinal(letter);
    y = y - 1;
    if (x < 0 || y < 0 || x >= width || y >= height) throw new IndexOutOfBoundsException();
    return cells[x][y];
  }

  public void setCell(char letter, int y, Cell c) {
    int x = letterToOrdinal(letter);
    y = y - 1;
    if (x < 0 || y < 0 || x >= width || y >= height) throw new IndexOutOfBoundsException();
    cells[x][y] = c;
  }

  boolean allShipsSank() {
    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < height; ++y) {
        if (cells[x][y].getShipType() != ShipType.NONE && !cells[x][y].isHit()) return false;
      }
    }
    return true;
  }

  public String toString() {
    StringBuilder str = new StringBuilder();
    // number header
    str.append(' '); // space for letters
    for (int y = 1; y <= height; ++y) {
      str.append(' ').append(y);
    }
    str.append('\n');

    // content
    for (int i = 0; i < width; ++i) {
      char x = (char) ('A' + i);
      str.append(x);
      for (int y = 1; y <= height; ++y) {
        Cell cell = getCell(x, y);
        str.append(' ');
        if (cell.isHit()) {
          if (cell.getShipType() == ShipType.NONE) str.append('@');
          else str.append('X');
        } else str.append(cell.getShipType().getSize());
      }
      str.append('\n');
    }
    return str.toString();
  }
}
