package ch.heigvd.dai.gameclass;

public class Cell {
  private ShipType type;
  private boolean isHit;

  public Cell(ShipType type) {
    this.type = type;
    this.isHit = false;
  }

  protected Cell() {
    this(ShipType.NONE);
  }

  protected ShipType getShipType() {
    return type;
  }

  protected boolean isHit() {
    return isHit;
  }

  public void hit() {
    isHit = true;
  }

  public String toString() {
    if (type == ShipType.NONE) {
      if (isHit) return "0";
      return "~";
    }
    if (isHit) return "X";
    return String.valueOf(type.getSize());
  }
}
