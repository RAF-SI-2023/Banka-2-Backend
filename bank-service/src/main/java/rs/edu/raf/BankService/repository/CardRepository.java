package rs.edu.raf.BankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.edu.raf.BankService.data.entities.card.Card;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("SELECT c FROM Card c WHERE c.identificationCardNumber = :identificationCardNumber")
    Optional<Card> findByIdentificationCardNumber(@Param("identificationCardNumber") Long identificationCardNumber);

    @Query("SELECT c FROM Card c WHERE c.accountNumber = :accountNumber AND c.status = :active")
    List<Card> findActiveCardsAccountNumber(@Param("accountNumber") String accountNumber, @Param("active") boolean status);

    List<Card> findByAccountNumber(String accountNumber);

    @Query("SELECT c FROM Card c WHERE c.status = :b")
    List<Card> findActiveCards(boolean b);
}
