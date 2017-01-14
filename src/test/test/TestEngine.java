package test.test;

import state.EnvironmentGridState;
import execution.EnvironmentGridExecution;
import execution.SimulationEntityExecution;
import org.apache.log4j.Logger;
import siso.smackdown.utilities.Vector3;
import test.test.passive.PassiveSimulationEntity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * This class will act as an engine to drive the Mars Sim, without using HLA
 *
 *
 * Created by Andrew on 1/13/2017.
 */
public class TestEngine {
    final static Logger logger = Logger.getLogger(TestEngine.class);

    public EnvironmentGridState gridEntity;
    public EnvironmentGridExecution gridExecution;
    public ArrayList<SimulationEntityExecution> entities;
    public HashMap<SimulationEntityExecution, PassiveSimulationEntity> mockMap;

    public TestEngine(EnvironmentGridExecution gridExecution) {
        this.gridExecution = gridExecution;
        this.gridEntity = gridExecution.grid;

        entities = new ArrayList<>();
        mockMap = new HashMap<>();
    }

    public TestEngine() {
        gridExecution = new EnvironmentGridExecution(new Vector3(), 1.0, 10, 10);
        gridEntity = gridExecution.grid;

        entities = new ArrayList<>();
        mockMap = new HashMap<>();
    }

    public void addEntity(long hlaID, int collisionRadius,
                          SimulationEntityExecution execution,
                          PassiveSimulationEntity mock) {
        try {
            gridEntity.placeEntity(hlaID, execution.simulationEntityState.gridIndex.col,
                                execution.simulationEntityState.gridIndex.row, collisionRadius);

            this.entities.add(execution);
            this.mockMap.put(execution, mock);
        } catch (EnvironmentGridState.PlacementException e) {
            logger.error(e);
            assert false;
        }
    }

    public void update() {
        for(SimulationEntityExecution execution : entities) {
            execution.activeUpdate();
            PassiveSimulationEntity mock = mockMap.get(execution);
            if(mock != null) {
                mock.passiveUpdate();
            }
        }
    }
}
