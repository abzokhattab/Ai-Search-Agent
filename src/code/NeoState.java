package code;

import java.awt.Point;
import java.util.ArrayList;

public class NeoState extends State{
	Point neo;
	int c;
	Point telephoneBooth;
	ArrayList<Point> agents;
	ArrayList<Point> pills; 
	ArrayList<Point> pads; 
	ArrayList<Point> hostages;
	int damage; //cumulative or just the new damage?
	
	
	public NeoState(Point neo, int c, Point telephoneBooth, ArrayList<Point> agents, ArrayList<Point> pills, ArrayList<Point> pads, ArrayList<Point> hostages, int damage) {
		
		super();
		this.neo = neo;
		this.c = c;
		this.telephoneBooth = telephoneBooth;
		this.agents = agents;
		this.pills = pills;
		this.pads = pads;
		this.hostages = hostages;
		this.damage = damage;
		this.pathCost = this.damage;
		this.heuristicOne=heuristicFunction();
		this.heuristicTwo=heuristicFunction();
		
	}
	
	public int heuristicFunction() {
		
		
		return (Math.abs((this.telephoneBooth.y-this.neo.y)+(this.telephoneBooth.x-this.neo.x)));
		
	}
	
//	public int heuristicFunctionTwo() {
//		
//	}

	public static void main(String[]args) {
		
	}
}



