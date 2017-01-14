package entity;

/**
 * Created by rick on 1/13/17.
 */
public class SimpleChargeableEntity extends ChargeableEntity {
    public double capacity;
    public double currentCharge;

    public SimpleChargeableEntity(int gridX, int gridY,
                                  int isruGridX, int isruGridY,
                                  double capacity) {
        super(gridX, gridY, isruGridX, isruGridY);
        this.capacity = capacity;
        this.currentCharge = 0.0;
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
        return this.currentCharge < this.capacity * 0.2;
    }

    @Override
    public void doCharge(Object chargeAmount) {
        this.currentCharge = Math.min(capacity, this.currentCharge + (double) chargeAmount);
    }

    @Override
    public void useCharge() {
        if(this.chargeState != ChargeState.Charging) {
            if(this.movementState == MovementState.InMotion) {
                this.currentCharge = Math.max(0.0, this.currentCharge-0.2);
            }
        }
    }
}
