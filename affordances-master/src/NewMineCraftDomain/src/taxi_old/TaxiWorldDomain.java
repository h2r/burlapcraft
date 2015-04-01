package taxi;

import java.util.*;

import affordances.KnowledgeBase;
import burlap.behavior.affordances.AffordancesController;
import burlap.behavior.policyblocks.PolicyBlocksPolicy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.EpisodeSequenceVisualizer;
import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.ValueFunctionInitialization;
import burlap.behavior.singleagent.ValueFunctionInitialization.ConstantValueFunctionInitialization;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.options.PrimitiveOption;
import burlap.behavior.singleagent.planning.QComputablePlanner;
import burlap.behavior.singleagent.planning.commonpolicies.AffordanceGreedyQPolicy;
import burlap.behavior.singleagent.planning.commonpolicies.EpsilonGreedy;
import burlap.behavior.singleagent.planning.commonpolicies.GreedyQPolicy;
import burlap.behavior.singleagent.planning.stochastic.rtdp.AffordanceBoundedRTDP;
import burlap.behavior.singleagent.planning.stochastic.rtdp.RTDP;
import burlap.behavior.statehashing.DiscreteStateHashFactory;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.TransitionProbability;
import burlap.oomdp.singleagent.Action;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.singleagent.common.SingleGoalPFRF;
import burlap.oomdp.singleagent.common.SinglePFTF;
import burlap.oomdp.visualizer.Visualizer;

/**
 * Class definition for the taxi world domain. Allows for generation of a taxi
 * world domain with any number of passengers. Based on the four rooms domain
 */
public class TaxiWorldDomain implements DomainGenerator {
    public static int[][] MAP;
    public static boolean MAPGENERATED = false;
    public static Domain DOMAIN = null;

    // Misc. Values
    public static final int MAXX = 16;
    public static final int MAXY = 11;
    // Using Integer instead of int because if not set, will throw a
    // NullPointerException
    public static Integer GOALX;
    public static Integer GOALY;
    public static Integer GOALLEFT;
    
    public static final double LEARNINGRATE = 0.99;
    public static final double DISCOUNTFACTOR = 0.95;
    public static final double LAMBDA = 1.0;
    public static final double GAMMA = 0.2;
    public static RewardFunction rf;
    public static TerminalFunction tf;

    /**
     * Returns random open spots on the map, bounded by max
     * 
     * @param max
     *            the maximum number of passengers to be generated
     * @return 2-d array of open spots on the map
     */
    public static int[][] getRandomSpots(int max) {
	Random rand = new Random();
	int[][] spots = new int[max][2];
	ArrayList<Integer[]> open = getOpenSpots();

	for (int i = 0; i < max; i++) {
	    Integer[] temp = open.get(rand.nextInt(open.size()));
	    spots[i] = new int[] { (int) temp[0], (int) temp[1] };
	}

	return spots;
    }

    /**
     * Returns all empty spots on the map
     * 
     * @return an ArrayList of all empty spots in the form int[] {x, y}
     */
    public static ArrayList<Integer[]> getOpenSpots() {
	if (!MAPGENERATED) {
	    generateMap();
	}

	ArrayList<Integer[]> openSpots = new ArrayList<Integer[]>();
	for (int x = 0; x <= MAXX; x++) {
	    for (int y = 0; y <= MAXY; y++) {
		if (MAP[x][y] == 0) {
		    openSpots.add(new Integer[] { x, y });
		}
	    }
	}

	return openSpots;
    }

    /**
     * Concatenates an arbitrary number of arrays
     * 
     * @param first
     *            the first array in the ordering
     * @param rest
     *            all of the other arrays in the ordering
     * @return a concatenation of all arrays passed
     */
    public static <T> T[] arrConcat(T[] first, T[]... rest) {
	int totalLength = first.length;
	for (T[] array : rest) {
	    totalLength += array.length;
	}

	T[] result = Arrays.copyOf(first, totalLength);
	int offset = first.length;

	for (T[] array : rest) {
	    System.arraycopy(array, 0, result, offset, array.length);
	    offset += array.length;
	}

	return result;
    }

    @Override
    public Domain generateDomain() {
	if (TaxiNameSpace.MAXPASS == null) {
	    throw new RuntimeException(
		    "Maximum number of passengers not set; set MAXPASS.");
	}

	DOMAIN = new SADomain();
	generateMap();

	Attribute xatt = new Attribute(DOMAIN, TaxiNameSpace.ATTX,
		Attribute.AttributeType.DISC);
	xatt.setDiscValuesForRange(0, MAXX, 1);
	Attribute yatt = new Attribute(DOMAIN, TaxiNameSpace.ATTY,
		Attribute.AttributeType.DISC);
	yatt.setDiscValuesForRange(0, MAXY, 1);
	Attribute cyatt = new Attribute(DOMAIN, TaxiNameSpace.ATTCARRY,
		Attribute.AttributeType.DISC);
	cyatt.setDiscValuesForRange(0, TaxiNameSpace.MAXPASS, 1);
	Attribute cdatt = new Attribute(DOMAIN, TaxiNameSpace.ATTCARRIED,
		Attribute.AttributeType.DISC);
	cdatt.setDiscValuesForRange(0, 1, 1);
	Attribute datt = new Attribute(DOMAIN, TaxiNameSpace.ATTDROPPED,
		Attribute.AttributeType.DISC);
	datt.setDiscValuesForRange(0, 1, 1);

	DOMAIN.addAttribute(xatt);
	DOMAIN.addAttribute(yatt);
	DOMAIN.addAttribute(cyatt);
	DOMAIN.addAttribute(cdatt);
	DOMAIN.addAttribute(datt);

	ObjectClass agentClass = new ObjectClass(DOMAIN, TaxiNameSpace.CLASSAGENT);
	agentClass.addAttribute(xatt);
	agentClass.addAttribute(yatt);
	agentClass.addAttribute(cyatt);

	ObjectClass passClass = new ObjectClass(DOMAIN, TaxiNameSpace.CLASSPASS);
	passClass.addAttribute(xatt);
	passClass.addAttribute(yatt);
	passClass.addAttribute(cdatt);
	passClass.addAttribute(datt);
	DOMAIN.addObjectClass(passClass);

	DOMAIN.addObjectClass(agentClass);

	Action north = new PrimitiveOption(new NorthAction(TaxiNameSpace.ACTIONNORTH, DOMAIN,
		""));
	Action south = new PrimitiveOption(new SouthAction(TaxiNameSpace.ACTIONSOUTH, DOMAIN,
		""));
	Action east = new PrimitiveOption(
		new EastAction(TaxiNameSpace.ACTIONEAST, DOMAIN, ""));
	Action west = new PrimitiveOption(
		new WestAction(TaxiNameSpace.ACTIONWEST, DOMAIN, ""));
	Action pickUp = new PrimitiveOption(new PickUpAction(TaxiNameSpace.ACTIONPICKUP,
		DOMAIN, ""));
	Action dropOff = new PrimitiveOption(new DropOffAction(TaxiNameSpace.ACTIONDROPOFF,
		DOMAIN, ""));

	DOMAIN.addAction(north);
	DOMAIN.addAction(south);
	DOMAIN.addAction(east);
	DOMAIN.addAction(west);
	DOMAIN.addAction(pickUp);
	DOMAIN.addAction(dropOff);

	PropositionalFunction atGoal = new AtGoalPF(TaxiNameSpace.PFATGOAL, DOMAIN,
		new String[] { TaxiNameSpace.CLASSAGENT, TaxiNameSpace.CLASSPASS });
	PropositionalFunction atPassenger = new AtPassPF(TaxiNameSpace.PFATPASS, DOMAIN,
		new String[] { TaxiNameSpace.CLASSAGENT, TaxiNameSpace.CLASSPASS });
	PropositionalFunction atFinish = new AtFinishPF(TaxiNameSpace.PFATFINISH, DOMAIN,
		new String[] { TaxiNameSpace.CLASSAGENT, TaxiNameSpace.CLASSPASS });

	DOMAIN.addPropositionalFunction(atGoal);
	DOMAIN.addPropositionalFunction(atPassenger);
	DOMAIN.addPropositionalFunction(atFinish);

	rf = new SingleGoalPFRF(DOMAIN.getPropFunction(TaxiNameSpace.PFATFINISH));
	tf = new SinglePFTF(DOMAIN.getPropFunction(TaxiNameSpace.PFATFINISH));

	return DOMAIN;
    }

    /**
     * Get a clean state
     * 
     * @return a clean state s for the domain
     */
    public static State getCleanState() {
	State s = new State();

	s.addObject(new ObjectInstance(DOMAIN.getObjectClass(TaxiNameSpace.CLASSAGENT),
			TaxiNameSpace.CLASSAGENT));

	for (int i = 1; i <= TaxiNameSpace.MAXPASS; i++) {
	    s.addObject(new ObjectInstance(DOMAIN.getObjectClass(TaxiNameSpace.CLASSPASS),
	    		TaxiNameSpace.CLASSPASS + i));
	}

	return s;
    }

    /**
     * Sets the agent to position (x, y)
     * 
     * @param s
     *            the current state
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     */
    public static void setAgent(State s, int x, int y) {
	ObjectInstance agent = s.getObjectsOfTrueClass(TaxiNameSpace.CLASSAGENT).get(0);
	agent.setValue(TaxiNameSpace.ATTX, x);
	agent.setValue(TaxiNameSpace.ATTY, y);
	agent.setValue(TaxiNameSpace.ATTCARRY, 0);
    }

    /**
     * Sets the goal to position (x, y)
     * 
     * @param s
     *            the current state
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     */
    public static void setGoal(int x, int y) {
    	GOALX = x;
    	GOALY = y;
    	GOALLEFT = TaxiNameSpace.MAXPASS;
    }

    /**
     * Sets the passenger num to the coordinate (x, y)
     * 
     * @param s
     *            the current state
     * @param num
     *            the passenger number
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     */
    public static void setPassenger(State s, int num, int x, int y) {
	if (num > TaxiNameSpace.MAXPASS || num < 1) {
	    throw new IllegalArgumentException("Invalid passenger number");
	}

	ObjectInstance pass = s.getObjectsOfTrueClass(TaxiNameSpace.CLASSPASS).get(num - 1);

	pass.setValue(TaxiNameSpace.ATTX, x);
	pass.setValue(TaxiNameSpace.ATTY, y);
	pass.setValue(TaxiNameSpace.ATTCARRIED, 0);
	pass.setValue(TaxiNameSpace.ATTDROPPED, 0);
    }

    /**
     * Generates the map
     */
    public static void generateMap() {
	MAP = new int[MAXX + 1][MAXY + 1];
	frameMap();
	setStandardWalls();
	MAPGENERATED = true;
    }

    /**
     * Sets the outside frame of the map
     */
    public static void frameMap() {
	for (int x = 0; x <= MAXX; x++) {
	    for (int y = 0; y <= MAXY; y++) {
		if (x == 0 || x == MAXX || y == 0 || y == MAXY) {
		    MAP[x][y] = 1;
		} else {
		    MAP[x][y] = 0;
		}
	    }
	}
    }

    /**
     * Sets the standard wall positions
     */
    public static void setStandardWalls() {
	verticalWall(1, 4, 2);
	verticalWall(1, 4, 6);
	verticalWall(1, 4, 12);
	verticalWall(7, 10, 4);
	verticalWall(5, 8, 9);
	verticalWall(7, 10, 12);
    }

    /**
     * Creates a horizontal wall for given values
     * 
     * @param xi
     *            initial x coordinate
     * @param xf
     *            final x coordinate
     * @param y
     *            y coordinate
     */
    protected static void horizontalWall(int xi, int xf, int y) {
	for (int x = xi; x <= xf; x++) {
	    MAP[x][y] = 1;
	}
    }

    /**
     * Creates a vertical wall for given values
     * 
     * @param yi
     *            initial y coordinate
     * @param yf
     *            final y coordinate
     * @param x
     *            x coordinate
     */
    protected static void verticalWall(int yi, int yf, int x) {
	for (int y = yi; y <= yf; y++) {
	    MAP[x][y] = 1;
	}
    }

    /**
     * Attempts to move the agent
     * 
     * @param s
     *            the current state
     * @param xd
     *            the x coordinate of the destination
     * @param yd
     *            the y coordinate of the destination
     */
    public static void move(State s, int xd, int yd) {
	ObjectInstance agent = s.getObjectsOfTrueClass(TaxiNameSpace.CLASSAGENT).get(0);
	int ax = agent.getDiscValForAttribute(TaxiNameSpace.ATTX);
	int ay = agent.getDiscValForAttribute(TaxiNameSpace.ATTY);
	int nx = ax + xd;
	int ny = ay + yd;

	if (MAP[nx][ny] == 1) {
	    nx = ax;
	    ny = ay;
	}

	int passNum = agent.getDiscValForAttribute(TaxiNameSpace.ATTCARRY);

	if (passNum != 0) {
	    ObjectInstance pass = s.getObjectsOfTrueClass(TaxiNameSpace.CLASSPASS).get(
		    passNum - 1);
	    pass.setValue(TaxiNameSpace.ATTX, nx);
	    pass.setValue(TaxiNameSpace.ATTY, ny);
	}

	agent.setValue(TaxiNameSpace.ATTX, nx);
	agent.setValue(TaxiNameSpace.ATTY, ny);
    }

    /**
     * Attempts to pick up a passenger
     * 
     * @param s
     *            the current state
     */
    public static void pickUp(State s) {
	ObjectInstance agent = s.getObjectsOfTrueClass(TaxiNameSpace.CLASSAGENT).get(0);
	int ax = agent.getDiscValForAttribute(TaxiNameSpace.ATTX);
	int ay = agent.getDiscValForAttribute(TaxiNameSpace.ATTY);

	for (int i = 1; i <= TaxiNameSpace.MAXPASS; i++) {
	    ObjectInstance pass = s.getObjectsOfTrueClass(TaxiNameSpace.CLASSPASS).get(i - 1);
	    int px = pass.getDiscValForAttribute(TaxiNameSpace.ATTX);
	    int py = pass.getDiscValForAttribute(TaxiNameSpace.ATTY);

	    if (agent.getDiscValForAttribute(TaxiNameSpace.ATTCARRY) == 0
		    && pass.getDiscValForAttribute(TaxiNameSpace.ATTDROPPED) == 0 && ax == px
		    && ay == py) {
		agent.setValue(TaxiNameSpace.ATTCARRY, i);
		pass.setValue(TaxiNameSpace.ATTCARRIED, 1);
		break;
	    }
	}
    }

    /**
     * Attempts to drop off a passenger
     * 
     * @param s
     *            the current state
     */
    public static void dropOff(State s) {
	ObjectInstance agent = s.getObjectsOfTrueClass(TaxiNameSpace.CLASSAGENT).get(0);
	int ax = agent.getDiscValForAttribute(TaxiNameSpace.ATTX);
	int ay = agent.getDiscValForAttribute(TaxiNameSpace.ATTY);

	int gx = GOALX;
	int gy = GOALY;

	int passNum = agent.getDiscValForAttribute(TaxiNameSpace.ATTCARRY);

	if (passNum == 0) {
	    return;
	}
	ObjectInstance pass = s.getObjectsOfTrueClass(TaxiNameSpace.CLASSPASS).get(
		passNum - 1);

	if (pass.getDiscValForAttribute(TaxiNameSpace.ATTCARRIED) == 1
		&& agent.getDiscValForAttribute(TaxiNameSpace.ATTCARRY) != 0 && ax == gx
		&& ay == gy) {
	    agent.setValue(TaxiNameSpace.ATTCARRY, 0);
	    pass.setValue(TaxiNameSpace.ATTDROPPED, 1);
	    pass.setValue(TaxiNameSpace.ATTCARRIED, 0);
	    GOALLEFT--;
	}
    }

    /**
     * Defines the action of moving north
     */
    public static class NorthAction extends Action {
	/**
	 * Constructs the action
	 * 
	 * @param name
	 *            name of the action
	 * @param domain
	 *            domain tied to the action
	 * @param parameterClasses
	 *            string of parameters
	 */
	public NorthAction(String name, Domain domain, String parameterClasses) {
	    super(name, domain, parameterClasses);
	}

	@Override
	protected State performActionHelper(State st, String[] params) {
	    move(st, 0, 1);
	    return st;
	}
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		List<TransitionProbability> tps = new ArrayList<TransitionProbability>(4);
		State sprime = s.copy();
		move(sprime,0,1);
		tps.add(new TransitionProbability(sprime, 1.0));
		return tps;
	}
    }

    /**
     * Defines the action of moving south
     */
    public static class SouthAction extends Action {
	/**
	 * Constructs the action
	 * 
	 * @param name
	 *            name of the action
	 * @param domain
	 *            domain tied to the action
	 * @param parameterClasses
	 *            string of parameters
	 */
	public SouthAction(String name, Domain domain, String parameterClasses) {
	    super(name, domain, parameterClasses);
	}

	@Override
	protected State performActionHelper(State st, String[] params) {
	    move(st, 0, -1);
	    return st;
	}
	
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		List<TransitionProbability> tps = new ArrayList<TransitionProbability>(4);
		State sprime = s.copy();
		move(sprime,0,-1);
		tps.add(new TransitionProbability(sprime, 1.0));
		return tps;
	}
	
    }

    /**
     * Defines the action of moving east
     */
    public static class EastAction extends Action {
	/**
	 * Constructs the action
	 * 
	 * @param name
	 *            name of the action
	 * @param domain
	 *            domain tied to the action
	 * @param parameterClasses
	 *            string of parameters
	 */
	public EastAction(String name, Domain domain, String parameterClasses) {
	    super(name, domain, parameterClasses);
	}

	@Override
	protected State performActionHelper(State st, String[] params) {
	    move(st, 1, 0);
	    return st;
	}
	
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		List<TransitionProbability> tps = new ArrayList<TransitionProbability>(4);
		State sprime = s.copy();
		move(sprime,1,0);
		tps.add(new TransitionProbability(sprime, 1.0));
		return tps;
	}
    }

    /**
     * Defines the action of moving west
     */
    public static class WestAction extends Action {
	/**
	 * Constructs the action
	 * 
	 * @param name
	 *            name of the action
	 * @param domain
	 *            domain tied to the action
	 * @param parameterClasses
	 *            string of parameters
	 */
	public WestAction(String name, Domain domain, String parameterClasses) {
	    super(name, domain, parameterClasses);
	}

	@Override
	protected State performActionHelper(State st, String[] params) {
	    move(st, -1, 0);
	    return st;
	}
	
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		List<TransitionProbability> tps = new ArrayList<TransitionProbability>(4);
		State sprime = s.copy();
		move(sprime,-1,0);
		tps.add(new TransitionProbability(sprime, 1.0));
		return tps;
	}
    }

    /**
     * Defines the action of picking up a passenger
     */
    public static class PickUpAction extends Action {
	/**
	 * Constructs the action
	 * 
	 * @param name
	 *            name of the action
	 * @param domain
	 *            domain tied to the action
	 * @param parameterClasses
	 *            string of parameters
	 */
	public PickUpAction(String name, Domain domain, String parameterClasses) {
	    super(name, domain, parameterClasses);
	}

	@Override
	protected State performActionHelper(State st, String[] params) {
	    pickUp(st);
	    return st;
	}
	
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		List<TransitionProbability> tps = new ArrayList<TransitionProbability>(4);
		State sprime = s.copy();
		pickUp(sprime);
		tps.add(new TransitionProbability(sprime, 1.0));
		return tps;
	}
	
    }

    /**
     * Defines the action of dropping off a passenger
     */
    public static class DropOffAction extends Action {
	/**
	 * Constructs the action
	 * 
	 * @param name
	 *            name of the action
	 * @param domain
	 *            domain tied to the action
	 * @param parameterClasses
	 *            string of parameters
	 */
	public DropOffAction(String name, Domain domain, String parameterClasses) {
	    super(name, domain, parameterClasses);
	}

	@Override
	protected State performActionHelper(State st, String[] params) {
	    dropOff(st);
	    return st;
	}
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		List<TransitionProbability> tps = new ArrayList<TransitionProbability>(4);
		State sprime = s.copy();
		dropOff(sprime);
		tps.add(new TransitionProbability(sprime, 1.0));
		return tps;
	}
    }

    /**
     * Defines the propositional function of being at a goal
     */
    public static class AtGoalPF extends PropositionalFunction {
	/**
	 * Constructs the PF
	 * 
	 * @param name
	 *            name of the PF
	 * @param domain
	 *            domain tied to the PF
	 * @param parameterClasses
	 *            string of parameters
	 */
	public AtGoalPF(String name, Domain domain, String[] parameterClasses) {
	    super(name, domain, parameterClasses);
	}

	@Override
	public boolean isTrue(State st, String[] params) {
	    ObjectInstance agent = st.getObject(params[0]);
	    int ax = agent.getDiscValForAttribute(TaxiNameSpace.ATTX);
	    int ay = agent.getDiscValForAttribute(TaxiNameSpace.ATTY);

	    int gx = GOALX;
	    int gy = GOALY;

	    if (ax == gx && ay == gy) {
		return true;
	    }

	    return false;
	}
    }

    /**
     * Defines the propositional function of being at the finish point
     */
    public static class AtFinishPF extends PropositionalFunction {
	/**
	 * Constructs the PF
	 * 
	 * @param name
	 *            name of the PF
	 * @param domain
	 *            domain tied to the PF
	 * @param parameterClasses
	 *            string of parameters
	 */
	public AtFinishPF(String name, Domain domain, String[] parameterClasses) {
	    super(name, domain, parameterClasses);
	}

	@Override
	public boolean isTrue(State st, String[] params) {
	    ObjectInstance agent = st.getObject(params[0]);
	    int ax = agent.getDiscValForAttribute(TaxiNameSpace.ATTX);
	    int ay = agent.getDiscValForAttribute(TaxiNameSpace.ATTY);

	    int gx = GOALY;
	    int gy = GOALY;

	    if (ax == gx && ay == gy && GOALLEFT == 0) {
		return true;
	    }
	    return false;
	}
    }

    /**
     * Defines the propositional function of being on a passenger
     */
    public static class AtPassPF extends PropositionalFunction {
	/**
	 * Constructs the PF
	 * 
	 * @param name
	 *            name of the PF
	 * @param domain
	 *            domain tied to the PF
	 * @param parameterClasses
	 *            string of parameters
	 */
	public AtPassPF(String name, Domain domain, String[] parameterClasses) {
	    super(name, domain, parameterClasses);
	}

	@Override
	public boolean isTrue(State st, String[] params) {
	    ObjectInstance agent = st.getObject(params[0]);
	    int ax = agent.getDiscValForAttribute(TaxiNameSpace.ATTX);
	    int ay = agent.getDiscValForAttribute(TaxiNameSpace.ATTY);

	    for (int i = 1; i <= TaxiNameSpace.MAXPASS; i++) {
		ObjectInstance pass = st.getObjectsOfTrueClass(TaxiNameSpace.CLASSPASS).get(
			i - 1);
		int px = pass.getDiscValForAttribute(TaxiNameSpace.ATTX);
		int py = pass.getDiscValForAttribute(TaxiNameSpace.ATTY);

		if (agent.getDiscValForAttribute(TaxiNameSpace.ATTCARRY) == 0
			&& pass.getDiscValForAttribute(TaxiNameSpace.ATTDROPPED) == 0
			&& pass.getDiscValForAttribute(TaxiNameSpace.ATTCARRIED) == 0
			&& ax == px && ay == py) {
		    	return true;
			}
	    }

	    return false;
	}
    }
    
    /**
     * Drives the learning
     * 
     * @param args
     *            none
     */
    public static void main(String[] args) {
	// Set the total number of passengers to be used in the domain

		TaxiWorldDomain txd = new TaxiWorldDomain();
		Domain d = txd.generateDomain();
		State initialState = getCleanState();
		TaxiWorldStateParser parser = new TaxiWorldStateParser();
		DiscreteStateHashFactory hashFactory = new DiscreteStateHashFactory();
		hashFactory.setAttributesForClass(TaxiNameSpace.CLASSAGENT,
			DOMAIN.getObjectClass(TaxiNameSpace.CLASSAGENT).attributeList);
//		QLearning Q = new QLearning(DOMAIN, rf, tf, DISCOUNTFACTOR, hashFactory, 0.2, LEARNINGRATE, Integer.MAX_VALUE);
//		PolicyBlocksPolicy p = new PolicyBlocksPolicy(Q, 0.05);
//		Q.setLearningPolicy(p);
		
		ValueFunctionInitialization lowerBound = new ConstantValueFunctionInitialization(-10.0);
		ValueFunctionInitialization upperBound = new ConstantValueFunctionInitialization(10.0);
		double delta = 1;
		int numRollouts = 2000;
		int maxDepth = 200;
		KnowledgeBase affKB = new KnowledgeBase();
		affKB.load(d, null, "/Users/dabel/Projects/affordances/src/NewMineCraftDomain/src/taxi/kb/taxi_test.kb", true, true);

		AffordancesController affController = affKB.getAffordancesController();
		
//		AffordanceBoundedRTDP affbrtdp = new AffordanceBoundedRTDP(DOMAIN, rf, tf, GAMMA, hashFactory, lowerBound, upperBound, delta, numRollouts, affController);
		RTDP planner = new RTDP(DOMAIN, rf, tf, GAMMA, hashFactory, 1, numRollouts, delta, maxDepth);
		int[][] passPos = getRandomSpots(TaxiNameSpace.MAXPASS);
		for (int i = 1; i <= 100; i++) {
		    setAgent(initialState, 4, 5);
		    setGoal(4, 5);
	
		    for (int j = 1; j <= TaxiNameSpace.MAXPASS; j++) {
			setPassenger(initialState, j, passPos[j - 1][0], passPos[j - 1][1]);
		    }
	
		    System.out.print("Episode " + i + ": ");
//		    affbrtdp.planFromState(initialState);
		    
		    planner.planFromState(initialState);
		    
//		    Policy policy = new GreedyQPolicy((QComputablePlanner)planner);
//			Policy policy = new AffordanceGreedyQPolicy(affController, (QComputablePlanner)affbrtdp);
//			EpisodeAnalysis ea = policy.evaluateBehavior(initialState, rf, tf);
			
//		    System.out.println("\tSteps: " + ea.numTimeSteps());
//		    analyzer.writeToFile(String.format("output/e%03d", i), parser);
//		    System.out.println(((EpsilonGreedy) policy).getEpsilon());
		}

	Visualizer v = TaxiWorldVisualizer.getVisualizer();
	new EpisodeSequenceVisualizer(v, d, parser, "output");
    }
}