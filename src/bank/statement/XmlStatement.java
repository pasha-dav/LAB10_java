package bank.statement;

import java.time.YearMonth;
import java.util.List;
import java.util.Locale;

public class XmlStatement implements StatementDocument {
    private final String account;
    private final YearMonth period;
    private final List<Operation> operations;
    private final Totals totals;

    public XmlStatement(String account, YearMonth period, List<Operation> operations, Totals totals) {
        this.account = account;
        this.period = period;
        this.operations = operations;
        this.totals = totals;
    }

    @Override public String format() { return "XML"; }
    @Override public String fileName() { return "vypyska_" + period + ".xml"; }

    @Override
    public String content() {
        StringBuilder sb = new StringBuilder();
        sb.append("<statement account=\"").append(account).append("\" period=\"").append(period).append("\">\n");
        for (Operation op : operations) {
            sb.append(String.format(Locale.US, "  <operation date=\"%s\" amount=\"%.2f\">%s</operation>%n",
                    op.date(), op.amount(), op.description()));
        }
        sb.append(String.format(Locale.US, "  <totals income=\"%.2f\" expense=\"%.2f\" balance=\"%.2f\"/>%n",
                totals.income(), totals.expense(), totals.balance()));
        sb.append("</statement>");
        return sb.toString();
    }
}
