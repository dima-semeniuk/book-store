package mate.academy.webapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.webapp.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.webapp.dto.category.CategoryResponseDto;
import mate.academy.webapp.dto.category.CreateCategoryRequestDto;
import mate.academy.webapp.exception.EntityNotFoundException;
import mate.academy.webapp.mapper.BookMapper;
import mate.academy.webapp.mapper.CategoryMapper;
import mate.academy.webapp.model.Book;
import mate.academy.webapp.model.Category;
import mate.academy.webapp.repository.CategoryRepository;
import mate.academy.webapp.repository.book.BookRepository;
import mate.academy.webapp.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 10L;
    private static Book book;
    private static Category category;
    private static Category categoryBeforeUpdating;
    private static Category savedCategory;
    private static Category updatedCategory;
    private static CreateCategoryRequestDto requestDto;
    private static CreateCategoryRequestDto updateRequestDto;
    private static CategoryResponseDto responseDto;
    private static CategoryResponseDto updatedResponseDto;
    private static BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeAll
    static void beforeAll() {
        category = new Category()
                .setName("Drama")
                .setDescription("Drama description");

        savedCategory = new Category()
                .setId(1L)
                .setName("Drama")
                .setDescription("Drama description");

        categoryBeforeUpdating = new Category()
                .setName("UpdatedDrama")
                .setDescription("UpdatedDrama description");

        updatedCategory = new Category()
                .setId(1L)
                .setName("UpdatedDrama")
                .setDescription("UpdatedDrama description");

        requestDto = new CreateCategoryRequestDto()
                .setName("Drama")
                .setDescription("Drama category");

        updateRequestDto = new CreateCategoryRequestDto()
                .setName("UpdatedDrama")
                .setDescription("UpdatedDrama category");

        responseDto = new CategoryResponseDto(
                1L,
                "Drama",
                "Drama description"
        );

        updatedResponseDto = new CategoryResponseDto(
                1L,
                "UpdatedDrama",
                "UpdatedDrama description"
        );

        book = new Book()
                .setId(1L)
                .setTitle("Book")
                .setAuthor("Author")
                .setIsbn("12345as")
                .setPrice(BigDecimal.valueOf(30))
                .setCategories(Set.of(category));

        bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setId(1L);
        bookDtoWithoutCategoryIds.setTitle("Book");
        bookDtoWithoutCategoryIds.setAuthor("Author");
        bookDtoWithoutCategoryIds.setIsbn("12345as");
        bookDtoWithoutCategoryIds.setPrice(BigDecimal.valueOf(30));
    }

    @Test
    @DisplayName("Save category to DataBase")
    public void save_ValidCreateCategoryRequestDto_ReturnCategoryResponseDto() {
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryMapper.toDto(savedCategory)).thenReturn(responseDto);

        CategoryResponseDto actual = categoryService.save(requestDto);
        assertEquals(responseDto, actual);
    }

    @Test
    @DisplayName("Find category by id")
    public void findById_ExistingId_ReturnCategoryResponseDto() {
        when(categoryRepository.findById(EXISTING_ID))
                .thenReturn(Optional.of(savedCategory));
        when(categoryMapper.toDto(savedCategory)).thenReturn(responseDto);

        CategoryResponseDto actual = categoryService.findById(EXISTING_ID);
        assertEquals(responseDto, actual);
    }

    @Test
    @DisplayName("Find category by not existing id")
    public void findById_NotExistingId_EntityNotFoundExceptionExpected() {
        when(categoryRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.findById(NOT_EXISTING_ID)
        );

        String expectedMessage = "Can't find category by id: " + NOT_EXISTING_ID;
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Get all categories")
    public void getAll_OneCategory_ReturnListOfBookResponseDto() {
        PageRequest pageRequest = PageRequest.of(0,20);
        when(categoryRepository.findAll(pageRequest))
                .thenReturn(new PageImpl<>(List.of(savedCategory)));
        when(categoryMapper.toDto(savedCategory)).thenReturn(responseDto);

        List<CategoryResponseDto> expected = List.of(responseDto);
        List<CategoryResponseDto> actual = categoryService.getAll(pageRequest);
        assertEquals(expected, actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    @DisplayName("Update category to by id")
    public void updateById_ExistingId_ReturnCategoryResponseDto() {
        when(categoryRepository.findById(EXISTING_ID))
                .thenReturn(Optional.of(savedCategory));
        when(categoryRepository.save(categoryBeforeUpdating)).thenReturn(updatedCategory);
        when(categoryMapper.toModel(updateRequestDto)).thenReturn(categoryBeforeUpdating);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(updatedResponseDto);

        CategoryResponseDto actual = categoryService.updateById(EXISTING_ID, updateRequestDto);
        assertEquals(updatedResponseDto, actual);
    }

    @Test
    @DisplayName("Update category by not existing id")
    public void updateById_NotExistingId_EntityNotFoundExceptionExpected() {
        when(categoryRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.updateById(NOT_EXISTING_ID, updateRequestDto)
        );

        String expectedMessage = "Can't find and update category by id: " + NOT_EXISTING_ID;
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Get all books by category id")
    public void findAllByCategoryId_ExistingId_ReturnListOfBookResponseDto() {
        PageRequest pageRequest = PageRequest.of(0,20);
        when(bookRepository.findAllByCategoryId(EXISTING_ID)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithoutCategoryIds);
        List<BookDtoWithoutCategoryIds> actual = categoryService
                .findAllByCategoryId(EXISTING_ID, pageRequest);
        assertEquals(expected, actual);
        assertEquals(expected.size(), actual.size());
    }
}
