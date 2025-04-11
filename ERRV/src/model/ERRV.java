package model;

import skf.coder.HLAunicodeStringCoder;
import skf.model.object.annotations.Attribute;
import skf.model.object.annotations.ObjectClass;
import java.util.ArrayList;

@ObjectClass(name = "PhysicalInterface.ERRV")
public class ERRV {

	@Attribute(name = "name", coder = HLAunicodeStringCoder.class)
	private String name = null;

	@Attribute(name = "parent_name", coder = HLAunicodeStringCoder.class)
	private String parent_name = null;

	@Attribute(name = "position", coder = HLAPositionCoder.class)
	private Position position = null;

	private boolean availability;
	private double velocity = 0;

	// New attribute
	private double energyLevel;

	// New attribute: Arm
	private Arm arm;

	public ERRV() {
	}

	public ERRV(String name, String parent_name, Position position, double energyLevel, Arm arm) {
		this.name = name;
		this.parent_name = parent_name;
		this.position = position;
		this.availability = true;
		this.velocity = 10;
		this.energyLevel = energyLevel;
		this.arm = arm;
	}

	public ArrayList<Parts> sendParts() {
		ArrayList<Parts> parts = new ArrayList<>();
		parts.add(new Parts("Body", 100));
		parts.add(new Parts("Wheel", 50));
		parts.add(new Parts("Board", 50));
		parts.add(new Parts("Battery", 100));

		return parts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParent_name() {
		return parent_name;
	}

	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public boolean getAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getEnergyLevel() {
		return energyLevel;
	}

	public void setEnergyLevel(double energyLevel) {
		this.energyLevel = energyLevel;
	}

	public Arm getArm() {
		return arm;
	}

	public void setArm(Arm arm) {
		this.arm = arm;
	}

}
