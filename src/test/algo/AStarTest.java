package test.algo;

import algo.AStar;
import entity.EnvironmentGridEntity;
import environment.EnvironmentGridFactory;
import environment.GridCell;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Andrew on 1/8/2017.
 */
public class AStarTest {
    final static Logger logger = Logger.getLogger(AStarTest.class);

    @org.junit.Test
    public void reconstructPath() throws Exception {

    }
    @org.junit.Test
    public void blockedPath() throws Exception {
        logger.debug("Beginning Test Case: pathFromGrid");

        String[] gridSource = {
                "S _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "B B B B B B B B B _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ B B B B B B B B B B B B B ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ B B B B B B B B B B _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ _ B B B B _ _ _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ _ _ _ _ B _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ _ B _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ B B B B B B ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ B _ _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ B _ _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ B _ _ _ E _ ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ B B B B B _ ",
                "_ _ _ _ _ _ B B B B _ B _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
        };

        GridCell[][] grid = new GridCell[20][20];
        GridCell start = null;
        GridCell end = null;

        for(int i = 0; i < 20; i++) {
            for(int j = 0; j < 20; j++) {
                char sourceChar = gridSource[i].charAt(j*2);

                grid[i][j] = new GridCell(j, i);

                if(sourceChar == 'B') {
                    grid[i][j].blocked = true;
                } else if(sourceChar == 'S') {
                    start = grid[i][j];
                } else if(sourceChar == 'E') {
                    end = grid[i][j];
                }
            }
        }

        assert start != null;
        assert end != null;

        AStar<GridCell> a = new AStar<>();
        List<GridCell> path = a.pathFromGrid(grid,
                new int[] {start.gridY, start.gridX},
                new int[] {end.gridY, end.gridX});

        assert path == null;

        printGridWithPath(grid, path, start, end);

        logger.debug("blockedPath completed successfully");
    }



    @org.junit.Test
    public void pathFromGrid() throws Exception {
        logger.debug("Beginning Test Case: pathFromGrid");

        String[] gridSource = {
                "S _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ E _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "B B B B B B B B B _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ B B B B B B B B _ _ _ _ ",
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

        GridCell[][] grid = new GridCell[20][20];
        GridCell start = null;
        GridCell end = null;

        for(int i = 0; i < 20; i++) {
            for(int j = 0; j < 20; j++) {
                char sourceChar = gridSource[i].charAt(j*2);

                grid[i][j] = new GridCell(j, i);

                if(sourceChar == 'B') {
                    grid[i][j].blocked = true;
                } else if(sourceChar == 'S') {
                    start = grid[i][j];
                } else if(sourceChar == 'E') {
                    end = grid[i][j];
                }
            }
        }

        assert start != null;
        assert end != null;

        AStar<GridCell> a = new AStar<>();
        List<GridCell> path = a.pathFromGrid(grid,
                new int[] {start.gridY, start.gridX},
                new int[] {end.gridY, end.gridX});

        assert path != null;

        printGridWithPath(grid, path, start, end);

        logger.debug("pathFromGrid completed successfully");
    }

    @Test
    public void accuracyTest() {
        String[] gridSource = {
                "S B E _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "B B _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ ",
        };

        EnvironmentGridEntity grid = EnvironmentGridFactory.gridFromTXT(gridSource);

        GridCell start = null;
        GridCell end = null;

        for(int i = 0; i < gridSource.length/2; i++) {
            for(int j = 0; j < gridSource[0].length()/2; j++) {
                char sourceChar = gridSource[i].charAt(j*2);

                GridCell cell = grid.gridArray[i][j];

                if(sourceChar == 'S') {
                    start = cell;
                } else if(sourceChar == 'E') {
                    end = cell;
                }
            }
        }

        AStar<GridCell> a = new AStar<>();
        List<GridCell> path = a.pathFromGrid(grid.gridArray,
                new int[] {start.gridY, start.gridX},
                new int[] {end.gridY, end.gridX});

        printGridWithPath(grid.gridArray, path, start, end);
        logger.debug(path);

        assert path.get(0).gridX == 2;
        assert path.get(0).gridY == 0;

        assert path.get(1).gridX == 1;
        assert path.get(1).gridY == 1;

        assert path.get(2).gridX == 0;
        assert path.get(2).gridY == 0;
    }

    public void printGridWithPath(GridCell[][] grid, List<GridCell> path, GridCell start, GridCell end) {
        Set<GridCell> pathSet = new HashSet<>();

        if(path!=null) {
            pathSet.addAll(path);
        }

        for(int i = 0; i < grid.length; i++) {
            StringBuilder sb = new StringBuilder();

            for(int j = 0; j < grid[0].length; j++) {
                GridCell cell = grid[i][j];
                if(cell.isBlocked() || cell.isOccupied()) {
                    sb.append("B ");
                } else if(cell == start) {
                    sb.append("S ");
                } else if(cell == end) {
                    sb.append("E ");
                } else if(pathSet.contains(cell)){
                    sb.append("X ");
                } else {
                    sb.append("_ ");
                }
            }

            logger.debug(sb);
        }
    }
}