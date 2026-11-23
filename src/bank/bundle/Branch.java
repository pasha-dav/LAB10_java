package bank.bundle;

import bank.common.Log;

/** Client: працює лише з абстракціями ProductFactory / Product. */
public class Branch {
    private static final String SRC = "Відділення";

    public ServicePackage openService(String client, ProductFactory factory) {
        Log.log(SRC, "Відкриття обслуговування: " + client + ", сегмент «" + factory.segmentName() + "»");

        Card card = factory.createCard();
        Log.log(SRC, "Видано картку: " + card.describe() + " [" + card.segment() + "]");

        CurrentAccount account = factory.createCurrentAccount();
        Log.log(SRC, "Відкрито рахунок: " + account.describe() + " [" + account.segment() + "]");

        TariffPlan tariff = factory.createTariffPlan();
        Log.log(SRC, "Підключено тариф: " + tariff.describe() + " [" + tariff.segment() + "]");

        boolean consistent = card.segment().equals(account.segment())
                && account.segment().equals(tariff.segment());
        Log.log(SRC, consistent
                ? "Перевірка: усі продукти пакета належать до сегмента «" + card.segment() + "»"
                : "ПОМИЛКА: продукти пакета належать до різних сегментів!");

        return new ServicePackage(client, factory.segmentName(), card, account, tariff);
    }
}
