package bank.statement;

/** Product: документ виписки. */
public interface StatementDocument {
    String format();
    String fileName();
    String content();
}
