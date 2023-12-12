package mate.academy.webapp.repository.book.providers;

import java.util.Arrays;
import mate.academy.webapp.model.Book;
import mate.academy.webapp.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String ATTRIBUTE = "author";

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(ATTRIBUTE)
                .in(Arrays.stream(params).toArray());
    }
}
