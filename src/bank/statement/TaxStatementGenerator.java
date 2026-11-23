package bank.statement;

import java.time.YearMonth;
import java.util.List;

/** ConcreteCreator: податкова отримує XML. */
public class TaxStatementGenerator extends StatementGenerator {
    public TaxStatementGenerator(Ledger ledger) {
        super(ledger);
    }

    @Override
    protected StatementDocument createDocument(String account, YearMonth period,
                                               List<Operation> operations, Totals totals) {
        return new XmlStatement(account, period, operations, totals);
    }

    @Override
    protected String recipient() {
        return "Податкова";
    }
}
