package code;

import java.awt.Point;

public class Hostage extends Point {
	int damage;
	public Hostage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Hostage(int x, int y, int damage) {
		super(x, y);
		this.damage = damage;
	}

	public Hostage(Point p, int damage) {
		super(p);
		this.damage = damage;
		// TODO Auto-generated constructor stub
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
}
