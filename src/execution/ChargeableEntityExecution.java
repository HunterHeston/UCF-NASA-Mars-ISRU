package execution;

import state.ChargeableEntityState;
import state.SimulationEntityState;

/**
 * Created by rick on 1/13/17.
 */
public class ChargeableEntityExecution extends SimulationEntityExecution {
    public ChargeableEntityExecution(ChargeableEntityState simulationEntity) {
        super(simulationEntity);
    }

    /**
     *
     * Re-Implement activeUpdate, to kill the state when the battery dies.
     *
     */
    @Override
    public void activeUpdate() {
        ChargeableEntityState entity = (ChargeableEntityState) this.simulationEntityState;

        //  Wrap active state updates to enforce dead-ness
        if(!entity.isDead()) {
            movementUpdate();

            //  We also must be in a Null chargeState to return control to sub-implementations
            if(simulationEntityState.movementState == SimulationEntityState.MovementState.Stopped &&
                    entity.chargeState == ChargeableEntityState.ChargeState.Null) {
                this.activeEntityUpdate();
            } else {
                //  Chargeable Entity is in control
                if(entity.chargeState == ChargeableEntityState.ChargeState.TransitToISRU &&
                        entity.movementState == SimulationEntityState.MovementState.Stopped)  {

                    //  We should be at the ISRU, try and connect
                    entity.attemptConnectToISRU();
                } else if(entity.chargeState == ChargeableEntityState.ChargeState.Charging && entity.chargeFull()) {
                    entity.disconnectFromISRU();
                }
            }

            this.staticUpdate();
        }
    }

    @Override
    public void staticUpdate() {
        super.staticUpdate();

        ChargeableEntityState entity = (ChargeableEntityState) this.simulationEntityState;
        entity.useCharge();
    }

    public void receiveChargeConnectResponse(boolean success) {
        ChargeableEntityState entity = (ChargeableEntityState) this.simulationEntityState;

        if(success) {
            entity.connectToISRU();
        } else {
            entity.waitForPort();
        }
    }

    public void receivePortAvailableResponse() {
        ChargeableEntityState entity = (ChargeableEntityState) this.simulationEntityState;
        entity.connectToISRU();
    }

    public void receiveChargeAmountInteraction(Object chargeAmount) {
        ChargeableEntityState entity = (ChargeableEntityState) this.simulationEntityState;
        entity.doCharge(chargeAmount);
    }

    public void sendChargeConnectInteraction() {}
    public void sendChargeDisconnectInteraction() {}
}
