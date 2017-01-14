package test.test;

import state.EnvironmentGridState;
import environment.EnvironmentGridFactory;
import environment.GridCell;
import execution.EnvironmentGridExecution;
import execution.SimulationEntityExecution;
import org.apache.log4j.Logger;
import siso.smackdown.utilities.Vector3;
import state.SimulationEntityState;
import test.test.passive.PassiveDummyRover;
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
    public SimulationEntityState.GridIndex isruIndex;
    public int gridWidth, gridHeight;

    public void drawGrid(Graphics g) {
        int cellSize = gridWidth / grid.length;

        for(int i=0; i<grid.length; i++) {
            for(int j=0; j<grid.length; j++) {
                g.setColor(Color.GREEN);
                g.drawRect((j*cellSize) + 6, (i*cellSize) + 6, cellSize, cellSize);

                if(grid[i][j].isBlocked()) {
                    g.setColor(Color.white);
                    g.fillRect((j*cellSize) + 6, (i*cellSize) + 6, cellSize, cellSize);
                } else if(grid[i][j].isOccupied()) {
                    g.setColor(Color.cyan);
                    g.drawOval((j*cellSize) + 9, (i*cellSize) + 9, cellSize-12, cellSize-12);
                }
            }
        }
    }

    public void drawEntities(Graphics g) {
        int cellSize = gridWidth / grid.length;

        g.setColor(Color.green);
        g.fillRect(this.isruIndex.col*cellSize+9, this.isruIndex.row*cellSize+9, cellSize-6, cellSize-6);

        for(SimulationEntityExecution execution : this.engine.entities) {
            if(execution instanceof DummyRoverExecution){
                DummyRoverState rover = (DummyRoverState) execution.simulationEntityState;
                g.setColor(Color.red);

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


                g.setColor(Color.white);
                g.drawString("" + rover, 15, gridHeight + (int)(25*rover.identifier) + 25);
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
                "_ _ R _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "B B B B B _ B B B _ _ _ _ _ _ _ _ R _ _ ",
                "_ _ _ _ _ _ _ B B B B B B _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ B B B B B B B B B B _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B B B B B _ _ _ _ _ _ ",
                "_ _ I _ _ _ B _ _ B _ B _ _ _ B _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ _ B _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ B B B B B B ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ B _ _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ B _ _ R _ _ ",
                "_ _ _ R _ _ B _ _ B _ B _ _ B _ _ _ _ _ ",
                "_ _ _ _ _ _ B _ _ B _ B _ _ B B B B B _ ",
                "_ _ _ _ _ _ B B _ B _ B _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
                "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ",
        };

        EnvironmentGridState grid = EnvironmentGridFactory.gridFromTXT(gridSource);
        EnvironmentGridExecution gridExecution = new EnvironmentGridExecution(grid, new Vector3(), 1.0);

        TestEngine engine = new TestEngine(gridExecution);

        TestRunner runner = new TestRunner(engine);

        runner.isruIndex = findISRU(gridSource);
        extractEntitiesFromTXT(gridSource, engine, runner.isruIndex);

        JFrame frame = new JFrame("UCF - Mars Sim Test Runner");
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(800, 900));
        panel.setLayout(null);

        runner.setBounds(0, 0, 800, 900);
        panel.add(runner);

        runner.setIgnoreRepaint(true);
        runner.gridWidth = 775;
        runner.gridHeight = 775;

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        runner.createBufferStrategy(2);
        strategy = runner.getBufferStrategy();

        Thread thread = new Thread(runner);
        thread.start();
    }

    private static SimulationEntityState.GridIndex findISRU(String[] gridSource) {
        for(int i=0; i < gridSource.length; i++) {
            for(int j=0; j < gridSource[0].length()/2; j++) {
                char c = gridSource[i].charAt(j*2);
                if(c == 'I') {
                    logger.debug(i + "," + j);
                    return new SimulationEntityState.GridIndex(i, j);
                }
            }
        }

        return null;
    }

    private static void extractEntitiesFromTXT(String[] gridSource, TestEngine engine, SimulationEntityState.GridIndex isruIndex) {
        long hlaID = 0;
        int isruX = isruIndex == null ? 0 : isruIndex.col;
        int isruY = isruIndex == null ? 0 : isruIndex.row;

        for(int i=0; i < gridSource.length; i++) {
            for(int j=0; j < gridSource[0].length()/2; j++) {
                char c = gridSource[i].charAt(j*2);
                if(c == 'R') {
                    DummyRoverState rover = new DummyRoverState(hlaID, j, i, isruX, isruY, 20.0, 0.2, 1.0);
                    DummyRoverExecution execution = new DummyRoverExecution(rover);
                    PassiveDummyRover mock = new PassiveDummyRover(hlaID, execution, engine.gridExecution);

                    engine.addEntity(hlaID, 1, execution, mock);
                    logger.debug("Added state: " + hlaID);

                    hlaID++;
                }
            }
        }
    }
}

