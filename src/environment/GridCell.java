package environment;

/**
 * Created by Andrew on 1/1/2017.
 */
public class GridCell {

    public boolean blocked;
    public int gridX, gridY, collisionRadiusOverlap;
    public long hlaID;

    public GridCell(int gridX, int gridY) {
        this.collisionRadiusOverlap = 0;
        this.gridX = gridX;
        this.gridY = gridY;

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
        return "Cell: (" + this.gridX + "," + this.gridY + "): B=" + this.isBlocked() + " O=" + this.isOccupied();
    }

}
