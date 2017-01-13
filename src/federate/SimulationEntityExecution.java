package federate;

import entity.SimulationEntity;
import org.apache.log4j.Logger;

import java.util.Queue;

/**
 * Created by Andrew on 1/12/2017.
 */
public class SimulationEntityExecution {
    final static Logger logger = Logger.getLogger(SimulationEntityExecution.class);

    public double gridCellSize;
    public double movementSpeed;

    public SimulationEntity simulationEntity;

    public SimulationEntityExecution(SimulationEntity simulationEntity) {
        this.simulationEntity = simulationEntity;

        this.gridCellSize = 1;
        this.movementSpeed = 0.2;
    }

    public void activeUpdate() {
        movementUpdate();

        if(simulationEntity.movementState == SimulationEntity.MovementState.Stopped) {
            this.activeEntityUpdate();
        }

        this.staticUpdate();
    }

    public void movementUpdate() {

        if(this.simulationEntity.movementState == SimulationEntity.MovementState.InMotion) {
            //  We need to gridMove on the first of each path
            if(this.simulationEntity.isOnNewPath) {
                this.simulationEntity.gridMovement();
                this.simulationEntity.isOnNewPath = false;
            } else {
                boolean targetArrival = this.simulationEntity.moveTowardsTarget();
                logger.debug("In motion and moved towards target arrived=" + targetArrival + " new pos=" + simulationEntity.position[0] + "," + simulationEntity.position[1]);

                if (targetArrival) {
                    if (!this.simulationEntity.path.isEmpty()) {
                        this.simulationEntity.gridMovement();
                    } else {
                        this.simulationEntity.stopTransit();
                    }
                }
            }
        }
    }

    public void staticUpdate() {
    }

    public void activeEntityUpdate() {
    }

    public void receivePathFindingInteractionResponse(Queue<SimulationEntity.GridIndex> path) {
        logger.debug(path);
        if(path == null || path.isEmpty()) {
            this.simulationEntity.nullPathFindingResponse();
        } else {
            this.simulationEntity.beginTransit(path);
        }
    }

    public void receiveGridMovementInteractionResponse(boolean success) {
        this.simulationEntity.gridMovementResponse(success);
    }

    public void sendGridMovementInteraction() {
    }

    public void sendPathFindingInteraction(int targetX, int targetY) {
    }

}
