package ch.heigvd.dai.gameclass;

public class Cell {
  private final ShipType type;
  private boolean isHit;

  public Cell(ShipType type) {
    this.type = type;
    this.isHit = false;
  }

  protected Cell() {
    this(ShipType.NONE);
  }

  public ShipType getShipType() {
    return type;
  }

  protected boolean isHit() {
    return isHit;
  }

  public void hit() {
    isHit = true;
  }

  public boolean hasShip() {
    return type != ShipType.NONE;
  }

  public String toString() {
    if (!hasShip()) {
      return isHit ? "0" : "~";
    }
    if (isHit) {
      return "X";
    }
    return String.valueOf(type.getSize());
  }
}
