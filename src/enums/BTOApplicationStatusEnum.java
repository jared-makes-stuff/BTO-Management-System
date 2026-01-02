package enums;

/**
 * Represents the different statuses a BTO application can have during its lifecycle.
 * Tracks the application's progress from submission to final resolution.
 */
public enum BTOApplicationStatusEnum {
	/** Application submitted and awaiting selection results */
	PENDING,
	/** Application was selected in the balloting process */
	SUCCESSFUL,
	/** Application was not selected in the balloting process */
	UNSUCCESSFUL,
	/** Applicant has selected and booked a specific flat */
	BOOKED,
	/** Applicant has withdrawn their application */
	WITHDRAWN
}