package environment;

import state.EnvironmentGridState;

/**
 * Created by Andrew on 1/8/2017.
 */
public class EnvironmentGridFactory {
    public static EnvironmentGridState gridFromTXT(String[] gridLines) {
        assert gridLines.length > 0;

        int height = gridLines.length;
        int width  = gridLines[0].length()/2;

        EnvironmentGridState grid = new EnvironmentGridState(width, height);

        for(int i=0; i<height; i++) {
            String txtLine = gridLines[i];
            assert txtLine.length() == width * 2;

            for(int j=0; j<width; j++) {
                char c = txtLine.charAt(j*2);
                if(c == 'B') {
                    GridCell cell = grid.gridArray[i][j];
                    cell.blocked = true;
                }
            }
        }

        return grid;
    }

    public static EnvironmentGridState gridFromTXT(String txtGrid) {
        String[] gridLines = txtGrid.split("\n");
        assert gridLines.length > 0;

        return gridFromTXT(gridLines);
    }

}
