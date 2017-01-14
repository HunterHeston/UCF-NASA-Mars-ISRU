package execution;

import state.EnvironmentGridState;
import environment.GridCell;
import org.apache.log4j.Logger;
import siso.smackdown.utilities.Vector3;

/**
 *
 * TODO: Test Class
 * Created by Andrew on 1/1/2017.
 */
public class EnvironmentGridExecution {
    final static Logger logger = Logger.getLogger(EnvironmentGridExecution.class);
    public Vector3 gridOrigin;
    public double gridCellSize;
    public int gridHeight, gridWidth;
    public EnvironmentGridState grid;

    public EnvironmentGridExecution(EnvironmentGridState grid, Vector3 gridOrigin,
                                    double gridCellSize) {
        this.grid = grid;
        this.gridOrigin = gridOrigin;
        this.gridCellSize = gridCellSize;
        this.gridHeight = grid.gridHeight;
        this.gridWidth = grid.gridWidth;
    }

    public EnvironmentGridExecution(Vector3 gridOrigin, double gridCellSize, int gridHeight, int gridWidth) {
        this.gridOrigin = gridOrigin;
        this.gridCellSize = gridCellSize;
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;

        this.grid = new EnvironmentGridState(this.gridWidth, this.gridHeight);
    }

    public boolean receivePlaceEntityInteraction(int hlaID, int targetX,
                                                 int targetY, int collisionRadius) {
        try {
            return this.grid.placeEntity(hlaID, targetX, targetY, collisionRadius);
        } catch (EnvironmentGridState.PlacementException e) {
            System.out.print("Unhandled Exception placing state " + hlaID);
            e.printStackTrace();
        }

        return false;
    }

    public boolean receiveGridMoveInteraction(long hlaID, int targetX, int targetY) {
        try {
            return this.grid.gridMove(hlaID, targetX, targetY);
        } catch (EnvironmentGridState.PlacementException e) {
            System.out.print("Unhandled Exception placing state " + hlaID);
            e.printStackTrace();
        }

        return false;
    }

    public GridCell[] receivePathFindingInteraction(long hlaID, int targetX, int targetY) {
        GridCell start = this.grid.getCellFromHLAId(hlaID);
        GridCell end = this.grid.getCell(targetX, targetY);
        logger.debug(start + " " + end);
        return this.grid.findPath(start, end);
    }

}
