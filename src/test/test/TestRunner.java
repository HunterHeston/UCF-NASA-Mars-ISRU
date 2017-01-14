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
import java.awt.image.BufferStrategy;

/**
 * Created by Andrew on 1/12/2017.
 */
public class TestRunner extends Canvas implements Runnable {

    public static BufferStrategy strategy;
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

                g.setColor(Color.blue);
                g.drawLine(execution.simulationEntity.finalGridIndex.col*cellSize+6,
                        execution.simulationEntity.finalGridIndex.row*cellSize+6,
                        execution.simulationEntity.finalGridIndex.col*cellSize+cellSize+6,
                        execution.simulationEntity.finalGridIndex.row*cellSize+cellSize+6);

                g.drawLine(execution.simulationEntity.finalGridIndex.col*cellSize+cellSize+6,
                        execution.simulationEntity.finalGridIndex.row*cellSize+6,
                        execution.simulationEntity.finalGridIndex.col*cellSize+6,
                        execution.simulationEntity.finalGridIndex.row*cellSize+cellSize+6);


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

        Graphics g = strategy.getDrawGraphics();
        clearScreen(g);

        drawGrid(g);
        drawEntities(g);

        g.dispose();
        strategy.show();
    }

    public TestRunner(TestEngine engine) {
        this.engine = engine;
        this.grid = engine.gridEntity.gridArray;
    }

    public static void main(String[] args) throws Exception {
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
                "_ _ _ _ _ _ B _ _ B _ B _ _ _ B _ _ _ _ ",
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

        JFrame frame = new JFrame("UCF - Mars Sim Test Runner");
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(530, 600));
        panel.setLayout(null);

        runner.setBounds(0, 0, 530, 600);
        panel.add(runner);

        runner.setIgnoreRepaint(true);

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        runner.createBufferStrategy(2);
        strategy = runner.getBufferStrategy();

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

