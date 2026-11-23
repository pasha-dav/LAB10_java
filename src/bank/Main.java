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
