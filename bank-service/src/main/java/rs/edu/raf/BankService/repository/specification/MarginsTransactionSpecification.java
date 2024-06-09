package rs.edu.raf.BankService.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import rs.edu.raf.BankService.data.entities.MarginsTransaction;

import java.time.LocalDateTime;

public class MarginsTransactionSpecification {

    public static Specification<MarginsTransaction> hasMarginAccountId(Long marginAccountId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("marginsAccount").get("id"), marginAccountId);
    }

    public static Specification<MarginsTransaction> hasCurrency(String currencyCode) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("currencyCode"), currencyCode);
    }

    public static Specification<MarginsTransaction> isBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
            }
            else if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate);
            }
            else if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate);
            }
            else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static Specification<MarginsTransaction> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("marginsAccount").get("email"), email);
    }
}