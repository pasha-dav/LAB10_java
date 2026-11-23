package bank.statement;

import java.util.List;

public record Totals(double income, double expense, double balance) {

    public static Totals of(List<Operation> operations) {
        double income = 0;
        double expense = 0;
        for (Operation op : operations) {
            if (op.amount() >= 0) {
                income += op.amount();
            } else {
                expense += -op.amount();
            }
        }
        return new Totals(income, expense, income - expense);
    }
}
