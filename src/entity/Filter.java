package entity;

import enums.*;
import java.util.ArrayList;

/**
 * Represents search criteria for filtering BTO projects.
 * Allows users to specify preferences like project name, neighborhood,
 * price range, and flat types to find matching housing options.
 */
public class Filter {

	/**
	 * Instance variables for storing filter criteria
	 */
	private String projectName;
	private ArrayList<String> neighborhoodList;
	private double minPrice;
	private double maxPrice;
	private ArrayList<FlatTypeEnum> flatTypes;

	/**
	 * Constructors
	 */
	public Filter() {
		this.projectName = "";
		this.neighborhoodList = new ArrayList<>();
		this.minPrice = 0.0;
		this.maxPrice = Double.MAX_VALUE;
		this.flatTypes = new ArrayList<>();
	}

	/**
	 * Parameterized constructor to create a fully specified filter.
	 * 
	 * @param projectName The name of the project to filter by
	 * @param neighborhoodList List of neighborhoods to include in search results
	 * @param minPrice Minimum price threshold for flats
	 * @param maxPrice Maximum price ceiling for flats
	 * @param flatTypes List of flat types to include in search
	 */
	public Filter(String projectName, ArrayList<String> neighborhoodList, double minPrice, double maxPrice, ArrayList<FlatTypeEnum> flatTypes) {
		this.projectName = projectName;
		this.neighborhoodList = neighborhoodList != null ? neighborhoodList : new ArrayList<>();
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.flatTypes = flatTypes != null ? flatTypes : new ArrayList<>();
	}

	/**
	 * Setter methods for individual filter criteria
	 */
	
	/**
	 * Sets the project name filter.
	 * 
	 * @param projectName Name of the project to search for
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * Sets the list of neighborhoods to filter by.
	 * 
	 * @param neighborhoodList List of neighborhood names to include
	 */
	public void setNeighborhoodList(ArrayList<String> neighborhoodList) {
		this.neighborhoodList = neighborhoodList;
	}

	/**
	 * Sets the minimum price threshold.
	 * 
	 * @param minPrice Lowest acceptable price in dollars
	 */
	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}

	/**
	 * Sets the maximum price ceiling.
	 * 
	 * @param maxPrice Highest acceptable price in dollars
	 */
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}

	/**
	 * Sets the list of flat types to filter by.
	 * 
	 * @param flatTypes List of flat types to include in search
	 */
	public void setFlatTypes(ArrayList<FlatTypeEnum> flatTypes) {
		this.flatTypes = flatTypes;
	}

	/**
	 * Getter methods for accessing filter criteria
	 */
	
	/**
	 * Gets the project name filter.
	 * 
	 * @return The project name to search for
	 */
	public String getProjectName() {
		return this.projectName;
	}

	/**
	 * Gets the list of neighborhoods in the filter.
	 * 
	 * @return List of neighborhood names to include in search
	 */
	public ArrayList<String> getNeighborhoodList() {
		return this.neighborhoodList;
	}

	/**
	 * Gets the minimum price threshold.
	 * 
	 * @return Lowest acceptable price in dollars
	 */
	public double getMinPrice() {
		return this.minPrice;
	}

	public double getMaxPrice() {
		return this.maxPrice;
	}

	/**
	 * Gets the list of flat types in the filter.
	 * 
	 * @return List of flat types to include in search
	 */
	public ArrayList<FlatTypeEnum> getFlatTypes() {
		return this.flatTypes;
	}

	/**
	 * Instance Methods
	 */
	/**
	 * Sets both minimum and maximum price in a single operation.
	 * Only updates if the min price is less than or equal to max price.
	 * 
	 * @param minPrice Minimum price threshold in dollars
	 * @param maxPrice Maximum price ceiling in dollars
	 */
	public void setPriceRange(double minPrice, double maxPrice) {
		if (minPrice <= maxPrice) {
			this.minPrice = minPrice;
			this.maxPrice = maxPrice;
		}
	}

	/**
	 * Adds a single flat type to the filter if not already present.
	 * 
	 * @param flatType The flat type to add to the filter criteria
	 */
	public void addFlatType(FlatTypeEnum flatType) {
		if (flatType != null && !this.flatTypes.contains(flatType)) {
			this.flatTypes.add(flatType);
		}
	}

	/**
	 * Adds a single neighborhood to the filter if not already present.
	 * 
	 * @param neighborhood The neighborhood name to add to the filter criteria
	 */
	public void addNeighborhood(String neighborhood) {
		if (neighborhood != null && !this.neighborhoodList.contains(neighborhood)) {
			this.neighborhoodList.add(neighborhood);
		}
	}

	/**
	 * Checks if a project matches this filter's criteria.
	 * 
	 * @param project The project to check against filter criteria
	 * @return true if the project matches all filter criteria, false otherwise
	 */
	public boolean matchesProject(Project project) {
		if (project == null) {
			return false;
		}
		
		// Check project name if specified
		if (projectName != null && !projectName.isEmpty() && 
			!projectName.equals(project.getProjectName())) {
			return false;
		}
		
		// Check neighborhood if specified
		if (neighborhoodList != null && !neighborhoodList.isEmpty() && 
			!neighborhoodList.contains(project.getNeighborhood())) {
			return false;
		}
		
		// Check if any flat type in the project matches price range and type filters
		boolean matchesFlat = false;
		for (FlatType flatType : (ArrayList<FlatType>) project.getFlatTypes()) {
			// Check price range
			double price = flatType.getSellingPrice();
			boolean priceMatches = price >= minPrice && price <= maxPrice;
			
			// Check flat type if specified
			boolean typeMatches = flatTypes == null || flatTypes.isEmpty() || 
								  flatTypes.contains(flatType.getType());
			
			if (priceMatches && typeMatches) {
				matchesFlat = true;
				break;
			}
		}
		
		return matchesFlat;
	}

	/**
	 * Helper Functions
	 */
	@Override()
	public String toString() {
		return "Filter [projectName=" + projectName + ", neighborhoods=" + neighborhoodList.size() + ", priceRange=" + minPrice + "-" + maxPrice + ", flatTypes=" + flatTypes.size() + "]";
	}
}