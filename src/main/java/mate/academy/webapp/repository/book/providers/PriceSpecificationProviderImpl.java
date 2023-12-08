package mate.academy.webapp.repository.book.providers;

import java.math.BigDecimal;
import mate.academy.webapp.model.Book;
import mate.academy.webapp.repository.PriceSpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProviderImpl implements PriceSpecificationProvider<Book> {
    public Specification<Book> getSpecification(BigDecimal from, BigDecimal to) {
        return (root, query, criteriaBuilder) -> {
            if (from != null && to != null) {
                return criteriaBuilder.between(root.get("price"), from, to);
            }
            if (from != null) {
                return criteriaBuilder.greaterThan(root.get("price"), from);
            }
            return criteriaBuilder.lessThan(root.get("price"), to);
        };
    }
}
