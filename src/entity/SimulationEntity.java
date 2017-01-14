package entity;

import org.apache.log4j.Logger;

import java.util.Queue;

/**
 *
 * This is the base class for all entities within the simulation.
 * An entity class acts to encapsulate data pertaining to the entity.
 * An entity class also provides methods to transition between states.
 * Each of these state transition methods should strictly enforce state
 * sequences, preferring runtime errors (via assertion) as an enforcement
 * method.
 *
 * Created by Andrew on 1/9/2017.
 */
public class SimulationEntity {
    final static Logger logger = Logger.getLogger(SimulationEntity.class);
    public boolean isOnNewPath = false;

    public enum MovementState {
        Stopped,
        InMotion,
        PathFinding,
        GridMovement,
    }
    public static class GridIndex {

        public int row, col;
        public GridIndex(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if(o == this) return true;
            if(o == null) return false;
            if(getClass() != o.getClass()) return false;
            GridIndex oo = (GridIndex) o;

            return this.row == oo.row && this.col == oo.col;
        }

        @Override
        public String toString() {
            return "[" + this.row + "][" + this.col + "]";
        }

    }

    public MovementState movementState = MovementState.Stopped;

    public GridIndex gridIndex;
    public GridIndex targetGridIndex;
    public GridIndex finalGridIndex;

    public Queue<GridIndex> path;
    public double movementSpeed;
    public double gridCellSize;

    public double[] position;

    public int collisionRadius;

    public SimulationEntity(int gridX, int gridY, int collisionRadius) {
        this(gridX, gridY);
        this.collisionRadius = collisionRadius;
    }
    public SimulationEntity(int gridX, int gridY) {
        this.gridIndex = new GridIndex(gridY, gridX);
        this.movementSpeed = 0;
        this.gridCellSize = 1.0;

        this.position = new double[3];
        this.position[0] = gridX * this.gridCellSize;
        this.position[1] = gridY * this.gridCellSize;

        this.collisionRadius = 0;
    }

    public SimulationEntity(int gridX, int gridY, double movementSpeed, double gridCellSize) {
        this(gridX, gridY);
        this.movementSpeed = movementSpeed;
        this.gridCellSize = gridCellSize;
    }

    public void beginPathFinding(int gridX, int gridY) {
        assert this.movementState == MovementState.Stopped;
        assert this.gridIndex.row != gridY && this.gridIndex.col != gridX;

        this.finalGridIndex = new GridIndex(gridY, gridX);
        this.movementState = MovementState.PathFinding;
        logger.debug("State transition: Stopped -> Pathfinding");
    }

    public void nullPathFindingResponse() {
        assert this.movementState == MovementState.PathFinding;

        this.targetGridIndex = null;
        this.finalGridIndex = null;
        this.movementState = MovementState.Stopped;
        logger.debug("State transition: PathFinding -> Stopped");
    }

    public void beginTransit(Queue<GridIndex> path) {
        assert path != null && !path.isEmpty();
        assert this.movementState == MovementState.PathFinding;

        this.path = path;
        this.targetGridIndex = path.poll();
        this.movementState = MovementState.InMotion;
        this.isOnNewPath = true;
        logger.debug("State transition: PathFinding -> InMotion");
    }

    public void gridMovement() {
        assert this.movementState == MovementState.InMotion;
        this.movementState = MovementState.GridMovement;
        logger.debug("State transition: InMotion -> GridMovement");
    }

    public void gridMovementResponse(boolean success) {
        assert this.movementState == MovementState.GridMovement;

        if(success) {
            continueTransit();
        } else {
            stopTransit();
        }
    }

    public void continueTransit() {
        assert this.movementState == MovementState.GridMovement;
        assert path != null && !path.isEmpty();
        assert this.targetGridIndex != null;

        this.targetGridIndex = path.poll();
        this.movementState = MovementState.InMotion;
        logger.debug("State transition GridMovement -> InMotion");
    }

    public void stopTransit() {
        this.targetGridIndex = null;
        this.finalGridIndex = null;
        this.path.clear();

        this.movementState = MovementState.Stopped;
        logger.debug("State transition GridMovement -> Stopped " + this.gridIndex);
    }

    //  Returns true when we have arrived at target
    public boolean moveTowardsTarget() {
        assert this.movementState == MovementState.InMotion;
        assert this.targetGridIndex != null;

        //  TODO: Convert to 3D
        double ux, uy, dx, dy, l;

        //  Vector length from gridIndex to targetGridIndex
        l = Math.sqrt(Math.pow(targetGridIndex.col-gridIndex.col, 2) + Math.pow(targetGridIndex.row-gridIndex.row, 2));

        //  Unit vector components from gridIndex to targetGridIndex
        ux = ((double)targetGridIndex.col-gridIndex.col) / l;
        uy = ((double)targetGridIndex.row-gridIndex.row) / l;

        dx = ux * this.movementSpeed;
        dy = uy * this.movementSpeed;

        double actX, actY, targetGridX, targetGridY;

        //  New positions
        actX = this.position[0] + dx;
        actY = this.position[1] + dy;

        //  Target position
        targetGridX = targetGridIndex.col * this.gridCellSize;
        targetGridY = targetGridIndex.row * this.gridCellSize;

        //  Make sure we only move to the target at most
        actX = (dx > 0 && actX >= targetGridX) || (dx < 0 && actX <= targetGridX) ? targetGridX : actX;
        actY = (dy > 0 && actY >= targetGridY) || (dy < 0 && actY <= targetGridY) ? targetGridY : actY;

        //  Update our pos vector
        this.position[0] = actX;
        this.position[1] = actY;

        //  Return true when we have arrived
        if(actX == targetGridX && actY == targetGridY) {
            this.gridIndex = this.targetGridIndex;
            return true;
        }

        return false;
    }
}
