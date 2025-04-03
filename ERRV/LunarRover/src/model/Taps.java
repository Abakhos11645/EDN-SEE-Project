package model;

import skf.coder.HLAunicodeStringCoder;
import skf.model.object.annotations.Attribute;
import skf.model.object.annotations.ObjectClass;

@ObjectClass(name = "PhysicalInterface.Taps")
public class Taps {

	@Attribute(name = "name", coder = HLAunicodeStringCoder.class)
	private String name = null;

	@Attribute(name = "parent_name", coder = HLAunicodeStringCoder.class)
	private String parent_name = null;

	@Attribute(name = "position", coder = HLAPositionCoder.class)
	private Position position = null;

    private boolean availability;
    private double velocity = 0;

	public Taps(){}

	public Taps(String name, String parent_name, Position position) {
		this.name = name;
		this.parent_name = parent_name;
		this.position = position;
		this.availability = true;
		this.velocity = 10;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parent_name
	 */
	public String getParent_name() {
		return parent_name;
	}

	/**
	 * @param parent_name the parent_name to set
	 */
	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	public boolean getAvailability() {
		return availability;
		// TODO Auto-generated method stub
	}

	public void setAvailability(boolean b) {
		this.availability = b;
		// TODO Auto-generated method stub
	}

	public double getVelocity() {
		return this.velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}


}
