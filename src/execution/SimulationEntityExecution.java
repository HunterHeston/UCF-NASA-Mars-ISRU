package execution;

import entity.SimulationEntity;
import org.apache.log4j.Logger;

import java.util.Queue;

/**
 *
 * This class is the base entity execution class for all entities in the simulation
 * This class will be used as an intermediary between the HLA-specific driver code
 * (Or test driver code) and the entity itself.  This class should contain methods
 * and callbacks to be accessed when interactions are received or need to be sent.
 *
 * The primary goal of this class is to expose an interface to the functionality
 * of the entity, without depending on HLA to run tests against this behavior.
 *
 *
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


    /**
     *
     * This method is called once per simulation frame.  Movement updates are made first,
     * followed by the active updates of the implementing entities, iff the entity is stopped.
     *
     */
    public void activeUpdate() {
        movementUpdate();

        if(simulationEntity.movementState == SimulationEntity.MovementState.Stopped) {
            this.activeEntityUpdate();
        }

        this.staticUpdate();
    }

    /**
     *
     * This method makes any necessary movement related state transitions and data updates
     * during the active update phase.  This includes calls to HLA when necessary (TODO)
     * as well as direct updates to the position vector.
     *
     */
    public void movementUpdate() {
        if(this.simulationEntity.movementState == SimulationEntity.MovementState.InMotion) {
            //  We need to gridMove on the first of each path
            if(this.simulationEntity.isOnNewPath) {
                this.simulationEntity.gridMovement();
                this.simulationEntity.isOnNewPath = false;
            } else {
                boolean targetArrival = this.simulationEntity.moveTowardsTarget();
                //logger.debug("In motion and moved towards target arrived=" + targetArrival + " new pos=" + simulationEntity.position[0] + "," + simulationEntity.position[1]);

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

    /**
     *
     * This method is called at the end of the active update cycle, and should be overriden
     * by inheriting child classes as needed.
     *
     */
    public void staticUpdate() {
    }

    /**
     *
     * This method is called during the active update cycle iff the entity is stopped.  This method
     * should be overriden by child classes, to create higher level entity logic.
     *
     */
    public void activeEntityUpdate() {
    }


    /**
     *
     * This method is called when a PathFindingInteractionResponse is received by the execution.
     *
     * @param path
     *
     */
    public void receivePathFindingInteractionResponse(Queue<SimulationEntity.GridIndex> path) {
        logger.debug(path);
        if(path == null || path.isEmpty()) {
            this.simulationEntity.nullPathFindingResponse();
        } else {
            this.simulationEntity.beginTransit(path);
        }
    }

    /**
     *
     * This method is called when a GridMovementInteractionResponse is received by the execution
     *
     * @param success
     */
    public void receiveGridMovementInteractionResponse(boolean success) {
        this.simulationEntity.gridMovementResponse(success);
    }

    /**
     *
     * This method is called to send a new GridMovementInteraction (TODO)
     *
     */
    public void sendGridMovementInteraction() {
    }

    /**
     *
     * This method is called to send a new PathFindingInteraction (TODO)
     *
     * @param targetX
     * @param targetY
     */
    public void sendPathFindingInteraction(int targetX, int targetY) {
    }

}
