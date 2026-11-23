package bank.statement;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/** Журнал операцій рахунку. */
public class Ledger {
    private final List<Operation> operations = new ArrayList<>();

    public void add(Operation operation) {
        operations.add(operation);
    }

    public List<Operation> select(YearMonth period) {
        List<Operation> result = new ArrayList<>();
        for (Operation op : operations) {
            if (YearMonth.from(op.date()).equals(period)) {
                result.add(op);
            }
        }
        return result;
    }
}
