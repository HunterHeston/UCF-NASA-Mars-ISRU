package test.test;

import entity.EnvironmentGridEntity;
import federate.EnvironmentGridExecution;
import federate.SimulationEntityExecution;
import org.apache.log4j.Logger;
import siso.smackdown.utilities.Vector3;
import test.federate.mock.SimulationEntityMock;

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

    public EnvironmentGridEntity gridEntity;
    public EnvironmentGridExecution gridExecution;
    public ArrayList<SimulationEntityExecution> entities;
    public HashMap<SimulationEntityExecution, SimulationEntityMock> mockMap;

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
                          SimulationEntityMock mock) {
        try {
            gridEntity.placeEntity(hlaID, execution.simulationEntity.gridIndex.col,
                                execution.simulationEntity.gridIndex.row, collisionRadius);

            this.entities.add(execution);
            this.mockMap.put(execution, mock);
        } catch (EnvironmentGridEntity.PlacementException e) {
            logger.error(e);
            assert false;
        }
    }

    public void update() {
        for(SimulationEntityExecution execution : entities) {
            execution.activeUpdate();
            SimulationEntityMock mock = mockMap.get(execution);
            if(mock != null) {
                mock.update();
            }
        }
    }
}
