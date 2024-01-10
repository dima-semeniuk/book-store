package mate.academy.webapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import mate.academy.webapp.dto.book.BookResponseDto;
import mate.academy.webapp.dto.book.BookSearchParametersDto;
import mate.academy.webapp.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@Sql(scripts = {"classpath:database/books/add-books-to-book-table.sql",
        "classpath:database/categories/add-categories-to-categories-table.sql",
        "classpath:database/books-categories/add-books-categories-relations.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:database/books/delete-books-from-book-table.sql",
        "classpath:database/categories/delete-categories-from-categories-table.sql",
        "classpath:database/books-categories/delete-books-categories-relations.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    private static final Long INDEX_OF_EXISTING_BOOK = 1L;
    private static final Long INDEX_OF_DELETING_BOOK = 2L;
    private static final Long INDEX_OF_UPDATING_BOOK = 3L;
    private static final int EXPECTED_LIST_SIZE = 3;
    private static final int EXPECTED_LIST_SIZE_BY_PARAM = 1;
    private static final Long INDEX_OF_NOT_EXISTING_BOOK = 20L;
    private static CreateBookRequestDto requestDto;
    private static BookSearchParametersDto searchRequestDto;
    private static CreateBookRequestDto updateExistingBookRequestDto;
    private static CreateBookRequestDto emptyAuthorBookRequestDto;
    private static BookResponseDto responseDto;
    private static BookResponseDto updatingResponseDto;
    private static BookResponseDto responseDtoIdOne;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

        updateExistingBookRequestDto = new CreateBookRequestDto()
                .setTitle("Traveling around the world")
                .setAuthor("Famous Author")
                .setIsbn("123345se")
                .setPrice(BigDecimal.valueOf(34))
                .setCategories(Set.of(1L));

        updatingResponseDto = new BookResponseDto()
                .setId(INDEX_OF_UPDATING_BOOK)
                .setAuthor(updateExistingBookRequestDto.getAuthor())
                .setTitle(updateExistingBookRequestDto.getTitle())
                .setIsbn(updateExistingBookRequestDto.getIsbn())
                .setPrice(updateExistingBookRequestDto.getPrice())
                .setCategoryIds(updateExistingBookRequestDto.getCategories());

        requestDto = new CreateBookRequestDto()
                .setTitle("Interesting book")
                .setAuthor("Author4")
                .setIsbn("13445rt")
                .setPrice(BigDecimal.valueOf(45))
                .setCategories(Set.of(1L));

        searchRequestDto = new BookSearchParametersDto(new String[]{"Author", "Author2"},
                BigDecimal.valueOf(35),
                BigDecimal.valueOf(40));

        emptyAuthorBookRequestDto = new CreateBookRequestDto()
                .setTitle("Interesting historical book")
                .setIsbn("124343as")
                .setPrice(BigDecimal.valueOf(35))
                .setCategories(Set.of(2L));

        responseDtoIdOne = new BookResponseDto()
                .setId(1L)
                .setAuthor("Author")
                .setTitle("History")
                .setIsbn("123321as")
                .setPrice(BigDecimal.valueOf(35))
                .setCategoryIds(Set.of(2L));

        responseDto = new BookResponseDto()
                .setId(10L)
                .setAuthor(requestDto.getAuthor())
                .setTitle(requestDto.getTitle())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setCategoryIds(requestDto.getCategories());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get book by existing id")
    void getBookById_ExistingId_Success() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/books/{id}", INDEX_OF_EXISTING_BOOK))
                .andExpect(status().isOk())
                .andReturn();

        BookResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookResponseDto.class);

        assertNotNull(actual);
        assertEquals(responseDtoIdOne, actual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get book by not existing id")
    void getBookById_NotExistingId_NotFound() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/books/{id}", INDEX_OF_NOT_EXISTING_BOOK))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get all books")
    void getAll_Success() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/books"))
                .andExpect(status().isOk())
                .andReturn();

        List<BookResponseDto> actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), new TypeReference<List<BookResponseDto>>() {});

        assertEquals(EXPECTED_LIST_SIZE, actual.size());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Search book by authors and price")
    void searchBooks_SearchParamOk_Success() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/books/search")
                        .param("authors", searchRequestDto.authors())
                        .param("priceFrom", String.valueOf(searchRequestDto.priceFrom()))
                        .param("priceTo", String.valueOf(searchRequestDto.priceTo()))
                )
                .andExpect(status().isOk())
                .andReturn();

        List<BookResponseDto> actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), new TypeReference<List<BookResponseDto>>() {});

        assertEquals(EXPECTED_LIST_SIZE_BY_PARAM, actual.size());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new book, empty author")
    void createBook_NotValidRequestDto_BadRequest() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(emptyAuthorBookRequestDto);

        MvcResult result = mockMvc.perform(
                post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new book")
    void createBook_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookResponseDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(responseDto, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update book by id")
    void updateBook_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(updateExistingBookRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/books/{id}", INDEX_OF_UPDATING_BOOK)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookResponseDto.class);

        assertNotNull(actual);
        assertEquals(updatingResponseDto, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update book by not existing id")
    void updateBook_NotExistingId_NotFound() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(updateExistingBookRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/books/{id}", INDEX_OF_NOT_EXISTING_BOOK)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update book, empty author")
    void updateBook_NotValidRequestDto_BadRequest() throws Exception {

        String jsonRequest = objectMapper.writeValueAsString(emptyAuthorBookRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/books/{id}", INDEX_OF_UPDATING_BOOK)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete book by id")
    void deleteBookById_ExistingId_NoConnect() throws Exception {
        mockMvc.perform(
                        delete("/books/{id}", INDEX_OF_DELETING_BOOK)
                )
                .andExpect(status().isNoContent());
    }
}
