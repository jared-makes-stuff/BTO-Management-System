package enums;

/**
 * Represents the status of an enquiry in the housing application system.
 * Tracks whether an enquiry has been addressed by staff members.
 */
public enum EnquiryStatusEnum {
	/** Enquiry has been submitted but not yet answered by staff */
	PENDING,
	/** Enquiry has been reviewed and answered by a staff member */
	REPLIED
}