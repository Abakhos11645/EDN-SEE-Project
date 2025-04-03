package model;

public class ObjectToDeliver {

	private String param = null;
	private String name = null;
	private Position position = null;

	public ObjectToDeliver() {

	}

	public ObjectToDeliver(String param) {

		this.param = param;
		String[] paramSplited = param.split(",");
		this.name = paramSplited[0];
		this.position = new Position(Double.parseDouble(paramSplited[1]), Double.parseDouble(paramSplited[2]),
				Double.parseDouble(paramSplited[3]));

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

}
