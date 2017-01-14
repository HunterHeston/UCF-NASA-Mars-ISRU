package environment;

/**
 * Created by Andrew on 1/1/2017.
 */
public class GridCell {

    public boolean blocked;
    public int col, row, collisionRadiusOverlap;
    public long hlaID;

    public GridCell(int col, int row) {
        this.collisionRadiusOverlap = 0;
        this.col = col;
        this.row = row;

        this.hlaID = Integer.MAX_VALUE;
        this.blocked = false;
    }

    public boolean isBlocked() {
        return this.blocked;
    }

    public boolean isOccupied() {
        return this.hlaID != Integer.MAX_VALUE || this.collisionRadiusOverlap > 0;
    }

    public void removeEntity() {
        this.hlaID = Integer.MAX_VALUE;
    }

    public void placeEntity(long hlaID) {
        assert this.hlaID == Integer.MAX_VALUE;
        this.hlaID = hlaID;
    }

    public String toString() {
        return "Cell: [" + this.row + "][" + this.col + "]: B=" + this.isBlocked() + " O=" + this.isOccupied();
    }

}
