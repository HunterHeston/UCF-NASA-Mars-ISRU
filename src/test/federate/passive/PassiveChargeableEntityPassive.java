package test.federate.passive;

import state.ChargeableEntityState;
import execution.ChargeableEntityExecution;
import execution.EnvironmentGridExecution;
import execution.SimulationEntityExecution;

/**
 * Created by rick on 1/13/17.
 */
public class PassiveChargeableEntityPassive extends PassiveSimulationEntity {
    public PassiveChargeableEntityPassive(long hlaID, SimulationEntityExecution entityExecution,
                                          EnvironmentGridExecution gridExecution) {
        super(hlaID, entityExecution, gridExecution);
    }

    @Override
    public void passiveUpdate() {
        super.passiveUpdate();

        ChargeableEntityState chargeableEntityState = (ChargeableEntityState) this.entityExecution.simulationEntityState;
        ChargeableEntityExecution entityExecution = (ChargeableEntityExecution) this.entityExecution;

        if(chargeableEntityState.chargeState == ChargeableEntityState.ChargeState.WaitingConnectionResponse) {
            entityExecution.receiveChargeConnectResponse(true);
        } else if(chargeableEntityState.chargeState == ChargeableEntityState.ChargeState.Charging) {
            entityExecution.receiveChargeAmountInteraction(1.0);
        }
    }

}
