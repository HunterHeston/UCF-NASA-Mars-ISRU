package test.federate.passive;

import execution.EnvironmentGridExecution;
import execution.SimulationEntityExecution;
import org.apache.log4j.Logger;
import test.test.rover.TestRover;
import test.test.rover.TestRoverExecution;

/**
 *
 *  Passive updates for a dummy rover.
 *
 * Created by Andrew on 1/13/2017.
 */
public class PassiveRover extends PassiveSimulationEntity {
    final static Logger logger = Logger.getLogger(PassiveRover.class);

    public PassiveRover(long hlaID, SimulationEntityExecution entityExecution,
                        EnvironmentGridExecution gridExecution) {
        super(hlaID, entityExecution, gridExecution);
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
        super.passiveUpdate();

        TestRover rover = (TestRover) this.entityExecution.simulationEntityState;
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
