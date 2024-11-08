package ch.heigvd.dai.gameclass;

public class Board {
  final int MAX_DIM = 26;
  private Cell[][] cells;
  int width, height;

  Board(int width, int height) {
    if(width < 0 || height < 0 || width >= MAX_DIM || height >= MAX_DIM) {
      throw new IllegalArgumentException("Dimensions cannot be smaller than 0.");
    }
    this.cells = new Cell[width][height];
    this.width = width;
    this.height = height;
  }

  public Board(){
    this(10,10);
  }

  int letterToOrdinal(char letter) {
    char upper = Character.toUpperCase(letter);
    if(upper < 'A' || upper > 'Z')
      throw new IllegalArgumentException("First coordinate must be a letter (case-insensitive)");
    return upper - 'A';

  }

  Cell getCell(char letter, int y) {
    int x = letterToOrdinal(letter);
    if(x < 0 || y < 0 || x >= width || y >= height)
      throw new IndexOutOfBoundsException();
    return cells[x][y];
  }
}
