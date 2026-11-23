package bank.statement;

import java.time.YearMonth;
import java.util.List;

/** ConcreteCreator: аудитор (новий адресат; StatementGenerator не змінюється). */
public class AuditorStatementGenerator extends StatementGenerator {
    public AuditorStatementGenerator(Ledger ledger) {
        super(ledger);
    }

    @Override
    protected StatementDocument createDocument(String account, YearMonth period,
                                               List<Operation> operations, Totals totals) {
        return new JsonStatement(account, period, operations, totals);
    }

    @Override
    protected String recipient() {
        return "Аудитор";
    }
}
