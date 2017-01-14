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
    
    public DummyRoverExecution(ChargeableEntityState entityState) {
        super(entityState);
    }

    /**
     *
     * Put your active update phase logic here.  In this case, we have a simple rover
     * which has a standby phase, and corresponding State method.  We also need to
     * detect when the rover arrives at a place, so we detect when we *should* have arrived
     * at that place, and a corresponding State method.
     *
     */
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

    /**
     *
     * When a response is received, this method gets called.
     *
     * @param gridX
     * @param gridY
     */
    public void receivePickPlaceResponse(int gridX, int gridY) {
        DummyRoverState rover = (DummyRoverState) this.simulationEntityState;
        rover.beginMovingToPlace(gridX, gridY);
    }

    /**
     *
     * This is where we would send the HLA Interaction ... Maybe, we may want to
     * encapsulate that stuff completely separately.
     *
     */
    public void sendPickPlaceInteraction() {}
}
