package environment;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Andrew on 1/1/2017.
 */
public class EnvironmentGrid {

    public ConcurrentHashMap<Long, GridCell> entityToGridCellMap;
    public ConcurrentHashMap<Long, Integer> entityToCollisionRadiusMap;

    public GridCell[][] gridArray;

    public int gridWidth, gridHeight;

    public class PlacementException extends Exception {
        public PlacementException(String m) {
            super(m);
        }
    }

    /**
     * @param gridWidth
     * @param gridHeight
     */
    public EnvironmentGrid(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        this.gridArray = new GridCell[this.gridHeight][this.gridWidth];

        for(int i=0; i < this.gridHeight; i++) {
            for(int j=0; j < this.gridWidth; j++) {
                this.gridArray[i][j] = new GridCell(j, i);
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

        if(targetX < 0 || targetX >= this.gridWidth || targetY < 0 || targetY >= this.gridHeight) {
            throw new PlacementException("Placement indices out of bounds (" + targetX + "," + targetY + " vs ("
                                        + this.gridWidth + "," + this.gridHeight + ")");
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

                if(collisionX < 0 || collisionX >= this.gridWidth || collisionY < 0 || collisionY >= this.gridHeight) {
                    continue;
                }

                this.gridArray[collisionY][collisionX].collisionRadiusOverlap += decrement ? -1 : 1;
            }
        }
    }

    public List<GridCell> findPath(GridCell start, GridCell finish) {
        ArrayList<GridCell> path = new ArrayList<>();

        boolean[][] visitedNodes = new boolean[this.gridHeight][this.gridWidth];
        Arrays.fill(visitedNodes, false);

        Set<GridCell> openCells = new HashSet<>();
        openCells.add(start);

        int[][] gScore = new int[this.gridHeight][this.gridWidth];
        Arrays.fill(gScore, -1);

        gScore[start.gridY][start.gridX] = 0;

        int[][] fScore = new int[this.gridHeight][this.gridWidth];
        Arrays.fill(fScore, -1);

        fScore[start.gridY][start.gridX] = (int)Math.abs(Math.sqrt(Math.pow((double)finish.gridX-start.gridX, 2.0) + Math.pow((double)finish.gridY-start.gridY, 2.0)));

        while(!openCells.isEmpty()) {

        }

        return path;
    }

    /**
     *
     * @return
     */
    public String debugPrintGrid() {
        StringBuilder sb = new StringBuilder();

        for(int i=0; i < this.gridHeight; i++) {
            for(int j=0; j < this.gridWidth; j++) {
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
