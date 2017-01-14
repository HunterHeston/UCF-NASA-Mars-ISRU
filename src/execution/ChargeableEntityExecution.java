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
            super.activeUpdate();
        }
    }

    @Override
    public void staticUpdate() {
        super.staticUpdate();

        ChargeableEntity entity = (ChargeableEntity) this.simulationEntity;
        entity.useCharge();
    }
}
