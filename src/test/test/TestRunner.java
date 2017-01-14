package test.test;

import entity.EnvironmentGridEntity;
import environment.EnvironmentGridFactory;
import environment.GridCell;
import federate.EnvironmentGridExecution;
import federate.SimulationEntityExecution;
import siso.smackdown.utilities.Vector3;
import test.federate.mock.TestRoverMock;
import test.test.rover.TestRover;
import test.test.rover.TestRoverExecution;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Andrew on 1/12/2017.
 */
public class TestRunner extends JPanel implements Runnable {

    public GridCell[][] grid;
    public TestEngine engine;

    public void drawGrid(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawRect(6, 6, 500, 500);

        int cellSize = 500 / grid.length;

        for(int i=0; i<grid.length; i++) {
            for(int j=0; j<grid.length; j++) {
                g.setColor(Color.GREEN);
                g.drawRect((j*cellSize) + 6, (i*cellSize) + 6, cellSize, cellSize);

                if(grid[i][j].isBlocked()) {
                    g.setColor(Color.white);
                    g.fillRect((j*cellSize) + 6, (i*cellSize) + 6, cellSize, cellSize);
                }
            }
        }
    }

    public void drawEntities(Graphics g) {
        for(SimulationEntityExecution execution : this.engine.entities) {
            if(execution instanceof TestRoverExecution){
                g.setColor(Color.red);
                int cellSize = 500 / grid.length;

                g.fillOval((int)(execution.simulationEntity.position[0]*cellSize) + 6,
                            (int)(execution.simulationEntity.position[1]*cellSize) + 6,
                            cellSize, cellSize);

            }
        }

    }

    @Override
    public void run() {
        while(true) {
            try {
                update();
                Thread.sleep(100);
            } catch(Exception e) {

            }
        }
    }

    public void clearScreen(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public void update() {
        this.engine.update();

        Graphics g = getGraphics();
        clearScreen(g);

        drawGrid(g);
        drawEntities(g);

        paintComponents(g);
    }

    public TestRunner(TestEngine engine) {
        this.engine = engine;
        this.grid = engine.gridEntity.gridArray;
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("UCF - Mars Sim Test Runner");
        frame.setSize(new Dimension(530, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] gridSource = {
                "R _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "B B B B B _ B B B _ _ _ _ _ _ _ _ _ _ _ ",
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

        EnvironmentGridEntity grid = EnvironmentGridFactory.gridFromTXT(gridSource);
        EnvironmentGridExecution gridExecution = new EnvironmentGridExecution(grid, new Vector3(), 1.0);

        TestEngine engine = new TestEngine(gridExecution);

        extractEntitiesFromTXT(gridSource, engine);

        TestRunner runner = new TestRunner(engine);
        frame.getContentPane().add(runner);

        frame.setVisible(true);
        Thread thread = new Thread(runner);
        thread.start();
    }

    private static void extractEntitiesFromTXT(String[] gridSource, TestEngine engine) {
        long hlaID = 0;

        for(int i=0; i < gridSource.length; i++) {
            for(int j=0; j < gridSource[0].length()/2; j++) {
                char c = gridSource[i].charAt(j*2);
                if(c == 'R') {
                    TestRover rover = new TestRover(j, i, 0.2, 1.0);
                    TestRoverExecution execution = new TestRoverExecution(rover);
                    TestRoverMock mock = new TestRoverMock(hlaID, execution, engine.gridExecution);

                    engine.addEntity(hlaID, 1, execution, mock);

                    hlaID++;
                }
            }
        }
    }
}

