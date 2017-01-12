package entity;

import org.apache.log4j.Logger;

/**
 * Created by rick on 1/12/17.
 */
public abstract class ChargeableEntity extends SimulationEntity {
    final static Logger logger = Logger.getLogger(ChargeableEntity.class);

    public static boolean adjacentToISRU(GridIndex pos, GridIndex isruPos) {
        int rowDelta, colDelta;

        rowDelta = Math.abs(pos.row-isruPos.row);
        colDelta = Math.abs(pos.col-isruPos.col);

        return rowDelta <= 1 && colDelta <= 1;
    }

    public enum ChargeState {
        Null,
        TransitToISRU,
        WaitingConnectionResponse,
        Charging,
        WaitingForPort,
    }

    public ChargeState chargeState = ChargeState.Null;
    public GridIndex isruIndex;

    public ChargeableEntity(int gridX, int gridY, int isruGridX, int isruGridY) {
        super(gridX, gridY);
        this.isruIndex = new GridIndex(isruGridX, isruGridY);
    }

    public abstract boolean chargeFull();
    public abstract boolean needsCharge();
    public abstract void doCharge(Object chargeAmount);

    public void handleCharge() {
        assert this.chargeState == ChargeState.Null;

        if(needsCharge()) {
            this.chargeState = ChargeState.TransitToISRU;
            this.beginPathFinding(isruIndex.row, isruIndex.col);
            logger.debug("Charge State Transition: Null -> TransitToISRU");
        }
    }

    public void attemptConnectToISRU() {
        assert this.movementState == MovementState.Stopped;
        assert this.chargeState == ChargeState.TransitToISRU;
        assert adjacentToISRU(this.gridIndex, this.isruIndex);

        this.chargeState = ChargeState.WaitingConnectionResponse;
        logger.debug("Charge State Transition: TransitToISRU -> WaitingConnectionResponse");
    }

    public void waitForPort() {
        assert this.chargeState == ChargeState.WaitingConnectionResponse;
        this.chargeState = ChargeState.WaitingForPort;
        logger.debug("Charge State Transition: WaitingConnectionResponse -> WaitingForPort");
    }

    public void connectToISRU() {
        assert this.chargeState == ChargeState.WaitingConnectionResponse || this.chargeState == ChargeState.WaitingForPort;
        this.chargeState = ChargeState.Charging;
        logger.debug("Charge State Transition: WaitingConnectionResponse|WaitingForPort -> Charging");
    }

    public void charge(Object chargeAmount) {
        assert this.chargeState == ChargeState.Charging;
        this.doCharge(chargeAmount);

        if(this.chargeFull()) {
            this.chargeState = ChargeState.Null;
            logger.debug("Charge State Transition: Charging -> Null");
        }
    }
}
