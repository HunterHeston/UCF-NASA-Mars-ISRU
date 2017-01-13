package environment;

import entity.EnvironmentGridEntity;
import siso.smackdown.utilities.Vector3;
import skf.core.SEEAbstractFederate;
import skf.core.SEEAbstractFederateAmbassador;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Andrew on 1/1/2017.
 */
public class EnvironmentExecution extends SEEAbstractFederate implements Observer {

    public Vector3 gridOrigin;
    public double gridCellSize;
    public int gridHeight, gridWidth;
    public EnvironmentGridEntity grid;

    public EnvironmentExecution(SEEAbstractFederateAmbassador federateAmbassador) {
        this(federateAmbassador, new Vector3(), 1.0f, 10, 10);
    }

    public EnvironmentExecution(SEEAbstractFederateAmbassador federateAmbassador, Vector3 gridOrigin, double gridCellSize, int gridHeight, int gridWidth) {
        super(federateAmbassador);
        this.gridOrigin = gridOrigin;
        this.gridCellSize = gridCellSize;
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;

        this.grid = new EnvironmentGridEntity(this.gridWidth, this.gridHeight);
    }

    @Override
    protected void doAction() {
        EnvironmentGridEntity.printGrid(this.grid.gridArray);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    protected void doPlaceEntity(int hlaID, int targetX, int targetY, int collisionRadius) {
        try {
            boolean success = this.grid.placeEntity(hlaID, targetX, targetY, collisionRadius);
            if(success) {

            }

        } catch (EnvironmentGridEntity.PlacementException e) {
            System.out.print("Unhandled Exception placing entity " + hlaID);
            e.printStackTrace();
        }
    }

    protected void doGridMovement(long hlaID, Vector3 movementVector) {

    }


}
