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
import java.util.ArrayList;
import java.util.List;
import mate.academy.webapp.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.webapp.dto.category.CategoryResponseDto;
import mate.academy.webapp.dto.category.CreateCategoryRequestDto;
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
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static final Long INDEX_OF_EXISTING_CATEGORY = 1L;
    private static final Long INDEX_OF_UPDATING_CATEGORY = 2L;
    private static final Long INDEX_OF_NOT_EXISTING_CATEGORY = 20L;
    private static final int EXPECTED_LIST_SIZE = 2;
    private static CreateCategoryRequestDto requestDto;
    private static CreateCategoryRequestDto emptyCategoryNameRequestDto;
    private static CreateCategoryRequestDto updateExistingCategoryRequestDto;
    private static CategoryResponseDto responseDto;
    private static CategoryResponseDto updatingResponseDto;
    private static CategoryResponseDto responseDtoIdOne;
    private static List<BookDtoWithoutCategoryIds> responseDtosWithoutCategories;

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

        requestDto = new CreateCategoryRequestDto()
                .setName("Fantasy")
                .setDescription("Fantasy category");

        emptyCategoryNameRequestDto = new CreateCategoryRequestDto()
                .setDescription("Some description");

        updateExistingCategoryRequestDto = new CreateCategoryRequestDto()
                .setName("Horror")
                .setDescription("Horror category");

        responseDto = new CategoryResponseDto(
                10L,
                requestDto.getName(),
                requestDto.getDescription());

        updatingResponseDto = new CategoryResponseDto(INDEX_OF_UPDATING_CATEGORY,
                updateExistingCategoryRequestDto.getName(),
                updateExistingCategoryRequestDto.getDescription());

        responseDtoIdOne = new CategoryResponseDto(
                1L,
                "Classic",
                "Classic category");

        responseDtosWithoutCategories = new ArrayList<>();
        responseDtosWithoutCategories.add(new BookDtoWithoutCategoryIds()
                .setId(2L).setTitle("About fishing")
                .setAuthor("Author2")
                .setIsbn("123334vf")
                .setPrice(BigDecimal.valueOf(30)));
        responseDtosWithoutCategories.add(new BookDtoWithoutCategoryIds()
                .setId(3L).setTitle("Traveling")
                .setAuthor("Author3")
                .setIsbn("123345se")
                .setPrice(BigDecimal.valueOf(29)));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get category by existing id")
    void getCategoryById_ExistingId_Success() throws Exception {

        MvcResult result = mockMvc.perform(
                        get("/categories/{id}", INDEX_OF_EXISTING_CATEGORY))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryResponseDto.class);

        assertNotNull(actual);
        assertEquals(responseDtoIdOne, actual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get category by not existing id")
    void getCategoryById_NotExistingId_NotFound() throws Exception {

        MvcResult result = mockMvc.perform(
                        get("/categories/{id}", INDEX_OF_NOT_EXISTING_CATEGORY))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get all categories")
    void getAll_Success() throws Exception {

        MvcResult result = mockMvc.perform(
                        get("/categories"))
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryResponseDto> actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), new TypeReference<List<CategoryResponseDto>>() {});

        assertEquals(EXPECTED_LIST_SIZE, actual.size());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get books by existing category id")
    void getBooksByCategoryId_ExistingId_Success() throws Exception {

        MvcResult result = mockMvc.perform(
                        get("/categories/{id}/books", INDEX_OF_EXISTING_CATEGORY))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDtoWithoutCategoryIds> actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(),
                new TypeReference<List<BookDtoWithoutCategoryIds>>() {});

        assertNotNull(actual);
        assertEquals(responseDtosWithoutCategories.size(), actual.size());
        assertEquals(responseDtosWithoutCategories, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new category, empty category name")
    void createCategory_NotValidRequestDto_BadRequest() throws Exception {

        String jsonRequest = objectMapper.writeValueAsString(emptyCategoryNameRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new category")
    void createCategory_ValidRequestDto_Success() throws Exception {

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryResponseDto.class);

        assertNotNull(actual);
        assertNotNull(actual.id());
        assertTrue(EqualsBuilder.reflectionEquals(responseDto, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category by id")
    void updateCategory_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(updateExistingCategoryRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/categories/{id}", INDEX_OF_UPDATING_CATEGORY)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryResponseDto.class);

        assertNotNull(actual);
        assertEquals(updatingResponseDto, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category by not existing id")
    void updateCategory_NotExistingId_NotFound() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(updateExistingCategoryRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/categories/{id}", INDEX_OF_NOT_EXISTING_CATEGORY)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category, empty category name")
    void updateCategory_NotValidRequestDto_BadRequest() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(emptyCategoryNameRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/categories/{id}", INDEX_OF_UPDATING_CATEGORY)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete category by id")
    void deleteCategoryById_ExistingId_NoConnect() throws Exception {
        mockMvc.perform(
                        delete("/categories/{id}", INDEX_OF_UPDATING_CATEGORY)
                )
                .andExpect(status().isNoContent());
    }
}
