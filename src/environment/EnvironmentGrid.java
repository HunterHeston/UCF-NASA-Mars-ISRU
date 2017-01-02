package environment;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Andrew on 1/1/2017.
 */
public class EnvironmentGrid {

    public ConcurrentHashMap<Long, GridCell> entityToGridCellMap;
    public ConcurrentHashMap<Long, Integer> entityToCollisionRadiusMap;

    public EnvironmentFederate environmentFederate;
    public GridCell[][] gridArray;

    public class PlacementException extends Exception {
        public PlacementException(String m) {
            super(m);
        }
    }

    /**
     *
     * @param environmentFederate
     */
    public EnvironmentGrid(EnvironmentFederate environmentFederate) {
        this.environmentFederate = environmentFederate;
        this.gridArray = new GridCell[this.environmentFederate.gridHeight][this.environmentFederate.gridWidth];

        for(int i=0; i < this.environmentFederate.gridHeight; i++) {
            for(int j=0; j < this.environmentFederate.gridWidth; j++) {
                this.gridArray[i][j] = new GridCell(j, i, this);
            }
        }

        this.entityToGridCellMap = new ConcurrentHashMap<>();
        this.entityToCollisionRadiusMap = new ConcurrentHashMap<>();
    }

    /**
     * 
     * @param hlaID
     * @param targetX
     * @param targetY
     * @param collisionRadius
     * @return
     * @throws PlacementException
     */
    public boolean placeEntity(long hlaID, int targetX, int targetY, int collisionRadius) throws PlacementException {
        if(entityToGridCellMap.contains(hlaID)) {
            throw new PlacementException("Entity with id " + hlaID + " already exists in grid");
        }

        if(targetX < 0 || targetX >= this.environmentFederate.gridWidth || targetY < 0 || targetY >= this.environmentFederate.gridHeight) {
            throw new PlacementException("Placement indices out of bounds (" + targetX + "," + targetY + " vs ("
                                        + this.environmentFederate.gridWidth + "," + this.environmentFederate.gridHeight + ")");
        }

        GridCell cell = this.gridArray[targetY][targetX];
        cell.hlaID = hlaID;

        this.entityToGridCellMap.put(hlaID, cell);
        this.entityToCollisionRadiusMap.put(hlaID, collisionRadius);
        this.applyCollisions(collisionRadius, targetX, targetY, false);

        return true;
    }

    /**
     *
     * Mark collisions in the area surrounding the target.  Use decrement to "un-collide"
     *
     * @param collisionRadius
     * @param targetX
     * @param targetY
     * @param decrement
     */
    public void applyCollisions(int collisionRadius, int targetX, int targetY, boolean decrement) {
        collisionRadius -= 1;

        for(int i=-collisionRadius; i<=collisionRadius; i++) {
            for(int j=-collisionRadius; j<=collisionRadius; j++) {
                int collisionX = targetX+i;
                int collisionY = targetY+j;

                if(collisionX < 0 || collisionX >= this.environmentFederate.gridWidth || collisionY < 0 || collisionY >= this.environmentFederate.gridHeight) {
                    continue;
                }

                this.gridArray[collisionY][collisionX].collisionRadiusOverlap += decrement ? -1 : 1;
            }
        }
    }

    /**
     *
     * @return
     */
    public String debugPrintGrid() {
        StringBuilder sb = new StringBuilder();

        for(int i=0; i < this.environmentFederate.gridHeight; i++) {
            for(int j=0; j < this.environmentFederate.gridWidth; j++) {
                GridCell cell = this.gridArray[i][j];
                sb.append("(" + cell.gridX + "," + cell.gridY + ")");

                char a = cell.isBlocked() ? 'B' : (cell.isOccupied() ? 'E' : ' ');
                sb.append("{" + a + "}|");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

}
