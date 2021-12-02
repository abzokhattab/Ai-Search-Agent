package code;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;


public abstract class MatrixProblem {
	State initialState;
	String [] operators;
	int nodesExpanded = 0;
	int currlevel = 0;
	int deadHostages = 0;
	int deadAgents = 0;
	Hashtable<String, String> states = new Hashtable<String, String>();
	
	public abstract boolean goalTest(SearchTreeNode node);
	
	public abstract State transitionFunction(State state, String operator, int depth);
	
	public ArrayList<SearchTreeNode> expand(SearchTreeNode node){
		ArrayList<SearchTreeNode> nodes = new ArrayList<SearchTreeNode>();
		nodesExpanded +=1;
		for(int i = 0; i < operators.length; i++)
		{
			State state = transitionFunction(node.state, operators[i], node.depth+1);
			if(state != null){
				SearchTreeNode nodeTemp = new SearchTreeNode(state,node,operators[i],node.depth+1,state.pathCost,state.heuristicOne,state.heuristicTwo);
				nodes.add(nodeTemp);
			}
		}
		return nodes;
	}
	public ArrayList<SearchTreeNode> addToQueue(String strategy, ArrayList<SearchTreeNode> oldNodes, ArrayList<SearchTreeNode> newNodes){
		switch(strategy){
		case "BF":{
			 oldNodes.addAll(newNodes);
			 return oldNodes;
		}
		case "DF":{
			 newNodes.addAll(oldNodes);
			 return newNodes;
		}
		case "ID":{
			
			if(!newNodes.isEmpty() && newNodes.get(0).depth <= currlevel){
				newNodes.addAll(oldNodes);
				return newNodes;
			}
			else if(oldNodes.isEmpty()){
				currlevel +=1;
				states= new Hashtable<String,String>();
				oldNodes.add(new SearchTreeNode(this.initialState,null,null,0,0,0,0)); // new queue with the root node only
				return oldNodes;
			}
			return oldNodes;
			
		}
		
		case "UC":{
			oldNodes.addAll(newNodes);
			return sort(oldNodes);
		}
		
		case "GR1":{
			oldNodes.addAll(newNodes);
			return sortHeuristicOne(oldNodes);
		}
		
		case "GR2":{
			oldNodes.addAll(newNodes);
			return sortHeuristicTwo(oldNodes);
		}
		
		case "AS1":{
			oldNodes.addAll(newNodes);
			return sortAHeuristicOne(oldNodes);
		}
		
		case "AS2":{
			oldNodes.addAll(newNodes);
			return sortAHeuristicTwo(oldNodes);
		}
		
		default:
			return oldNodes;
			
		}
	}
	
	public ArrayList<SearchTreeNode> sort(ArrayList<SearchTreeNode> nodes) {
		
		nodes.sort(Comparator.comparing(a ->a.pathCost));
		return nodes;
	}
	public static ArrayList<SearchTreeNode> sortHeuristicOne(ArrayList<SearchTreeNode> nodes)  {
		
		nodes.sort(Comparator.comparing(a ->a.getHeuristicOne()));
        return nodes;
	}
	public static ArrayList<SearchTreeNode> sortHeuristicTwo(ArrayList<SearchTreeNode> nodes)  {
		
		nodes.sort(Comparator.comparing(a ->a.getHeuristicTwo()));
        return nodes;
	}
	public static ArrayList<SearchTreeNode> sortAHeuristicOne(ArrayList<SearchTreeNode> nodes)  {
		
		nodes.sort(Comparator.comparing(a ->(a.getHeuristicOne()+a.gePathCost())));
        return nodes;
	}
	public static ArrayList<SearchTreeNode> sortAHeuristicTwo(ArrayList<SearchTreeNode> nodes)  {
	
		nodes.sort(Comparator.comparing(a ->(a.getHeuristicTwo()+a.gePathCost())));
    	return nodes;
	}
	public SearchTreeNode generalSearch(String strategy){
		ArrayList<SearchTreeNode> nodes = new ArrayList<SearchTreeNode>();
		nodes.add(new SearchTreeNode(this.initialState,null,null,0,0,0,0));
		while(!nodes.isEmpty()){
			SearchTreeNode node = nodes.remove(0);
			if(this.goalTest(node)){
				return node;
			}
			nodes = addToQueue(strategy, nodes, expand(node));
		}	
		return null;
	}

}


