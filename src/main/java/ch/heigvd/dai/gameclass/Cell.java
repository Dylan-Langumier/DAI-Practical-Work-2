package ch.heigvd.dai.gameclass;

class Cell {
  enum SHIP_TYPE {
    /* 0 */ NONE,
    /* Design decision to have the enum value match the length of the ship it represents */
    /* 1 */ unused,

    /* 2 */ DESTROYER,
    /* 3 */ CRUISER,
    /* 4 */ BATTLESHIP,
    /* 5 */ CARRIER
  }

  private SHIP_TYPE type;
  private boolean isHit;

  protected Cell(SHIP_TYPE type) {
    this.type = type;
    this.isHit = false;
  }

  protected Cell() {
    this(SHIP_TYPE.NONE);
  }

  protected SHIP_TYPE getShipType() {
    return type;
  }

  protected int getShipLength() {
    return type.ordinal();
  }

  protected boolean isHit() {
    return isHit;
  }
}
