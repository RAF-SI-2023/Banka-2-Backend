package rs.edu.raf.BankService.data.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class SavedAccount {

    private String name;
    private String accountNumber;
}
