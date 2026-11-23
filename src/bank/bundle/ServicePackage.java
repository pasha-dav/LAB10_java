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
