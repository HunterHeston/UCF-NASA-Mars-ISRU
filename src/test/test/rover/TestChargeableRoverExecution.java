package test.test.rover;

import entity.ChargeableEntity;
import entity.SimulationEntity;
import execution.ChargeableEntityExecution;
import org.apache.log4j.Logger;

/**
 * Created by rick on 1/13/17.
 */
public class TestChargeableRoverExecution extends ChargeableEntityExecution {
    final static Logger logger = Logger.getLogger(TestChargeableRoverExecution.class);

    public TestChargeableRoverExecution(ChargeableEntity simulationEntity) {
        super(simulationEntity);
    }

    @Override
    public void activeEntityUpdate() {
        TestChargeableRover rover = (TestChargeableRover) this.simulationEntity;

        if(rover.testRoverState == TestChargeableRover.TestRoverState.Standby) {
            rover.standby();
        } else if(this.simulationEntity.movementState == SimulationEntity.MovementState.Stopped
                && rover.testRoverState == TestChargeableRover.TestRoverState.GoingToPlace) {
            rover.atPlace();
        }
    }

    public void receivePickPlaceResponse(int gridX, int gridY) {
        ((TestChargeableRover)this.simulationEntity).pickPlace(gridX, gridY);
    }


}
