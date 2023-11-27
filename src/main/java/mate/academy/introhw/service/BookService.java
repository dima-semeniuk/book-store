package mate.academy.introhw.service;

import java.util.List;
import mate.academy.introhw.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
