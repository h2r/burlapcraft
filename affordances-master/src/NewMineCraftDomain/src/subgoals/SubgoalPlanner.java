package subgoals;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.planning.OOMDPPlanner;
import burlap.behavior.singleagent.planning.QComputablePlanner;
import burlap.behavior.singleagent.planning.StateConditionTest;
import burlap.behavior.singleagent.planning.commonpolicies.GreedyQPolicy;
import burlap.behavior.singleagent.planning.deterministic.TFGoalCondition;
import burlap.behavior.singleagent.planning.stochastic.rtdp.RTDP;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.logicalexpressions.LogicalExpression;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.common.SingleGoalLERF;
import burlap.oomdp.singleagent.common.SingleGoalPFRF;
import burlap.oomdp.singleagent.common.SingleLETF;
import burlap.oomdp.singleagent.common.SinglePFTF;


public class SubgoalPlanner {

	private List<Subgoal> 	subgoalKB;
	private Node				root;
	private State				initialState;
	private RewardFunction		rf;
	private TerminalFunction	tf;
	private OOMDPPlanner		planner;
	private Domain				domain;
	
	public SubgoalPlanner(Domain domain, State initialState, RewardFunction rf, TerminalFunction tf, OOMDPPlanner planner, List<Subgoal> subgoals) {
		this.subgoalKB = subgoals;
		this.root = generateGraph();
		this.initialState = initialState;
		this.planner = planner;
		this.domain = domain;
	}

	/**
	 * Creates a graph out of a list of subgoals
	 * @param kb
	 * @return
	 */
	private Node generateGraph() {
		HashMap<LogicalExpression,Node> nodes = new HashMap<LogicalExpression,Node>();
		
		// Initialize Root of tree (based on final goal)
		Node root = new Node(this.subgoalKB.get(0).getPost(), null);
		nodes.put(this.subgoalKB.get(0).getPost(), root);
		
		// Create a node for each LogicalExpression
		for (int i = 0; i < this.subgoalKB.size(); i++) {
			LogicalExpression pre = this.subgoalKB.get(i).getPre();
			LogicalExpression post = this.subgoalKB.get(i).getPost();
			
			Node postNode = new Node(post, null);
			Node preNode = new Node(pre, null);
			System.out.println("Post Node: " + post);
			System.out.println("Pre Node: " + pre);
			if (!nodes.containsKey(post)) {
				nodes.put(post, postNode);
			}
			
			if (!nodes.containsKey(preNode)) {
				nodes.put(pre, preNode);
			}
		}

		// Add edges between the nodes to form a tree of LogicalExpressions
		for (int i = 0; i < this.subgoalKB.size(); i++) {
			Subgoal edge = this.subgoalKB.get(i);
			
			LogicalExpression edgeStart = edge.getPre();
			LogicalExpression edgeEnd = edge.getPost();
			
			Node startNode = nodes.get(edgeStart);
			Node endNode = nodes.get(edgeEnd);
			
			if (startNode != null) {
				startNode.setParent(endNode);				
				endNode.addChild(startNode);
			}
						
		}
		
		return root;
	}
	
	// === Subgoal Planner	===
	public List<String> solve(){
		
		// Initialize action plan
		List<String> actionSequence = new ArrayList<String>();
		
		// Get high level plan
		Node highLevelPlan = getHighLevelPlan();
		
		// Run VI between each subgoal in the chain
		State currState = initialState;
		StateConditionTest goalCondition;
		
		// Change reward function
		while(highLevelPlan != null) {
			System.out.println("Current goal: " + this.root.getLogicalExpression().toString());
			
			// Update reward and terminal functions
			
			rf = new SingleGoalLERF(this.root.getLogicalExpression(), 10, -1);
			tf = new SingleLETF(this.root.getLogicalExpression()); 
			goalCondition = new TFGoalCondition(tf);
			
			planner.setRf(rf);
			planner.setTf(tf);
			
			// Solve the OO-MDP
			planner.planFromState(currState);
			Policy p = new GreedyQPolicy((QComputablePlanner)planner);

			EpisodeAnalysis ea = p.evaluateBehavior(currState, rf, tf);
			
			// Add low level plan to overall plan and update current state to the end of that subgoal plan
			actionSequence.add(ea.getActionSequenceString());
			currState = ea.getState(ea.stateSequence.size() - 1);
			this.root = this.root.getParent();
			
			
			highLevelPlan = highLevelPlan.getParent();
		}
		
		return actionSequence; 	

	}
		
	/**
	 * Performs a BFS on a graph of subgoals to find a high-level plan from the start to the goal condition
	 * @param root
	 * @param initialState
	 * @return
	 */
	public Node getHighLevelPlan() {
		ArrayDeque<Node> nodeQueue = new ArrayDeque<Node>();
		
		nodeQueue.add(this.root);
		Node curr = null;
		while (!nodeQueue.isEmpty()) {
			curr = nodeQueue.poll();
			if (curr.getLogicalExpression().evaluateIn(this.initialState)) {
				return curr;
			}
			if (curr.getChildren() != null) {
				nodeQueue.addAll(curr.getChildren());
			}
		}
		
		return curr;
	}
	
}
