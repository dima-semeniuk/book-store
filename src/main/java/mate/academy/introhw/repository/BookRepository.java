package mate.academy.introhw.repository;

import java.util.List;
import mate.academy.introhw.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
