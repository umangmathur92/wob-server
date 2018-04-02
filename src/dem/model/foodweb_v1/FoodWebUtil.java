package dem.model.foodweb_v1;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import dem.db.foodweb.ConsumeDAO;

/**
 * This is a set of utility functions for use by the Food Web.
 * This represents the food web as a graph by using standard
 * Adjacency Lists of nodes for the predators and prey.
 * Each [predator, prey] relation and each [prey, preditor] relation
 * is a directed edge in an adjacency list.
 * 
 * @author Cheryl Nielsen
 * for CSC 831 Multiplayer Game Development, San Francisco State University, Spring 2018
 */
public class FoodWebUtil {
	
	// Map of a species id with the list of the preditor ids that eat that species.
	// This is a list of directed edges in a graph.
	private HashMap<Integer, ArrayList<Integer>> foodWebPredators;
	// Map of a species id with the list of the prey ids that species will eat.
	// This is a list of directed edges in a graph.
	private HashMap<Integer, ArrayList<Integer>> foodWebPrey;

	public FoodWebUtil() 
	{
		foodWebPredators = new HashMap<Integer, ArrayList<Integer>>();
		foodWebPrey = new HashMap<Integer, ArrayList<Integer>>();
		buildWebAdjacencyLists();
	}
	

	public void buildWebAdjacencyLists() 
	{
		int predator = 0;
		int prey = 1;
		// List of the 1 to 1 Predator to Prey Relationships from the database.
		ArrayList<int[]> predatorPreyRelationships = new ArrayList<int[]>();
		// Key value pairs for making the Maps.
		int key;
		ArrayList<Integer> value = new ArrayList<Integer>();
		
		if (predatorPreyRelationships == null || predatorPreyRelationships.isEmpty())
		{			
			// Get the full list of [predator, prey] species pairs from the database.
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
	
	
	public ArrayList<Integer> getPredatorsOf(int species_id)
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
	
	
	public ArrayList<Integer> getListOfAllPredators()
	{
		Set<Integer> set = null;
		ArrayList<Integer> list = null;
		
		if (foodWebPredators != null && !foodWebPredators.isEmpty())
		{
			set = foodWebPredators.keySet();
		}
		
		list = new ArrayList<Integer>(set);		
		return list;
	}
	
	
	public ArrayList<Integer> getListOfAllPrey()
	{
		Set<Integer> set = null;
		ArrayList<Integer> list = null;
		
		if (foodWebPrey != null && !foodWebPrey.isEmpty())
		{
			set = foodWebPrey.keySet();
		}
		
		list = new ArrayList<Integer>(set);		
		return list;
	}
	

}
