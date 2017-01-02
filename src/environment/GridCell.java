package environment;

/**
 * Created by Andrew on 1/1/2017.
 */
public class GridCell {

    public boolean blocked;
    public int gridX, gridY, collisionRadiusOverlap;
    public EnvironmentGrid grid;
    public Long hlaID;

    public GridCell(int gridX, int gridY, EnvironmentGrid grid) {
        this.collisionRadiusOverlap = 0;
        this.gridX = gridX;
        this.gridY = gridY;
        this.grid = grid;

        this.hlaID = null;
        this.blocked = false;
    }

    public boolean isBlocked() {
        return this.blocked || this.collisionRadiusOverlap > 0;
    }

    public boolean isOccupied() {
        return this.hlaID != null;
    }

}
