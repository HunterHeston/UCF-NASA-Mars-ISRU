package test.entity;

import state.SimulationEntityState;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Andrew on 1/9/2017.
 */
public class SimulationEntityStateTest {
    final static Logger logger = Logger.getLogger(SimulationEntityStateTest.class);

    @Test
    public void stateTransitionTest() throws Exception {
        SimulationEntityState entity = new SimulationEntityState(0, 0, 2.0, 1.0);
        assert entity.movementState == SimulationEntityState.MovementState.Stopped;

        entity.beginPathFinding(1, 1);
        assert entity.movementState == SimulationEntityState.MovementState.PathFinding;

        Queue<SimulationEntityState.GridIndex> path = (Queue) new LinkedList<>();
        SimulationEntityState.GridIndex target = new SimulationEntityState.GridIndex(1, 1);

        path.add(target);
        entity.beginTransit(path);
        assert entity.movementState == SimulationEntityState.MovementState.InMotion;

        entity.moveTowardsTarget();
        logger.debug("In motion and moved towards target new pos=" + entity.position[0] + "," + entity.position[1]);

        assert entity.gridIndex.row == 1 && entity.gridIndex.col == 1;
    }

    @Test
    public void longMovementTest() throws Exception {
        SimulationEntityState entity = new SimulationEntityState(0, 0, 2, 1);
        assert entity.movementState == SimulationEntityState.MovementState.Stopped;

        entity.beginPathFinding(2, 3);
        assert entity.movementState == SimulationEntityState.MovementState.PathFinding;

        Queue<SimulationEntityState.GridIndex> path = (Queue) new LinkedList<>();
        SimulationEntityState.GridIndex[] target = new SimulationEntityState.GridIndex[3];

        path.add(new SimulationEntityState.GridIndex(1, 1));
        path.add(new SimulationEntityState.GridIndex(2, 2));
        path.add(new SimulationEntityState.GridIndex(2, 3));

        Queue<SimulationEntityState.GridIndex> path2 = (Queue) new LinkedList<>();

        path2.add(new SimulationEntityState.GridIndex(1, 1));
        path2.add(new SimulationEntityState.GridIndex(2, 2));
        path2.add(new SimulationEntityState.GridIndex(2, 3));

        entity.beginTransit(path);
        assert entity.movementState == SimulationEntityState.MovementState.InMotion;

        while(!path2.isEmpty()) {
            SimulationEntityState.GridIndex index = path2.poll();

            entity.moveTowardsTarget();
            assert entity.movementState == SimulationEntityState.MovementState.InMotion;
            assert entity.gridIndex.row == index.row && entity.gridIndex.col == index.col;

            entity.gridMovement();
            assert entity.movementState == SimulationEntityState.MovementState.GridMovement;

            if(!path2.isEmpty()) {
                entity.gridMovementResponse(true);
                assert entity.movementState == SimulationEntityState.MovementState.InMotion;
            } else {
            }
        }

        entity.stopTransit();
        assert entity.gridIndex.row == 2 && entity.gridIndex.col == 3;
        assert entity.movementState == SimulationEntityState.MovementState.Stopped;
    }

    @Test
    public void failureTest() throws Exception {
        SimulationEntityState entity = new SimulationEntityState(0, 0);
        assert entity.movementState == SimulationEntityState.MovementState.Stopped;



    }

}