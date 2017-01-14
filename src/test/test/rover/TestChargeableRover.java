package test.test.rover;

import entity.ChargeableEntity;
import entity.SimpleChargeableEntity;
import org.apache.log4j.Logger;

/**
 * Created by rick on 1/13/17.
 */
public class TestChargeableRover extends SimpleChargeableEntity {
    final static Logger logger = Logger.getLogger(TestChargeableRover.class);

    public enum TestRoverState {
        Standby,
        WaitingForPlace,
        GoingToPlace,
    }

    public TestRoverState testRoverState = TestRoverState.Standby;
    public TestChargeableRover(int gridX, int gridY, int isruGridX,
                               int isruGridY, double capacity,
                                double movementSpeed, double gridCellSize) {

        super(gridX, gridY, isruGridX, isruGridY, capacity, movementSpeed, gridCellSize);
    }

    public void standby() {
        assert this.testRoverState == TestRoverState.Standby;
        this.handleCharge();

        if(this.chargeState == ChargeState.Null) {
            this.testRoverState = TestRoverState.WaitingForPlace;
            logger.debug("Chargeable Test Rover State Transition: Standby -> WaitingForPlace");
        }
    }

    public void pickPlace(int gridX, int gridY) {
        assert this.testRoverState == TestRoverState.WaitingForPlace;
        this.testRoverState = TestRoverState.GoingToPlace;
        this.beginPathFinding(gridX, gridY);
        logger.debug("Chargeable Test Rover State Transition: WaitingForPlace -> GoingToPlace");
    }

    public void atPlace() {
        assert this.testRoverState == TestRoverState.GoingToPlace;
        assert this.movementState == MovementState.Stopped;
        this.testRoverState = TestRoverState.Standby;
        logger.debug("Chargeable Test Rover State Transition: GoingToPlace -> Standby");

    }
}
