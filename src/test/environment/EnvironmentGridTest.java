package test.environment;

import environment.EnvironmentGrid;
import environment.EnvironmentGridFactory;
import org.junit.Test;
import org.junit.rules.ExpectedException;


/**
 * Created by Andrew on 1/8/2017.
 */
public class EnvironmentGridTest {
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

        EnvironmentGrid grid = EnvironmentGridFactory.gridFromTXT(gridSource);

        //  Multiple place success
        grid.placeEntity(1, 0, 0, 1);
        grid.placeEntity(2, 4, 4, 1);
        grid.placeEntity(3, 6, 4, 1);

        assert grid.gridArray[0][0].hlaID == 1;
        assert grid.gridArray[4][4].hlaID == 2;
        assert grid.gridArray[4][6].hlaID == 3;

        EnvironmentGrid.printGrid(grid.gridArray);

        //  Duplicate place fails
        boolean annoying = false;
        try { grid.placeEntity(3, 9, 9, 1); } catch(EnvironmentGrid.PlacementException e) {annoying=true;}
        assert annoying;

        //  Blocked place fails
        annoying = false;
        try { grid.placeEntity(4, 0, 5, 1); } catch(EnvironmentGrid.PlacementException e) {annoying=true;}
        assert annoying;

        //  Direct overlap place fails (other entity)
        annoying = false;
        try { grid.placeEntity(4, 5, 5, 1); } catch(EnvironmentGrid.PlacementException e) {annoying=true;}
        assert annoying;

        //  Direct overlap place fails (blocked cell)
        grid.placeEntity(4, 4, 9, 2);
        EnvironmentGrid.printGrid(grid.gridArray);

        annoying = false;
        try { grid.placeEntity(5, 4, 8, 1); } catch(EnvironmentGrid.PlacementException e) {annoying=true;}
        assert annoying;

        //  Adjacent overlap place fails (other entity)
        annoying = false;
        try { grid.placeEntity(5, 4, 7, 2); } catch(EnvironmentGrid.PlacementException e) {annoying=true;}
        assert annoying;

        //  Adjacent overlap place fails (blocked cell)
        annoying = false;
        try { grid.placeEntity(5, 3, 4, 2); } catch(EnvironmentGrid.PlacementException e) {annoying=true;}
        assert annoying;

        //  Out of bounds fails
        annoying = false;
        try { grid.placeEntity(5, 30, 40, 2); } catch(EnvironmentGrid.PlacementException e) {annoying=true;}
        assert annoying;
    }

    @Test
    public void applyCollisions() throws Exception {

    }

    @Test
    public void findPath() throws Exception {

    }

    @Test
    public void debugPrintGrid() throws Exception {

    }

}