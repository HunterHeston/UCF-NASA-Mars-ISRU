package federate;

import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAfloat64LE;
import hla.rti1516e.exceptions.RTIinternalError;
import skf.coder.Coder;

/**
 * Created by Andrew on 11/4/2016.
 */
public class PositionVectorCoder implements Coder<PositionVector> {

    private EncoderFactory factory = null;
    private HLAfixedRecord coder = null;

    private HLAfloat64LE x, y, z;

    public PositionVectorCoder() throws RTIinternalError {
        this.factory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
        this.coder = factory.createHLAfixedRecord();

        this.x = factory.createHLAfloat64LE();
        this.y = factory.createHLAfloat64LE();
        this.z = factory.createHLAfloat64LE();

        coder.add(x);
        coder.add(y);
        coder.add(z);
    }


    @Override
    public PositionVector decode(byte[] bytes) throws DecoderException {
        coder.decode(bytes);
        return new PositionVector(x.getValue(), y.getValue(), z.getValue());
    }

    @Override
    public byte[] encode(PositionVector vector) {
        this.x.setValue(vector.x);
        this.y.setValue(vector.y);
        this.z.setValue(vector.z);

        return coder.toByteArray();
    }

    @Override
    public Class<PositionVector> getAllowedType() {
        return PositionVector.class;
    }
}
