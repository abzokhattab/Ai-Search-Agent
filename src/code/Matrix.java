package code;

import java.util.Random;

public class Matrix {
	
	public static String solve(String grid, String strategy, boolean visualize) {
		StateSpace tree = new StateSpace(grid);
		SearchTreeNode node = tree.generalSearch(strategy);
		String result = "";
		String visualizeGrid = "";
//		String visualizeString = "";

		if(node != null) {
			while(node.parent != null) {
				result = node.operator + "," + result;
				if(visualize)
					visualizeGrid = ((NeoState) node.state).populateGrid() + visualizeGrid;
//				visualizeString = node.operator + " " + ((NeoState) node.state).stringVisualization() + "\n" + visualizeString;
				node = node.parent;
			}
//			visualizeString = "initial State " + ((NeoState) tree.initialState).stringVisualization() + "\n" + visualizeString;
			if(visualize)
				visualizeGrid = ((NeoState) tree.initialState).populateGrid() + visualizeGrid;
			result = result.substring(0,result.length()-1) + ";" + tree.deadHostages + ";" + tree.deadAgents + ";" + tree.nodesExpanded;
		}
		else
			result = "No Solution";
		if(visualize)
			System.out.println(visualizeGrid);
//		System.out.println(visualizeString);
		System.out.println(result + "\n" );
		return result;
	}
	
	public static String genGrid() {
		String agentString = "";
		String pillString = "";
		String padString = "";
		String hostageString = "";
		Random random = new Random();
		int m = random.nextInt(11) + 5;
		int n = random.nextInt(11) + 5;
		int gridSize = m*n;
		String[][] grid = new String[m][n];
		int c = random.nextInt(4) + 1;
		String gridString = m + "," + n +";" + c +";";
		
		// Initialize Neo
		int x = random.nextInt(m);
		int y = random.nextInt(n);
		grid[x][y] = "N";
		gridString += x + "," + y +";"; 
		
		// Initialize TelephoneBooth
		int tbx = random.nextInt(m);
		int tby = random.nextInt(n);
		while(grid[tbx][tby] != null) {
			tbx = random.nextInt(m);
			tby = random.nextInt(n);
		}
		grid[tbx][tby] = "TB";
		gridString += tbx + "," + tby +";"; 

		
		// Initialize hostages and pills
		int hostages = random.nextInt(6) + 5;
		for (int i = 0; i < hostages; i++) {
			int hostageHealthRand = random.nextInt(99) + 1;
			int hx = random.nextInt(m);
			int hy = random.nextInt(n);
			
			while(grid[hx][hy] != null) {
				hx = random.nextInt(m);
				hy = random.nextInt(n);
			}
			grid[hx][hy] = "H(" + hostageHealthRand + ")";
			if(i != hostages-1)
				hostageString += hx + "," + hy + "," + hostageHealthRand + ","; 
			else
				hostageString += hx + "," + hy + "," + hostageHealthRand + ";"; 
		}
		
		// pills
		int pills = random.nextInt(hostages) + 1;
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
		
		// Initialize Agents
		int maxAgents = gridSize - 2 - hostages - pills - 2;
		int agents = random.nextInt(maxAgents) + 1;
		for (int i = 0; i < agents; i++) {
			int ax = random.nextInt(m);
			int ay = random.nextInt(n);
			while(grid[ax][ay] != null) {
				ax = random.nextInt(m);
				ay = random.nextInt(n);
			}
			grid[ax][ay] = "A";
			if(i != agents-1)
				agentString += ax + "," + ay +","; 
			else
				agentString += ax + "," + ay +";"; 
		}	

		// Initialize Pads
		int maxPads = gridSize - 2 - hostages - pills - agents;
		maxPads = maxPads/2;
		int padPoints = random.nextInt(maxPads) + 1;
		for (int i = 0; i < padPoints; i++) {
			int x1 = random.nextInt(m);
			int y1 = random.nextInt(n);
			while (grid[x1][y1] != null) {
				x1 = random.nextInt(m);
				y1 = random.nextInt(n);
			}
			grid[x1][y1] = "P" + i + "(" + x1 + "," + y1 + ")";

			int x2 = random.nextInt(m);
			int y2 = random.nextInt(n);
			while (grid[x2][y2] != null) {
				x2 = random.nextInt(m);
				y2 = random.nextInt(n);
			}
			grid[x2][y2] = "P" + i + "(" + x2 + "," + y2 + ")";
			
			if(i != padPoints-1)
				padString += x1 + "," + y1 + "," + x2 + "," + y2 + "," + x2 + "," + y2 + "," + x1 + "," + y1 + ",";
			else
				padString += x1 + "," + y1 + "," + x2 + "," + y2 + "," + x2 + "," + y2 + "," + x1 + "," + y1 + ";";
		}
		
		gridString += agentString + pillString + padString + hostageString;
		return gridString;
	}
	public static void main(String[] args) {
		String grid = genGrid();
		String result = solve(grid, "DF", false);
		System.out.println(result);

	}

}
