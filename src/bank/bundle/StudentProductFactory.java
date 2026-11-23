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
