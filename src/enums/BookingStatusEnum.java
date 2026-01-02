package enums;

/**
 * Represents the status of a flat booking in the BTO system.
 * Tracks whether a booking is in process, finalized, or cancelled.
 */
public enum BookingStatusEnum {
	/** Booking has been initiated but not yet finalized */
	PENDING,
	/** Booking has been confirmed and payment processed */
	CONFIRMED,
	/** Booking has been cancelled by the applicant or system */
	CANCELLED
}