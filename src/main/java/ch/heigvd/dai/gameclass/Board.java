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
    this.width = width;
    this.height = height;
  }

  public Board() {
    this(10, 10);
  }

  public boolean place(ShipType ship, char x, int y, ORIENTATION orientation ){
    for(int i = 0; i < ship.getSize(); ++i){
      Cell cell = switch (orientation){
        case TOP -> getCell(x,(y + i));
        case BOTTOM -> getCell(x,(y - i));
        case RIGHT -> getCell((char)(x + i),y);
        case LEFT -> getCell((char)(x - i), y);
      };
      if(cell.getShipType() != ShipType.NONE)
        return false;
    }
    return true;
  }

  int letterToOrdinal(char letter) {
    char upper = Character.toUpperCase(letter);
    if (upper < 'A' || upper > 'Z')
      throw new IllegalArgumentException("First coordinate must be a letter (case-insensitive)");
    return upper - 'A';
  }

  Cell getCell(char letter, int y) {
    int x = letterToOrdinal(letter);
    if (x < 0 || y < 0 || x >= width || y >= height) throw new IndexOutOfBoundsException();
    return cells[x][y];
  }

  boolean allShipsSank(){
    for(int x = 0; x < width; ++x){
      for(int y = 0; y < height; ++y){
        if(cells[x][y].getShipType() != ShipType.NONE && !cells[x][y].isHit())
          return false;
      }
    }
    return true;
  }
}
