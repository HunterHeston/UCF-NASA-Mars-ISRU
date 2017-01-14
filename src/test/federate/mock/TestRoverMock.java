package test.federate.mock;

import execution.EnvironmentGridExecution;
import execution.SimulationEntityExecution;
import org.apache.log4j.Logger;
import test.test.rover.TestRover;
import test.test.rover.TestRoverExecution;

/**
 *
 * This is an example of a Mock for a simple rover.
 *
 * Created by Andrew on 1/13/2017.
 */
public class TestRoverMock extends SimulationEntityMock {
    final static Logger logger = Logger.getLogger(TestRoverMock.class);

    public TestRoverMock(long hlaID, SimulationEntityExecution entityExecution,
                                     EnvironmentGridExecution gridExecution) {
        super(hlaID, entityExecution, gridExecution);
    }

    @Override
    public void passiveUpdate() {
        super.passiveUpdate();

        TestRover rover = (TestRover) this.entityExecution.simulationEntity;
        TestRoverExecution roverExecution = (TestRoverExecution) this.entityExecution;

        //  In the passive passiveUpdate phase, we are looking for a rover state of WaitingForPlace.
        //  This state means that we are waiting for an interaction containing a new location.
        //  Once we are in that state, go ahead and make the appropriate callbacks, to emulate
        //  the receipt of a PickPlacementResponseInteraction
        if(rover.testRoverState == TestRover.TestRoverState.WaitingForPlace) {
            int newX, newY;
            newX = (rover.gridIndex.col + 5) % this.gridExecution.grid.gridArray[0].length;
            newY = (rover.gridIndex.row + 5) % this.gridExecution.grid.gridArray.length;

            roverExecution.receivePickPlaceResponse(newX, newY);
        }
    }

}
