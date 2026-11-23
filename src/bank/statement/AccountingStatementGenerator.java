package bank.statement;

import java.time.YearMonth;
import java.util.List;

/** ConcreteCreator: бухгалтерія підприємства отримує CSV. */
public class AccountingStatementGenerator extends StatementGenerator {
    public AccountingStatementGenerator(Ledger ledger) {
        super(ledger);
    }

    @Override
    protected StatementDocument createDocument(String account, YearMonth period,
                                               List<Operation> operations, Totals totals) {
        return new CsvStatement(account, period, operations, totals);
    }

    @Override
    protected String recipient() {
        return "Бухгалтерія підприємства";
    }
}
