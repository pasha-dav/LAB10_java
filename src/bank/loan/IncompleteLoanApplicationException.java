package bank.loan;

public class IncompleteLoanApplicationException extends IllegalStateException {
    public IncompleteLoanApplicationException(String message) {
        super(message);
    }
}
