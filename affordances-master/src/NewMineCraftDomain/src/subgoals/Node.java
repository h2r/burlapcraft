package subgoals;

import java.util.ArrayList;

import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.logicalexpressions.LogicalExpression;

public class Node {
	
	private Node parent;
	private LogicalExpression le;
	private ArrayList<Node> children;

	public Node(LogicalExpression le, Node p) {
		this.parent = p;
		this.le = le;
		this.children = new ArrayList<Node>();
	}
	
	public Node getParent(){
		return this.parent;
	}
	
	public void setParent(Node p) {
		this.parent = p;
	}
	
	public void addChild(Node c){
		this.children.add(c);
	}
	
	public ArrayList<Node> getChildren() {
		return this.children;
	}
	
	public LogicalExpression getLogicalExpression() {
		return this.le;
	}

}
