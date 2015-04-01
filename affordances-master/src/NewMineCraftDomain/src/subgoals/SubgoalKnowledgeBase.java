package subgoals;

import java.util.ArrayList;
import java.util.List;

import minecraft.NameSpace;
import minecraft.MinecraftDomain.PropositionalFunctions.AgentHasAtLeastXGoldOrePF;
import minecraft.MinecraftDomain.PropositionalFunctions.AtLocationPF;
import burlap.behavior.affordances.AffordanceDelegate;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.GroundedProp;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.logicalexpressions.Conjunction;
import burlap.oomdp.logicalexpressions.LogicalExpression;
import burlap.oomdp.logicalexpressions.PFAtom;

public class SubgoalKnowledgeBase {

	private String worldName;
	private Domain domain;
	
	public SubgoalKnowledgeBase(String worldName, Domain domain) {
		// TODO Auto-generated constructor stub
		this.worldName = worldName;
		this.domain = domain;
	}

	public List<Subgoal> generateSubgoalKB(String worldName) {
		List<Subgoal> subgoalKB = new ArrayList<Subgoal>();
		
		// NOTE: ALWAYS add a subgoal with the FINAL goal first
		
		PropositionalFunction atLocOne = new AtLocationPF("AtLocationPF", this.domain, new String[]{NameSpace.CLASSAGENT}, 0,1,1);
		String[] atLocParams = AffordanceDelegate.makeFreeVarListFromObjectClasses(atLocOne.getParameterClasses());
		GroundedProp gp = new GroundedProp(atLocOne, atLocParams);
		LogicalExpression atLocLE = new PFAtom(gp);
		
		PropositionalFunction agentHasGoldOre = new AgentHasAtLeastXGoldOrePF("AgentHasGoldOre", this.domain, new String[]{NameSpace.CLASSAGENT}, 1);
		String[] goldOreParams = AffordanceDelegate.makeFreeVarListFromObjectClasses(agentHasGoldOre.getParameterClasses());
		GroundedProp goldOreGP = new GroundedProp(agentHasGoldOre, goldOreParams);
		LogicalExpression hasGoldLE = new PFAtom(goldOreGP);
		
		List<LogicalExpression> conjunctions = new ArrayList<LogicalExpression>();
		conjunctions.add(atLocLE);
		conjunctions.add(hasGoldLE);
		
		LogicalExpression atLocWithGold = new Conjunction(conjunctions);
		
		Subgoal sg = new Subgoal(atLocLE, atLocWithGold);
		
		subgoalKB.add(sg);
		
		return subgoalKB;
		
	}
	
}
