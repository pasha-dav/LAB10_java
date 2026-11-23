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
