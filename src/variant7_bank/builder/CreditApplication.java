package variant7_bank.builder;

public class CreditApplication {
    private final String clientName;
    private final double amount;
    private final int termMonths;
    private final String purpose;
    private final String collateral;
    private final String guarantor;
    private final boolean incomeStatementProvided;
    private final boolean insuranceIncluded;

    CreditApplication(CreditApplicationBuilder builder) {
        this.clientName = builder.clientName;
        this.amount = builder.amount;
        this.termMonths = builder.termMonths;
        this.purpose = builder.purpose;
        this.collateral = builder.collateral;
        this.guarantor = builder.guarantor;
        this.incomeStatementProvided = builder.incomeStatementProvided;
        this.insuranceIncluded = builder.insuranceIncluded;
    }

    public String getClientName() { return clientName; }
    public double getAmount() { return amount; }
    public int getTermMonths() { return termMonths; }
    public String getPurpose() { return purpose; }
    public String getCollateral() { return collateral; }
    public String getGuarantor() { return guarantor; }
    public boolean isIncomeStatementProvided() { return incomeStatementProvided; }
    public boolean isInsuranceIncluded() { return insuranceIncluded; }

    @Override
    public String toString() {
        return "Кредитна заявка [" +
                "Клієнт: '" + clientName + '\'' +
                ", Сума: " + amount + " грн" +
                ", Строк: " + termMonths + " міс." +
                ", Мета: '" + purpose + '\'' +
                ", Застава: " + (collateral != null ? collateral : "відсутня") +
                ", Поручитель: " + (guarantor != null ? guarantor : "відсутній") +
                ", Довідка про доходи: " + (incomeStatementProvided ? "так" : "ні") +
                ", Страхування: " + (insuranceIncluded ? "так" : "ні") +
                "]";
    }
}