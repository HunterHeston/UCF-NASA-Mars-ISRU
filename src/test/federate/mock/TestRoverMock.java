package test.federate.mock;

import federate.EnvironmentGridExecution;
import federate.SimulationEntityExecution;
import org.apache.log4j.Logger;
import test.test.rover.TestRover;
import test.test.rover.TestRoverExecution;

/**
 * Created by Andrew on 1/13/2017.
 */
public class TestRoverMock extends SimulationEntityMock {
    final static Logger logger = Logger.getLogger(TestRoverMock.class);

    public TestRoverMock(long hlaID, SimulationEntityExecution entityExecution,
                                     EnvironmentGridExecution gridExecution) {
        super(hlaID, entityExecution, gridExecution);
    }

    @Override
    public void update() {
        super.update();

        TestRover rover = (TestRover) this.entityExecution.simulationEntity;
        TestRoverExecution roverExecution = (TestRoverExecution) this.entityExecution;

        if(rover.testRoverState == TestRover.TestRoverState.WaitingForPlace) {
            int newX, newY;
            newX = (rover.gridIndex.col + 5) % this.gridExecution.grid.gridArray[0].length;
            newY = (rover.gridIndex.row + 5) % this.gridExecution.grid.gridArray.length;

            roverExecution.receivePickPlaceResponse(newX, newY);
        }
    }

}
