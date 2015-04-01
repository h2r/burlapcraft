package subgoals;

import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.logicalexpressions.LogicalExpression;

/*
 * CLASS: Subgoal
 * For use in Subgoal Planning
 */

public class Subgoal {
	
	private LogicalExpression pre;
	private LogicalExpression post;
	private LogicalExpression[] conditions;
	
	
	public Subgoal(LogicalExpression pre, LogicalExpression post) {
		this.pre = pre;
		this.post = post;
	}
	
	public LogicalExpression getPre() {
		return this.pre;
	}

	public LogicalExpression getPost() {
		return this.post;
	}
	
	public boolean isPost(LogicalExpression pf) {
		return this.post == pf;
	}
}
