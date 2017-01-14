package state;

/**
 * Created by rick on 1/13/17.
 */
public class SimpleChargeableEntityState extends ChargeableEntityState {
    public double capacity;
    public double currentCharge;

    public SimpleChargeableEntityState(int gridX, int gridY,
                                       int isruGridX, int isruGridY,
                                       double capacity, double movementSpeed, double gridCellSize) {
        super(gridX, gridY, isruGridX, isruGridY, movementSpeed, gridCellSize);
        this.capacity = capacity;
        this.currentCharge = capacity;
    }

    @Override
    public boolean isDead() {
        if(this.currentCharge <= 0) {
            this.currentCharge = 0;
            return true;
        }

        return false;
    }

    @Override
    public boolean chargeFull() {
        if(this.currentCharge >= capacity) {
            this.currentCharge = capacity;
            return true;
        }

        return false;
    }

    @Override
    public boolean needsCharge() {
        return this.currentCharge < this.capacity * 0.5;
    }

    @Override
    public void doCharge(Object chargeAmount) {
        this.currentCharge = Math.min(capacity, this.currentCharge + (double) chargeAmount);
    }

    @Override
    public void useCharge() {
        if(this.chargeState != ChargeState.Charging) {
            if(this.movementState == MovementState.InMotion) {
                this.currentCharge = Math.max(0.0, this.currentCharge-0.05);
            }
        }
    }

    @Override
    public Object getCharge() {
        return this.currentCharge;
    }
}
