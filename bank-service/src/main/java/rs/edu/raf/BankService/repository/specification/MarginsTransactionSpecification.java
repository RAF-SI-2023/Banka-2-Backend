package rs.edu.raf.BankService.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import rs.edu.raf.BankService.data.entities.MarginsTransaction;

import java.time.LocalDateTime;

public class MarginsTransactionSpecification {

    public static Specification<MarginsTransaction> hasCurrency(String currencyCode) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("currencyCode"), currencyCode);
    }

    public static Specification<MarginsTransaction> isBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
    }
}