package environment;

/**
 * Created by Andrew on 1/1/2017.
 */
public class RegolithData {
    private double waterContent, quantityKG;

    public RegolithData(double waterContent, double quantityKG) {
        this.setWaterContent(waterContent);
        this.setQuantityKG(quantityKG);
    }

    public double getWaterContent() {
        return waterContent;
    }

    public void setWaterContent(double waterContent) {
        assert waterContent > 0.0 && waterContent < 1.0;
        this.waterContent = waterContent;
    }

    public double getQuantityKG() {
        return quantityKG;
    }

    public void setQuantityKG(double quantityKG) {
        this.quantityKG = quantityKG;
    }
}
