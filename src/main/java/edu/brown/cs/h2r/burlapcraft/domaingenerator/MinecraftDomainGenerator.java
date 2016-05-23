package edu.brown.cs.h2r.burlapcraft.domaingenerator;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.singleagent.common.UniformCostRF;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction.PFAgentOnBlock;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction.PFBlockIsType;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.state.BCAgent;
import edu.brown.cs.h2r.burlapcraft.state.BCBlock;
import edu.brown.cs.h2r.burlapcraft.state.BCMap;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.*;

/**
 * Class to generate burlap domain for minecraft. You can set a white list for actions if you only want to use
 * a subset of the actions provided. The model for the domain is a {@link FactoredModel} that allows you
 * to specify independent reward functions and termination conditions from the state transition function. If you
 * do not set the {@link RewardFunction} or {@link TerminalFunction} then a {@link UniformCostRF} and
 * {@link GoldBlockTF} will be used (motivates the agent to get to a gold block). The state model generated
 * is a {@link MinecraftModel} instance.
 * @author Krishna Aluru and James MacGlashan
 *
 */

public class MinecraftDomainGenerator implements DomainGenerator {


	protected Set<String> whiteListActions = new HashSet<String>();

	protected RewardFunction rf;
	protected TerminalFunction tf;
	



	public void setActionWhiteListToNavigationOnly(){
		this.whiteListActions = new HashSet<String>();
		this.whiteListActions.add(HelperNameSpace.ACTION_MOVE);
		this.whiteListActions.add(HelperNameSpace.ACTION_ROTATE_LEFT);
		this.whiteListActions.add(HelperNameSpace.ACTION_ROTATE_RIGHT);
	}
	
	public void setActionWhiteListToNavigationAndDestroy(){
		this.whiteListActions = new HashSet<String>();
		this.whiteListActions.add(HelperNameSpace.ACTION_MOVE);
		this.whiteListActions.add(HelperNameSpace.ACTION_ROTATE_LEFT);
		this.whiteListActions.add(HelperNameSpace.ACTION_ROTATE_RIGHT);
		this.whiteListActions.add(HelperNameSpace.ACTION_DOWN_ONE);
		this.whiteListActions.add(HelperNameSpace.ACTION_DEST_BLOCK);
		this.whiteListActions.add(HelperNameSpace.ACTION_CHANGE_ITEM);
	}

	public Set<String> getWhiteListActions() {
		return whiteListActions;
	}

	public void setWhiteListActions(Set<String> whiteListActions) {
		this.whiteListActions = whiteListActions;
	}

	public void addActionToWhiteList(String actionName){
		this.whiteListActions.add(actionName);
	}


	public RewardFunction getRf() {
		return rf;
	}

	public void setRf(RewardFunction rf) {
		this.rf = rf;
	}

	public TerminalFunction getTf() {
		return tf;
	}

	public void setTf(TerminalFunction tf) {
		this.tf = tf;
	}

	public List<PropositionalFunction> generatePfs(){
		List<PropositionalFunction> pfs = new ArrayList<PropositionalFunction>();
		pfs.add(new PFAgentOnBlock(HelperNameSpace.PF_AGENT_ON_BLOCK, new String[] {CLASS_AGENT, HelperNameSpace.CLASS_BLOCK}));

		for(Block b : HelperActions.mineableBlocks) {
			int id = Block.getIdFromBlock(b);
			pfs.add(new PFBlockIsType(HelperNameSpace.PF_BLOCK_IS_TYPE +id, new String[]{HelperNameSpace.CLASS_BLOCK}, id));
		}
		return pfs;
	}

	@Override
	public OOSADomain generateDomain() {
		
		OOSADomain domain = new OOSADomain();

		domain.addStateClass(CLASS_AGENT, BCAgent.class)
				.addStateClass(CLASS_BLOCK, BCBlock.class)
				.addStateClass(CLASS_MAP, BCMap.class);

		
		// Actions
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_MOVE)) {
			domain.addActionType(new MinecraftActionType(ACTION_MOVE));
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_ROTATE_RIGHT)) {
			domain.addActionType(new MinecraftActionType(ACTION_ROTATE_RIGHT));
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_ROTATE_LEFT)) {
			domain.addActionType(new MinecraftActionType(ACTION_ROTATE_LEFT));
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_AHEAD)) {
			domain.addActionType(new MinecraftActionType(ACTION_AHEAD));
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_DOWN_ONE)) {
			domain.addActionType(new MinecraftActionType(ACTION_DOWN_ONE));
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_PLACE_BLOCK)) {
			domain.addActionType(new MinecraftActionType(ACTION_PLACE_BLOCK));
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_DEST_BLOCK)) {
			domain.addActionType(new MinecraftActionType(ACTION_DEST_BLOCK));
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_CHANGE_ITEM)) {
			domain.addActionType(new MinecraftActionType(ACTION_CHANGE_ITEM));
		}


		OODomain.Helper.addPfsToDomain(domain, this.generatePfs());

		MinecraftModel smodel = new MinecraftModel();
		RewardFunction rf = this.rf;
		TerminalFunction tf = this.tf;

		if(rf == null){
			rf = new UniformCostRF();
		}
		if(tf == null){
			tf = new GoldBlockTF();
		}

		FactoredModel model = new FactoredModel(smodel, rf, tf);
		domain.setModel(model);

		return domain;
		
	}
	
}