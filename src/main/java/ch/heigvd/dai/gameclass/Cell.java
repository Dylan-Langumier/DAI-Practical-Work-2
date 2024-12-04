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
}
