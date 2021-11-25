package code;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;


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
		Point neo = new Point(Integer.parseInt(neoSplit[0]), Integer.parseInt(neoSplit[1]));

		// position of telephone booth
		String [] tb = g[3].split(",");
		Point telephoneBooth = new Point(Integer.parseInt(tb[0]), Integer.parseInt(tb[1]));

		// positions of agents
		String[] agentSplit = g[4].split(",");
		ArrayList<Agent> agents = new ArrayList<Agent>();
		for(int i = 0; i < agentSplit.length; i = i + 2){
			agents.add(new Agent(Integer.parseInt(agentSplit[i]), Integer.parseInt(agentSplit[i+1]), false));
		}
		
		// positions of pills
		String[] pillSplit = g[5].split(",");
		ArrayList<Point> pills = new ArrayList<Point>();
		for(int i=0; i< pillSplit.length; i = i + 2){
			pills.add(new Point(Integer.parseInt(pillSplit[i]), Integer.parseInt(pillSplit[i+1])));
		}
		
		// positions of pads
		String[] padSplit = g[6].split(",");
		HashMap<String,String> pads = new HashMap<String,String>();
		for(int i = 0; i < padSplit.length -3 ; i += 4) {
			pads.put(padSplit[i] + "," + padSplit[i+1], padSplit[i+2] + "," + padSplit[i+3]);
		}
		// positions of hostages
		String[] hostageSplit = g[7].split(",");
		ArrayList<Hostage> hostages = new ArrayList<Hostage>();
		for(int i = 0; i < hostageSplit.length; i = i + 3){
			hostages.add(new Hostage(Integer.parseInt(hostageSplit[i]), Integer.parseInt(hostageSplit[i+1]), Integer.parseInt(hostageSplit[i+2])));
		}
		
		initialState = new NeoState(neo, c, telephoneBooth, agents, pills, pads, hostages, new ArrayList<Hostage>(), false, 0, m, n);
		System.out.println("initialState " + initialState);
		operators = new String[]{"up", "down", "right", "left", "drop", "fly", "takePill", "kill"};
	


//		
//		String[] hstg = gridArray[7].split(",");
//		HashMap<String,Integer> m7 = new HashMap<String,Integer>();
//		for(int i = 0;i< hstg.length -2; i+=3) {
//			m7.put(hstg[i]+","+hstg[i+1],Integer.parseInt(hstg[i+2]));
//		}
//		
//		TH s = new TH(m,n,x10,x11,x00, x01,capacity, xyz,m4,m5,m7);
//		boolean linkin = true;
	}
	public boolean agentCollision(ArrayList<Agent> agents, Point neo) {
		for(int i = 0; i < agents.size(); i++) {
			if(neo.x == agents.get(i).x && neo.y == agents.get(i).y && !agents.get(i).isKilled())
				return true;
		}
		return false;
	}
	public Hostage canCarry(Point neo, ArrayList<Hostage> hostages) {
		for(int i = 0; i < hostages.size(); i++) {
			if(neo.x == hostages.get(i).x && neo.y == hostages.get(i).y && !hostages.get(i).isSaved())
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
	public int killAgents(Point neo, ArrayList<Agent> agents, int damage) {
		boolean killed = false;
		if(damage + 20 >= 100) {
			return damage; 
		}
		for(int i = 0; i < agents.size(); i++) {
			if(( (neo.x + 1 == agents.get(i).x && neo.y == agents.get(i).y ) || (neo.x - 1 == agents.get(i).x && neo.y == agents.get(i).y)
					|| (neo.x == agents.get(i).x && neo.y + 1 == agents.get(i).y) || (neo.x == agents.get(i).x && neo.y - 1 == agents.get(i).y) ) 
					&& !agents.get(i).isKilled()) {				
				agents.get(i).setKilled(true);
				killed = true;
			}
		}
		if(killed) {
			damage += 20;
		}
		return damage;

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
				if(currentDamage >= 100 && !hostages.get(i).isSaved()) {
					agents.add(new Agent(hostages.get(i).x, hostages.get(i).y, true));
					hostages.remove(i);
				}
				else
					hostages.get(i).setDamage(currentDamage);
			}
					
		}
		for(int i = 0; i < carriedHostages.size(); i++) {
			int currentDamage = carriedHostages.get(i).getDamage();
			if(pillCycle) {
				if(carriedHostages.get(i).getDamage() < 100) {
					currentDamage -= 20;
					if(currentDamage < 0)
						currentDamage = 0;
				}
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
		deadHostages = 0;
		deadAgents = 0;
		if(damage >= 100) 
			return false;
		if(carriedHostages.size() != 0)
			return false;
		if(!neo.equals(telephoneBooth))
			return false;
		for(int i = 0; i < hostages.size(); i++) {
			if(hostages.get(i).x != telephoneBooth.x || hostages.get(i).y != telephoneBooth.y)
				return false;
			if(hostages.get(i).getDamage() >= 100 && hostages.get(i).isSaved())
				deadHostages++;
		}
		for(int i = 0; i < agents.size(); i++) {
			if(agents.get(i).isHostage() && !agents.get(i).isKilled())
				return false;
			if(agents.get(i).isKilled())
				deadAgents++;
			if(agents.get(i).isHostage() && agents.get(i).isKilled())
				deadHostages++;
		}
//		System.out.println(states.toString());
		return true;
	}
	@Override 
	public State transitionFunction(State state, String operator) {
//		System.out.print(operator);
//		System.out.println(state.toString());
		Point neoOriginal = ((NeoState) state).getNeo();	
		int c = ((NeoState) state).getC();
		boolean tookPill = ((NeoState) state).isTookPill();
		Point telephoneBooth = ((NeoState) state).getTelephoneBooth();
		ArrayList<Agent> agentsOriginal = ((NeoState) state).getAgents();
		ArrayList<Point> pillsOriginal = ((NeoState) state).getPills(); 
		HashMap<String, String> pads = ((NeoState) state).getPads(); 
		ArrayList<Hostage> hostagesOriginal = ((NeoState) state).getHostages();
		ArrayList<Hostage> carriedHostagesOriginal = ((NeoState) state).getCarriedHostages();
		ArrayList<Agent> agents = new ArrayList<Agent>();
		ArrayList<Point> pills = new ArrayList<Point>();
		ArrayList<Hostage> hostages = new ArrayList<Hostage>();
		ArrayList<Hostage> carriedHostages = new ArrayList<Hostage>();
		for(int i = 0; i < agentsOriginal.size(); i++) {
			Agent currentAgent = new Agent(agentsOriginal.get(i).x, agentsOriginal.get(i).y, agentsOriginal.get(i).isHostage());
			if(agentsOriginal.get(i).isKilled())
				currentAgent.setKilled(true);
			agents.add(currentAgent);
		}
		for(int i = 0; i < pillsOriginal.size(); i++) {
			pills.add(new Point(pillsOriginal.get(i).x, pillsOriginal.get(i).y));
		}
		for(int i = 0; i < hostagesOriginal.size(); i++) {
			Hostage currentHostage = new Hostage(hostagesOriginal.get(i).x, hostagesOriginal.get(i).y, hostagesOriginal.get(i).getDamage());
			if(hostagesOriginal.get(i).isSaved())
				currentHostage.setSaved(true);
			hostages.add(currentHostage);
		}
		for(int i = 0; i < carriedHostagesOriginal.size(); i++) {
			carriedHostages.add(new Hostage(carriedHostagesOriginal.get(i).x, carriedHostagesOriginal.get(i).y, carriedHostagesOriginal.get(i).getDamage()));
		}
		Point neo = new Point(neoOriginal.x, neoOriginal.y);
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
				if(newPosition.x < m && !agentCollision(agents, newPosition)) {
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
				if(newPosition.y < n && !agentCollision(agents, newPosition)) {
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
					if(carriedHostages.size() == 0)
						return null;
					for(int i = 0; i < carriedHostages.size(); i++) {
						Hostage droppedHostage = new Hostage(telephoneBooth.x, telephoneBooth.y, carriedHostages.get(i).getDamage());
						droppedHostage.setSaved(true);
						hostages.add(droppedHostage);
					}
					carriedHostages.clear();
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
			case "takePill": {
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
				 int newDamage = killAgents(neo, agents, damage);
				 if(damage != newDamage) {
					 timeStep(false, hostages, carriedHostages, agents);
					 State newState = new NeoState(neo, c, telephoneBooth, agents, pills, pads, hostages, carriedHostages, tookPill, newDamage, m, n);
					 if(states.containsKey(newState.toString())) {
							return null;
					 }
					 states.put(newState.toString(), "");
					 return newState;
				 }
				 return null;
			}
			case "fly": {
				String fly = pads.get(neo.x + "," + neo.y);
				if(fly != null) {
					Point newPosition = new Point(Integer.parseInt(fly.charAt(0) +""), Integer.parseInt(fly.charAt(2) +""));
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
