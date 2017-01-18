package state;

import org.apache.log4j.Logger;

/**
 * Created by rick on 1/13/17.
 */
public class DummyRoverState extends SimpleChargeableEntityState {
    public boolean pathfail = false;
    final static Logger logger = Logger.getLogger(DummyRoverState.class);

    public enum RoverState {
        Standby,
        WaitingForPlace,
        GoingToPlace,
    }


    /**
     *
     * Rover specific implementation state, start in standby
     *
     */
    public RoverState roverState = RoverState.Standby;

    /**
     * @param gridX
     * @param gridY
     * @param isruGridX
     * @param isruGridY
     * @param batteryCapacity - Simple battery capacity, See: SimpleChargeableEntityState
     * @param movementSpeed
     * @param gridCellSize
     */
    public DummyRoverState(long identifier, int gridX, int gridY, int isruGridX,
                           int isruGridY, double batteryCapacity,
                           double movementSpeed, double gridCellSize) {

        super(identifier, gridX, gridY, isruGridX, isruGridY, batteryCapacity, movementSpeed, gridCellSize);
    }

    /**
     *
     * Standby method, called during active update when roverState is Standby
     *
     */
    public void standby() {
        assert this.roverState == RoverState.Standby;
        this.handleCharge();

        if(this.chargeState == ChargeState.Null) {
            this.roverState = RoverState.WaitingForPlace;
            logger.debug("Chargeable Test Rover State Transition: Standby -> WaitingForPlace");
        }
    }

    /**
     *
     * This gets called in activeUpdate to indicate that we are now going to be moving to place.
     *
     * @param gridX
     * @param gridY
     */
    public void beginMovingToPlace(int gridX, int gridY) {
        assert this.roverState == RoverState.WaitingForPlace;
        this.roverState = RoverState.GoingToPlace;

        //  Call this to actually start the movement update, control *should* be returned
        //  When you arrive at your location, unless:
        //      -   The battery dies (this.isDead())
        //      -   A path could not be found
        //      -   A path was found, but something obstructed the path before arrival
        //
        //  Some of these will probably be refactored out, eventually...  but for now
        //  assume that there can be issues.
        this.beginPathFinding(gridX, gridY);
        logger.debug("Chargeable Test Rover State Transition: WaitingForPlace -> GoingToPlace");
    }

    /**
     *
     * This gets called in activeUpdate when we were GoingToPlace, but now have stopped.
     *
     */
    public void atPlace() {
        assert this.roverState == RoverState.GoingToPlace;
        assert this.movementState == MovementState.Stopped;

        this.roverState = RoverState.Standby;
        logger.debug("Chargeable Test Rover State Transition: GoingToPlace -> Standby");

    }

    public String toString() {
        return super.toString() + String.format(" %s", this.roverState);
    }
}
