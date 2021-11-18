package code;

import java.awt.Point;
import java.util.ArrayList;

public class StateSpace extends SearchProblem{
	int m;
	int n;
	
	public StateSpace(String grid) {
		String[] operators = {"up", "down", "left", "right", "carry", "drop", "pill", "killUp", "killDown", "killLeft", "killRight", "fly"};
		String[] g = grid.split(";");
		String[] g1 = g[0].split(",");
		
		// grid dimensions
		m = Integer.parseInt(g1[0]);
		n = Integer.parseInt(g1[1]);
		
		// carry
		String c = g[1];
		
		// position of neo
		String[] neoSplit = g[2].split(",");
		Point neo = new Point(Integer.parseInt(neoSplit[0]),Integer.parseInt(neoSplit[1]));

		// position of telephone booth
		String [] tb = g[3].split(",");
		Point telephoneBooth = new Point(Integer.parseInt(tb[0]),Integer.parseInt(tb[1]));

		// positions of agents
		String[] agentSplit = g[4].split(",");
		ArrayList<Point> agents = new ArrayList<Point>();
		for(int i=0; i< agentSplit.length; i = i+2){
			agents.add(new Point(Integer.parseInt(agentSplit[i]),Integer.parseInt(agentSplit[i+1])));
		}
		
		// positions of pills
		String[] pillSplit = g[5].split(",");
		ArrayList<Point> pills = new ArrayList<Point>();
		for(int i=0; i< pillSplit.length; i = i+2){
			pills.add(new Point(Integer.parseInt(pillSplit[i]),Integer.parseInt(pillSplit[i+1])));
		}
		
		// positions of pads
		String[] padSplit = g[6].split(",");
		ArrayList<Pad> pads = new ArrayList<Pad>();
		for(int i = 0; i < padSplit.length ; i = i+4){
			pads.add(new Pad (new Point(Integer.parseInt(padSplit[i]),Integer.parseInt(padSplit[i+1])), 
					new Point(Integer.parseInt(padSplit[i+2]),Integer.parseInt(padSplit[i+3]))));
		}
		
		// positions of pills
		String[] hostageSplit = g[7].split(",");
		ArrayList<Hostage> hostages = new ArrayList<Hostage>();
		for(int i=0; i< pillSplit.length; i = i+3){
			hostages.add(new Hostage(Integer.parseInt(pillSplit[i]), Integer.parseInt(pillSplit[i+1]), Integer.parseInt(pillSplit[i+2])));
		}
		
//		ArrayList[][] positions= new ArrayList()[][];
//		positions.add(ironman);
//		positions.add(new Point(-1,-1));
//		positions.addAll(warriors);
//		positions.add(new Point(-1,-1));
//		positions.addAll(stones);
//
//		initialState = new IronmanState(positions,0);
//		operators = new String[]{"collect","down","left","right","up","kill"};

		//SearchTreeNode root = new SearchTreeNode(initialState,null,null,0,0); 

	}
	@Override
	public boolean goalTest(SearchTreeNode node) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public State transitionFunction(State state, String operator) {
		// TODO Auto-generated method stub
		return null;
	}
}
