package dem.model.foodweb_v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
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
	

	// A food web with all possible species included.
	public void buildFullFoodWeb(FoodWebUtil foodWebUtil)
	{
		ArrayList<Integer> prey = new ArrayList<Integer>();
		ArrayList<Integer> predators = new ArrayList<Integer>();
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
	
	
	// For use in producing a simplified food web when needed.
	// This assumes that the selected species node has been pruned from the 
	// food web or was never added to the food web.
	// Warning, this cannot be undone without reseting all the preditor and prey
	// listings in the foodweb from the full listings in FoodWebUtil.
	public void prunePreditorPreyLists(int species_id)
	{
		ArrayList<Integer> prey = new ArrayList<Integer>();
		ArrayList<Integer> predators = new ArrayList<Integer>();
		
		for (Entry<Integer, FoodWebNode> node : foodWeb.entrySet())
		{
			prey = node.getValue().getPreyNodes();
			predators = node.getValue().getPredatorNodes();
						
			if(prey.contains(species_id))
			{
				prey.remove(Integer.valueOf(species_id));
			}
			
			if(predators.contains(species_id))
			{
				predators.remove(Integer.valueOf(species_id));
			}
			
			node.getValue().setPreyNodes(prey);
			node.getValue().setPredatorNodes(predators);
		}
	}
	
	
	// This resets all the preditor and prey listings in the 
	// foodweb to their original values from the listings in FoodWebUtil.
	public void resetPreditorPreyLists(FoodWebUtil foodWebUtil)
	{
		ArrayList<Integer> prey = new ArrayList<Integer>();
		ArrayList<Integer> predators = new ArrayList<Integer>();
		int species_id;
		
		for (Entry<Integer, FoodWebNode> node : foodWeb.entrySet())
		{
			species_id = node.getKey();
			prey = foodWebUtil.getPreyOf(species_id);
			predators = foodWebUtil.getPredatorsOf(species_id);
			node.getValue().setPreyNodes(prey);
			node.getValue().setPredatorNodes(predators);
		}

	}
	
	
	// This deletes the node, but it does not remove edges pointing to the node.
	// So other nodes may still list that species_id as a prey or predator.
	public void deletNode(int species_id)
	{
		if(foodWeb.containsKey(species_id))
		{
			foodWeb.remove(species_id);
		}
	}
	
	
	// This adds the node, but does not update edges to or from the node.
	// So other nodes may not list that species_id as a prey or predator.
	public void addNode(FoodWebNode node)
	{
		int species_id = node.getSpecies_id();
		foodWeb.put(species_id, node);
	}
	
	
	public FoodWebNode getNode(int species_id)
	{
		FoodWebNode node = new FoodWebNode();
		node = foodWeb.get(species_id);
		return node;
	}
	
	
	// Get the list of prey shared by 2 species.
	public ArrayList<Integer> getCommonPrey(int species1, int species2)
	{
		ArrayList<Integer> node1Prey = foodWeb.get(species1).getPreyNodes();
		ArrayList<Integer> node2Prey = foodWeb.get(species2).getPreyNodes();
		ArrayList<Integer> list = new ArrayList<Integer>();
				
		for (int species_id : node1Prey)
		{
			if (node2Prey.contains(species_id))
			{
				list.add(species_id);
			}
		}
		
		return list;
	}
	
	
	// Get the list of prey shared by 2 species.
	public ArrayList<Integer> getCommonPredators(int species1, int species2)
	{
		ArrayList<Integer> node1Predator = foodWeb.get(species1).getPredatorNodes();
		ArrayList<Integer> node2Predator = foodWeb.get(species2).getPredatorNodes();
		ArrayList<Integer> list = new ArrayList<Integer>();
				
		for (int species_id : node1Predator)
		{
			if (node2Predator.contains(species_id))
			{
				list.add(species_id);
			}
		}
		
		return list;
	}
	
	
	
	// The set of nodes with no predators.
	public ArrayList<Integer> getRootNodeIDs()
	{
		ArrayList<Integer> roots = new ArrayList<Integer>();
		
		for (Entry<Integer, FoodWebNode> node : foodWeb.entrySet())
		{
			if (node.getValue().getPredatorNodes() == null || node.getValue().getPredatorNodes().isEmpty())
			{
				roots.add(node.getKey());
			}
		}
		
		return roots;
	}
	
	
	// The set of nodes with no prey.
	public ArrayList<Integer> getLeafNodeIDs()
	{
		ArrayList<Integer> leaves = new ArrayList<Integer>();
		
		for (Entry<Integer, FoodWebNode> node : foodWeb.entrySet())
		{
			if (node.getValue().getPreyNodes() == null || node.getValue().getPreyNodes().isEmpty())
			{
				leaves.add(node.getKey());
			}
		}
		
		return leaves;
	}

	
	
}




