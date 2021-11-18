package code;

import java.awt.Point;

public class Pad {
	Point startPad;
	Point finishPad;
	public Pad(Point startPad, Point finishPad) {
		super();
		this.startPad = startPad;
		this.finishPad = finishPad;
	}
	public Point getStartPad() {
		return startPad;
	}
	public void setStartPad(Point startPad) {
		this.startPad = startPad;
	}
	public Point getFinishPad() {
		return finishPad;
	}
	public void setFinishPad(Point finishPad) {
		this.finishPad = finishPad;
	}
	
	

}
