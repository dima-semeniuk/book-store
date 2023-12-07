package mate.academy.webapp.repository.bookspecification;

import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;

public interface PriceSpecificationProvider<T> {
    Specification<T> getSpecification(BigDecimal from, BigDecimal to);
}
