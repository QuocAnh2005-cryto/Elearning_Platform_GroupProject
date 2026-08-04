package vn.edu.fpt.dto.transaction_manager;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionCountByStatusDTO {
    private int numberOfTransactionSuccess;
    private int numberOfTransactionPending;
    private int numberOfTransactionFailed;
    private int numberOfTransactionCanceled;
    private int numberOfTransactionExpired;

    public TransactionCountByStatusDTO(int numberOfTransactionSuccess, int numberOfTransactionPending, int numberOfTransactionFailed, int numberOfTransactionCanceled, int numberOfTransactionExpired) {
        this.numberOfTransactionSuccess = numberOfTransactionSuccess;
        this.numberOfTransactionPending = numberOfTransactionPending;
        this.numberOfTransactionFailed = numberOfTransactionFailed;
        this.numberOfTransactionCanceled = numberOfTransactionCanceled;
        this.numberOfTransactionExpired = numberOfTransactionExpired;
    }

    public int getAllTransaction() {
        return numberOfTransactionSuccess + numberOfTransactionPending + numberOfTransactionFailed + numberOfTransactionCanceled + numberOfTransactionExpired;
    }
}
