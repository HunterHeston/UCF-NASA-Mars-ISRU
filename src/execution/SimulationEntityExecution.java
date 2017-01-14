package execution;

import state.SimulationEntityState;
import org.apache.log4j.Logger;

import java.util.Queue;

/**
 *
 * This class is the base state execution class for all entities in the simulation
 * This class will be used as an intermediary between the HLA-specific driver code
 * (Or test driver code) and the state itself.  This class should contain methods
 * and callbacks to be accessed when interactions are received or need to be sent.
 *
 * The primary goal of this class is to expose an interface to the functionality
 * of the state, without depending on HLA to run tests against this behavior.
 *
 *
 * Created by Andrew on 1/12/2017.
 */
public class SimulationEntityExecution {
    final static Logger logger = Logger.getLogger(SimulationEntityExecution.class);

    public double gridCellSize;
    public double movementSpeed;

    public SimulationEntityState simulationEntityState;

    public SimulationEntityExecution(SimulationEntityState simulationEntityState) {
        this.simulationEntityState = simulationEntityState;

        this.gridCellSize = 1;
        this.movementSpeed = 0.2;
    }


    /**
     *
     * This method is called once per simulation frame.  Movement updates are made first,
     * followed by the active updates of the implementing entities, iff the state is stopped.
     *
     */
    public void activeUpdate() {
        movementUpdate();

        if(simulationEntityState.movementState == SimulationEntityState.MovementState.Stopped) {
            this.activeEntityUpdate();
        }

        this.staticUpdate();
    }

    /**
     *
     * This method makes any necessary movement related state transitions and data updates
     * during the active passiveUpdate phase.  This includes calls to HLA when necessary (TODO)
     * as well as direct updates to the position vector.
     *
     */
    public void movementUpdate() {
        if(this.simulationEntityState.movementState == SimulationEntityState.MovementState.InMotion) {
            //  We need to gridMove on the first of each path
            if(this.simulationEntityState.isOnNewPath) {
                this.simulationEntityState.gridMovement();
                this.simulationEntityState.isOnNewPath = false;
            } else {
                boolean targetArrival = this.simulationEntityState.moveTowardsTarget();
                //logger.debug("In motion and moved towards target arrived=" + targetArrival + " new pos=" + simulationEntityState.position[0] + "," + simulationEntityState.position[1]);

                if (targetArrival) {
                    if (!this.simulationEntityState.path.isEmpty()) {
                        this.simulationEntityState.gridMovement();
                    } else {
                        this.simulationEntityState.stopTransit();
                    }
                }
            }
        }
    }

    /**
     *
     * This method is called at the end of the active passiveUpdate cycle, and should be overriden
     * by inheriting child classes as needed.
     *
     */
    public void staticUpdate() {
    }

    /**
     *
     * This method is called during the active passiveUpdate cycle iff the state is stopped.  This method
     * should be overriden by child classes, to create higher level state logic.
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
    public void receivePathFindingInteractionResponse(Queue<SimulationEntityState.GridIndex> path) {
        logger.debug(path);
        if(path == null || path.isEmpty()) {
            this.simulationEntityState.nullPathFindingResponse();
        } else {
            this.simulationEntityState.beginTransit(path);
        }
    }

    /**
     *
     * This method is called when a GridMovementInteractionResponse is received by the execution
     *
     * @param success
     */
    public void receiveGridMovementInteractionResponse(boolean success) {
        this.simulationEntityState.gridMovementResponse(success);
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
