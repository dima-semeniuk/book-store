package mate.academy.webapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.webapp.dto.book.BookResponseDto;
import mate.academy.webapp.dto.book.CreateBookRequestDto;
import mate.academy.webapp.exception.EntityNotFoundException;
import mate.academy.webapp.mapper.BookMapper;
import mate.academy.webapp.model.Book;
import mate.academy.webapp.model.Category;
import mate.academy.webapp.repository.CategoryRepository;
import mate.academy.webapp.repository.book.BookRepository;
import mate.academy.webapp.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 100L;
    private static Book book;
    private static Book bookBeforeUpdate;
    private static Book updatedBook;
    private static Book savedBook;
    private static Category category;
    private static CreateBookRequestDto requestDto;
    private static CreateBookRequestDto updateRequestDto;
    private static CreateBookRequestDto requestDtoWithNotExistingCategoryId;
    private static BookResponseDto responseDto;
    private static BookResponseDto updatedResponseDto;
    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeAll
    static void beforeAll() {
        category = new Category()
                .setId(1L)
                .setName("Drama")
                .setDescription("Drama description");

        book = new Book()
                .setTitle("Book")
                .setAuthor("Author")
                .setIsbn("12345as")
                .setPrice(BigDecimal.valueOf(30));

        savedBook = new Book()
                .setId(1L)
                .setTitle("Book")
                .setAuthor("Author")
                .setIsbn("12345as")
                .setPrice(BigDecimal.valueOf(30))
                .setCategories(Set.of(category));

        bookBeforeUpdate = new Book()
                .setTitle("UpdatedBook")
                .setAuthor("UpdatedAuthor")
                .setIsbn("12345as")
                .setPrice(BigDecimal.valueOf(35));

        updatedBook = new Book()
                .setId(1L)
                .setTitle("UpdatedBook")
                .setAuthor("UpdatedAuthor")
                .setIsbn("12345as")
                .setPrice(BigDecimal.valueOf(35))
                .setCategories(Set.of(category));

        requestDto = new CreateBookRequestDto()
                .setTitle("Book")
                .setAuthor("Author")
                .setIsbn("12345as")
                .setPrice(BigDecimal.valueOf(30))
                .setCategories(Set.of(1L));

        updateRequestDto = new CreateBookRequestDto()
                .setTitle("UpdatedBook")
                .setAuthor("UpdatedAuthor")
                .setIsbn("12345as")
                .setPrice(BigDecimal.valueOf(35))
                .setCategories(Set.of(1L));

        requestDtoWithNotExistingCategoryId = new CreateBookRequestDto()
                .setTitle("Book")
                .setAuthor("Author")
                .setIsbn("12345as")
                .setPrice(BigDecimal.valueOf(30))
                .setCategories(Set.of(10L));

        responseDto = new BookResponseDto()
                .setId(1L)
                .setTitle("Book")
                .setAuthor("Author")
                .setIsbn("12345as")
                .setPrice(BigDecimal.valueOf(30))
                .setCategoryIds(Set.of(1L));

        updatedResponseDto = new BookResponseDto()
                .setId(1L)
                .setTitle("UpdatedBook")
                .setAuthor("UpdatedAuthor")
                .setIsbn("12345as")
                .setPrice(BigDecimal.valueOf(35))
                .setCategoryIds(Set.of(1L));
    }

    @Test
    @DisplayName("Save book to DataBase")
    public void save_ValidCreateBookRequestDto_ReturnBookResponseDto() {
        Mockito.when(bookRepository.save(book)).thenReturn(savedBook);
        Mockito.when(categoryRepository.findAllById(requestDto.getCategories()))
                .thenReturn(List.of(category));
        Mockito.when(bookMapper.toModel(requestDto)).thenReturn(book);
        Mockito.when(bookMapper.toDto(savedBook)).thenReturn(responseDto);

        BookResponseDto actual = bookService.save(requestDto);
        assertEquals(responseDto, actual);
    }

    @Test
    @DisplayName("Save book to with not existing category id")
    public void save_NotExistingCategoryId_EntityNotFoundExceptionExpected() {
        Mockito.when(categoryRepository.findAllById(
                requestDtoWithNotExistingCategoryId.getCategories()))
                .thenReturn(Collections.emptyList());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                    () -> bookService.save(requestDtoWithNotExistingCategoryId)
        );

        String expectedMessage = "No categories with id "
                + requestDtoWithNotExistingCategoryId.getCategories();
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Find book by id")
    public void findById_ExistingId_ReturnBookResponseDto() {
        Mockito.when(bookRepository.findById(EXISTING_ID)).thenReturn(Optional.of(savedBook));
        Mockito.when(bookMapper.toDto(savedBook)).thenReturn(responseDto);

        BookResponseDto actual = bookService.findById(EXISTING_ID);
        assertEquals(responseDto, actual);
    }

    @Test
    @DisplayName("Find book by not existing id")
    public void findById_NotExistingId_EntityNotFoundExceptionExpected() {
        Mockito.when(bookRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(NOT_EXISTING_ID)
        );

        String expectedMessage = "Can't find book by id: " + NOT_EXISTING_ID;
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Get all books")
    public void getAll_OneBook_ReturnListOfBookResponseDto() {
        PageRequest pageRequest = PageRequest.of(0,20);
        Mockito.when(bookRepository.findAll(pageRequest))
                .thenReturn(new PageImpl<>(List.of(savedBook)));
        Mockito.when(bookMapper.toDto(savedBook)).thenReturn(responseDto);

        List<BookResponseDto> expected = List.of(responseDto);
        List<BookResponseDto> actual = bookService.getAll(pageRequest);
        assertEquals(expected, actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    @DisplayName("Update book to by id")
    public void updateById_ExistingId_ReturnBookResponseDto() {
        Mockito.when(bookRepository.findById(EXISTING_ID)).thenReturn(Optional.of(savedBook));
        Mockito.when(categoryRepository.findAllById(updateRequestDto.getCategories()))
                .thenReturn(List.of(category));
        Mockito.when(bookRepository.save(bookBeforeUpdate)).thenReturn(updatedBook);
        Mockito.when(bookMapper.toModel(updateRequestDto)).thenReturn(bookBeforeUpdate);
        Mockito.when(bookMapper.toDto(updatedBook)).thenReturn(updatedResponseDto);

        BookResponseDto actual = bookService.updateById(EXISTING_ID, updateRequestDto);
        assertEquals(updatedResponseDto, actual);
    }

    @Test
    @DisplayName("Update book to by not existing id")
    public void updateById_NotExistingId_EntityNotFoundExceptionExpected() {
        Mockito.when(bookRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.updateById(NOT_EXISTING_ID, updateRequestDto)
        );

        String expectedMessage = "Can't find and update book by id: " + NOT_EXISTING_ID;
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Update book to by not existing category id")
    public void updateById_NotValidUpdateRequestDto_EntityNotFoundExceptionExpected() {
        Mockito.when(bookRepository.findById(EXISTING_ID)).thenReturn(Optional.of(savedBook));
        Mockito.when(categoryRepository.findAllById(
                requestDtoWithNotExistingCategoryId.getCategories()))
                .thenReturn(Collections.emptyList());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.updateById(EXISTING_ID, requestDtoWithNotExistingCategoryId)
        );

        String expectedMessage = "No categories with id "
                + requestDtoWithNotExistingCategoryId.getCategories();
        assertEquals(expectedMessage, exception.getMessage());
    }
}
