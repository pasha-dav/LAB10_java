package variant7_bank.builder;

public class CreditApplicationBuilder {
    String clientName;
    double amount;
    int termMonths;
    String purpose;
    String collateral;
    String guarantor;
    boolean incomeStatementProvided;
    boolean insuranceIncluded;

    public CreditApplicationBuilder setClient(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public CreditApplicationBuilder setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public CreditApplicationBuilder setTermMonths(int termMonths) {
        this.termMonths = termMonths;
        return this;
    }

    public CreditApplicationBuilder setPurpose(String purpose) {
        this.purpose = purpose;
        return this;
    }

    public CreditApplicationBuilder setCollateral(String collateral) {
        this.collateral = collateral;
        return this;
    }

    public CreditApplicationBuilder setGuarantor(String guarantor) {
        this.guarantor = guarantor;
        return this;
    }

    public CreditApplicationBuilder setIncomeStatement(boolean provided) {
        this.incomeStatementProvided = provided;
        return this;
    }

    public CreditApplicationBuilder setInsurance(boolean included) {
        this.insuranceIncluded = included;
        return this;
    }

    public CreditApplication build() {
        if (clientName == null || clientName.isBlank()) {
            throw new IllegalArgumentException("Неможливо сформувати заявку: не вказано клієнта.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Неможливо сформувати заявку: не вказано суму кредиту.");
        }
        if (termMonths <= 0) {
            throw new IllegalArgumentException("Неможливо сформувати заявку: не вказано строк кредиту.");
        }
        if (purpose == null || purpose.isBlank()) {
            throw new IllegalArgumentException("Неможливо сформувати заявку: не вказано мету кредиту.");
        }
        return new CreditApplication(this);
    }
}