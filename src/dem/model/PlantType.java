package dem.model;

// Other Imports
import shared.metadata.Constants;


public class PlantType extends SpeciesType {

    public PlantType(int species_id) {
        organism_type = Constants.ORGANISM_TYPE_PLANT;
        this.species_id = species_id;
    }
}
