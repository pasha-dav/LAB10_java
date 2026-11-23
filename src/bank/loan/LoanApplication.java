package bank.loan;

/** Product. Незмінна кредитна заявка; створюється лише через білдер. */
public final class LoanApplication {
    private final String client;
    private final long amount;
    private final int termMonths;
    private final String purpose;
    private final String collateral;
    private final String guarantor;
    private final boolean incomeCertificate;
    private final boolean insurance;

    LoanApplication(StandardLoanApplicationBuilder b) {
        this.client = b.client;
        this.amount = b.amount;
        this.termMonths = b.termMonths;
        this.purpose = b.purpose;
        this.collateral = b.collateral;
        this.guarantor = b.guarantor;
        this.incomeCertificate = b.incomeCertificate;
        this.insurance = b.insurance;
    }

    public String getClient() { return client; }
    public long getAmount() { return amount; }
    public int getTermMonths() { return termMonths; }
    public String getPurpose() { return purpose; }
    public String getCollateral() { return collateral; }
    public String getGuarantor() { return guarantor; }
    public boolean hasIncomeCertificate() { return incomeCertificate; }
    public boolean hasInsurance() { return insurance; }

    @Override
    public String toString() {
        return "Кредитна заявка {\n"
                + "  клієнт:            " + client + "\n"
                + "  сума:              " + String.format("%,d", amount).replace(',', ' ') + " грн\n"
                + "  строк:             " + termMonths + " міс.\n"
                + "  мета:              " + (purpose == null ? "—" : purpose) + "\n"
                + "  застава:           " + (collateral == null ? "—" : collateral) + "\n"
                + "  поручитель:        " + (guarantor == null ? "—" : guarantor) + "\n"
                + "  довідка про доходи: " + (incomeCertificate ? "так" : "—") + "\n"
                + "  страхування:       " + (insurance ? "так" : "—") + "\n"
                + "}";
    }
}
