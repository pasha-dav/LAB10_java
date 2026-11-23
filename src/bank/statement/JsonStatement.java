package bank.statement;

import java.time.YearMonth;
import java.util.List;
import java.util.Locale;

public class JsonStatement implements StatementDocument {
    private final String account;
    private final YearMonth period;
    private final List<Operation> operations;
    private final Totals totals;

    public JsonStatement(String account, YearMonth period, List<Operation> operations, Totals totals) {
        this.account = account;
        this.period = period;
        this.operations = operations;
        this.totals = totals;
    }

    @Override public String format() { return "JSON"; }
    @Override public String fileName() { return "vypyska_" + period + ".json"; }

    @Override
    public String content() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"account\": \"").append(account).append("\",\n");
        sb.append("  \"period\": \"").append(period).append("\",\n");
        sb.append("  \"operations\": [\n");
        for (int i = 0; i < operations.size(); i++) {
            Operation op = operations.get(i);
            sb.append(String.format(Locale.US, "    {\"date\": \"%s\", \"description\": \"%s\", \"amount\": %.2f}%s%n",
                    op.date(), op.description(), op.amount(), i < operations.size() - 1 ? "," : ""));
        }
        sb.append("  ],\n");
        sb.append(String.format(Locale.US, "  \"totals\": {\"income\": %.2f, \"expense\": %.2f, \"balance\": %.2f}%n",
                totals.income(), totals.expense(), totals.balance()));
        sb.append("}");
        return sb.toString();
    }
}
