package mate.academy.webapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import mate.academy.webapp.model.Book;
import mate.academy.webapp.repository.book.BookRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    private static List<Book> expectedList = new ArrayList<>();

    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    static void beforeAll() {
        Book history = new Book()
                .setId(2L)
                .setAuthor("Author2")
                .setTitle("About fishing")
                .setIsbn("123334vf")
                .setPrice(BigDecimal.valueOf(30));
        Book traveling = new Book()
                .setId(3L)
                .setAuthor("Author3")
                .setTitle("Traveling")
                .setIsbn("123345se")
                .setPrice(BigDecimal.valueOf(29));
        expectedList.add(history);
        expectedList.add(traveling);
    }

    @Test
    @DisplayName("Find all books by existing category id")
    @Sql(scripts = {
            "classpath:database/books/add-books-to-book-table.sql",
            "classpath:database/categories/add-categories-to-categories-table.sql",
            "classpath:database/books-categories/add-books-categories-relations.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-books-from-book-table.sql",
            "classpath:database/categories/delete-categories-from-categories-table.sql",
            "classpath:database/books-categories/delete-books-categories-relations.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_CategoryIdOne_ReturnTwoBooks() {
        Long categoryId = 1L;

        List<Book> actual = bookRepository.findAllByCategoryId(categoryId);

        int expectedSize = 2;
        assertEquals(expectedSize, actual.size());
        assertEquals(expectedList, actual);
    }

    @Test
    @DisplayName("Find all books by not existing category id")
    @Sql(scripts = {
            "classpath:database/books/add-books-to-book-table.sql",
            "classpath:database/categories/add-categories-to-categories-table.sql",
            "classpath:database/books-categories/add-books-categories-relations.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-books-from-book-table.sql",
            "classpath:database/categories/delete-categories-from-categories-table.sql",
            "classpath:database/books-categories/delete-books-categories-relations.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_NotExistingCategoryId_ReturnEmptyList() {
        Long categoryId = 4L;

        List<Book> actual = bookRepository.findAllByCategoryId(categoryId);

        List<Book> emptyList = new ArrayList<>();
        assertEquals(emptyList, actual);
    }
}
