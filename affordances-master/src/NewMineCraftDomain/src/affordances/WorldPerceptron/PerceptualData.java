package affordances.WorldPerceptron;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import minecraft.MinecraftStateParser;
import minecraft.NameSpace;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.Action;

public class PerceptualData {
	
	private String tag;
	private int[] data;
	
	/**
	 * 
	 * @param data
	 * @param tag
	 */
	public PerceptualData(int [] data, String tag) {
		this.tag = tag;
		this.data = data;
	}
	
	/**
	 * 
	 * @param state
	 * @param rows
	 * @param cols
	 * @param height
	 * @param xLookDistance
	 * @param yLookDistance
	 * @param zLookDistance
	 * @param sampleDownBy
	 */
	public PerceptualData(State state, int rows, int cols, int height, int xLookDistance, int yLookDistance, int zLookDistance) {
		char[][][] charArray = MinecraftStateParser.stateToCharArray(state, rows, cols, height);
		
		ObjectInstance agent = state.getObjectsOfTrueClass(NameSpace.CLASSAGENT).get(0);

		int agentRotation = agent.getDiscValForAttribute(NameSpace.ATROTDIR);
		int agentVertRotation = agent.getDiscValForAttribute(NameSpace.ATVERTDIR);
		
		this.data = PerceptronHelpers.agentPerceptionToFactoredVector(charArray, yLookDistance, xLookDistance, zLookDistance, agentRotation, agentVertRotation);
	}
	
	public PerceptualData(Instance instance) {
		this.tag = instance.stringValue(instance.classAttribute());		
		int [] data = new int [instance.numAttributes()-1];
		double [] dataset = instance.toDoubleArray();
		for (int i = 0; i < instance.numAttributes()-1; i++) {
			data[i] = (int) (dataset[i]);
		}
		this.data = data;
	}
	
	public String getTagString() {
		return this.tag;
	}
	
	public int[] getData() {
		return this.data;
	}
	
	/**
	 * 
	 * @param outputPath
	 * @param fileName
	 * @param allAttributes
	 * @param labeled
	 */
	public void printToArffFile(String outputPath, String fileName, boolean labeled, HashSet<String> allTags) {
		List<PerceptualData> percData = new ArrayList<PerceptualData>();
		percData.add(this);
		ArffHelpers.printArffFile(outputPath, fileName, percData, allTags, labeled);
	}
	
	public void removeUniformIndicesAndResample(HashSet<Integer> uniformIndices, int sampleDownByX) {
		int newToReturnPercDataSize = this.data.length-uniformIndices.size();
		int[] newPercDataData = new int[newToReturnPercDataSize];
		int newPercDataIndex = 0;
		
		//Throw out uniform indices
		for (int percDataIndex = 0; percDataIndex < this.getData().length; percDataIndex++) {
			if (!uniformIndices.contains(percDataIndex)) {
				newPercDataData[newPercDataIndex] = this.getData()[percDataIndex];
				newPercDataIndex++;
			}
		}
		//Resample data
		int [] resampledData = PerceptronHelpers.resamplePercData(newPercDataData, sampleDownByX);
		
		this.data = resampledData;
	}
	
	
	/**
	 * 
	 * @param classifier
	 * @return action to execute
	 */
	public Action getActionFromClassifier(Classifier classifier, String outputPath, HashSet<String> allTags, Domain domain) {
		String tempFileName = "temp";
		
		//Write the arff file
		printToArffFile(outputPath, tempFileName, false, allTags);
		
		//Get label using classifier
		String toRead = outputPath + tempFileName + ".arff";
		Instances unlabeled = ArffHelpers.fileToInstances(toRead);
		unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
		unlabeled.firstInstance().setClassMissing();
		
		Double label = null;
		try {
			double [] distribution = classifier.distributionForInstance(unlabeled.firstInstance());
			for (double perc: distribution) {
				System.out.print(perc + " ");
			}
			label = classifier.classifyInstance(unlabeled.firstInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		unlabeled.firstInstance().setClassValue(label);
		
		//Delete the arff file
//		File file = new File(toRead);
//		file.delete();
		
		
		//Turn label into grounded action
		Instance firstInstance = unlabeled.firstInstance();
		String labelString = firstInstance.stringValue(firstInstance.classAttribute());		

		Action toReturn = domain.getAction(labelString);
		return toReturn;
	}
	
	
	public String getArffString(boolean labeled) {
		StringBuffer sb = new StringBuffer();
		String prefix = "";
		for (int i = 0; i < data.length; i++) {
			sb.append(prefix + Integer.toString(this.data[i]));
			prefix = ",";
		} 
		sb.append(",");
		if (labeled) {
			
			sb.append(this.tag);
		}
		else {
			sb.append("?");
		}

		sb.append("\n");
		String toReturn = sb.toString();
		
		return toReturn;
	}
	
}
