package code;
import java.util.Random;

public class Matrix {
	Random random = new Random(); // random.nextInt(max - min + 1) + min
	String[][] grid;
	String gridString;
	int m;
	int n;
	int gridSize;
	int padPairs;
	Pair neo;
	Pair telephoneBooth;
	int agents;
	int hostages;
	int pills;
	int c;
	Pair[][] padPairsArr;
	private String agentString = "";
	private String pillString = "";
	private String padString = "";
	private String hostageString = "";
	public Matrix() {
		genGrid();
	}
	public void genGrid() {
		m = random.nextInt(11) + 5;
		n = random.nextInt(11) + 5;
		gridSize = m*n;
		grid = new String[m][n];
		c = random.nextInt(4) + 1;
		gridString = m + "," + n +";" + c +";"; 
		initalizeNeo();
		initalizeTelephoneBooth();
		initalizeHostagesAndPills();
		initalizeAgents();
		initalizePads();
		gridString += agentString + pillString + padString + hostageString;
	}
	
	public static String solve(String grid, String strategy, boolean visualize) {
		StateSpace tree = new StateSpace(grid);
		SearchTreeNode node = tree.generalSearch(strategy);
		String result = "";
		if(node != null){
			//int pathcost = node.pathCost;
			//result += "snap";
			while(node.parent != null){
				result = node.operator + "," + result;
				node = node.parent;
			}
			result += ";" + tree.deadHostages + ";" + tree.deadAgents + ";" + tree.nodesExpanded;
		}
		
		return result;
	}
	void initalizeNeo() {
		int x = random.nextInt(m);
		int y = random.nextInt(n);
		neo = new Pair(x, y);
		grid[x][y] = "N";
		gridString += x + "," + y +";"; 
	}
	void initalizeTelephoneBooth() {
		int x = random.nextInt(m);
		int y = random.nextInt(n);
		while(grid[x][y] != null) {
			x = random.nextInt(m);
			y = random.nextInt(n);
		}
		telephoneBooth = new Pair(x, y);
		grid[x][y] = "TB";
		gridString += x + "," + y +";"; 
	}
	void initalizeHostagesAndPills() {
		hostages = random.nextInt(6) + 5;
		for (int i = 0; i < hostages; i++) {
			int hostageHealthRand = random.nextInt(99) + 1;
			int x = random.nextInt(m);
			int y = random.nextInt(n);
			
			while(grid[x][y] != null) {
				x = random.nextInt(m);
				y = random.nextInt(n);
			}
			grid[x][y] = "H(" + hostageHealthRand + ")";
			if(i != hostages-1)
				hostageString += x + "," + y + "," + hostageHealthRand + ","; 
			else
				hostageString += x + "," + y + "," + hostageHealthRand + ";"; 
			
			
		}
		
		// pills
		pills = random.nextInt(hostages) + 1;
		for (int i = 0; i < pills; i++) {
			int xP = random.nextInt(m);
			int yP = random.nextInt(n);
			while (grid[xP][yP] != null) {
				xP = random.nextInt(m);
				yP = random.nextInt(n);
			}
			grid[xP][yP] = "P";
			if(i != pills-1)
				pillString += xP + "," + yP +","; 
			else
				pillString += xP + "," + yP +";"; 
		}
		
	}
	void initalizeAgents() {
		int maxAgents = gridSize - 2 - hostages - pills - 2;
		agents = random.nextInt(maxAgents) + 1;
		for (int i = 0; i < agents; i++) {
			int x = random.nextInt(m);
			int y = random.nextInt(n);
			while(grid[x][y] != null) {
				x = random.nextInt(m);
				y = random.nextInt(n);
			}
			grid[x][y] = "A";
			if(i != agents-1)
				agentString += x + "," + y +","; 
			else
				agentString += x + "," + y +";"; 
		}	
	}
	void initalizePads() {
		int maxPads = gridSize - 2 - hostages - pills - agents;
		maxPads = maxPads/2;
		padPairs = random.nextInt(maxPads) + 1;
		padPairsArr = new Pair[padPairs][2];
		for (int i = 0; i < padPairs; i++) {
			int x1 = random.nextInt(m);
			int y1 = random.nextInt(n);
			int x2 = random.nextInt(m);
			int y2 = random.nextInt(n);
			while (grid[x1][y1] != null) {
				x1 = random.nextInt(m);
				y1 = random.nextInt(n);
			}
			while (grid[x2][y2] != null) {
				x2 = random.nextInt(m);
				y2 = random.nextInt(n);
			}
			padPairsArr[i][0] = new Pair(x1, y1);
			padPairsArr[i][1] = new Pair(x2, y2);
			grid[x1][y1] = "P" + i + "(" + x1 + "," + y1 + ")";
			grid[x2][y2] = "P" + i + "(" + x2 + "," + y2 + ")";
			if(i != padPairs-1)
				padString += x1 + "," + y1 + "," + x2 + "," + y2 + "," + x2 + "," + y2 + "," + x1 + "," + y1 + ",";
			else
				padString += x1 + "," + y1 + "," + x2 + "," + y2 + "," + x2 + "," + y2 + "," + x1 + "," + y1 + ";";
		}
	}
//	void populateStrings() {
//		int agentsNum = 0;
//		int pillsNum = 0;
//		int hostagesNum = 0;
//		int padsNum = 0;
//		for(int i = 0; i < grid.length; i++) {
//			for (int j = 0; j < grid[i].length; j++) {
//				if(grid[i][j].equals("A")) {
//					if(agentsNum != agents-1)
//						agentString += i + "," + j +","; 
//					else
//						agentString += i + "," + j +";";
//					agentsNum++;
//				} else if(grid[i][j].equals("P")) {
//					if(pillsNum != pills-1)
//						pillString += i + "," + j +","; 
//					else
//						pillString += i + "," + j +";";
//					pillsNum++;
//				} else if(grid[i][j].charAt(0) == 'P' && grid[i][j].length() != 0) {
//					// pads
//				} else if(grid[i][j].charAt(0) == 'H') {
//					// hostage
//				}
//					
//			}
//		}
//	}
	void displayGrid() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if(grid[i][j] == null)
					grid[i][j] = "e";
				System.out.print(" " + grid[i][j] + " ");
				System.out.print("|");
			}
			System.out.println("");
		}
	}
	public static void main(String[] args) {
		Matrix maze = new Matrix();
		maze.displayGrid();
//		System.out.println(maze.m + "x" + maze.n);
		System.out.println("Carry: " + maze.c);
//		System.out.println("Neo: (" + maze.neo.getX() +", " + maze.neo.getY()+")");
//		System.out.println("Telephone: (" + maze.telephoneBooth.getX() +", " + maze.telephoneBooth.getY()+")");
		System.out.println("Agents: " + maze.agents);
//		System.out.println("Pills: " + maze.pills);
//		System.out.println("Pad Pairs: " + maze.padPairs);
		System.out.println("Hostages: " + maze.hostages);
//		System.out.println(maze.gridString);
		String s = solve(maze.gridString,"UC",false);
		System.out.println(s);
//		System.out.println("Hello");
//		Random random = new Random();
//		int m = random.nextInt(11) + 5;
//		int n = random.nextInt(11) + 5;
//		int x = random.nextInt(m+1);
//		int y = random.nextInt(n+1);
//		System.out.println(m+" "+n+" "+x+" "+y);
	}
}
