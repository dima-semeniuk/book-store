package mate.academy.webapp.service;

import java.util.List;
import mate.academy.webapp.dto.book.BookResponseDto;
import mate.academy.webapp.dto.book.BookSearchParametersDto;
import mate.academy.webapp.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(CreateBookRequestDto requestDto);

    BookResponseDto findById(Long id);

    List<BookResponseDto> getAll(Pageable pageable);

    BookResponseDto updateById(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookResponseDto> searchBooks(BookSearchParametersDto searchParameters, Pageable pageable);
}
