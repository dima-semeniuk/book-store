package mate.academy.webapp.service;

import java.util.List;
import mate.academy.webapp.dto.book.BookDto;
import mate.academy.webapp.dto.book.BookSearchParametersDto;
import mate.academy.webapp.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> getAll(Pageable pageable);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);

    List<BookDto> searchBooks(BookSearchParametersDto searchParameters, Pageable pageable);
}
