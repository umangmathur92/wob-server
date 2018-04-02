package dem.model.foodweb_v1;

/**
 * This represents a single animal or plant in the food web.
 * 
 * @author Cheryl Nielsen
 * for CSC 831 Multiplayer Game Development, San Francisco State University, Spring 2018
 */
public class Species 
{
    protected int species_id;
    protected String name;
    protected String description;
	protected int model_id;
    protected int biomassCost;
    protected int health;

    public Species() {
    }

    public Species(int species_id) {
        this.species_id = species_id;
    }


    public int getSpecies_id() {
		return species_id;
	}

	public void setSpecies_id(int species_id) {
		this.species_id = species_id;
	}

	public String getName() {
        return name;
    }

    public String setName(String name) {
        return this.name = name;
    }

    public int getBiomassCost() {
        return biomassCost;
    }

    public int setBiomassCost(int cost) {
        return this.biomassCost = cost;
    }

    public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
    public String getDescription() {
        return description;
    }

    public String setDescription(String description) {
        return this.description = description;
    }

    public int getModel_id() {
        return model_id;
    }

    public int setModel_id(int model_id) {
        return this.model_id = model_id;
    }

 
}
