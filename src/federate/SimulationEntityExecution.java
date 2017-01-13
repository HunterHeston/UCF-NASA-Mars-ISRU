package federate;

import entity.SimulationEntity;
import org.apache.log4j.Logger;

import java.util.Observable;
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

    public void update(Observable o, Object arg) {

    }

    public void doAction() {
        movementUpdate();

        if(simulationEntity.movementState == SimulationEntity.MovementState.Stopped) {
            this.activeEntityUpdate();
        }

        this.staticUpdate();
    }

    public void movementUpdate() {

        if(this.simulationEntity.movementState == SimulationEntity.MovementState.InMotion) {
            boolean targetArrival = this.simulationEntity.moveTowardsTarget();
            logger.debug("In motion and moved towards target arrived=" + targetArrival + " new pos=" + simulationEntity.position[0] + "," + simulationEntity.position[1]);

            if(targetArrival) {
                if(!this.simulationEntity.path.isEmpty()) {
                    this.simulationEntity.gridMovement();
                    this.beginGridMovementInteraction();
                } else {
                    this.simulationEntity.stopTransit();
                }
            }
        }
    }

    public void staticUpdate() {
    }

    public void activeEntityUpdate() {
    }

    //  Temporary arguments
    //  TODO: Parameter should be the interaction!
    public void handlePathFindingInteractionResponse(Queue<SimulationEntity.GridIndex> path) {
        if(path == null || path.isEmpty()) {
            this.simulationEntity.nullPathFindingResponse();
        } else {
            this.simulationEntity.beginTransit(path);
        }
    }

    public void beginGridMovementInteraction() {
    }

    public void handleGridMovementInteractionResponse(boolean success) {
        this.simulationEntity.gridMovementResponse(success);
    }

}
