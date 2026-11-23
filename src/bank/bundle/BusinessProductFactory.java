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
