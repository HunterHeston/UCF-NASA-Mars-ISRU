package test.federate.mock;

import entity.ChargeableEntity;
import execution.ChargeableEntityExecution;
import execution.EnvironmentGridExecution;
import execution.SimulationEntityExecution;
import test.test.rover.TestChargeableRover;
import test.test.rover.TestChargeableRoverExecution;
import test.test.rover.TestRover;

import java.util.Random;

/**
 * Created by rick on 1/13/17.
 */
public class TestChargeableRoverMock extends ChargeableEntityMock {
    public TestChargeableRoverMock(long hlaID, SimulationEntityExecution entityExecution,
                                   EnvironmentGridExecution gridExecution) {

        super(hlaID, entityExecution, gridExecution);
    }

    @Override
    public void update() {
        super.update();

        TestChargeableRover rover = (TestChargeableRover) this.entityExecution.simulationEntity;
        TestChargeableRoverExecution roverExecution = (TestChargeableRoverExecution) this.entityExecution;

        if(rover.testRoverState == TestChargeableRover.TestRoverState.WaitingForPlace) {
            int newX, newY;

            Random r = new Random(System.currentTimeMillis());

            newX = (int)(r.nextDouble() * gridExecution.gridWidth) % this.gridExecution.grid.gridArray[0].length;
            newY = (int)(r.nextDouble() * gridExecution.gridHeight) % this.gridExecution.grid.gridArray.length;

            while(gridExecution.grid.gridArray[newY][newX].isBlocked()) {
                newX = (int)(r.nextDouble() * gridExecution.gridWidth) % this.gridExecution.grid.gridArray[0].length;
                newY = (int)(r.nextDouble() * gridExecution.gridHeight) % this.gridExecution.grid.gridArray.length;
            }

            roverExecution.receivePickPlaceResponse(newX, newY);
        }
    }
}
