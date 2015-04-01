package taxi;

public class TaxiNameSpace {
	
	// Attributes
	
    public static final String ATTX = "x";
    public static final String ATTY = "y";
    public static final String ATTCARRIED = "being carried";
    public static final String ATTCARRY = "carrying";
    public static final String ATTDROPPED = "dropped off";

    // Classes
    
    public static final String CLASSAGENT = "agent";
    public static final String CLASSPASS = "passenger";

    // Actions
    
    public static final String ACTIONNORTH = "north";
    public static final String ACTIONSOUTH = "south";
    public static final String ACTIONEAST = "east";
    public static final String ACTIONWEST = "west";
    public static final String ACTIONPICKUP = "pickup";
    public static final String ACTIONDROPOFF = "dropoff";

    
    // PFs
    public static final String PFATGOAL = "atGoal";
    public static final String PFATPASS = "atPassenger";
    public static final String PFATFINISH = "atFinish";
    
    // Misc vals
    public static final Integer MAXPASS = 2;
}
