package entity;

import enums.*;
import java.util.ArrayList;

/**
 * The BookingFilter class provides a filtering mechanism for booking searches.
 * It allows users to specify various criteria to filter booking results,
 * including flat type, project name, applicant age, and marital status.
 * This class is used to narrow down booking searches based on user preferences.
 */
public class BookingFilter {

	/**
	 * List of flat types to filter by
	 */
	private ArrayList<FlatTypeEnum> flatType;
	
	/**
	 * Project name to filter by
	 */
	private String projectName;
	
	/**
	 * List of applicant ages to filter by
	 */
	private ArrayList<Integer> age;
	
	/**
	 * List of marital statuses to filter by
	 */
	private ArrayList<MarriageStatusEnum> maritalStatus;

	/**
	 * Default constructor that initializes a BookingFilter with null values.
	 * Filter criteria must be set individually using setter methods.
	 */
	public BookingFilter() {
	}

	/**
	 * Sets the flat types to filter by.
	 * 
	 * @param flatType List of flat types to include in the filter
	 */
	public void setFlatType(ArrayList<FlatTypeEnum> flatType) {
		this.flatType = flatType;
	}

	/**
	 * Sets the project name to filter by.
	 * 
	 * @param projectName The project name to filter by
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * Sets the applicant ages to filter by.
	 * 
	 * @param age List of ages to include in the filter
	 */
	public void setAge(ArrayList<Integer> age) {
		this.age = age;
	}

	/**
	 * Sets the marital statuses to filter by.
	 * 
	 * @param maritalStatus List of marital statuses to include in the filter
	 */
	public void setMaritalStatus(ArrayList<MarriageStatusEnum> maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
	 * Gets the flat types being filtered for.
	 * 
	 * @return List of flat types in the filter
	 */
	public ArrayList<FlatTypeEnum> getFlatType() {
		return this.flatType;
	}

	/**
	 * Gets the project name being filtered for.
	 * 
	 * @return The project name in the filter
	 */
	public String getProjectName() {
		return this.projectName;
	}

	/**
	 * Gets the applicant ages being filtered for.
	 * 
	 * @return List of ages in the filter
	 */
	public ArrayList<Integer> getAge() {
		return this.age;
	}

	/**
	 * Gets the marital statuses being filtered for.
	 * 
	 * @return List of marital statuses in the filter
	 */
	public ArrayList<MarriageStatusEnum> getMaritalStatus() {
		return this.maritalStatus;
	}

}