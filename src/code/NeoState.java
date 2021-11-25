package code;

import java.awt.Point;
import java.util.ArrayList;

public class NeoState extends State{
	Point neo;
	int c;
	Point telephoneBooth;
	ArrayList<Agent> agents;
	ArrayList<Point> pills; 
	ArrayList<Pad> pads; 
	ArrayList<Hostage> hostages;
	ArrayList<Hostage> carriedHostages;
	int damage; //cumulative or just the new damage?
	boolean tookPill = false;
	String[][] grid;
	
	public NeoState(Point neo, int c, Point telephoneBooth, ArrayList<Agent> agents, ArrayList<Point> pills, 
			ArrayList<Pad> pads, ArrayList<Hostage> hostages, ArrayList<Hostage> carriedHostages, boolean tookPill, int damage, int m, int n) {
		
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
		this.carriedHostages = carriedHostages;
		this.tookPill = tookPill;
		this.grid = new String[m][n];
		this.heuristicOne=heuristicFunction();
		this.heuristicTwo=heuristicFunction();
		populateGrid();

	}
	
	public int heuristicFunction() {
		
		
		return (Math.abs((this.telephoneBooth.y-this.neo.y)+(this.telephoneBooth.x-this.neo.x)));
		
	}
	
//	public int heuristicFunctionTwo() {
//		
//	}

	@Override
	public String toString() {
		String result = "";
		result += "N" + neo.x + "," + neo.y + ";";
		//result += c + ";";
		result += "TB" + telephoneBooth.x + "," + telephoneBooth.y + "; A";
		for(int i = 0; i < agents.size(); i++) {
			if(i != agents.size() - 1)
				result += agents.get(i).x + "," + agents.get(i).y + "," + agents.get(i).isKilled() + "," + agents.get(i).isHostage();
			else
				result += agents.get(i).x + "," + agents.get(i).y + "," + agents.get(i).isKilled() + "," + agents.get(i).isHostage() + ";";
		}
		result += " P";
		for(int i = 0; i < pills.size(); i++) {
			if(i != pills.size() - 1)
				result += pills.get(i).x + "," + pills.get(i).y + ",";
			else
				result += pills.get(i).x + "," + pills.get(i).y + ";";	
		}
		result += " PAD";

//		for(int i = 0; i < pads.size(); i+=2) {
//			if(i != pads.size() - 1)
//				result += pads.get(i).getStartPad().x + "," + pads.get(i).getStartPad().y + "," + pads.get(i).getFinishPad().x + "," + pads.get(i).getFinishPad().y + ",";
//			else
//				result += pads.get(i).getStartPad().x + "," + pads.get(i).getStartPad().y + "," + pads.get(i).getFinishPad().x + "," + pads.get(i).getFinishPad().y + ";";
//		}
		result += " H";

		for(int i = 0; i < hostages.size(); i++) {
			if(i != hostages.size() - 1)
				result += hostages.get(i).x + "," + hostages.get(i).y + "," + hostages.get(i).getDamage() + ",";
			else
				result += hostages.get(i).x + "," + hostages.get(i).y + "," + hostages.get(i).getDamage() + ";";
		}
		result += " CH";

		for(int i = 0; i < carriedHostages.size(); i++) {
			if(i != carriedHostages.size() - 1)
				result += carriedHostages.get(i).x + "," + carriedHostages.get(i).y + "," + carriedHostages.get(i).getDamage() + ",";
			else
				result += carriedHostages.get(i).x + "," + carriedHostages.get(i).y + "," + carriedHostages.get(i).getDamage() + ";" ;
		}
		result += " D";

		result +=  damage + ";" + tookPill + ";";
		
		return result;
//		for (int i = 0; i < grid.length; i++) {
//			for (int j = 0; j < grid[i].length; j++) {
//				if(grid[i][j] == null)
//					grid[i][j] = "e";
//				result += " " + grid[i][j] + " |";
//			}
//			result += '\n';
//		}
//		result += '\n';
//		return result;
	}
	public void visaulizeState() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if(grid[i][j] == null)
					grid[i][j] = "e";
				System.out.print(" " + grid[i][j] + " ");
				System.out.print("|");
			}
			System.out.println("");
			System.out.println("");
		}
	}
	public void populateGrid() {
		
		for(int i = 0; i < agents.size(); i++) {
			Agent agent = agents.get(i);
			grid[agent.x][agent.y] = "A";
		}
		for(int i = 0; i < pills.size(); i++) {
			Point pill = pills.get(i);
			grid[pill.x][pill.y] = "P";
		}
		for(int i = 0; i < pads.size(); i += 2) {
			Point pad1 = pads.get(i).startPad;
			Point pad2 = pads.get(i).finishPad;
			grid[pad1.x][pad1.y] = "Pad1 (" + pad1.x + ", " + pad1.y + ")";
			grid[pad2.x][pad2.y] = "Pad1 (" + pad2.x + ", " + pad2.y + ")";
			
		}
		for(int i = 0; i < hostages.size(); i++) {
			Hostage hostage = hostages.get(i);
			grid[hostage.x][hostage.y] = "H (" + hostage.getDamage() + ")";

		}
		grid[neo.x][neo.y] = "N";
		grid[telephoneBooth.x][telephoneBooth.y] = "TB";
	}
	public Point getNeo() {
		return neo;
	}

	public void setNeo(Point neo) {
		this.neo = neo;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public Point getTelephoneBooth() {
		return telephoneBooth;
	}

	public void setTelephoneBooth(Point telephoneBooth) {
		this.telephoneBooth = telephoneBooth;
	}

	public ArrayList<Agent> getAgents() {
		return agents;
	}

	public void setAgents(ArrayList<Agent> agents) {
		this.agents = agents;
	}

	public ArrayList<Point> getPills() {
		return pills;
	}

	public void setPills(ArrayList<Point> pills) {
		this.pills = pills;
	}

	public ArrayList<Pad> getPads() {
		return pads;
	}

	public void setPads(ArrayList<Pad> pads) {
		this.pads = pads;
	}

	public ArrayList<Hostage> getHostages() {
		return hostages;
	}

	public void setHostages(ArrayList<Hostage> hostages) {
		this.hostages = hostages;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	public ArrayList<Hostage> getCarriedHostages() {
		return carriedHostages;
	}
	public void setCarriedHostages(ArrayList<Hostage> carriedHostages) {
		this.carriedHostages = carriedHostages;
	}
	public boolean isTookPill() {
		return tookPill;
	}
	public void setTookPill(boolean tookPill) {
		this.tookPill = tookPill;
	}

}



