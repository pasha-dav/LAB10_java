package bank.bundle;

/** AbstractFactory: видає узгоджений пакет продуктів одного сегмента. */
public interface ProductFactory {
    String segmentName();
    Card createCard();
    CurrentAccount createCurrentAccount();
    TariffPlan createTariffPlan();
}
