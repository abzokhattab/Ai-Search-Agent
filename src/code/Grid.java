package code;
import java.awt.Point;
import java.util.Random;

public class Grid {
	Random random = new Random(); // random.nextInt(max - min + 1) + min
	String[][] grid;
	String gridString;
	int m;
	int n;
	int gridSize;
	int padPoints;
	Point neo;
	Point telephoneBooth;
	int agents;
	int hostages;
	int pills;
	int c;
	Point[][] padPointsArr;
	private String agentString = "";
	private String pillString = "";
	private String padString = "";
	private String hostageString = "";
	public Grid() {
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
	
	void initalizeNeo() {
		int x = random.nextInt(m);
		int y = random.nextInt(n);
		neo = new Point(x, y);
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
		telephoneBooth = new Point(x, y);
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
		padPoints = random.nextInt(maxPads) + 1;
		padPointsArr = new Point[padPoints][2];
		for (int i = 0; i < padPoints; i++) {
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
			padPointsArr[i][0] = new Point(x1, y1);
			padPointsArr[i][1] = new Point(x2, y2);
			grid[x1][y1] = "P" + i + "(" + x1 + "," + y1 + ")";
			grid[x2][y2] = "P" + i + "(" + x2 + "," + y2 + ")";
			if(i != padPoints-1)
				padString += x1 + "," + y1 + "," + x2 + "," + y2 + "," + x2 + "," + y2 + "," + x1 + "," + y1 + ",";
			else
				padString += x1 + "," + y1 + "," + x2 + "," + y2 + "," + x2 + "," + y2 + "," + x1 + "," + y1 + ";";
		}
	}
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

}
