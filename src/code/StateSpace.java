package code;

import java.awt.Point;
import java.util.ArrayList;

public class StateSpace extends SearchProblem{
	int m;
	int n;
	
	public StateSpace(String grid) {
		String[] g = grid.split(";");
		String[] g1 = g[0].split(",");
		
		// grid dimensions
		m = Integer.parseInt(g1[0]);
		n = Integer.parseInt(g1[1]);
		
		// carry
		int c = Integer.parseInt(g[1]);
		
		// position of neo
		String[] neoSplit = g[2].split(",");
		Point neo = new Point(Integer.parseInt(neoSplit[0]),Integer.parseInt(neoSplit[1]));

		// position of telephone booth
		String [] tb = g[3].split(",");
		Point telephoneBooth = new Point(Integer.parseInt(tb[0]),Integer.parseInt(tb[1]));

		// positions of agents
		String[] agentSplit = g[4].split(",");
		ArrayList<Agent> agents = new ArrayList<Agent>();
		for(int i=0; i< agentSplit.length; i = i+2){
			agents.add(new Agent(Integer.parseInt(agentSplit[i]),Integer.parseInt(agentSplit[i+1]), false));
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
			pads.add(new Pad (new Point(Integer.parseInt(padSplit[i]), Integer.parseInt(padSplit[i+1])), 
					new Point(Integer.parseInt(padSplit[i+2]), Integer.parseInt(padSplit[i+3]))));
		}
		
		// positions of hostages
		String[] hostageSplit = g[7].split(",");
		ArrayList<Hostage> hostages = new ArrayList<Hostage>();
		for(int i=0; i< hostageSplit.length; i = i+3){
			hostages.add(new Hostage(Integer.parseInt(hostageSplit[i]), Integer.parseInt(hostageSplit[i+1]), Integer.parseInt(hostageSplit[i+2])));
		}
		
		initialState = new NeoState(neo, c, telephoneBooth, agents, pills, pads, hostages, new ArrayList<Hostage>(), false, 0, m, n);
		operators = new String[]{"up", "down", "left", "right", "carry", "drop", "pill", "killUp", "killDown", "killLeft", "killRight", "fly"};
	}
	public boolean agentCollision(ArrayList<Agent> agents, Point neo) {
		for(int i = 0; i < agents.size(); i++) {
			if(neo.x == agents.get(i).x && neo.y == agents.get(i).y)
				return true;
		}
		return false;
	}
	public Hostage canCarry(Point neo, ArrayList<Hostage> hostages) {
		for(int i = 0; i < hostages.size(); i++) {
			if(neo.x == hostages.get(i).x && neo.y == hostages.get(i).y)
				return hostages.get(i);
		}
		return new Hostage(-1, -1, -1);
	}
	public Point takePill(Point neo, ArrayList<Point> pills) {
		for(int i = 0; i < pills.size(); i++) {
			if(neo.x == pills.get(i).x && neo.y == pills.get(i).y)
				return pills.get(i);
		}
		return new Point(-1, -1);
	}
	public boolean killAgents(Point neo, ArrayList<Agent> agents, int damage) {
		boolean killed = false;
		for(int i = 0; i < agents.size(); i++) {
			if((neo.x + 1 == agents.get(i).x && neo.y == agents.get(i).y) || (neo.x - 1 == agents.get(i).x && neo.y == agents.get(i).y)
					|| (neo.x == agents.get(i).x && neo.y + 1 == agents.get(i).y) || (neo.x == agents.get(i).x && neo.y - 1 == agents.get(i).y)) {				
				agents.remove(i);
				killed = true;
				i--;
			}
		}
		if(killed) {
			damage += 20;
//			if(damage >= 100)
//				return ; ///////////////////////////////////// gameover
		}
		return killed;

	}
	public void timeStep(boolean pillCycle, ArrayList<Hostage> hostages, ArrayList<Hostage> carriedHostages, ArrayList<Agent> agents) {
		for(int i = 0; i < hostages.size(); i++) {
			int currentDamage = hostages.get(i).getDamage();
			if(pillCycle) {
				currentDamage -= 20;
				if(currentDamage < 0)
					currentDamage = 0;
				hostages.get(i).setDamage(currentDamage);
			}
			else {
				currentDamage +=  2;
				if(currentDamage >= 100) {
					hostages.remove(i);
					agents.add(new Agent(hostages.get(i).x, hostages.get(i).y, true));
				}
				else
					hostages.get(i).setDamage(currentDamage);
			}
					
		}
		for(int i = 0; i < carriedHostages.size(); i++) {
			int currentDamage = hostages.get(i).getDamage();
			if(pillCycle) {
				currentDamage -= 20;
				if(currentDamage < 0)
					currentDamage = 0;
			}
			else
				currentDamage += 2;
			
			carriedHostages.get(i).setDamage(currentDamage);
		}
	}
	@Override
	public boolean goalTest(SearchTreeNode node) {
		ArrayList<Hostage> hostages = ((NeoState) node.state).getHostages();
		Point telephoneBooth = ((NeoState) node.state).getTelephoneBooth();
		Point neo = ((NeoState) node.state).getNeo();
		ArrayList<Hostage> carriedHostages = ((NeoState) node.state).getCarriedHostages();
		ArrayList<Agent> agents = ((NeoState) node.state).getAgents();
		int damage = ((NeoState) node.state).getDamage();

		if(damage >= 100) 
			return false;
		if(carriedHostages.size() != 0)
			return false;
		if(!neo.equals(telephoneBooth))
			return false;
		for(int i = 0; i < hostages.size(); i++) {
			if(hostages.get(i).x != telephoneBooth.x || hostages.get(i).y != telephoneBooth.y)
				return false;
		}
		for(int i = 0; i < agents.size(); i++) {
			if(agents.get(i).isHostage())
				return false;
		}
		return true;
	}
	@Override
	public State transitionFunction(State state, String operator) {
		Point neo = ((NeoState) state).getNeo();		
		int c = ((NeoState) state).getC();
		boolean tookPill = ((NeoState) state).isTookPill();
		Point telephoneBooth = ((NeoState) state).getTelephoneBooth();
		ArrayList<Agent> agents = ((NeoState) state).getAgents();
		ArrayList<Point> pills = ((NeoState) state).getPills(); 
		ArrayList<Pad> pads = ((NeoState) state).getPads(); 
		ArrayList<Hostage> hostages = ((NeoState) state).getHostages();
		ArrayList<Hostage> carriedHostages = ((NeoState) state).getCarriedHostages();
		int damage = ((NeoState) state).getDamage();
		
		switch(operator) {
			case "up" : {
				Point newPosition = new Point(neo.x - 1, neo.y);
				if(newPosition.x >= 0 && !agentCollision(agents, newPosition)) {
					timeStep(false, hostages, carriedHostages, agents);
					State newState = new NeoState(newPosition, c, telephoneBooth, agents, pills, pads, hostages, carriedHostages, tookPill, damage, m, n);
					if(states.containsKey(newState.toString())) {
						return null;
					}
					states.put(newState.toString(), "");
					return newState;
				}
				return null; // no change
			}
			case "down": {
				Point newPosition = new Point(neo.x + 1, neo.y);
				if(newPosition.x <= m && !agentCollision(agents, newPosition)) {
					timeStep(false, hostages, carriedHostages, agents);
					State newState = new NeoState(newPosition, c, telephoneBooth, agents, pills, pads, hostages, carriedHostages, tookPill, damage, m, n);
					if(states.containsKey(newState.toString())){
						return null;
					}
					states.put(newState.toString(), "");
					return newState;
				}
				return null; // no change
			}
			case "left": {
				Point newPosition = new Point(neo.x, neo.y - 1);
				if(newPosition.y >= 0 && !agentCollision(agents, newPosition)) {
					timeStep(false, hostages, carriedHostages, agents);
					State newState = new NeoState(newPosition, c, telephoneBooth, agents, pills, pads, hostages, carriedHostages, tookPill, damage, m, n);
					if(states.containsKey(newState.toString())){
						return null;
					}
					states.put(newState.toString(), "");
					return newState;
				}
				return null; // no change
			}
			case "right": {
				Point newPosition = new Point(neo.x, neo.y + 1);
				if(newPosition.y <= n && !agentCollision(agents, newPosition)) {
					timeStep(false, hostages, carriedHostages, agents);
					State newState = new NeoState(newPosition, c, telephoneBooth, agents, pills, pads, hostages, carriedHostages, tookPill, damage, m, n);
					if(states.containsKey(newState.toString())){
						return null;
					}
					states.put(newState.toString(), "");
					return newState;
				}
				return null; // no change
			}
			case "carry": {
				if(c == carriedHostages.size()) // maximumCapacity
					return null;
				Hostage saved = canCarry(neo, hostages);
				if(!saved.equals(new Hostage(-1, -1, -1))) {
					carriedHostages.add(saved);
					int i = hostages.indexOf(saved);
					hostages.remove(i);
					timeStep(false, hostages, carriedHostages, agents);
					State newState = new NeoState(neo, c, telephoneBooth, agents, pills, pads, hostages, carriedHostages, tookPill, damage, m, n);
					if(states.containsKey(newState.toString())){
						return null;
					}
					states.put(newState.toString(), "");
					return newState;
				}
				return null;
			}
			case "drop": {
				if(neo.equals(telephoneBooth)) {
					for(int i = 0; i < carriedHostages.size(); i++) {
						Hostage droppedHostage = new Hostage(telephoneBooth.x, telephoneBooth.y, carriedHostages.get(i).getDamage());
						hostages.add(droppedHostage);
					}
					carriedHostages.clear();
					timeStep(false, hostages, carriedHostages, agents);
					State newState = new NeoState(neo, c, telephoneBooth, agents, pills, pads, hostages, carriedHostages, tookPill, damage, m, n);
					if(states.containsKey(newState.toString())){
						return null;
					}
					states.put(newState.toString(), "");
				}
				return null;
			}
			case "pill": {
				if(!tookPill) {
					Point pill = takePill(neo, pills);
					if(!pill.equals(new Point(-1, -1))) {
						int i = pills.indexOf(pill);
						pills.remove(i);
						timeStep(true, hostages, carriedHostages, agents);
						State newState = new NeoState(neo, c, telephoneBooth, agents, pills, pads, hostages, carriedHostages, true, damage, m, n);
						if(states.containsKey(newState.toString())){
							return null;
						}
						states.put(newState.toString(), "");
						return newState;
					}
				}
				return null;			
			}
			case "kill": { 
				 boolean kill = killAgents(neo, agents, damage);
				 if(kill) {
					 timeStep(false, hostages, carriedHostages, agents);
					 State newState = new NeoState(neo, c, telephoneBooth, agents, pills, pads, hostages, carriedHostages, tookPill, damage, m, n);
					 if(states.containsKey(newState.toString())) {
							return null;
					 }
					 states.put(newState.toString(), "");
					 return newState;
				 }
				 return null;
			}
			case "fly": {
				Point newPosition = new Point(-1, -1);
				for(int i = 0; i < pads.size(); i++) {
					Pad currentPad = pads.get(i);
					if(neo.x == currentPad.startPad.x && neo.y == currentPad.startPad.y) {
						newPosition = new Point(currentPad.finishPad.x, currentPad.finishPad.y);
						break;
					}
					else if(neo.x == currentPad.finishPad.x && neo.y == currentPad.finishPad.y) {
						newPosition = new Point(currentPad.startPad.x, currentPad.startPad.y);
						break;
					}
				}
				if(!newPosition.equals(new Point(-1, -1))) {
					timeStep(false, hostages, carriedHostages, agents);
					State newState = new NeoState(newPosition, c, telephoneBooth, agents, pills, pads, hostages, carriedHostages, tookPill, damage, m, n);
					if(states.containsKey(newState.toString())){
						return null;
					}
					states.put(newState.toString(), "");
					return newState;
				}
				return null;
			}
			default: return null;
		}	
	}
}
