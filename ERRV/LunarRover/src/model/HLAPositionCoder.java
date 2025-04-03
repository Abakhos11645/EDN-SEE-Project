package model;

import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAfloat64LE;
import hla.rti1516e.exceptions.RTIinternalError;
import skf.coder.Coder;

public class HLAPositionCoder implements Coder<Position> {
	
	private EncoderFactory factory = null;
	private HLAfixedRecord coder = null;
	
	private HLAfloat64LE x;
	private HLAfloat64LE y;
	private HLAfloat64LE z;
	
	public HLAPositionCoder() throws RTIinternalError {
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
	public Position decode(byte[] arg0) throws DecoderException {
		coder.decode(arg0);
		
		this.x = (HLAfloat64LE) coder.get(0);
		this.y = (HLAfloat64LE) coder.get(1);
		this.z = (HLAfloat64LE) coder.get(2);
		
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
