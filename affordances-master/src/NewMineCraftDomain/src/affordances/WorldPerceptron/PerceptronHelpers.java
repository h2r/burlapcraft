package affordances.WorldPerceptron;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import minecraft.MapIO;
import minecraft.NameSpace;
import minecraft.MinecraftDomain.Helpers;

/**
 * Used to get perception data or resample it from a char array
 * @author Dhershkowitz
 *
 */
public class PerceptronHelpers {
	
	/**
	 * 
	 * @param charArray
	 * @return 3 element, x,y,z array of agent's position
	 */
	private static int[] getAgentPosition(char[][][] charArray) {
		for (int row = 0; row < charArray.length; row++) {
			for(int col = 0; col < charArray[0].length; col++) {
				for(int currHeight = 0; currHeight < charArray[0][0].length; currHeight++) {
					char currChar = charArray[row][col][currHeight];
					if (currChar == NameSpace.CHARAGENT) {
						return new int[]{col, row, currHeight};
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param numberOfRotations
	 * @param charArray
	 * @return
	 */
	private static char[][][] rotateCharArray(int numberOfRotations, char[][][] charArray) {
		char[][][]toReturn = charArray;
		for(int i = 0; i < numberOfRotations; i++) {
			char [][][] oldToReturn = toReturn;
			toReturn = new char[oldToReturn[0].length][oldToReturn.length][oldToReturn[0][0].length];
			for (int oldRow = 0; oldRow < oldToReturn.length; oldRow++) {
				for(int oldCol = 0; oldCol < oldToReturn[0].length; oldCol++) {
					for(int oldHeight = 0; oldHeight < oldToReturn[0][0].length; oldHeight++){
						toReturn[oldCol][oldToReturn.length-oldRow-1][oldHeight] = oldToReturn[oldRow][oldCol][oldHeight];
						
					}
				}
			}
		}
		return toReturn;
	}


	/**
	 * 
	 * @param charArray
	 * @param yLookDistance
	 * @param xLookDistance
	 * @param zLookDistance
	 * @param agentRotation
	 * @return a 3d char array centered around the agent
	 */
	public static char[][][] getAgentPerception(char[][][] charArray, int yLookDistance, int xLookDistance, int zLookDistance, int agentRotation) {
		char[][][] toReturn = new char[yLookDistance*2+1][xLookDistance*2+1][zLookDistance*2+1];
		int[] agentPos = getAgentPosition(charArray);
		
		int mapRows = charArray.length;
		int mapCols = charArray[0].length;
		int mapHeight = charArray[0][0].length;
		
		int agentX = agentPos[0];
		int agentY = agentPos[1];
		int agentZ = agentPos[2];
	
		for(int currX = -xLookDistance; currX<=xLookDistance; currX++) {
			for(int currY = -yLookDistance; currY<=yLookDistance; currY++) {
				for(int currZ = -zLookDistance; currZ<=zLookDistance; currZ++) {
					if (Helpers.withinMapAt(agentX+currX, agentY+currY, agentZ+currZ, mapCols, mapRows, mapHeight)){
						char currChar = charArray[agentY+currY][agentX+currX][agentZ+currZ];
						toReturn[currY+yLookDistance][currX+xLookDistance][currZ+zLookDistance] = currChar;
					}
					else {
						toReturn[currY+yLookDistance][currX+xLookDistance][currZ+zLookDistance] = NameSpace.CHAROUTOFBOUNDS;
					}
				}
			}	
		}
		
		//Rotate for agent's view
		toReturn = rotateCharArray(agentRotation, toReturn);
		
		return toReturn;
	}
	
	/**
	 * 
	 * @param charArray
	 */
	public static void printCharArray(char[][][] charArray) {
		for (int currHeight = charArray[0][0].length-1; currHeight >= 0; currHeight--) {
			for (int row = 0; row < charArray.length; row++) {
				for(int col = 0; col < charArray[0].length; col++) {
					char currChar = charArray[row][col][currHeight];
					System.out.print(currChar );
				}
				System.out.print(NameSpace.rowSeparator);
			}
			System.out.print(NameSpace.planeSeparator);
		}
	}
	
	/**
	 * Gets a perception vector that is as long as the number of percieved blocks, where each 
	 * int is just the character percieved
	 * @param charArray
	 * @param yLookDistance
	 * @param xLookDistance
	 * @param zLookDistance
	 * @param agentRotation
	 * @return perception vector
	 */
	public static int[] agentPerceptionToCompressedVector(char[][][] charArray, int yLookDistance, int xLookDistance, int zLookDistance, int agentRotation, int agentVertRotation) {
		char[][][] agentPerception = getAgentPerception(charArray, yLookDistance, xLookDistance, zLookDistance, agentRotation);
		int rows = agentPerception.length;
		int cols = agentPerception[0].length;
		int height = agentPerception[0][0].length;
		
		int[] toReturn = new int[rows*cols*height + 1];
		
		int index = 0;
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				for(int currHeight = 0; currHeight < height; currHeight++) {
					toReturn[index] = agentPerception[row][col][currHeight];
					index +=1;
				}
			}
		}
		
		//Add agent vert rotation
		toReturn[toReturn.length-1] = agentVertRotation;
		
		return toReturn;
	}
	
	/**
	 * Gets a list of all blocks that the agent can percieve
	 * @return said list
	 */
	private static List<Character> getAllBlockCharactersSorted() {
		List<Character> toReturn = new ArrayList<Character>();
		
		//ADD CHARACTERS HERE FOO
		//toReturn.add(NameSpace.CHARAGENT);
		//toReturn.add(NameSpace.CHARAGENTFEET);
		toReturn.add(NameSpace.CHARDIRTBLOCKPICKUPABLE);
		toReturn.add(NameSpace.CHARDIRTBLOCKNOTPICKUPABLE);
		toReturn.add(NameSpace.CHAROUTOFBOUNDS);
		toReturn.add(NameSpace.CHARGOAL);
		toReturn.add(NameSpace.CHARGOLDBLOCK);
		toReturn.add(NameSpace.CHARFURNACE);
		toReturn.add(NameSpace.CHARINDBLOCK);
		
		Collections.sort(toReturn);
		return toReturn;
	}
	
	/**
	 * Gets an array that is the number of blocks perceived*the number of types of blocks perceived blocks long
	 * where each perception value is a bit of whether a block is perceived
	 * @param charArray
	 * @param yLookDistance
	 * @param xLookDistance
	 * @param zLookDistance
	 * @param agentRotation
	 * @return
	 */
	public static int[] agentPerceptionToFactoredVector(char[][][] charArray, int yLookDistance, int xLookDistance, int zLookDistance, int agentRotation, int agentVertRotation) {
		int[] compressedPercVector = agentPerceptionToCompressedVector(charArray, yLookDistance, xLookDistance, zLookDistance, agentRotation, agentVertRotation);
		int numPercBlocks = compressedPercVector.length;
		
		List<Character> characterList = getAllBlockCharactersSorted();
		
		
		int[] toReturn = new int[characterList.size()*numPercBlocks+1];
		
		int charIndex = 0;
		for (Character currChar: characterList) {
			for (int percBlockIndex = 0; percBlockIndex < numPercBlocks; percBlockIndex++) {
				char currPercBlock = (char) compressedPercVector[percBlockIndex];
				if (currPercBlock == currChar) {
					toReturn[charIndex*numPercBlocks + percBlockIndex] = 1;
				}
			}
				charIndex += 1;
		}
		
		//Add vertical view
		toReturn[toReturn.length-1] = agentVertRotation;
		
		return toReturn;
	}
	
	/**
	 * 
	 * @param baseData
	 * @param sampleDownByX
	 * @return baseData with every sampleDownByX blocks added togeher
	 */
	public static int[] resamplePercData(int [] baseData, int sampleDownByX) {
		int [] toReturn = new int [baseData.length/sampleDownByX];
		
		for (int toReturnIndex = 0; toReturnIndex < toReturn.length; toReturnIndex ++) {
			int toReturnValue = 0;
			for (int offSetIndex = 0; offSetIndex < sampleDownByX; offSetIndex++) {
				toReturnValue += baseData[toReturnIndex*sampleDownByX + offSetIndex];
			}
			toReturn[toReturnIndex] = toReturnValue;
		}
		return toReturn;
	}
	
	/**
	 * 
	 * @param charArray
	 * @param yLookDistance
	 * @param xLookDistance
	 * @param zLookDistance
	 * @param agentRotation
	 * @param agentVertRotation
	 * @param sampleDownByX
	 * @return
	 */
	public static int[] agentPerceptionToFactoredResampledVector(char[][][] charArray, int yLookDistance, int xLookDistance, int zLookDistance, int agentRotation, int agentVertRotation, int sampleDownByX) {
		int [] baseData = agentPerceptionToFactoredVector(charArray, yLookDistance, xLookDistance, zLookDistance, agentRotation, agentVertRotation);
		return resamplePercData(baseData, sampleDownByX);
	}
	
	public static void main(String[] args) {
		MapIO currIO = new MapIO("src/minecraft/maps/goldTest.map");
		char[][][] array = currIO.getMapAs3DCharArray();
		printCharArray(array);
		char[][][] agentArray = getAgentPerception(array, 1, 1, 1, 0);
		printCharArray(agentArray);
		int[] agentPercVect = agentPerceptionToFactoredVector(array, 1, 1, 0, 0, 0);
		for (int i = 0; i < agentPercVect.length; i++) {
			System.out.print(agentPercVect[i] + " ");
		}
	}
}
