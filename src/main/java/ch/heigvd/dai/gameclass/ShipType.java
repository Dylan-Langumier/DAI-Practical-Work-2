package ch.heigvd.dai.gameclass;

public enum ShipType {

    NONE(0),
    CARRIER (5),
    BATTLESHIP 	(4),
    DESTROYER 	(3),
    SUBMARINE 	(3),
    PATROLER 	(2);

    // constructor
    private ShipType(final int size) {
        this.size = size;
    }

    // internal state
    final private int size;

    public int getSize() {
        return size;
    }
}

