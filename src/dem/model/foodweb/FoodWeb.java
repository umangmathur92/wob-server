package dem.model.foodweb;


import java.util.ArrayList;
import java.util.HashMap;

import dem.db.foodweb.ConsumeDAO;
import dem.db.foodweb.SpeciesDAO;

/**
 * This represents the food web of all species as a standard graph by using 
 * Adjacency Lists for the predators and prey.
 * 
 * 
 * @author Cheryl Nielsen
 * for CSC 831 Multiplayer Game Development, San Francisco State University, Spring 2018
 */
public class FoodWeb {
	
	// Map of a species id with the list of the preditor ids that eat that species
	private HashMap<Integer, ArrayList<Integer>> foodWebPredators;
	// Map of a species id with the list of the prey ids that species will eat
	private HashMap<Integer, ArrayList<Integer>> foodWebPrey;
	// List of the 1 to 1 Predator to Prey Relationships from the database.
	private ArrayList<int[]> predatorPreyRelationships;
	

	public FoodWeb() 
	{
		foodWebPredators = new HashMap<Integer, ArrayList<Integer>>();
		foodWebPrey = new HashMap<Integer, ArrayList<Integer>>();
		predatorPreyRelationships = null;
	}

	
	public void buildWeb() 
	{
		if (predatorPreyRelationships == null || predatorPreyRelationships.isEmpty())
		{
			predatorPreyRelationships = new ArrayList<int[]>();
			predatorPreyRelationships = ConsumeDAO.getPredatorPreyRelationships();
		}
		
		
	}
	
	

}
