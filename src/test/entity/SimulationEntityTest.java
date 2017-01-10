package test.entity;

import entity.SimulationEntity;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Andrew on 1/9/2017.
 */
public class SimulationEntityTest {
    @Test
    public void stateTransitionTest() throws Exception {
        SimulationEntity entity = new SimulationEntity(0, 0);
        assert entity.movementState == SimulationEntity.MovementState.Stopped;

        entity.beginPathFinding(1, 1);
        assert entity.movementState == SimulationEntity.MovementState.PathFinding;

        Queue<SimulationEntity.GridIndex> path = (Queue) new LinkedList<>();
        SimulationEntity.GridIndex target = new SimulationEntity.GridIndex(1, 1);

        path.add(target);
        entity.beginTransit(path);
        assert entity.movementState == SimulationEntity.MovementState.InMotion;

        entity.gridMovement();
        assert entity.movementState == SimulationEntity.MovementState.GridMovement;

        entity.walkPath();
        assert entity.movementState == SimulationEntity.MovementState.Stopped;
        assert entity.gridIndex.row == target.row && entity.gridIndex.col == target.col;
    }

    @Test
    public void longMovementTest() throws Exception {
        SimulationEntity entity = new SimulationEntity(0, 0);
        assert entity.movementState == SimulationEntity.MovementState.Stopped;

        entity.beginPathFinding(2, 3);
        assert entity.movementState == SimulationEntity.MovementState.PathFinding;

        Queue<SimulationEntity.GridIndex> path = (Queue) new LinkedList<>();
        SimulationEntity.GridIndex[] target = new SimulationEntity.GridIndex[3];

        path.add(new SimulationEntity.GridIndex(1, 1));
        path.add(new SimulationEntity.GridIndex(2, 2));
        path.add(new SimulationEntity.GridIndex(2, 3));

        entity.beginTransit(path);
        assert entity.movementState == SimulationEntity.MovementState.InMotion;

        entity.gridMovement();
        assert entity.movementState == SimulationEntity.MovementState.GridMovement;

        entity.walkPath();
        assert entity.gridIndex.row == 1 && entity.gridIndex.col == 1;
        assert entity.movementState == SimulationEntity.MovementState.GridMovement;

        entity.walkPath();
        assert entity.gridIndex.row == 2 && entity.gridIndex.col == 2;
        assert entity.movementState == SimulationEntity.MovementState.GridMovement;

        entity.walkPath();
        assert entity.gridIndex.row == 2 && entity.gridIndex.col == 3;
        assert entity.movementState == SimulationEntity.MovementState.Stopped;
    }


}