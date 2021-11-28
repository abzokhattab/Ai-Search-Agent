package code;

public class Matrix {
	
	public static String solve(String grid, String strategy, boolean visualize) {
		StateSpace tree = new StateSpace(grid);
		SearchTreeNode node = tree.generalSearch(strategy);
		String result = "";
		String visualizeGrid = "";
		String visualizeString = "";

		if(node != null) {
			while(node.parent != null) {
				result = node.operator + "," + result;
//				states = node.state + "\n" + states;
				if(visualize)
					visualizeGrid = ((NeoState) node.state).populateGrid() + visualizeGrid;
				visualizeString = node.operator + " " + ((NeoState) node.state).stringVisualization() + "\n" + visualizeString;
//				System.out.println(node.operator);

				node = node.parent;
			}
			visualizeString = "initial State " + ((NeoState) tree.initialState).stringVisualization() + "\n" + visualizeString;
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
	
	public static void main(String[] args) {
		Grid maze = new Grid();
		maze.displayGrid();
		System.out.println(maze.m + "x" + maze.n);
		System.out.println("Carry: " + maze.c);
		System.out.println("Neo: (" + maze.neo.getX() +", " + maze.neo.getY()+")");
		System.out.println("Telephone: (" + maze.telephoneBooth.getX() +", " + maze.telephoneBooth.getY()+")");
		System.out.println("Agents: " + maze.agents);
		System.out.println("Pills: " + maze.pills);
		System.out.println("Pad Pairs: " + maze.padPoints);
		System.out.println("Hostages: " + maze.hostages);
		System.out.println(maze.gridString);
		String s = solve(maze.gridString, "ID",false);
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
