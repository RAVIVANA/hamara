package customEception;

public class LoanApplicationSaveException extends Exception {
    public LoanApplicationSaveException() {
        super();
    }

    public LoanApplicationSaveException(String message) {
        super(message);
    }

    public LoanApplicationSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoanApplicationSaveException(Throwable cause) {
        super(cause);
    }

    // Add any additional constructors or customizations if needed
}
