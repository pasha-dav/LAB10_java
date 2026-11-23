package bank.statement;

import bank.common.Log;

import java.time.YearMonth;
import java.util.List;

/**
 * Creator. Процес формування виписки описаний один раз (template method);
 * вид документа визначає фабричний метод createDocument().
 */
public abstract class StatementGenerator {
    private final Ledger ledger;

    protected StatementGenerator(Ledger ledger) {
        this.ledger = ledger;
    }

    public final StatementDocument generate(String account, YearMonth period) {
        String src = getClass().getSimpleName();
        Log.log(src, "Початок формування виписки для адресата «" + recipient() + "», період " + period);

        List<Operation> operations = ledger.select(period);
        Log.log(src, "Вибрано операцій за період: " + operations.size());

        Totals totals = Totals.of(operations);
        Log.log(src, String.format(java.util.Locale.US,
                "Підсумки: надходження %.2f, витрати %.2f, сальдо %.2f",
                totals.income(), totals.expense(), totals.balance()));

        StatementDocument document = createDocument(account, period, operations, totals);
        Log.log(src, "Сформовано документ формату " + document.format() + ": " + document.fileName());

        Log.log(src, "Надіслано адресату «" + recipient() + "»");
        return document;
    }

    /** Фабричний метод. */
    protected abstract StatementDocument createDocument(String account, YearMonth period,
                                                        List<Operation> operations, Totals totals);

    protected abstract String recipient();
}
