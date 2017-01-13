package test.federate;

import entity.SimulationEntity;
import federate.SimulationEntityExecution;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Andrew on 1/12/2017.
 */
public class SimulationEntityExecutionTest {

    @Test
    public void movementTest() {
        SimulationEntity simulationEntity = new SimulationEntity(0, 0, 0.2, 1.0);
        SimulationEntityExecution entityExecution = new SimulationEntityExecution(simulationEntity);

        assert simulationEntity.movementState == SimulationEntity.MovementState.Stopped;

        simulationEntity.beginPathFinding(5, 5);
        assert simulationEntity.movementState == SimulationEntity.MovementState.PathFinding;

        Queue<SimulationEntity.GridIndex> path = (Queue) new LinkedList<>();
        path.add(new SimulationEntity.GridIndex(1, 1));
        path.add(new SimulationEntity.GridIndex(2, 2));
        path.add(new SimulationEntity.GridIndex(3, 3));
        path.add(new SimulationEntity.GridIndex(4, 5));
        path.add(new SimulationEntity.GridIndex(5, 5));

        entityExecution.receivePathFindingInteractionResponse(path);
        assert simulationEntity.movementState == SimulationEntity.MovementState.InMotion;

        for(int i=0;i<500 && simulationEntity.movementState != SimulationEntity.MovementState.Stopped; i++) {
            entityExecution.activeUpdate();
            if(simulationEntity.movementState == SimulationEntity.MovementState.GridMovement) {
                entityExecution.receiveGridMovementInteractionResponse(true);
            }
        }

        assert simulationEntity.movementState == SimulationEntity.MovementState.Stopped;
        assert simulationEntity.gridIndex.row == 5 && simulationEntity.gridIndex.col == 5;
        assert simulationEntity.position[0] == 5.0 && simulationEntity.position[1] == 5.0;
    }

    @Test
    public void failedMovementTest() {
        SimulationEntity simulationEntity = new SimulationEntity(0, 0, 0.2, 1.0);
        SimulationEntityExecution entityExecution = new SimulationEntityExecution(simulationEntity);

        assert simulationEntity.movementState == SimulationEntity.MovementState.Stopped;

        simulationEntity.beginPathFinding(5, 5);
        assert simulationEntity.movementState == SimulationEntity.MovementState.PathFinding;

        Queue<SimulationEntity.GridIndex> path = (Queue) new LinkedList<>();
        path.add(new SimulationEntity.GridIndex(1, 1));
        path.add(new SimulationEntity.GridIndex(2, 2));
        path.add(new SimulationEntity.GridIndex(3, 3));
        path.add(new SimulationEntity.GridIndex(4, 5));
        path.add(new SimulationEntity.GridIndex(5, 5));

        entityExecution.receivePathFindingInteractionResponse(path);
        assert simulationEntity.movementState == SimulationEntity.MovementState.InMotion;

        for(int i=0;i<500 && simulationEntity.movementState != SimulationEntity.MovementState.Stopped; i++) {
            entityExecution.activeUpdate();
            if(simulationEntity.movementState == SimulationEntity.MovementState.GridMovement && simulationEntity.path.size() != 3) {
                entityExecution.receiveGridMovementInteractionResponse(true);
            } else if(simulationEntity.movementState == SimulationEntity.MovementState.GridMovement && simulationEntity.path.size() == 3) {
                entityExecution.receiveGridMovementInteractionResponse(false);
            }
        }

        assert simulationEntity.movementState == SimulationEntity.MovementState.Stopped;
        assert simulationEntity.gridIndex.row == 2 && simulationEntity.gridIndex.col == 2;
        assert simulationEntity.position[0] == 2.0 && simulationEntity.position[1] == 2.0;
    }
}
