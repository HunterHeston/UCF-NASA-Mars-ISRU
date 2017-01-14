package execution;

import entity.ChargeableEntity;
import entity.SimulationEntity;

/**
 * Created by rick on 1/13/17.
 */
public class ChargeableEntityExecution extends SimulationEntityExecution {
    public ChargeableEntityExecution(ChargeableEntity simulationEntity) {
        super(simulationEntity);
    }

    @Override
    public void activeUpdate() {
        ChargeableEntity entity = (ChargeableEntity) this.simulationEntity;

        //  Wrap active entity updates to enforce dead-ness
        if(!entity.isDead()) {
            movementUpdate();

            //  We also must be in a Null chargeState to return control to sub-implementations
            if(simulationEntity.movementState == SimulationEntity.MovementState.Stopped &&
                    entity.chargeState == ChargeableEntity.ChargeState.Null) {
                this.activeEntityUpdate();
            } else {
                if(entity.chargeState == ChargeableEntity.ChargeState.TransitToISRU &&
                        entity.movementState == SimulationEntity.MovementState.Stopped)  {
                    entity.attemptConnectToISRU();
                } else if(entity.chargeState == ChargeableEntity.ChargeState.Charging && entity.chargeFull()) {
                    entity.disconnectFromISRU();
                }
            }

            this.staticUpdate();
        }
    }

    @Override
    public void staticUpdate() {
        super.staticUpdate();

        ChargeableEntity entity = (ChargeableEntity) this.simulationEntity;
        entity.useCharge();
    }

    public void receiveChargeConnectResponse(boolean success) {
        ChargeableEntity entity = (ChargeableEntity) this.simulationEntity;

        if(success) {
            entity.connectToISRU();
        } else {
            entity.waitForPort();
        }
    }

    public void receivePortAvailableResponse() {
        ChargeableEntity entity = (ChargeableEntity) this.simulationEntity;
        entity.connectToISRU();
    }

    public void receiveChargeAmountInteraction(Object chargeAmount) {
        ChargeableEntity entity = (ChargeableEntity) this.simulationEntity;
        entity.doCharge(chargeAmount);
    }

    public void sendChargeConnectInteraction() {}
    public void sendChargeDisconnectInteraction() {}
}
