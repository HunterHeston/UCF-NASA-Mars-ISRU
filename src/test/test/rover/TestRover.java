package test.test.rover;

import entity.SimulationEntity;
import org.apache.log4j.Logger;

/**
 * Created by Andrew on 1/13/2017.
 */
public class TestRover extends SimulationEntity {
    final static Logger logger = Logger.getLogger(SimulationEntity.class);

    public enum TestRoverState {
        Standby,
        WaitingForPlace,
        GoingToPlace,
    }

    public TestRoverState testRoverState = TestRoverState.Standby;

    public TestRover(int gridX, int gridY, double movementSpeed, double gridCellSize) {
        super(gridX, gridY, movementSpeed, gridCellSize);
    }

    public void standby() {
        assert this.testRoverState == TestRoverState.Standby;
        this.testRoverState = TestRoverState.WaitingForPlace;
        logger.debug("Test Rover State Transition: Standby -> WaitingForPlace");
    }

    public void pickPlace(int gridX, int gridY) {
        assert this.testRoverState == TestRoverState.WaitingForPlace;
        this.testRoverState = TestRoverState.GoingToPlace;
        this.beginPathFinding(gridX, gridY);
        logger.debug("Test Rover State Transition: WaitingForPlace -> GoingToPlace");
    }

    public void atPlace() {
        assert this.testRoverState == TestRoverState.GoingToPlace;
        assert this.movementState == MovementState.Stopped;
        this.testRoverState = TestRoverState.Standby;
        logger.debug("Test Rover State Transition: GoingToPlace -> Standby");

    }

}
