package test.federate.mock;

import entity.ChargeableEntity;
import execution.ChargeableEntityExecution;
import execution.EnvironmentGridExecution;
import execution.SimulationEntityExecution;

/**
 * Created by rick on 1/13/17.
 */
public class ChargeableEntityMock extends SimulationEntityMock {
    public ChargeableEntityMock(long hlaID, SimulationEntityExecution entityExecution,
                                EnvironmentGridExecution gridExecution) {
        super(hlaID, entityExecution, gridExecution);
    }

    @Override
    public void passiveUpdate() {
        super.passiveUpdate();

        ChargeableEntity chargeableEntity = (ChargeableEntity) this.entityExecution.simulationEntity;
        ChargeableEntityExecution entityExecution = (ChargeableEntityExecution) this.entityExecution;

        if(chargeableEntity.chargeState == ChargeableEntity.ChargeState.WaitingConnectionResponse) {
            entityExecution.receiveChargeConnectResponse(true);
        } else if(chargeableEntity.chargeState == ChargeableEntity.ChargeState.Charging) {
            entityExecution.receiveChargeAmountInteraction(1.0);
        }
    }

}
