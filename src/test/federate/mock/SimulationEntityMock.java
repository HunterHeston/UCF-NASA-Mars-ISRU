package test.federate.mock;

import entity.SimulationEntity;
import environment.GridCell;
import federate.EnvironmentGridExecution;
import federate.SimulationEntityExecution;
import org.apache.log4j.Logger;

import java.util.LinkedList;

/**
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

    @Override
    public void update() {
        if(this.entityExecution.simulationEntity.movementState ==
                SimulationEntity.MovementState.PathFinding) {

            GridCell[] path =
                    this.gridExecution.receivePathFindingInteraction(this.hlaID,
                        this.entityExecution.simulationEntity.finalGridIndex.col,
                        this.entityExecution.simulationEntity.finalGridIndex.row);

            logger.debug("sanity " + path);

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

            this.entityExecution.receiveGridMovementInteractionResponse(
                    this.gridExecution.receiveGridMoveInteraction(hlaID,
                            this.entityExecution.simulationEntity.targetGridIndex.row,
                            this.entityExecution.simulationEntity.targetGridIndex.col));
        }
    }
}
