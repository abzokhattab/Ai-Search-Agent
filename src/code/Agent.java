package code;

import java.awt.Point;

public class Agent extends Point {
	boolean hostage;
	public Agent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Agent(int x, int y, boolean hostage) {
		super(x, y);
		this.hostage = hostage;
	}

	public Agent(Point p, boolean hostage) {
		super(p);
		this.hostage = hostage;
		// TODO Auto-generated constructor stub
	}

	public boolean isHostage() {
		return hostage;
	}

	public void setHostage(boolean hostage) {
		this.hostage = hostage;
	}


}
