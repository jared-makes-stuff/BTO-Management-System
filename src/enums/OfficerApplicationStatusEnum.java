package enums;

/**
 * Represents the status of an officer's application to be assigned to a project.
 * Tracks the approval process for officer assignment requests.
 */
public enum OfficerApplicationStatusEnum {
	/** Application submitted and awaiting manager review */
	PENDING,
	/** Application has been reviewed and approved by the manager */
	APPROVED,
	/** Application has been reviewed and rejected by the manager */
	REJECTED
}