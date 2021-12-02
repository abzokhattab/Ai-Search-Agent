package code;

public class SearchTreeNode {
	State state;
	SearchTreeNode parent;
	String operator;
	int depth;
	int pathCost;
	int heuristicOne;
	int heuristicTwo;
	
	public SearchTreeNode(State state, SearchTreeNode parent, String operator,
			int depth, int pathCost, int heuristicOne,int heuristicTwo) {
		
		super();
		this.state = state;
		this.parent = parent;
		this.operator = operator;
		this.depth = depth;
		this.pathCost = pathCost;
		this.heuristicOne = heuristicOne;
		this.heuristicTwo = heuristicTwo;
		
	}
	
	public int getHeuristicOne() {
		return this.heuristicOne;
	}
	public int getHeuristicTwo() {
		return this.heuristicTwo;
	}
	
	public int gePathCost () {
		return this.pathCost;
	}
}
