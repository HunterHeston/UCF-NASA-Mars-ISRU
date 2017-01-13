package entity;

import algo.AStar;
import com.bluecatcode.junit.shaded.org.apache.commons.lang3.ArrayUtils;
import environment.GridCell;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Andrew on 1/1/2017.
 */
public class EnvironmentGridEntity {
    final static Logger logger = Logger.getLogger(EnvironmentGridEntity.class);

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
    public EnvironmentGridEntity(int gridWidth, int gridHeight) {
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
     * @return
     * @throws PlacementException
     */
    public boolean placeEntity(long hlaID, int targetX, int targetY, int collisionRadius) throws PlacementException {
        if(entityToGridCellMap.containsKey(hlaID)) {
            throw new PlacementException("Entity with id " + hlaID + " already exists in grid");
        }

        checkGridIndex(targetX, targetY);

        GridCell cell = this.gridArray[targetY][targetX];

        //  Make sure the cell is open
        if(this.hasCollisionsWithinRadius(collisionRadius, targetX, targetY)) {
            throw new PlacementException("Occupied Cell: " + cell);
        }

        cell.hlaID = hlaID;

        this.entityToGridCellMap.put(hlaID, cell);
        this.entityToCollisionRadiusMap.put(hlaID, collisionRadius);
        this.applyCollisions(collisionRadius, targetX, targetY, false);

        return true;
    }

    public GridCell[] findPath(GridCell start, GridCell finish) {
        AStar a = new AStar<GridCell>();

        List<GridCell> path = a.pathFromGrid(this.gridArray,
                                             new int[] {start.gridY, start.gridX},
                                             new int[] {finish.gridY, finish.gridX});

        if(!path.isEmpty()) {
            GridCell[] pathArray = (GridCell[]) path.toArray();
            ArrayUtils.reverse(pathArray);

            GridCell[] finalPathArray = new GridCell[pathArray.length-1];
            for(int i=0; i<pathArray.length; i++) {
                finalPathArray[i] = pathArray[i];
            }

            return finalPathArray;
        }

        return null;
    }

    public boolean gridMove(long hlaID, int targetX, int targetY) throws PlacementException {
        GridCell cell = entityToGridCellMap.get(hlaID);
        if(cell == null) {
            logger.error("hlaID=" + hlaID + " Not found in gridMove");
            return false;
        }

        checkGridIndex(targetX, targetY);
        GridCell target = this.gridArray[targetY][targetX];

        int collisionRadius = entityToCollisionRadiusMap.get(hlaID);
        applyCollisions(collisionRadius, cell.gridX, cell.gridY, true);

        //  Thread danger here, applyCollisions changes shared memory.....
        if(hasCollisionsWithinRadius(collisionRadius, targetX, targetY)) {
            applyCollisions(collisionRadius, cell.gridX, cell.gridY, false);
            return false;
        }

        cell.removeEntity();
        target.placeEntity(hlaID);
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

    public boolean hasCollisionsWithinRadius(int collisionRadius, int targetX, int targetY) {
        collisionRadius -= 1;

        if(collisionRadius == 0) {
            GridCell cell = this.gridArray[targetY][targetX];
            return cell.isBlocked() || cell.isOccupied();
        }

        for(int i=-collisionRadius; i<=collisionRadius; i++) {
            for(int j=-collisionRadius; j<=collisionRadius; j++) {
                int collisionX = targetX+i;
                int collisionY = targetY+j;

                if(collisionX < 0 || collisionX >= this.gridWidth || collisionY < 0 || collisionY >= this.gridHeight) {
                    continue;
                }

                GridCell cell = this.gridArray[collisionY][collisionX];
                if(cell.isOccupied() || cell.isBlocked()) {
                    return true;
                }

            }
        }

        return false;
    }

    public static void printGrid(GridCell[][] grid) {
        printGridWithPath(grid, null, null, null);
    }

    /**
     *
     * @return
     */
    public static void printGridWithPath(GridCell[][] grid, List<GridCell> path, GridCell start, GridCell end) {
        Set<GridCell> pathSet = new HashSet<>();

        if(path!=null) {
            pathSet.addAll(path);
        }

        for(int i = 0; i < grid.length; i++) {
            StringBuilder sb = new StringBuilder();

            for(int j = 0; j < grid[0].length; j++) {
                GridCell cell = grid[i][j];
                if(cell.isBlocked()) {
                    sb.append("B ");
                } else if (cell.isOccupied()) {
                    sb.append("E ");
                } else if(cell == start) {
                    sb.append("S ");
                } else if(cell == end) {
                    sb.append("E ");
                } else if(pathSet.contains(cell)){
                    sb.append("X ");
                } else {
                    sb.append("_ ");
                }
            }

            logger.debug(sb);
        }
    }

    public void checkGridIndex(int targetX, int targetY) throws PlacementException {
        if(targetX < 0 || targetX >= this.gridWidth || targetY < 0 || targetY >= this.gridHeight) {
            throw new PlacementException("Placement indices out of bounds (" + targetX + "," + targetY + " vs ("
                    + this.gridWidth + "," + this.gridHeight + ")");
        }
    }

}
