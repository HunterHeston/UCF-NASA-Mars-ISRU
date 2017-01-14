package test.federate.mock;

import execution.SimulationEntityExecution;

/**
 * Created by Andrew on 1/13/2017.
 */
public abstract class PassiveUpdateMock {

    public SimulationEntityExecution entityExecution;

    public PassiveUpdateMock(SimulationEntityExecution entityExecution) {
        this.entityExecution = entityExecution;
    }

    /**
     * Drive passive update callbacks from this method.
     * Infer the correct callback from the entity state.
     */
    public abstract void update();

}
