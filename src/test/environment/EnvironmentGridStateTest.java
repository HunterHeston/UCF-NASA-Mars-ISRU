package test.environment;

import state.EnvironmentGridState;
import environment.EnvironmentGridFactory;
import environment.GridCell;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Andrew on 1/8/2017.
 */
public class EnvironmentGridStateTest {
    final static Logger logger = Logger.getLogger(EnvironmentGridStateTest.class);

    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void placeEntity() throws Exception {
        String[] gridSource = {
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "B B B B B B B B B _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ B B B B B B _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ B B B B B B B B B B _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ _ B B B B _ _ _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ _ _ _ _ B _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ _ B _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ B B B B B B ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ B _ _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ B _ _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ B _ _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ B B B B B _ ",
                "_ _ _ _ _ _ B B B B _ B _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
        };

        EnvironmentGridState grid = EnvironmentGridFactory.gridFromTXT(gridSource);

        //  Multiple place success
        grid.placeEntity(1, 0, 0, 1);
        grid.placeEntity(2, 4, 4, 1);
        grid.placeEntity(3, 6, 4, 1);

        assert grid.gridArray[0][0].hlaID == 1;
        assert grid.gridArray[4][4].hlaID == 2;
        assert grid.gridArray[4][6].hlaID == 3;

        EnvironmentGridState.printGrid(grid.gridArray);

        //  Duplicate place fails
        boolean annoying = false;
        try { grid.placeEntity(3, 9, 9, 1); } catch(EnvironmentGridState.PlacementException e) {annoying=true;}
        assert annoying;

        //  Blocked place fails
        annoying = false;
        try { grid.placeEntity(4, 0, 5, 1); } catch(EnvironmentGridState.PlacementException e) {annoying=true;}
        assert annoying;

        //  Direct overlap place fails (other state)
        annoying = false;
        try { grid.placeEntity(4, 5, 5, 1); } catch(EnvironmentGridState.PlacementException e) {annoying=true;}
        assert annoying;

        //  Direct overlap place fails (blocked cell)
        grid.placeEntity(4, 4, 9, 2);
        EnvironmentGridState.printGrid(grid.gridArray);

        annoying = false;
        try { grid.placeEntity(5, 4, 8, 1); } catch(EnvironmentGridState.PlacementException e) {annoying=true;}
        assert annoying;

        //  Adjacent overlap place fails (other state)
        annoying = false;
        try { grid.placeEntity(5, 4, 7, 2); } catch(EnvironmentGridState.PlacementException e) {annoying=true;}
        assert annoying;

        //  Adjacent overlap place fails (blocked cell)
        annoying = false;
        try { grid.placeEntity(5, 3, 4, 2); } catch(EnvironmentGridState.PlacementException e) {annoying=true;}
        assert annoying;

        //  Out of bounds fails
        annoying = false;
        try { grid.placeEntity(5, 30, 40, 2); } catch(EnvironmentGridState.PlacementException e) {annoying=true;}
        assert annoying;
    }

    @Test
    public void findPath() throws Exception {
        String[] gridSource = {
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "B B _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
        };

        EnvironmentGridState grid = EnvironmentGridFactory.gridFromTXT(gridSource);

        GridCell start = grid.gridArray[0][0];
        GridCell end   = grid.gridArray[3][0];

        GridCell[] path = grid.findPath(start, end);

        grid.printGridWithPath(grid.gridArray, path, start, end);

        assert path[0].col == 1;
        assert path[0].row == 0;

        assert path[1].col == 2;
        assert path[1].row == 1;

        assert path[2].col == 2;
        assert path[2].row == 2;

        assert path[3].col == 2;
        assert path[3].row == 3;

        assert path[4].col == 1;
        assert path[4].row == 3;

    }

    @Test
    public void gridMoveTest() throws Exception {
        String[] gridSource = {
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ E E _ _ _ _ _ _ ",
                "B B B B _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
        };

        EnvironmentGridState grid = EnvironmentGridFactory.gridFromTXT(gridSource);
        GridCell[] entities = getEntityCellsFromText(grid, gridSource);

        logger.debug("Grid Move Initial");
        grid.printGrid(grid.gridArray);
        GridCell cell = entities[1];

        //  Normal Move
        long hlaID = cell.hlaID;
        grid.gridMove(hlaID, 4, 1);

        assert grid.entityToGridCellMap.get(hlaID).col == 4;
        assert grid.entityToGridCellMap.get(hlaID).row == 1;

        logger.debug("Grid Move Final");
        grid.printGrid(grid.gridArray);

        //  Too Far Move
        assert !grid.gridMove(hlaID, 6, 1);
        assert grid.entityToGridCellMap.get(hlaID).col == 4;
        assert grid.entityToGridCellMap.get(hlaID).row == 1;

        //  Blocked Move
        assert !grid.gridMove(hlaID, 3,  2);
        assert grid.entityToGridCellMap.get(hlaID).col == 4;
        assert grid.entityToGridCellMap.get(hlaID).row == 1;

        //  Occupied Move
        assert grid.gridMove(hlaID, 3, 1);
        assert !grid.gridMove(hlaID, 2, 1);
        assert grid.entityToGridCellMap.get(hlaID).col == 3;
        assert grid.entityToGridCellMap.get(hlaID).row == 1;

        //  Out of Bounds Move
        assert grid.gridMove(hlaID, 2, 0);
        boolean annoying = false;
        try {grid.gridMove(hlaID, 2, -1);} catch(Exception e) {annoying=true;}

        assert annoying;
        assert grid.entityToGridCellMap.get(hlaID).col == 2;
        assert grid.entityToGridCellMap.get(hlaID).row == 0;

    }

    public static GridCell[] getEntityCellsFromText(EnvironmentGridState grid, String[] gridSource) throws Exception {
        ArrayList<GridCell> cellList = new ArrayList<>();

        for(int i=0; i<gridSource.length; i++) {
            for(int j=0; j<gridSource[0].length()/2; j++) {
                if(gridSource[i].charAt(j*2) == 'E') {
                    grid.placeEntity(cellList.size()+1, j, i, 1);
                    cellList.add(grid.gridArray[i][j]);
                }
            }
        }

        return Arrays.copyOf(cellList.toArray(), cellList.size(), GridCell[].class);
    }

}