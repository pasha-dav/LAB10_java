package bank.loan;

import bank.common.Log;

/** ConcreteBuilder. */
public class StandardLoanApplicationBuilder implements LoanApplicationBuilder {
    private static final String SRC = "Builder";

    String client;
    long amount;
    int termMonths;
    String purpose;
    String collateral;
    String guarantor;
    boolean incomeCertificate;
    boolean insurance;

    @Override
    public LoanApplicationBuilder withClient(String client) {
        this.client = client;
        Log.log(SRC, "Задано клієнта: " + client);
        return this;
    }

    @Override
    public LoanApplicationBuilder withAmount(long amount) {
        this.amount = amount;
        Log.log(SRC, "Задано суму: " + amount + " грн");
        return this;
    }

    @Override
    public LoanApplicationBuilder withTerm(int months) {
        this.termMonths = months;
        Log.log(SRC, "Задано строк: " + months + " міс.");
        return this;
    }

    @Override
    public LoanApplicationBuilder withPurpose(String purpose) {
        this.purpose = purpose;
        Log.log(SRC, "Задано мету: " + purpose);
        return this;
    }

    @Override
    public LoanApplicationBuilder withCollateral(String collateral) {
        this.collateral = collateral;
        Log.log(SRC, "Додано заставу: " + collateral);
        return this;
    }

    @Override
    public LoanApplicationBuilder withGuarantor(String guarantor) {
        this.guarantor = guarantor;
        Log.log(SRC, "Додано поручителя: " + guarantor);
        return this;
    }

    @Override
    public LoanApplicationBuilder withIncomeCertificate() {
        this.incomeCertificate = true;
        Log.log(SRC, "Додано довідку про доходи");
        return this;
    }

    @Override
    public LoanApplicationBuilder withInsurance() {
        this.insurance = true;
        Log.log(SRC, "Додано страхування");
        return this;
    }

    @Override
    public LoanApplication build() {
        if (client == null || client.isBlank()) {
            fail("не вказано клієнта");
        }
        if (amount <= 0) {
            fail("не вказано суму кредиту");
        }
        if (termMonths <= 0) {
            fail("не вказано строк кредиту");
        }
        LoanApplication application = new LoanApplication(this);
        Log.log(SRC, "Заявку сформовано: " + client + ", " + amount + " грн, " + termMonths + " міс.");
        return application;
    }

    private void fail(String reason) {
        String message = "Неможливо сформувати заявку: " + reason;
        Log.log(SRC, message);
        throw new IncompleteLoanApplicationException(message);
    }
}
