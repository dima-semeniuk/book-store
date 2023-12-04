package mate.academy.webapp.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.webapp.dto.BookDto;
import mate.academy.webapp.dto.CreateBookRequestDto;
import mate.academy.webapp.exception.EntityNotFoundException;
import mate.academy.webapp.mapper.BookMapper;
import mate.academy.webapp.model.Book;
import mate.academy.webapp.repository.BookRepository;
import mate.academy.webapp.service.BookService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookRepository.save(bookMapper.toModel(requestDto));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> getAll() {
        return bookRepository.getAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
