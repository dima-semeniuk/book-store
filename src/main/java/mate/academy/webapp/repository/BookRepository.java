package mate.academy.webapp.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.webapp.model.Book;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> findById(Long id);

    List<Book> getAll();
}
