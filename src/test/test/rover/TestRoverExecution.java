package test.test.rover;

import entity.SimulationEntity;
import execution.SimulationEntityExecution;
import org.apache.log4j.Logger;

/**
 * Created by Andrew on 1/13/2017.
 */
public class TestRoverExecution extends SimulationEntityExecution {
    final static Logger logger = Logger.getLogger(TestRoverExecution.class);

    public TestRoverExecution(SimulationEntity simulationEntity) {
        super(simulationEntity);
    }

    @Override
    public void activeEntityUpdate() {
        TestRover rover = (TestRover) this.simulationEntity;
        logger.debug(rover.testRoverState);
        if(rover.testRoverState == TestRover.TestRoverState.Standby) {
            rover.standby();
        } else if(this.simulationEntity.movementState == SimulationEntity.MovementState.Stopped
                && rover.testRoverState == TestRover.TestRoverState.GoingToPlace) {
            rover.atPlace();
        }
    }

    public void receivePickPlaceResponse(int gridX, int gridY) {
        ((TestRover)this.simulationEntity).pickPlace(gridX, gridY);
    }
}
