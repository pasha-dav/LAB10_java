#!/usr/bin/env bash
# Створює структуру проєкту «Прут-Кредит» (варіант 7) і всі Java-файли.
# Запуск з кореня проєкту:  bash setup_bank.sh        (лише створити файли)
#                           bash setup_bank.sh run    (створити, скомпілювати і запустити)
set -e

mkdir -p src/bank/common src/bank/loan src/bank/bundle src/bank/statement

# ======================= common =======================
cat > src/bank/common/Log.java <<'JAVA_EOF'
package bank.common;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Консольний журнал подій у форматі [час] [джерело] подія. Час умовний. */
public final class Log {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static LocalTime now = LocalTime.of(9, 0, 0);

    private Log() {
    }

    public static void log(String source, String event) {
        now = now.plusSeconds(1);
        System.out.printf("[%s] [%s] %s%n", now.format(FMT), source, event);
    }
}
JAVA_EOF

# ======================= loan (Builder) =======================
cat > src/bank/loan/IncompleteLoanApplicationException.java <<'JAVA_EOF'
package bank.loan;

public class IncompleteLoanApplicationException extends IllegalStateException {
    public IncompleteLoanApplicationException(String message) {
        super(message);
    }
}
JAVA_EOF

cat > src/bank/loan/LoanApplication.java <<'JAVA_EOF'
package bank.loan;

/** Product. Незмінна кредитна заявка; створюється лише через білдер. */
public final class LoanApplication {
    private final String client;
    private final long amount;
    private final int termMonths;
    private final String purpose;
    private final String collateral;
    private final String guarantor;
    private final boolean incomeCertificate;
    private final boolean insurance;

    LoanApplication(StandardLoanApplicationBuilder b) {
        this.client = b.client;
        this.amount = b.amount;
        this.termMonths = b.termMonths;
        this.purpose = b.purpose;
        this.collateral = b.collateral;
        this.guarantor = b.guarantor;
        this.incomeCertificate = b.incomeCertificate;
        this.insurance = b.insurance;
    }

    public String getClient() { return client; }
    public long getAmount() { return amount; }
    public int getTermMonths() { return termMonths; }
    public String getPurpose() { return purpose; }
    public String getCollateral() { return collateral; }
    public String getGuarantor() { return guarantor; }
    public boolean hasIncomeCertificate() { return incomeCertificate; }
    public boolean hasInsurance() { return insurance; }

    @Override
    public String toString() {
        return "Кредитна заявка {\n"
                + "  клієнт:            " + client + "\n"
                + "  сума:              " + String.format("%,d", amount).replace(',', ' ') + " грн\n"
                + "  строк:             " + termMonths + " міс.\n"
                + "  мета:              " + (purpose == null ? "—" : purpose) + "\n"
                + "  застава:           " + (collateral == null ? "—" : collateral) + "\n"
                + "  поручитель:        " + (guarantor == null ? "—" : guarantor) + "\n"
                + "  довідка про доходи: " + (incomeCertificate ? "так" : "—") + "\n"
                + "  страхування:       " + (insurance ? "так" : "—") + "\n"
                + "}";
    }
}
JAVA_EOF

cat > src/bank/loan/LoanApplicationBuilder.java <<'JAVA_EOF'
package bank.loan;

/** Builder: окремий зрозумілий крок для кожної частини заявки. */
public interface LoanApplicationBuilder {
    // обов'язкові частини
    LoanApplicationBuilder withClient(String client);
    LoanApplicationBuilder withAmount(long amount);
    LoanApplicationBuilder withTerm(int months);

    // необов'язкові частини
    LoanApplicationBuilder withPurpose(String purpose);
    LoanApplicationBuilder withCollateral(String collateral);
    LoanApplicationBuilder withGuarantor(String guarantor);
    LoanApplicationBuilder withIncomeCertificate();
    LoanApplicationBuilder withInsurance();

    /** Завершальний крок: перевіряє обов'язкові частини. */
    LoanApplication build();
}
JAVA_EOF

cat > src/bank/loan/StandardLoanApplicationBuilder.java <<'JAVA_EOF'
package bank.loan;

import bank.common.Log;

/** ConcreteBuilder. */
public class StandardLoanApplicationBuilder implements LoanApplicationBuilder {
    private static final String SRC = "Builder";

    String client;
    long amount;
    int termMonths;
    String purpose;
    String collateral;
    String guarantor;
    boolean incomeCertificate;
    boolean insurance;

    @Override
    public LoanApplicationBuilder withClient(String client) {
        this.client = client;
        Log.log(SRC, "Задано клієнта: " + client);
        return this;
    }

    @Override
    public LoanApplicationBuilder withAmount(long amount) {
        this.amount = amount;
        Log.log(SRC, "Задано суму: " + amount + " грн");
        return this;
    }

    @Override
    public LoanApplicationBuilder withTerm(int months) {
        this.termMonths = months;
        Log.log(SRC, "Задано строк: " + months + " міс.");
        return this;
    }

    @Override
    public LoanApplicationBuilder withPurpose(String purpose) {
        this.purpose = purpose;
        Log.log(SRC, "Задано мету: " + purpose);
        return this;
    }

    @Override
    public LoanApplicationBuilder withCollateral(String collateral) {
        this.collateral = collateral;
        Log.log(SRC, "Додано заставу: " + collateral);
        return this;
    }

    @Override
    public LoanApplicationBuilder withGuarantor(String guarantor) {
        this.guarantor = guarantor;
        Log.log(SRC, "Додано поручителя: " + guarantor);
        return this;
    }

    @Override
    public LoanApplicationBuilder withIncomeCertificate() {
        this.incomeCertificate = true;
        Log.log(SRC, "Додано довідку про доходи");
        return this;
    }

    @Override
    public LoanApplicationBuilder withInsurance() {
        this.insurance = true;
        Log.log(SRC, "Додано страхування");
        return this;
    }

    @Override
    public LoanApplication build() {
        if (client == null || client.isBlank()) {
            fail("не вказано клієнта");
        }
        if (amount <= 0) {
            fail("не вказано суму кредиту");
        }
        if (termMonths <= 0) {
            fail("не вказано строк кредиту");
        }
        LoanApplication application = new LoanApplication(this);
        Log.log(SRC, "Заявку сформовано: " + client + ", " + amount + " грн, " + termMonths + " міс.");
        return application;
    }

    private void fail(String reason) {
        String message = "Неможливо сформувати заявку: " + reason;
        Log.log(SRC, message);
        throw new IncompleteLoanApplicationException(message);
    }
}
JAVA_EOF

cat > src/bank/loan/LoanApplicationDirector.java <<'JAVA_EOF'
package bank.loan;

import bank.common.Log;

/** Director: типові заявки формуються однаковою послідовністю кроків, описаною в одному місці. */
public class LoanApplicationDirector {
    private static final String SRC = "Director";

    public LoanApplication buildAutoLoan(LoanApplicationBuilder builder, String client, long amount) {
        Log.log(SRC, "Типова заявка «Автокредит»");
        return builder
                .withClient(client)
                .withAmount(amount)
                .withTerm(60)
                .withPurpose("Купівля автомобіля")
                .withCollateral("Автомобіль, що купується")
                .withInsurance()
                .build();
    }

    public LoanApplication buildConsumerLoan(LoanApplicationBuilder builder, String client, long amount) {
        Log.log(SRC, "Типова заявка «Споживчий кредит»");
        return builder
                .withClient(client)
                .withAmount(amount)
                .withTerm(24)
                .withPurpose("Споживчі потреби")
                .withIncomeCertificate()
                .build();
    }
}
JAVA_EOF

# ======================= bundle (Abstract Factory) =======================
cat > src/bank/bundle/Product.java <<'JAVA_EOF'
package bank.bundle;

/** Спільна абстракція будь-якого продукту пакета. */
public interface Product {
    String segment();
    String describe();
}
JAVA_EOF

cat > src/bank/bundle/Card.java <<'JAVA_EOF'
package bank.bundle;

public interface Card extends Product {
}
JAVA_EOF

cat > src/bank/bundle/CurrentAccount.java <<'JAVA_EOF'
package bank.bundle;

public interface CurrentAccount extends Product {
}
JAVA_EOF

cat > src/bank/bundle/TariffPlan.java <<'JAVA_EOF'
package bank.bundle;

public interface TariffPlan extends Product {
}
JAVA_EOF

cat > src/bank/bundle/ProductFactory.java <<'JAVA_EOF'
package bank.bundle;

/** AbstractFactory: видає узгоджений пакет продуктів одного сегмента. */
public interface ProductFactory {
    String segmentName();
    Card createCard();
    CurrentAccount createCurrentAccount();
    TariffPlan createTariffPlan();
}
JAVA_EOF

cat > src/bank/bundle/RetailProductFactory.java <<'JAVA_EOF'
package bank.bundle;

/** ConcreteFactory: роздрібний сегмент. */
public class RetailProductFactory implements ProductFactory {
    private static final String SEGMENT = "Роздрібний";

    @Override public String segmentName() { return SEGMENT; }
    @Override public Card createCard() { return new RetailCard(); }
    @Override public CurrentAccount createCurrentAccount() { return new RetailAccount(); }
    @Override public TariffPlan createTariffPlan() { return new RetailTariff(); }

    private static final class RetailCard implements Card {
        @Override public String segment() { return SEGMENT; }
        @Override public String describe() { return "Дебетова картка Classic"; }
    }

    private static final class RetailAccount implements CurrentAccount {
        @Override public String segment() { return SEGMENT; }
        @Override public String describe() { return "Поточний рахунок «Стандарт»"; }
    }

    private static final class RetailTariff implements TariffPlan {
        @Override public String segment() { return SEGMENT; }
        @Override public String describe() { return "Тариф «Базовий»: 0 грн/міс, комісія за перекази 1%"; }
    }
}
JAVA_EOF

cat > src/bank/bundle/PremiumProductFactory.java <<'JAVA_EOF'
package bank.bundle;

/** ConcreteFactory: преміальний сегмент. */
public class PremiumProductFactory implements ProductFactory {
    private static final String SEGMENT = "Преміальний";

    @Override public String segmentName() { return SEGMENT; }
    @Override public Card createCard() { return new PremiumCard(); }
    @Override public CurrentAccount createCurrentAccount() { return new PremiumAccount(); }
    @Override public TariffPlan createTariffPlan() { return new PremiumTariff(); }

    private static final class PremiumCard implements Card {
        @Override public String segment() { return SEGMENT; }
        @Override public String describe() { return "Картка Platinum з кешбеком 3%"; }
    }

    private static final class PremiumAccount implements CurrentAccount {
        @Override public String segment() { return SEGMENT; }
        @Override public String describe() { return "Рахунок «Преміум» з персональним менеджером"; }
    }

    private static final class PremiumTariff implements TariffPlan {
        @Override public String segment() { return SEGMENT; }
        @Override public String describe() { return "Тариф «Преміум»: 299 грн/міс, перекази без комісії"; }
    }
}
JAVA_EOF

cat > src/bank/bundle/BusinessProductFactory.java <<'JAVA_EOF'
package bank.bundle;

/** ConcreteFactory: бізнес-сегмент. */
public class BusinessProductFactory implements ProductFactory {
    private static final String SEGMENT = "Бізнес";

    @Override public String segmentName() { return SEGMENT; }
    @Override public Card createCard() { return new BusinessCard(); }
    @Override public CurrentAccount createCurrentAccount() { return new BusinessAccount(); }
    @Override public TariffPlan createTariffPlan() { return new BusinessTariff(); }

    private static final class BusinessCard implements Card {
        @Override public String segment() { return SEGMENT; }
        @Override public String describe() { return "Бізнес-картка Corporate"; }
    }

    private static final class BusinessAccount implements CurrentAccount {
        @Override public String segment() { return SEGMENT; }
        @Override public String describe() { return "Розрахунковий рахунок підприємства"; }
    }

    private static final class BusinessTariff implements TariffPlan {
        @Override public String segment() { return SEGMENT; }
        @Override public String describe() { return "Тариф «Бізнес»: 499 грн/міс, 50 платіжок безкоштовно"; }
    }
}
JAVA_EOF

cat > src/bank/bundle/StudentProductFactory.java <<'JAVA_EOF'
package bank.bundle;

/** ConcreteFactory: студентський сегмент (додається новими класами). */
public class StudentProductFactory implements ProductFactory {
    private static final String SEGMENT = "Студентський";

    @Override public String segmentName() { return SEGMENT; }
    @Override public Card createCard() { return new StudentCard(); }
    @Override public CurrentAccount createCurrentAccount() { return new StudentAccount(); }
    @Override public TariffPlan createTariffPlan() { return new StudentTariff(); }

    private static final class StudentCard implements Card {
        @Override public String segment() { return SEGMENT; }
        @Override public String describe() { return "Картка Student зі знижками від партнерів"; }
    }

    private static final class StudentAccount implements CurrentAccount {
        @Override public String segment() { return SEGMENT; }
        @Override public String describe() { return "Рахунок «Студент» без плати за обслуговування"; }
    }

    private static final class StudentTariff implements TariffPlan {
        @Override public String segment() { return SEGMENT; }
        @Override public String describe() { return "Тариф «Студентський»: 0 грн/міс, перекази без комісії до 5 000 грн"; }
    }
}
JAVA_EOF

cat > src/bank/bundle/ServicePackage.java <<'JAVA_EOF'
package bank.bundle;

/** Готовий пакет продуктів клієнта. */
public class ServicePackage {
    private final String client;
    private final String segment;
    private final Card card;
    private final CurrentAccount account;
    private final TariffPlan tariff;

    public ServicePackage(String client, String segment, Card card, CurrentAccount account, TariffPlan tariff) {
        this.client = client;
        this.segment = segment;
        this.card = card;
        this.account = account;
        this.tariff = tariff;
    }

    @Override
    public String toString() {
        return "Пакет клієнта " + client + " (сегмент «" + segment + "») {\n"
                + "  картка:  " + card.describe() + "\n"
                + "  рахунок: " + account.describe() + "\n"
                + "  тариф:   " + tariff.describe() + "\n"
                + "}";
    }
}
JAVA_EOF

cat > src/bank/bundle/Branch.java <<'JAVA_EOF'
package bank.bundle;

import bank.common.Log;

/** Client: працює лише з абстракціями ProductFactory / Product. */
public class Branch {
    private static final String SRC = "Відділення";

    public ServicePackage openService(String client, ProductFactory factory) {
        Log.log(SRC, "Відкриття обслуговування: " + client + ", сегмент «" + factory.segmentName() + "»");

        Card card = factory.createCard();
        Log.log(SRC, "Видано картку: " + card.describe() + " [" + card.segment() + "]");

        CurrentAccount account = factory.createCurrentAccount();
        Log.log(SRC, "Відкрито рахунок: " + account.describe() + " [" + account.segment() + "]");

        TariffPlan tariff = factory.createTariffPlan();
        Log.log(SRC, "Підключено тариф: " + tariff.describe() + " [" + tariff.segment() + "]");

        boolean consistent = card.segment().equals(account.segment())
                && account.segment().equals(tariff.segment());
        Log.log(SRC, consistent
                ? "Перевірка: усі продукти пакета належать до сегмента «" + card.segment() + "»"
                : "ПОМИЛКА: продукти пакета належать до різних сегментів!");

        return new ServicePackage(client, factory.segmentName(), card, account, tariff);
    }
}
JAVA_EOF

# ======================= statement (Factory Method) =======================
cat > src/bank/statement/Operation.java <<'JAVA_EOF'
package bank.statement;

import java.time.LocalDate;

/** Операція за рахунком: додатна сума — надходження, від'ємна — витрата. */
public record Operation(LocalDate date, String description, double amount) {
}
JAVA_EOF

cat > src/bank/statement/Totals.java <<'JAVA_EOF'
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
JAVA_EOF

cat > src/bank/statement/Ledger.java <<'JAVA_EOF'
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
JAVA_EOF

cat > src/bank/statement/StatementDocument.java <<'JAVA_EOF'
package bank.statement;

/** Product: документ виписки. */
public interface StatementDocument {
    String format();
    String fileName();
    String content();
}
JAVA_EOF

cat > src/bank/statement/StatementGenerator.java <<'JAVA_EOF'
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
JAVA_EOF

cat > src/bank/statement/PdfStatement.java <<'JAVA_EOF'
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
JAVA_EOF

cat > src/bank/statement/XmlStatement.java <<'JAVA_EOF'
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
JAVA_EOF

cat > src/bank/statement/CsvStatement.java <<'JAVA_EOF'
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
JAVA_EOF

cat > src/bank/statement/JsonStatement.java <<'JAVA_EOF'
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
JAVA_EOF

cat > src/bank/statement/ClientStatementGenerator.java <<'JAVA_EOF'
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
JAVA_EOF

cat > src/bank/statement/TaxStatementGenerator.java <<'JAVA_EOF'
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
JAVA_EOF

cat > src/bank/statement/AccountingStatementGenerator.java <<'JAVA_EOF'
package bank.statement;

import java.time.YearMonth;
import java.util.List;

/** ConcreteCreator: бухгалтерія підприємства отримує CSV. */
public class AccountingStatementGenerator extends StatementGenerator {
    public AccountingStatementGenerator(Ledger ledger) {
        super(ledger);
    }

    @Override
    protected StatementDocument createDocument(String account, YearMonth period,
                                               List<Operation> operations, Totals totals) {
        return new CsvStatement(account, period, operations, totals);
    }

    @Override
    protected String recipient() {
        return "Бухгалтерія підприємства";
    }
}
JAVA_EOF

cat > src/bank/statement/AuditorStatementGenerator.java <<'JAVA_EOF'
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
JAVA_EOF

# ======================= Main =======================
cat > src/bank/Main.java <<'JAVA_EOF'
package bank;

import bank.bundle.Branch;
import bank.bundle.BusinessProductFactory;
import bank.bundle.PremiumProductFactory;
import bank.bundle.RetailProductFactory;
import bank.bundle.ServicePackage;
import bank.bundle.StudentProductFactory;
import bank.common.Log;
import bank.loan.IncompleteLoanApplicationException;
import bank.loan.LoanApplication;
import bank.loan.LoanApplicationDirector;
import bank.loan.StandardLoanApplicationBuilder;
import bank.statement.AccountingStatementGenerator;
import bank.statement.AuditorStatementGenerator;
import bank.statement.ClientStatementGenerator;
import bank.statement.Ledger;
import bank.statement.Operation;
import bank.statement.StatementDocument;
import bank.statement.StatementGenerator;
import bank.statement.TaxStatementGenerator;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Примусово UTF-8 у консолі (у Java 18+ System.out не залежить від -Dfile.encoding)
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));

        demoBuilderLoanApplication();
        demoAbstractFactoryPackages();
        demoFactoryMethodStatements();
    }

    // ---------- Завдання 1: Builder ----------
    private static void demoBuilderLoanApplication() {
        header("Завдання 1. Кредитна заявка (Builder)");

        Log.log("Main", "Сценарій 1: заявка на 300 000 грн на 36 міс. із заставою і поручителем");
        LoanApplication full = new StandardLoanApplicationBuilder()
                .withClient("Іваненко Олег")
                .withAmount(300_000)
                .withTerm(36)
                .withPurpose("Ремонт житла")
                .withCollateral("Квартира, м. Чернівці")
                .withGuarantor("Петренко Марія")
                .build();
        System.out.println(full);

        Log.log("Main", "Сценарій 2: типова заявка «Споживчий кредит»");
        LoanApplicationDirector director = new LoanApplicationDirector();
        LoanApplication typical = director.buildConsumerLoan(
                new StandardLoanApplicationBuilder(), "Шевченко Андрій", 80_000);
        System.out.println(typical);

        Log.log("Main", "Сценарій 3: спроба сформувати заявку без суми");
        try {
            new StandardLoanApplicationBuilder()
                    .withClient("Коваль Ірина")
                    .withTerm(12)
                    .build();
        } catch (IncompleteLoanApplicationException e) {
            Log.log("Main", "Заявку на скоринг не передано: " + e.getMessage());
        }
    }

    // ---------- Завдання 2: Abstract Factory ----------
    private static void demoAbstractFactoryPackages() {
        header("Завдання 2. Пакети продуктів (Abstract Factory)");

        Branch branch = new Branch();

        Log.log("Main", "Сценарій 1: три сегменти одним кодом відділення");
        List<ServicePackage> packages = List.of(
                branch.openService("Мельник Тарас", new RetailProductFactory()),
                branch.openService("Бондар Оксана", new PremiumProductFactory()),
                branch.openService("ТОВ «Буковина-Трейд»", new BusinessProductFactory()));
        packages.forEach(System.out::println);

        Log.log("Main", "Сценарій 2: додаємо студентський сегмент новими класами, Branch не змінюється");
        ServicePackage student = branch.openService("Гнатюк Софія", new StudentProductFactory());
        System.out.println(student);
    }

    // ---------- Завдання 3: Factory Method ----------
    private static void demoFactoryMethodStatements() {
        header("Завдання 3. Формування виписок (Factory Method)");

        Ledger ledger = new Ledger();
        ledger.add(new Operation(LocalDate.of(2026, 10, 28), "Оплата оренди (жовтень)", -12000.00));
        ledger.add(new Operation(LocalDate.of(2026, 11, 3), "Надходження від замовника", 45000.00));
        ledger.add(new Operation(LocalDate.of(2026, 11, 10), "Оплата постачальнику", -18250.50));
        ledger.add(new Operation(LocalDate.of(2026, 11, 17), "Комісія банку", -150.00));
        ledger.add(new Operation(LocalDate.of(2026, 11, 21), "Надходження від замовника", 20000.00));

        String account = "UA21 3052 9900 0002 6001 0000 0123 4";
        YearMonth period = YearMonth.of(2026, 11);

        Log.log("Main", "Сценарій 1: виписка за місяць для клієнта, податкової і бухгалтерії");
        List<StatementGenerator> generators = List.of(
                new ClientStatementGenerator(ledger),
                new TaxStatementGenerator(ledger),
                new AccountingStatementGenerator(ledger));
        for (StatementGenerator generator : generators) {
            StatementDocument document = generator.generate(account, period);
            printDocument(document);
        }

        Log.log("Main", "Сценарій 2: додаємо адресата «аудитор» новими класами, процес не змінюється");
        StatementDocument auditorDoc = new AuditorStatementGenerator(ledger).generate(account, period);
        printDocument(auditorDoc);
    }

    private static void printDocument(StatementDocument document) {
        System.out.println("--- " + document.fileName() + " ---");
        System.out.println(document.content());
        System.out.println("--- кінець файлу ---");
    }

    private static void header(String title) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println(title);
        System.out.println("==================================================");
    }
}
JAVA_EOF

echo "Готово: створено $(find src -name '*.java' | wc -l) Java-файлів у ./src/bank"

# ======================= необов'язковий запуск =======================
if [ "$1" = "run" ]; then
    mkdir -p out
    javac -encoding UTF-8 -d out $(find src -name "*.java")
    java -cp out bank.Main
else
    echo "Компіляція і запуск:"
    echo "  mkdir -p out && javac -encoding UTF-8 -d out \$(find src -name \"*.java\") && java -cp out bank.Main"
    echo "Або просто: bash setup_bank.sh run"
fi
