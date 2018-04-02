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
	// node ID is the species_id
	private int species_id;
	// The list of species that this species can eat.
	// These are the nodes that this node points to. (The child nodes)
	private ArrayList<Integer> preyNodes;
	// The list of species that can eat this species.
	// These are the nodes that point to this node. (The parent nodes)
	private ArrayList<Integer> predatorNodes;
	
	
	public FoodWebNode() 
	{
	}

	
	public FoodWebNode(int species_id, ArrayList<Integer> prey, ArrayList<Integer> predators)
	{
		this.species_id = species_id;
		this.preyNodes = prey;
		this.predatorNodes = predators;
	}
	
	
	public int getSpecies_id() {
		return species_id;
	}

	public void setSpecies_id(int species_id) {
		this.species_id = species_id;
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
