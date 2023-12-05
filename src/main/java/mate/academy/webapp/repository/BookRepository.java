package mate.academy.webapp.repository;

import java.math.BigDecimal;
import mate.academy.webapp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE Book b SET b.title = ?1, b.author = ?2, "
            + "b.isbn = ?3, b.price = ?4, b.description = ?5, "
            + "b.coverImage = ?6 WHERE b.id = ?7")
    void updateBookById(String title, String author, String isbn, BigDecimal price,
                        String description, String coverImage, Long id);

}
