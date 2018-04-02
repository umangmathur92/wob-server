package dem.model.foodweb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 * This represents the food web of all species.
 * It builds a full web, and can then return a partial web.
 * The web may result in a set of graphs if the set of species included
 * does not overlap for predator and prey relationships.
 * 
 * @author Cheryl Nielsen
 * for CSC 831 Multiplayer Game Development, San Francisco State University, Spring 2018
 */
public class FoodWeb 
{
	private HashMap<Integer, FoodWebNode> foodWeb;
	
	public FoodWeb()
	{
		foodWeb = new HashMap<Integer, FoodWebNode>();
	}
	

	public void buildFullFoodWeb(FoodWebUtil foodWebUtil)
	{
		ArrayList<Integer> prey = new ArrayList<Integer>();
		ArrayList<Integer> predators = new ArrayList<Integer>();
		
		foodWebUtil.buildWebAdjacencyLists();
		ArrayList<Integer> predatorSet = foodWebUtil.getListOfAllPredators();
		ArrayList<Integer> preySet = foodWebUtil.getListOfAllPrey();
		
		for (int species_id : predatorSet)
		{
			prey = foodWebUtil.getPreyOf(species_id);
			predators = foodWebUtil.getPredatorsOf(species_id);
			FoodWebNode node = new FoodWebNode(species_id, prey, predators);
			foodWeb.put(species_id, node);
		}

		for (int species_id : preySet)
		{	
			// Many of the prey will have already been added from the predator set.
			if (!foodWeb.containsKey(species_id))
			{
				prey = foodWebUtil.getPreyOf(species_id);
				predators = foodWebUtil.getPredatorsOf(species_id);
				FoodWebNode node = new FoodWebNode(species_id, prey, predators);
				foodWeb.put(species_id, node);
			}
		}
	}
	
	
	public FoodWeb getPartialFoodWeb(int rootSpecies, int maxTreeDepth, int maxNumSpecies)
	{
		// TODO
		return null;
	}
	

	public FoodWeb getPartialFoodWeb(int[] species)
	{
		// TODO
		return null;
	}
	
	
	public FoodWeb merge(FoodWeb web1, FoodWeb web2)
	{
		// TODO
		return null;
	}
	
	
	public FoodWeb deletNode(FoodWeb web, int species_id)
	{
		// TODO
		return null;
	}
	
	
	public FoodWebNode getNode(int species_id)
	{
		FoodWebNode node = new FoodWebNode();
		node = foodWeb.get(species_id);
		return node;
	}
	
	public void setNode(FoodWebNode node)
	{
		int id = node.species_id;
		foodWeb.put(id, node);
	}
	
	
	// The set of nodes with no predators.
	public ArrayList<Integer> getRootNodeIDs()
	{
		ArrayList<Integer> roots = new ArrayList<Integer>();
		FoodWebNode node = new FoodWebNode();
		
		for (int species_id : foodWeb.keySet())
		{
			node = foodWeb.get(species_id);
			if (node.getPredatorNodes() == null || node.getPredatorNodes().isEmpty())
			{
				roots.add(species_id);
			}
		}
		
		return roots;
	}
	
	
	// The set of nodes with no prey.
	public ArrayList<Integer> getLeafNodeIDs()
	{
		ArrayList<Integer> leaves = new ArrayList<Integer>();
		FoodWebNode node = new FoodWebNode();
		
		for (int species_id : foodWeb.keySet())
		{
			node = foodWeb.get(species_id);
			if (node.getPreyNodes() == null || node.getPreyNodes().isEmpty())
			{
				leaves.add(species_id);
			}
		}
		
		return leaves;
	}
	
	
	// Get the list of prey shared by 2 species.
	public ArrayList<Integer> getCommonPrey(int species1, int species2)
	{
		// TODO
		return null;
	}
	
	
	// Get the list of prey shared by 2 species.
	public ArrayList<Integer> getCommonPredators(int species1, int species2)
	{
		// TODO
		return null;
	}
		
	
	// Dose a path exist between 2 species on the graph.
	public boolean isConnected(int species1, int species2)
	{
		// TODO
		return true;
	}
	
	
}




