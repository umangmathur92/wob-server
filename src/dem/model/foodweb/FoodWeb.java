package dem.model.foodweb;


import java.util.ArrayList;
import java.util.HashMap;

import dem.db.foodweb.ConsumeDAO;

/**
 * This represents the food web of all species as a standard graph by using 
 * Adjacency Lists of nodes for the predators and prey.
 * Each [predator, prey] relation and each [prey, preditor] relation
 * is a directed edge in an adjacency list.
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
		predatorPreyRelationships = new ArrayList<int[]>();
	}

	
	public void buildTheWeb()
	{
		buildWebAdjacencyLists();
		buildWebNodes();
	}
	
	
	private void buildWebNodes() 
	{
		// TODO Auto-generated method stub
		
	}


	private void buildWebAdjacencyLists() 
	{
		int predator = 0;
		int prey = 1;
		int key;
		ArrayList<Integer> value = new ArrayList<Integer>();
		
		if (predatorPreyRelationships == null || predatorPreyRelationships.isEmpty())
		{
			predatorPreyRelationships = new ArrayList<int[]>();
			// Returns the full list of [predator, prey] species pairs from the database.
			predatorPreyRelationships = ConsumeDAO.getPredatorPreyRelationships();
		}
		
		for (int[] relation : predatorPreyRelationships)
		{
			key = relation[predator];
			
			if(foodWebPrey.containsKey(key))
			{
				value  = foodWebPrey.get(key); 
				value.add(relation[prey]);
				foodWebPrey.put(key, value);
			}
			else
			{
				value = new ArrayList<Integer>();
				value.add(relation[prey]);
				foodWebPrey.put(key, value);
			}
			
			key = relation[prey];
			
			if(foodWebPredators.containsKey(key))
			{
				value  = foodWebPredators.get(key); 
				value.add(relation[predator]);
				foodWebPredators.put(key, value);
			}
			else
			{
				value = new ArrayList<Integer>();
				value.add(relation[predator]);
				foodWebPredators.put(key, value);
			}
		}
		
	}
	
	
	public ArrayList<Integer> getPreditorsOf(int species_id)
	{
		ArrayList<Integer> list = null;
		
		if (foodWebPredators != null && !foodWebPredators.isEmpty())
		{
			list = new ArrayList<Integer>();
			list = foodWebPredators.get(species_id);
		}
		
		return list;
	}
	
	
	public ArrayList<Integer> getPreyOf(int species_id)
	{
		ArrayList<Integer> list = null;
		
		if (foodWebPrey != null && !foodWebPrey.isEmpty())
		{
			list = new ArrayList<Integer>();
			list = foodWebPrey.get(species_id);
		}
		
		return list;
	}
	
	

}
