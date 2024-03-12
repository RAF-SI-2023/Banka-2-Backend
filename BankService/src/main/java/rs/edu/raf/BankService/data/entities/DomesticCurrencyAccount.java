package rs.edu.raf.BankService.data.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("DomesticCurrencyAccount")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomesticCurrencyAccount extends Account {

    private Double interestRate; // vazi za retirement i student
}
