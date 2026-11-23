package bank.loan;

import bank.common.Log;

/** Director: типові заявки формуються однаковою послідовністю кроків, описаною в одному місці. */
public class LoanApplicationDirector {
    private static final String SRC = "Director";

    public LoanApplication buildAutoLoan(LoanApplicationBuilder builder, String client, long amount) {
        Log.log(SRC, "Типова заявка «Автокредит»");
        return builder
                .withClient(client)
                .withAmount(amount)
                .withTerm(60)
                .withPurpose("Купівля автомобіля")
                .withCollateral("Автомобіль, що купується")
                .withInsurance()
                .build();
    }

    public LoanApplication buildConsumerLoan(LoanApplicationBuilder builder, String client, long amount) {
        Log.log(SRC, "Типова заявка «Споживчий кредит»");
        return builder
                .withClient(client)
                .withAmount(amount)
                .withTerm(24)
                .withPurpose("Споживчі потреби")
                .withIncomeCertificate()
                .build();
    }
}
