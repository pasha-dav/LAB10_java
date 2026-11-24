import variant7_bank.builder.CreditApplication;
import variant7_bank.builder.CreditApplicationBuilder;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void log(String source, String message) {
        String time = LocalTime.now().format(TIME_FORMATTER);
        System.out.println("[" + time + "] [" + source + "] " + message);
    }

    public static void main(String[] args) {
        log("System", "==================================================");
        log("System", " ЛАБОРАТОРНА РОБОТА №10: ВАРІАНТ 7 (Банк «Прут-Кредит»)");
        log("System", "==================================================");

        runTask1BuilderDemo();
    }

    private static void runTask1BuilderDemo() {
        log("Main", "=== ДЕМОНСТРАЦІЯ ЗАВДАННЯ 1: Кредитна заявка (Builder) ===");

        // 1. Повна заявка (автокредит)
        try {
            CreditApplication autoLoan = new CreditApplicationBuilder()
                    .setClient("Олександр Шевченко")
                    .setAmount(300000.0)
                    .setTermMonths(36)
                    .setPurpose("Купівля автомобіля")
                    .setCollateral("Автомобіль Mazda CX-5")
                    .setGuarantor("Іван Шевченко")
                    .setIncomeStatement(true)
                    .setInsurance(true)
                    .build();

            log("CreditDepartment", "Сформовано повну заявку:");
            System.out.println("  -> " + autoLoan);
        } catch (Exception e) {
            log("Error", e.getMessage());
        }

        // 2. Типова заявка (споживчий кредит)
        try {
            CreditApplication consumerLoan = new CreditApplicationBuilder()
                    .setClient("Марія Коваль")
                    .setAmount(50000.0)
                    .setTermMonths(12)
                    .setPurpose("Споживчі потреби")
                    .setIncomeStatement(true)
                    .build();

            log("CreditDepartment", "Сформовано типовий «Споживчий кредит»:");
            System.out.println("  -> " + consumerLoan);
        } catch (Exception e) {
            log("Error", e.getMessage());
        }

        // 3. Спроба сформувати некоректну заявку (без суми)
        log("CreditDepartment", "Спроба сформувати заявку без суми кредиту...");
        try {
            CreditApplication invalidLoan = new CreditApplicationBuilder()
                    .setClient("Петро Мельник")
                    .setAmount(0.0)
                    .setTermMonths(24)
                    .setPurpose("Ремонт")
                    .build();
        } catch (IllegalArgumentException e) {
            log("Journal", e.getMessage());
        }
    }
}