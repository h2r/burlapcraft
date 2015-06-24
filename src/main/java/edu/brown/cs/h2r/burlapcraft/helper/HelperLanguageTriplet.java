package edu.brown.cs.h2r.burlapcraft.helper;

public class HelperLanguageTriplet<Command, StateList, ActionList> {
	private Command c;
    private StateList s;
    private ActionList a;
    
    public HelperLanguageTriplet(Command c, StateList s, ActionList a) {
        this.c = c;
        this.s = s;
        this.a = a;
    }
    
    public Command getCommand() { 
    	return c; 
    }
    
    public StateList getStateList() { 
    	return s; 
    }
    
    public ActionList getActionList() {
    	return a;
    }
    
    public void setCommand(Command c) { 
    	this.c = c; 
    }
    
    public void setStateList(StateList s){ 
    	this.s = s; 
    }
    
    public void setActionList(ActionList a) {
    	this.a = a;
    }
    
    @Override
    public String toString() {
        return "LanguageTriplet [Command=" + this.c + ", StateList=" + this.s + ", ActionList=" + this.a + "]";
    }
    
    
}
