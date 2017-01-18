package test.test.passive;

import state.SimulationEntityState;
import environment.GridCell;
import execution.EnvironmentGridExecution;
import execution.SimulationEntityExecution;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.Random;

/**
 *
 * This class will facilitate the testing of state logic, without the need to run
 * the test as an HLA application.  This is accomplished through the passiveUpdate method,
 * which should be overriden by child classes.
 *
 * Created by Andrew on 1/13/2017.
 */
public class PassiveSimulationEntity extends PassiveUpdateMock {
    final static Logger logger = Logger.getLogger(PassiveSimulationEntity.class);

    public long hlaID;
    public EnvironmentGridExecution gridExecution;

    public PassiveSimulationEntity(long hlaID,
                                   SimulationEntityExecution entityExecution,
                                   EnvironmentGridExecution gridExecution) {
        super(entityExecution);
        this.gridExecution = gridExecution;
        this.hlaID = hlaID;
    }

    /**
     *
     * This method represents an passiveUpdate during the passive passiveUpdate cycle.  Because this
     * class aims to remove the dependence on HLA for testing, there are no interactions.
     * Instead the handling of interactions can be emulated by inferring which callbacks
     * to the SimulationEntityExecution should be made.
     *
     * This class is called directly after the active updates are made to the state.  This
     * class should be overriden to test higher level behavior logic, however a call to
     * super.passiveUpdate() MUST be called at the beginning of each overriden instance of this method,
     * otherwise, movement will not work.
     *
     */
    @Override
    public void passiveUpdate() {
        if(this.entityExecution.simulationEntityState.movementState ==
                SimulationEntityState.MovementState.PathFinding) {

            //  The movement state is in PathFinding, therefore the state is expecting
            //  to receive a a PathFindingInteractionResponse containing the path.
            //  Instead of sending an interaction, the path is calculated directly
            //  from an environment instance, and the receivePathFindingInteractionResponse
            //  callback is made with the calculated path.

            GridCell[] path =
                    this.gridExecution.receivePathFindingInteraction(this.hlaID,
                        this.entityExecution.simulationEntityState.finalGridIndex.col,
                        this.entityExecution.simulationEntityState.finalGridIndex.row);

            if(path == null) {
                this.entityExecution.receivePathFindingInteractionResponse(null);
            }

            LinkedList<SimulationEntityState.GridIndex> pathQueue = new LinkedList<>();

            for(GridCell cell : path) {
                pathQueue.add(new SimulationEntityState.GridIndex(cell.row, cell.col));
            }

            this.entityExecution.receivePathFindingInteractionResponse(pathQueue);

        } else if(this.entityExecution.simulationEntityState.movementState ==
                       SimulationEntityState.MovementState.GridMovement) {

            boolean success = this.gridExecution.receiveGridMoveInteraction(hlaID,
                    this.entityExecution.simulationEntityState.path.peek().col,
                    this.entityExecution.simulationEntityState.path.peek().row);

            //  The movement state is in GridMovement, therefore the state is expecting to
            //  to receive a GridMovementInteractionResponse.  Instead of sending an interaction,
            //  The callback to the corresponding EnvironmentGridExecution method is made directly.
            this.entityExecution.receiveGridMovementInteractionResponse(success);
        }
    }
}
