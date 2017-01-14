package test.test.passive;

import execution.SimulationEntityExecution;

/**
 *
 * This class encapsulates a passiveUpdate method, which is called every simulation cycle
 * This method can be used to create test cases which emulate the callbacks happening in
 * the passive update phase.
 *
 * Created by Andrew on 1/13/2017.
 */
public abstract class PassiveUpdateMock {

    public SimulationEntityExecution entityExecution;

    public PassiveUpdateMock(SimulationEntityExecution entityExecution) {
        this.entityExecution = entityExecution;
    }

    /**
     * Drive passive passiveUpdate callbacks from this method.
     * Infer the correct callback from the state state.
     */
    public abstract void passiveUpdate();

}
