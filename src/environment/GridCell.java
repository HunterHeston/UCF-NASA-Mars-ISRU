package environment;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Andrew on 1/1/2017.
 */
public class GridCell {

    public boolean blocked;
    public int col, row, collisionRadiusOverlap;
    public long hlaID;

    public ArrayList<RegolithData> regolithContent;

    public GridCell(int col, int row) {
        this.collisionRadiusOverlap = 0;
        this.col = col;
        this.row = row;

        this.hlaID = Integer.MAX_VALUE;
        this.blocked = false;

        this.regolithContent = new ArrayList<>();

        Random r = new Random(System.currentTimeMillis());
        r.nextDouble();

        for(int i=0;i<10;i++) {
            RegolithData rd = new RegolithData(Math.min(r.nextDouble()*5.0, 0.5), 10.0);
            this.regolithContent.add(rd);
        }
    }

    public boolean isBlocked() {
        return this.blocked;
    }

    public boolean isOccupied() {
        return this.hlaID != Integer.MAX_VALUE || this.collisionRadiusOverlap > 0;
    }

    public void removeEntity() {
        this.hlaID = Integer.MAX_VALUE;
    }

    public void placeEntity(long hlaID) {
        assert this.hlaID == Integer.MAX_VALUE;
        this.hlaID = hlaID;
    }

    public double averageWaterContent() {
        double totalWaterContent = 0.0;
        double totalMassKG = 0.0;

        for(RegolithData rd : this.regolithContent) {
            totalWaterContent += rd.getWaterContent();
            totalMassKG += rd.getQuantityKG();
        }

        return totalWaterContent / (double) this.regolithContent.size();
    }

    public String toString() {
        return "Cell: [" + this.row + "][" + this.col + "]: B=" + this.isBlocked() + " O=" + this.isOccupied();
    }

}
