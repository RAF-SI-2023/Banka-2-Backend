package rs.edu.raf.BankService.data.enums;

/**
 * Hartije od vrednosti koje se mogu trgovati
 */
public enum ListingType {
    STOCK("STOCK"),
    FUTURE("FUTURE"),
    FOREX("FOREX"),
    OPTION("OPTION");

    public final String label;

    ListingType(String label) {
        this.label = label;
    }
}
