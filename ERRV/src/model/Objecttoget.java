package model;

public class Objecttoget {

	private String param = null;
	private String name = null;
	private Position position = null;
	private double weight = 0.0; // New attribute

	public Objecttoget() {
	}

	public Objecttoget(String param) {
		this.param = param;
		String[] paramSplited = param.split(",");
		this.name = paramSplited[0];
		this.position = new Position(
				Double.parseDouble(paramSplited[1]),
				Double.parseDouble(paramSplited[2]),
				Double.parseDouble(paramSplited[3]));

		// If weight is provided in the param string
		if (paramSplited.length > 4) {
			this.weight = Double.parseDouble(paramSplited[4]);
		}
	}

	public String getParam() {
		return this.param;
	}

	public String getName() {
		return this.name;
	}

	public double getX() {
		return this.position.getX();
	}

	public double getY() {
		return this.position.getY();
	}

	public double getZ() {
		return this.position.getZ();
	}

	public String getPosition() {
		return "Position [x=" + getX() + ", y=" + getY() + ", z=" + getZ() + "]";
	}

	// Getter for weight
	public double getWeight() {
		return weight;
	}

	// Setter for weight
	public void setWeight(double weight) {
		this.weight = weight;
	}
}
