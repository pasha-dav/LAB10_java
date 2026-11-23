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
