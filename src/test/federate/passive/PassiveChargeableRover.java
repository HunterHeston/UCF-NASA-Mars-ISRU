package test.federate.passive;

import execution.EnvironmentGridExecution;
import execution.SimulationEntityExecution;
import state.DummyRoverState;
import execution.DummyRoverExecution;

import java.util.Random;

/**
 * Created by rick on 1/13/17.
 */
public class PassiveChargeableRover extends PassiveChargeableEntityPassive {
    public PassiveChargeableRover(long hlaID, SimulationEntityExecution entityExecution,
                                  EnvironmentGridExecution gridExecution) {

        super(hlaID, entityExecution, gridExecution);
    }

    /**
     *
     * This method represents an passiveUpdate during the passive passiveUpdate cycle.  Because this
     * class aims to remove the dependence on HLA for testing, there are no interactions.
     * Instead, the handling of interactions can be emulated by inferring which callbacks
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

        DummyRoverState rover = (DummyRoverState) this.entityExecution.simulationEntityState;
        DummyRoverExecution roverExecution = (DummyRoverExecution) this.entityExecution;

        if(rover.testRoverState == DummyRoverState.TestRoverState.WaitingForPlace) {
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
