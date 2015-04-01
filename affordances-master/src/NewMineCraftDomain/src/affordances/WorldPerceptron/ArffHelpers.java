package affordances.WorldPerceptron;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

import weka.core.Instance;
import weka.core.Instances;

public class ArffHelpers {
	
	/**
	 * @param fileName
	 * @param toLearn
	 * @param labeled
	 * @return
	 */
	public static String getArffStringFromClusterPerceptions(String fileName, List<PerceptualData> perceptions, HashSet<String> allAttributes, boolean labeled) {
		StringBuffer sb = new StringBuffer();
		//Set up relation
		sb.append("@RELATION " + fileName + "\n");
		
		//No data -- break
		if (perceptions.isEmpty()) {
			System.out.println("\tNo data to write");
			return sb.toString();
		}
		
		int[] samplePercData = perceptions.get(0).getData();
		
		int numPerceptualDPs = samplePercData.length;
		//Set up attributes -- indices
		for (int index = 0; index < numPerceptualDPs; index++) {
			String attributeLine = "@ATTRIBUTE index" + index + " integer\n";  
			sb.append(attributeLine);
		}
		//Set up attributes -- clusterClasses
		String attributeString = getAttributesString(perceptions, allAttributes);
		sb.append(attributeString);

		//Set up data
		sb.append("@DATA\n");
		for (PerceptualData currPercData : perceptions) {
			String currArffString = currPercData.getArffString(labeled);
			sb.append(currArffString);
		}
		
		
		return sb.toString();
	}
	
	public static HashSet<String> allClassValStringsFromInstance(Instance instance) {
		HashSet<String> toReturn = new HashSet<String>();
		
		int numClassVals = instance.numClasses();
		
		double originalClassVal = instance.value(instance.classIndex());
		
		//Loop across indexes of class vals
		for (int i = 0; i < numClassVals; i++) {
			instance.setClassValue(i);
			String classAsString = instance.stringValue(instance.classAttribute());	
			toReturn.add(classAsString);
		}
		
		instance.setClassValue(originalClassVal);
		return toReturn;	
	}
	
	private static String getAttributesString(List<PerceptualData> perceptions, HashSet<String> allAttributes) {
		String toReturn = "@ATTRIBUTE class {";
		
		String prefix = "";
		for (String attributeString: allAttributes) {
			toReturn += prefix + attributeString;
			
			prefix = ",";
		}
		
		toReturn += "}\n";
		
		return toReturn;
	}
	
	 public static Instances fileToInstances(String filePath) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			return new Instances(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param outputPath directory path to print the .arff file to
	 * @param fileName name of the .arff file
	 * @param toLearn
	 */
	public static void printArffFile(String outputPath, String fileName, List<PerceptualData> perceptionsToPrint, HashSet<String> allAttributes, boolean labeled) {
		String arffString = getArffStringFromClusterPerceptions(fileName, perceptionsToPrint, allAttributes, labeled);
		PrintWriter out = null;
		try {
			out = new PrintWriter(outputPath + fileName + ".arff");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		out.println(arffString);
		out.close();
	}
}
