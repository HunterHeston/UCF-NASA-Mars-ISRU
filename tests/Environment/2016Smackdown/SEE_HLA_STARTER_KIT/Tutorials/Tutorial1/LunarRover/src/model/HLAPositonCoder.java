package model;

import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.*;
import hla.rti1516e.exceptions.RTIinternalError;
import skf.coder.Coder;

public class HLAPositonCoder implements Coder<Position> {
	
	private EncoderFactory factory = null;
	private HLAfixedArray<HLAfloat64LE> coder;
	
	private HLAfloat64LE x;
	private HLAfloat64LE y;
	private HLAfloat64LE z;
	
	public HLAPositonCoder() throws RTIinternalError {
		this.factory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
		this.x = factory.createHLAfloat64LE();
		this.y = factory.createHLAfloat64LE();
		this.z = factory.createHLAfloat64LE();

		this.coder = factory.createHLAfixedArray(x, y, z);
	}

	@Override
	public Position decode(byte[] arg0) throws DecoderException {
		coder.decode(arg0);
		return new Position(x.getValue(), y.getValue(), z.getValue());
	}

	@Override
	public byte[] encode(Position arg0) {
		this.x.setValue(arg0.getX());
		this.y.setValue(arg0.getY());
		this.z.setValue(arg0.getZ());
		return coder.toByteArray();
	}

	@Override
	public Class<Position> getAllowedType() {
		return Position.class;
	}

}
