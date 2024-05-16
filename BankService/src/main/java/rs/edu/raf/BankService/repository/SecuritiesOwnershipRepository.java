package rs.edu.raf.BankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;

import java.util.List;

@Repository
public interface SecuritiesOwnershipRepository extends JpaRepository<SecuritiesOwnership, Long> {

    List<SecuritiesOwnership> findAllByAccountNumber(String accountNumber);

    List<SecuritiesOwnership> findAllByAccountNumberAndSecuritiesSymbol(String accountNumber, String securitiesSymbol);

    List<SecuritiesOwnership> findAllBySecuritiesSymbol(String securitySymbol);


    @Query("SELECT so FROM SecuritiesOwnership so WHERE so.quantityOfPubliclyAvailable>0")
    List<SecuritiesOwnership> findSecuritiesOwnershipByQuantityOfPubliclyAvailable();


    @Query("Select so from SecuritiesOwnership so join BusinessCashAccount bca on so.accountNumber=bca.accountNumber where bca.PIB is not  null and so.quantityOfPubliclyAvailable>0")
    List<SecuritiesOwnership> findSecuritiesOwnershipByQuantityOfPubliclyAvailableAndIsBusiness();

    @Query("Select so from SecuritiesOwnership so join CashAccount bca on so.accountNumber=bca.accountNumber where so.quantityOfPubliclyAvailable>0")
    List<SecuritiesOwnership> findSecuritiesOwnershipByQuantityOfPubliclyAvailableAndIsPrivate();


}
