package bank.statement;

import java.time.YearMonth;
import java.util.List;
import java.util.Locale;

public class PdfStatement implements StatementDocument {
    private final String account;
    private final YearMonth period;
    private final List<Operation> operations;
    private final Totals totals;

    public PdfStatement(String account, YearMonth period, List<Operation> operations, Totals totals) {
        this.account = account;
        this.period = period;
        this.operations = operations;
        this.totals = totals;
    }

    @Override public String format() { return "PDF"; }
    @Override public String fileName() { return "vypyska_" + period + ".pdf"; }

    @Override
    public String content() {
        StringBuilder sb = new StringBuilder();
        sb.append("%PDF-1.7 (імітація)\n");
        sb.append("=== ВИПИСКА ПО РАХУНКУ ").append(account).append(" ===\n");
        sb.append("Період: ").append(period).append("\n");
        for (Operation op : operations) {
            sb.append(String.format(Locale.US, "  %s  %-28s %10.2f%n", op.date(), op.description(), op.amount()));
        }
        sb.append(String.format(Locale.US, "Надходження: %.2f | Витрати: %.2f | Сальдо: %.2f",
                totals.income(), totals.expense(), totals.balance()));
        return sb.toString();
    }
}
