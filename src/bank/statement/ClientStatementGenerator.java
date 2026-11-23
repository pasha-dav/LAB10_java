package bank.statement;

import java.time.YearMonth;
import java.util.List;

/** ConcreteCreator: клієнт отримує PDF. */
public class ClientStatementGenerator extends StatementGenerator {
    public ClientStatementGenerator(Ledger ledger) {
        super(ledger);
    }

    @Override
    protected StatementDocument createDocument(String account, YearMonth period,
                                               List<Operation> operations, Totals totals) {
        return new PdfStatement(account, period, operations, totals);
    }

    @Override
    protected String recipient() {
        return "Клієнт";
    }
}
