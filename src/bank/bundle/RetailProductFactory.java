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
