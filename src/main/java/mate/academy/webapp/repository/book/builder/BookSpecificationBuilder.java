package mate.academy.webapp.repository.book.builder;

import lombok.RequiredArgsConstructor;
import mate.academy.webapp.dto.BookSearchParametersDto;
import mate.academy.webapp.model.Book;
import mate.academy.webapp.repository.SpecificationBuilder;
import mate.academy.webapp.repository.PriceSpecificationProvider;
import mate.academy.webapp.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProvider<Book> authorSpecificationProvider;
    private final PriceSpecificationProvider<Book> priceSpecificationProviderImpl;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            spec = spec.and(authorSpecificationProvider
                    .getSpecification(searchParameters.authors()));
        }
        if (searchParameters.priceFrom() != null || searchParameters.priceTo() != null) {
            spec = spec.and(priceSpecificationProviderImpl
                    .getSpecification(searchParameters.priceFrom(), searchParameters.priceTo()));
        }
        return spec;
    }
}
