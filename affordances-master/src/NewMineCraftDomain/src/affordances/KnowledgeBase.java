package affordances;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import minecraft.NameSpace;
import tests.ResourceLoader;
import burlap.behavior.affordances.AffordanceDelegate;
import burlap.behavior.affordances.AffordancesController;
import burlap.behavior.singleagent.planning.OOMDPPlanner;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.Domain;
import burlap.oomdp.singleagent.Action;

/**
 * The knowledge-base is a wrapper around a collection of objects of type T that are used
 * to encode knowledge in the minecraft world (ie. affordances, subgoals, etc...)
 * @author dabel
 */
public class KnowledgeBase {
	private List<AffordanceDelegate>			affDelegateList;
	private AffordancesController				affController;
	private String								filename;

	public KnowledgeBase() {
		this.affDelegateList = new ArrayList<AffordanceDelegate>();
		this.affController = new AffordancesController(this.affDelegateList);
	}
	
	public void add(AffordanceDelegate aff) {
		// Only add if it's not already in the KB
		if(!this.affDelegateList.contains(aff)) {
			this.affDelegateList.add(aff);
			this.affController.addAffordanceDelegate(aff);
		}
	}
	
	public void remove(AffordanceDelegate aff) {
		// Only remove if it's in the KB
		if(this.affDelegateList.contains(aff)) {
			this.affDelegateList.remove(aff);
			this.affController.removeAffordance(aff);
		}
	}
	
	public String getName() {
		return this.filename;
	}
	
	public void setName(String filename) {
		this.filename = filename;
	}
	
	public void print() {
		// -- GRID --
		for (AffordanceDelegate aff: this.affDelegateList) {
			System.out.println(aff.toFile());
		}
	}
	
	public void save() {
		// -- NON GRID --
		String fpath = NameSpace.PATHKB + "/" + filename;
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fpath)));
			
			// For grid
//			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
			
			for (AffordanceDelegate aff: this.affDelegateList) {
				bw.write(aff.toFile());
			}
			bw.close();
		} catch (IOException e) {
			System.out.println("ERROR");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void load(Domain d, Map<String,Action> temporallyExtActions, String filename, boolean hardFlag, boolean expertFlag) {
		AffordanceDelegate aff = null;
		
		try {
//			BufferedReader reader = new BufferedReader(new FileReader(NameSpace.PATHKB + filename));
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			
			StringBuilder sBuilder = new StringBuilder();
			String nextLine = reader.readLine();

			while(nextLine != null) {
				sBuilder.append(nextLine + "\n");
				nextLine = reader.readLine();
			}
			
			String[] kbStrings = sBuilder.toString().split("===");
			
			String[] processedStrings = new String[kbStrings.length];
			
			// Remove the last element from KBString (closing equals signs)
			for(int i = 0; i < processedStrings.length; i++) {
				processedStrings[i] = kbStrings[i];
			}
			
			for(String affString : processedStrings) {
				// Remove the final newline character from the affordance
				String slicedString = affString.substring(0, affString.length() - 1);
				aff = AffordanceDelegate.load(d, temporallyExtActions, slicedString, expertFlag);
				this.affDelegateList.add(aff);
				reader.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.affController = new AffordancesController(this.affDelegateList, hardFlag, expertFlag);
	}
	
	// --- ACCESSORS ---
	
	public AffordancesController getAffordancesController() {
		return this.affController;
	}
	
	public List<AffordanceDelegate> getAffordances() {
		return this.affDelegateList;
	}

}
