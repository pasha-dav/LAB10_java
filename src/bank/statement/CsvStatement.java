package bank.statement;

import java.time.YearMonth;
import java.util.List;
import java.util.Locale;

public class CsvStatement implements StatementDocument {
    private final String account;
    private final YearMonth period;
    private final List<Operation> operations;
    private final Totals totals;

    public CsvStatement(String account, YearMonth period, List<Operation> operations, Totals totals) {
        this.account = account;
        this.period = period;
        this.operations = operations;
        this.totals = totals;
    }

    @Override public String format() { return "CSV"; }
    @Override public String fileName() { return "vypyska_" + period + ".csv"; }

    @Override
    public String content() {
        StringBuilder sb = new StringBuilder();
        sb.append("account;date;description;amount\n");
        for (Operation op : operations) {
            sb.append(String.format(Locale.US, "%s;%s;%s;%.2f%n", account, op.date(), op.description(), op.amount()));
        }
        sb.append(String.format(Locale.US, "TOTAL;;income=%.2f expense=%.2f;%.2f",
                totals.income(), totals.expense(), totals.balance()));
        return sb.toString();
    }
}
