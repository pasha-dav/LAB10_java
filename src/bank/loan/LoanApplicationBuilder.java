package bank.loan;

/** Builder: окремий зрозумілий крок для кожної частини заявки. */
public interface LoanApplicationBuilder {
    // обов'язкові частини
    LoanApplicationBuilder withClient(String client);
    LoanApplicationBuilder withAmount(long amount);
    LoanApplicationBuilder withTerm(int months);

    // необов'язкові частини
    LoanApplicationBuilder withPurpose(String purpose);
    LoanApplicationBuilder withCollateral(String collateral);
    LoanApplicationBuilder withGuarantor(String guarantor);
    LoanApplicationBuilder withIncomeCertificate();
    LoanApplicationBuilder withInsurance();

    /** Завершальний крок: перевіряє обов'язкові частини. */
    LoanApplication build();
}
