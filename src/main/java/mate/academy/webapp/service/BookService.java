package mate.academy.webapp.service;

import java.util.List;
import mate.academy.webapp.dto.BookDto;
import mate.academy.webapp.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> getAll();

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);
}
