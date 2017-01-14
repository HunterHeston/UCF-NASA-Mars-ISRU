package test.federate.mock;

import entity.SimulationEntity;
import environment.GridCell;
import federate.EnvironmentGridExecution;
import federate.SimulationEntityExecution;
import org.apache.log4j.Logger;

import java.util.LinkedList;

/**
 *
 * This class will facilitate the testing of entity logic, without the need to run
 * the test as an HLA application.  This is accomplished through the update method,
 * which should be overriden by child classes.
 *
 * Created by Andrew on 1/13/2017.
 */
public class SimulationEntityMock extends PassiveUpdateMock {
    final static Logger logger = Logger.getLogger(SimulationEntityMock.class);

    public long hlaID;
    public EnvironmentGridExecution gridExecution;

    public SimulationEntityMock(long hlaID,
                                SimulationEntityExecution entityExecution,
                                EnvironmentGridExecution gridExecution) {
        super(entityExecution);
        this.gridExecution = gridExecution;
        this.hlaID = hlaID;
    }

    /**
     *
     * This method represents an update during the passive update cycle.  Because this
     * class aims to remove the dependence on HLA for testing, there are no interactions.
     * Instead the handling of interactions can be emulated by inferring which callbacks
     * to the SimulationEntityExecution should be made.
     *
     * This class is called directly after the active updates are made to the entity.  This
     * class should be overriden to test higher level behavior logic, however a call to
     * super.update() MUST be called at the beginning of each overriden instance of this method,
     * otherwise, movement will not work.
     *
     */
    @Override
    public void update() {
        if(this.entityExecution.simulationEntity.movementState ==
                SimulationEntity.MovementState.PathFinding) {

            //  The movement state is in PathFinding, therefore the entity is expecting
            //  to receive a a PathFindingInteractionResponse containing the path.
            //  Instead of sending an interaction, the path is calculated directly
            //  from an environment instance, and the receivePathFindingInteractionResponse
            //  callback is made with the calculated path.

            GridCell[] path =
                    this.gridExecution.receivePathFindingInteraction(this.hlaID,
                        this.entityExecution.simulationEntity.finalGridIndex.col,
                        this.entityExecution.simulationEntity.finalGridIndex.row);

            if(path == null) {
                this.entityExecution.receivePathFindingInteractionResponse(null);
            }

            LinkedList<SimulationEntity.GridIndex> pathQueue = new LinkedList<>();

            for(GridCell cell : path) {
                pathQueue.add(new SimulationEntity.GridIndex(cell.row, cell.col));
            }

            this.entityExecution.receivePathFindingInteractionResponse(pathQueue);

        } else if(this.entityExecution.simulationEntity.movementState ==
                       SimulationEntity.MovementState.GridMovement) {

            //  The movement state is in GridMovement, therefore the entity is expecting to
            //  to receive a GridMovementInteractionResponse.  Instead of sending an interaction,
            //  The callback to the corresponding EnvironmentGridExecution method is made directly.
            this.entityExecution.receiveGridMovementInteractionResponse(
                    this.gridExecution.receiveGridMoveInteraction(hlaID,
                            this.entityExecution.simulationEntity.targetGridIndex.col,
                            this.entityExecution.simulationEntity.targetGridIndex.row));
        }
    }
}
