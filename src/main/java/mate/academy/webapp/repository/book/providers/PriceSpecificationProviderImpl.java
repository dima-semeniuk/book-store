package mate.academy.webapp.repository.book.providers;

import java.math.BigDecimal;
import mate.academy.webapp.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProviderImpl implements PriceSpecificationProvider<Book> {
    private static final String ATTRIBUTE = "price";

    public Specification<Book> getSpecification(BigDecimal from, BigDecimal to) {
        return (root, query, criteriaBuilder) -> {
            if (from != null && to != null) {
                return criteriaBuilder.between(root.get(ATTRIBUTE), from, to);
            }
            if (from != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(ATTRIBUTE), from);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get(ATTRIBUTE), to);
        };
    }
}
