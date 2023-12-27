package mate.academy.webapp.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.webapp.dto.book.BookResponseDto;
import mate.academy.webapp.dto.book.BookSearchParametersDto;
import mate.academy.webapp.dto.book.CreateBookRequestDto;
import mate.academy.webapp.exception.EntityNotFoundException;
import mate.academy.webapp.mapper.BookMapper;
import mate.academy.webapp.model.Book;
import mate.academy.webapp.model.Category;
import mate.academy.webapp.repository.CategoryRepository;
import mate.academy.webapp.repository.book.BookRepository;
import mate.academy.webapp.repository.book.builder.BookSpecificationBuilder;
import mate.academy.webapp.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookResponseDto save(CreateBookRequestDto requestDto) {
        Set<Category> categorySet = requestDto.getCategories().stream()
                .map(id -> categoryRepository.findById(id).orElseThrow(
                        () -> new EntityNotFoundException("Category with id " + id + " not found")))
                .collect(Collectors.toSet());
        Book book = bookMapper.toModel(requestDto);
        book.getCategories().addAll(categorySet);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public BookResponseDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookResponseDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BookResponseDto updateById(Long id, CreateBookRequestDto requestDto) {
        bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find and update book by id: " + id)
        );
        Book book = bookMapper.toModel(requestDto);
        book.setId(id);
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookResponseDto> searchBooks(BookSearchParametersDto searchParameters,
                                             Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification, pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
