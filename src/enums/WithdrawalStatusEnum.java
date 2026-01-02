package enums;

/**
 * Represents the status of a withdrawal request for a housing application.
 * Tracks the progression of withdrawal requests through the approval process.
 */
public enum WithdrawalStatusEnum {
	/** No withdrawal requested or applicable */
	NA,
	/** Withdrawal request has been submitted and awaiting review */
	PENDING,
	/** Withdrawal request has been reviewed and approved */
	APPROVED,
	/** Withdrawal request has been reviewed and denied */
	REJECTED
}