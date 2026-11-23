package bank.statement;

import java.time.LocalDate;

/** Операція за рахунком: додатна сума — надходження, від'ємна — витрата. */
public record Operation(LocalDate date, String description, double amount) {
}
