package execution;

import state.ChargeableEntityState;
import state.DummyRoverState;
import state.SimulationEntityState;
import org.apache.log4j.Logger;

/**
 * Created by rick on 1/13/17.
 */
public class DummyRoverExecution extends ChargeableEntityExecution {
    final static Logger logger = Logger.getLogger(DummyRoverExecution.class);

    public DummyRoverExecution(ChargeableEntityState simulationEntity) {
        super(simulationEntity);
    }

    @Override
    public void activeEntityUpdate() {
        DummyRoverState rover = (DummyRoverState) this.simulationEntityState;

        if(rover.testRoverState == DummyRoverState.TestRoverState.Standby) {
            rover.standby();
        } else if(this.simulationEntityState.movementState == SimulationEntityState.MovementState.Stopped
                && rover.testRoverState == DummyRoverState.TestRoverState.GoingToPlace) {
            rover.atPlace();
        }
    }

    public void receivePickPlaceResponse(int gridX, int gridY) {
        ((DummyRoverState)this.simulationEntityState).goToPlace(gridX, gridY);
    }


}
