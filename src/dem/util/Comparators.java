package dem.util;

// Java Imports
import java.util.Comparator;

// Other Imports
import dem.model.Organism;
import dem.model.SpeciesType;

/**
 * The Comparators class provides comparators to sort different lists.
 */
public class Comparators {

	public static Comparator<SpeciesType> SpeciesNameComparator = new Comparator<SpeciesType>() {
		@Override
		public int compare(SpeciesType o1, SpeciesType o2) {
			return o1.getName().compareToIgnoreCase(o2.getName());
		}
	};
}
