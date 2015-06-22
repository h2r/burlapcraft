package edu.brown.cs.h2r.burlapcraft.helper;

public class HelperCommandPair<Command, StateList> {
	private Command c;
    private StateList s;
    
    public HelperCommandPair(Command c, StateList s) {
        this.c = c;
        this.s = s;
    }
    
    public Command getCommand() { 
    	return c; 
    }
    
    public StateList getStateList() { 
    	return s; 
    }
    
    public void setC(Command c) { 
    	this.c = c; 
    }
    
    public void setS(StateList s){ 
    	this.s = s; 
    }
    
}
