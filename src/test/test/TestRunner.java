package test.test;

import environment.EnvironmentGrid;
import environment.EnvironmentGridFactory;
import environment.GridCell;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Andrew on 1/12/2017.
 */
public class TestRunner extends JPanel implements Runnable {

    public GridCell[][] grid;

    public void drawGrid(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawRect(6, 6, 500, 500);

        int cellSize = 500 / grid.length;

        for(int i=0; i<grid.length; i++) {
            for(int j=0; j<grid.length; j++) {
                g.setColor(Color.GREEN);
                g.drawRect((j*cellSize) + 6, (i*cellSize) + 6, cellSize, cellSize);

                g.setColor(Color.white);
                //g.drawChars(("["+j+","+i+"]").toCharArray(), 0, 10, ((j+1)*cellSize) + 6, ((i+1)*cellSize) + 6);
            }
        }
    }

    public void drawEntities(Graphics g) {
        int cellSize = 500 / grid.length;

        for(int i=0; i<grid.length; i++) {
            for(int j=0; j<grid[0].length; j++) {
                GridCell cell = grid[i][j];
                if(cell.isBlocked()) {
                    g.setColor(Color.gray);
                    g.fillRect((j*cellSize) + 6, (i*cellSize) + 6, cellSize, cellSize);
                } else if (cell.isOccupied()) {
                    g.setColor(Color.white);
                    g.drawChars(new char[] {'E'}, 0, 1, ((j+1)*cellSize)-(cellSize/2), ((i+1)*cellSize));
                }
            }
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                update();
                Thread.sleep(200);
            } catch(Exception e) {

            }
        }
    }

    public void clearScreen(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public void update() {
        Graphics g = getGraphics();
        clearScreen(g);

        drawGrid(g);
        drawEntities(g);

        paintComponents(g);
    }

    public TestRunner(GridCell[][] grid) {
        this.grid = grid;
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("UCF - Mars Sim Test Runner");
        frame.setSize(new Dimension(530, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        grid.placeEntity(1, 0, 0, 1);
        grid.placeEntity(2, 4, 4, 1);
        grid.placeEntity(3, 6, 4, 1);

        TestRunner runner = new TestRunner(grid.gridArray);
        frame.getContentPane().add(runner);

        frame.setVisible(true);
        Thread thread = new Thread(runner);
        thread.start();
    }
}

