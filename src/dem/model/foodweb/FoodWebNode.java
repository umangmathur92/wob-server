package dem.model.foodweb;

import java.util.ArrayList;

/**
 * This represents a node in the food web for a specific species.
 * 
 * @author Cheryl Nielsen
 * for CSC 831 Multiplayer Game Development, San Francisco State University, Spring 2018
 */
public class FoodWebNode 
{
	
	Species species;
	// nodeID is the species_id
	int nodeID;
	// The list of species that this species can eat.
	// These are the nodeIDs that this node points to. (The child nodes)
	ArrayList<Integer> preyNodes;
	// The list of species that can eat this species.
	// These are the nodeIDs that point to this node. (The parent nodes)
	ArrayList<Integer> predatorNodes;
	
	
	public FoodWebNode() 
	{
	}

	
	public FoodWebNode(Species species, ArrayList<Integer> prey, ArrayList<Integer> predators)
	{
		this.species = species;
		this.nodeID = species.species_id;
		this.preyNodes = prey;
		this.predatorNodes = predators;
	}
	
	
	public int getNodeID() {
		return nodeID;
	}

	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

	public Species getSpecies() {
		return species;
	}

	public void setSpecies(Species species) {
		this.species = species;
	}

	public ArrayList<Integer> getPreyNodes() {
		return preyNodes;
	}

	public void setPreyNodes(ArrayList<Integer> prey) {
		this.preyNodes = prey;
	}

	public ArrayList<Integer> getPredatorNodes() {
		return predatorNodes;
	}

	public void setPredatorNodes(ArrayList<Integer> predators) {
		this.predatorNodes = predators;
	}
	
}
