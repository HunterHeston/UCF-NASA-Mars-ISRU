package state;

import algo.AStar;
import environment.GridCell;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Andrew on 1/1/2017.
 */
public class EnvironmentGridState {
    final static Logger logger = Logger.getLogger(EnvironmentGridState.class);

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
    public EnvironmentGridState(int gridWidth, int gridHeight) {
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

    public GridCell getCellFromHLAId(long hlaID) {
        return this.entityToGridCellMap.get(hlaID);
    }

    public GridCell getCell(int gridX, int gridY) {
        try {
            this.checkGridIndex(gridX, gridY);
            return this.gridArray[gridY][gridX];
        } catch (PlacementException e) {
            e.printStackTrace();
        }

        return null;
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
                                             new int[] {start.row, start.col},
                                             new int[] {finish.row, finish.col});

        if(!path.isEmpty()) {
            Collections.reverse(path);
            GridCell[] pathArray = Arrays.copyOfRange(path.toArray(), 1, path.size(), GridCell[].class);
            return pathArray;
        }

        return null;
    }

    public boolean gridMove(long hlaID, int targetX, int targetY) throws PlacementException {
        GridCell cell = entityToGridCellMap.get(hlaID);
        if(cell == null) {
            logger.error("hlaID=" + hlaID + " Not found in gridMove");
            return false;
        }

        if(Math.abs(cell.col-targetX) > 1 || Math.abs(cell.row-targetY) > 1) {
            logger.error("Out of bounds attempt to move from " + cell + " to " + targetX + "," + targetY);
            return false;
        }

        checkGridIndex(targetX, targetY);
        GridCell target = this.gridArray[targetY][targetX];

        int collisionRadius = entityToCollisionRadiusMap.get(hlaID);
        applyCollisions(collisionRadius, cell.col, cell.row, true);

        //  Thread danger here, applyCollisions changes shared memory.....
        if(hasCollisionsWithinRadius(collisionRadius, targetX, targetY)) {
            applyCollisions(collisionRadius, cell.col, cell.row, false);
            logger.error("Invalid attempt to move to a collision grid");

            collisionRadius -= 1;
            for(int i=-collisionRadius; i<=collisionRadius; i++) {
                for(int j=-collisionRadius; j<=collisionRadius; j++) {
                    int collisionX = targetX+i;
                    int collisionY = targetY+j;

                    if(collisionX < 0 || collisionX >= this.gridWidth || collisionY < 0 || collisionY >= this.gridHeight) {
                        continue;
                    }

                    logger.debug("Collision: " + this.gridArray[collisionY][collisionX]);

                }
            }
            return false;
        }

        cell.removeEntity();
        target.placeEntity(hlaID);
        entityToGridCellMap.put(hlaID, target);
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
    public static void printGridWithPath(GridCell[][] grid, GridCell[] path, GridCell start, GridCell end) {
        Set<GridCell> pathSet = new HashSet<>();

        if(path!=null) {
            pathSet.addAll(Arrays.asList(path));
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
