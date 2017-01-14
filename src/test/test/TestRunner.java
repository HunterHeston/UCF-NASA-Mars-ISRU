package test.test;

import state.EnvironmentGridState;
import environment.EnvironmentGridFactory;
import environment.GridCell;
import execution.EnvironmentGridExecution;
import execution.SimulationEntityExecution;
import org.apache.log4j.Logger;
import siso.smackdown.utilities.Vector3;
import test.federate.passive.PassiveChargeableRover;
import state.DummyRoverState;
import execution.DummyRoverExecution;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created by Andrew on 1/12/2017.
 */
public class TestRunner extends Canvas implements Runnable {
    final static Logger logger = Logger.getLogger(TestRunner.class);

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
            if(execution instanceof DummyRoverExecution){
                g.setColor(Color.red);
                int cellSize = 500 / grid.length;

                g.fillOval((int)(execution.simulationEntityState.position[0]*cellSize) + 6,
                            (int)(execution.simulationEntityState.position[1]*cellSize) + 6,
                            cellSize, cellSize);

                g.setColor(Color.blue);
                g.drawLine(execution.simulationEntityState.finalGridIndex.col*cellSize+6,
                        execution.simulationEntityState.finalGridIndex.row*cellSize+6,
                        execution.simulationEntityState.finalGridIndex.col*cellSize+cellSize+6,
                        execution.simulationEntityState.finalGridIndex.row*cellSize+cellSize+6);

                g.drawLine(execution.simulationEntityState.finalGridIndex.col*cellSize+cellSize+6,
                        execution.simulationEntityState.finalGridIndex.row*cellSize+6,
                        execution.simulationEntityState.finalGridIndex.col*cellSize+6,
                        execution.simulationEntityState.finalGridIndex.row*cellSize+cellSize+6);

                g.setColor(Color.green);

                DummyRoverState rover = (DummyRoverState) execution.simulationEntityState;
                g.fillRect(rover.isruIndex.col*cellSize+9, rover.isruIndex.col*cellSize+9, cellSize-6, cellSize-6);

                g.setColor(Color.white);
                g.drawString("" + rover.getCharge(), 50, 550);

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
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ R _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
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

        EnvironmentGridState grid = EnvironmentGridFactory.gridFromTXT(gridSource);
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
                    DummyRoverState rover = new DummyRoverState(j, i, 8, 7, 20.0, 0.2, 1.0);
                    DummyRoverExecution execution = new DummyRoverExecution(rover);
                    PassiveChargeableRover mock = new PassiveChargeableRover(hlaID, execution, engine.gridExecution);

                    engine.addEntity(hlaID, 1, execution, mock);
                    logger.debug("Added state: " + hlaID);

                    hlaID++;
                }
            }
        }
    }
}

