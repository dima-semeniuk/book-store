package mate.academy.introhw;

import java.math.BigDecimal;
import mate.academy.introhw.model.Book;
import mate.academy.introhw.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IntroHwApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(IntroHwApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setTittle("Around the world");
                book.setAuthor("Some Author");
                book.setPrice(BigDecimal.valueOf(799));
                book.setIsbn("976-23-123-1234");
                book.setDescription("About traveling");

                bookService.save(book);
                System.out.println(bookService.findAll());
            }
        };
    }

}
