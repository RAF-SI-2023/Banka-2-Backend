package rs.edu.raf.BankService.data.enums;

public enum CreditType {
    GOTOVINSKI("GOTOVINSKI"),
    STAMBENI("STAMBENI"),
    AUTO("AUTO"),
    REFINANSIRAJUCI("REFINANSIRAJUCI");
    public final String label;

    CreditType(String label) {
        this.label = label;
    }

}
