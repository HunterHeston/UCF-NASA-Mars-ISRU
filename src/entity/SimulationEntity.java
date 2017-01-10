package entity;

import org.apache.log4j.Logger;

import java.util.Queue;

/**
 * Created by Andrew on 1/9/2017.
 */
public class SimulationEntity {
    final static Logger logger = Logger.getLogger(SimulationEntity.class);

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
            return "[" + this.row + "," + this.col + "]";
        }
    }

    public MovementState movementState = MovementState.Stopped;
    public GridIndex gridIndex;

    public GridIndex targetGridIndex;
    public Queue<GridIndex> path;

    public SimulationEntity(int gridX, int gridY) {
        this.gridIndex = new GridIndex(gridX, gridY);
    }

    public void beginPathFinding(int gridX, int gridY) throws Exception {
        assert this.movementState == MovementState.Stopped;
        assert this.gridIndex.row != gridY && this.gridIndex.col != gridX;

        this.movementState = MovementState.PathFinding;
        logger.debug("State transition: Stopped -> Pathfinding");
    }

    public void nullPathFindingResponse() {
        assert this.movementState == MovementState.PathFinding;

        this.targetGridIndex = null;
        this.movementState = MovementState.Stopped;
        logger.debug("State transition: PathFinding -> Stopped");
    }

    public void beginTransit(Queue<GridIndex> path) {
        assert path != null && !path.isEmpty();
        assert this.movementState == MovementState.PathFinding;

        this.path = path;
        this.targetGridIndex = path.poll();
        this.movementState = MovementState.InMotion;
        logger.debug("State transition: PathFinding -> InMotion");
    }

    public void gridMovement() {
        assert this.movementState == MovementState.InMotion;
        this.movementState = MovementState.GridMovement;
        logger.debug("State transition: InMotion -> GridMovement");
    }

    public void walkPath() {
        assert this.movementState == MovementState.GridMovement;

        this.gridIndex = this.targetGridIndex;
        this.targetGridIndex = path.poll();

        if(this.targetGridIndex == null) {
            this.movementState = MovementState.Stopped;
            logger.debug("State transition: GridMovement -> Stopped");
        }
    }
}
