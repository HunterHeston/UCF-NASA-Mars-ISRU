package test.federate;

import state.SimulationEntityState;
import execution.SimulationEntityExecution;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Andrew on 1/12/2017.
 */
public class SimulationEntityExecutionTest {

    @Test
    public void movementTest() {
        SimulationEntityState simulationEntityState = new SimulationEntityState(0, 0, 0, 0.2, 1.0);
        SimulationEntityExecution entityExecution = new SimulationEntityExecution(simulationEntityState);

        assert simulationEntityState.movementState == SimulationEntityState.MovementState.Stopped;

        simulationEntityState.beginPathFinding(5, 5);
        assert simulationEntityState.movementState == SimulationEntityState.MovementState.PathFinding;

        Queue<SimulationEntityState.GridIndex> path = (Queue) new LinkedList<>();
        path.add(new SimulationEntityState.GridIndex(1, 1));
        path.add(new SimulationEntityState.GridIndex(2, 2));
        path.add(new SimulationEntityState.GridIndex(3, 3));
        path.add(new SimulationEntityState.GridIndex(4, 5));
        path.add(new SimulationEntityState.GridIndex(5, 5));

        entityExecution.receivePathFindingInteractionResponse(path);
        assert simulationEntityState.movementState == SimulationEntityState.MovementState.InMotion;

        for(int i = 0; i<500 && simulationEntityState.movementState != SimulationEntityState.MovementState.Stopped; i++) {
            entityExecution.activeUpdate();
            if(simulationEntityState.movementState == SimulationEntityState.MovementState.GridMovement) {
                entityExecution.receiveGridMovementInteractionResponse(true);
            }
        }

        assert simulationEntityState.movementState == SimulationEntityState.MovementState.Stopped;
        assert simulationEntityState.gridIndex.row == 5 && simulationEntityState.gridIndex.col == 5;
        assert simulationEntityState.position[0] == 5.0 && simulationEntityState.position[1] == 5.0;
    }

    @Test
    public void failedMovementTest() {
        SimulationEntityState simulationEntityState = new SimulationEntityState(0,0, 0, 0.2, 1.0);
        SimulationEntityExecution entityExecution = new SimulationEntityExecution(simulationEntityState);

        assert simulationEntityState.movementState == SimulationEntityState.MovementState.Stopped;

        simulationEntityState.beginPathFinding(5, 5);
        assert simulationEntityState.movementState == SimulationEntityState.MovementState.PathFinding;

        Queue<SimulationEntityState.GridIndex> path = (Queue) new LinkedList<>();
        path.add(new SimulationEntityState.GridIndex(1, 1));
        path.add(new SimulationEntityState.GridIndex(2, 2));
        path.add(new SimulationEntityState.GridIndex(3, 3));
        path.add(new SimulationEntityState.GridIndex(4, 5));
        path.add(new SimulationEntityState.GridIndex(5, 5));

        entityExecution.receivePathFindingInteractionResponse(path);
        assert simulationEntityState.movementState == SimulationEntityState.MovementState.InMotion;

        for(int i = 0; i<500 && simulationEntityState.movementState != SimulationEntityState.MovementState.Stopped; i++) {
            entityExecution.activeUpdate();
            if(simulationEntityState.movementState == SimulationEntityState.MovementState.GridMovement && simulationEntityState.path.size() != 3) {
                entityExecution.receiveGridMovementInteractionResponse(true);
            } else if(simulationEntityState.movementState == SimulationEntityState.MovementState.GridMovement && simulationEntityState.path.size() == 3) {
                entityExecution.receiveGridMovementInteractionResponse(false);
            }
        }

        assert simulationEntityState.movementState == SimulationEntityState.MovementState.Stopped;
        assert simulationEntityState.gridIndex.row == 2 && simulationEntityState.gridIndex.col == 2;
        assert simulationEntityState.position[0] == 2.0 && simulationEntityState.position[1] == 2.0;
    }
}
