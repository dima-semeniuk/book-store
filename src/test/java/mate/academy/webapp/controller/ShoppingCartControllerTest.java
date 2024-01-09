package mate.academy.webapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.util.Set;
import mate.academy.webapp.dto.cartitem.CartItemRequestCreateDto;
import mate.academy.webapp.dto.cartitem.CartItemRequestUpdateDto;
import mate.academy.webapp.dto.cartitem.CartItemResponseDto;
import mate.academy.webapp.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.webapp.model.Role;
import mate.academy.webapp.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = {"classpath:database/books/add-books-to-book-table.sql",
        "classpath:database/categories/add-categories-to-categories-table.sql",
        "classpath:database/books-categories/add-books-categories-relations.sql",
        "classpath:database/users/add-user-to-users-table.sql",
        "classpath:database/shopping-carts/add-shopping-cart-to-shopping-carts-table.sql",
        "classpath:database/cart-items/add-cart-items-to-cart-items-table.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:database/cart-items/delete-cart-items-from-cart-items-table.sql",
        "classpath:database/books/delete-books-from-book-table.sql",
        "classpath:database/categories/delete-categories-from-categories-table.sql",
        "classpath:database/books-categories/delete-books-categories-relations.sql",
        "classpath:database/shopping-carts/delete-shopping-carts-from-sh-carts-table.sql",
        "classpath:database/users/delete-users-from-users-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    private static final Long CART_ITEM_INDEX = 1L;
    private static final Long INDEX_OF_NOT_EXISTING_CI = 50L;
    private static final int CART_ITEMS_COUNT = 2;
    private static Role userRole;
    private static User user;
    private static CartItemRequestCreateDto itemRequestDto;
    private static CartItemRequestCreateDto emptyBookItemRequestDto;
    private static CartItemRequestUpdateDto updateItemRequestDto;
    private static CartItemResponseDto itemResponseDto;
    private static CartItemResponseDto itemResponseDtoSecond;
    private static CartItemResponseDto itemResponseDtoThird;
    private static ShoppingCartResponseDto responseDto;

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

        userRole = new Role();
        userRole.setId(1L);
        userRole.setRoleName(Role.RoleName.USER);

        user = new User();
        user.setId(2L);
        user.setEmail("user@gmail.com");
        user.setPassword("$2a$10$dal35LUiS7lorGdjPF2z0ed4Dk7XYFOdHig/rqPdVWhPTuMuBxb1K");
        user.setFirstName("Adam");
        user.setLastName("Smith");
        user.setShippingAddress("Some address, 100");
        user.setRoles(Set.of(userRole));

        itemRequestDto = new CartItemRequestCreateDto()
                .setBookId(1L)
                .setQuantity(5);

        emptyBookItemRequestDto = new CartItemRequestCreateDto()
                .setQuantity(5);

        updateItemRequestDto = new CartItemRequestUpdateDto();
        updateItemRequestDto.setQuantity(10);

        itemResponseDto = new CartItemResponseDto()
                .setId(3L)
                .setBookId(1L)
                .setBookTitle("History")
                .setQuantity(5);

        itemResponseDtoSecond = new CartItemResponseDto()
                .setId(1L)
                .setBookId(1L)
                .setBookTitle("History")
                .setQuantity(3);
        itemResponseDtoThird = new CartItemResponseDto()
                .setId(2L)
                .setBookId(2L)
                .setBookTitle("About fishing")
                .setQuantity(2);

        responseDto = new ShoppingCartResponseDto()
                .setId(1L)
                .setUserId(2L)
                .setCartItems(Set.of(itemResponseDto, itemResponseDtoSecond, itemResponseDtoThird));
    }

    @BeforeEach
    void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Add book to shopping cart")
    void addBookToShoppingCart_ValidRequestDto_Success() throws Exception {

        String jsonRequest = objectMapper.writeValueAsString(itemRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/cart")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        ShoppingCartResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), ShoppingCartResponseDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(responseDto.getCartItems().size(), actual.getCartItems().size());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Add book to shopping cart, empty book")
    void addBookToShoppingCart_NotValidRequestDto_BadRequest() throws Exception {

        String jsonRequest = objectMapper.writeValueAsString(emptyBookItemRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/cart")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get user shopping cart with cart items")
    void getShoppingCart_Success() throws Exception {

        MvcResult result = mockMvc.perform(
                        get("/cart"))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartResponseDto.class);

        assertNotNull(actual);
        assertEquals(CART_ITEMS_COUNT, actual.getCartItems().size());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update quantity of books  by cart item id")
    void updateQuantityOfBooksInShoppingCart_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(updateItemRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/cart/cart-items/{id}", CART_ITEM_INDEX)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartResponseDto.class);
        int actualUpdatedQuantity = actual.getCartItems().stream()
                .filter(ci -> ci.getQuantity().equals(updateItemRequestDto.getQuantity()))
                .findFirst().get().getQuantity();

        assertNotNull(actual);
        assertEquals(updateItemRequestDto.getQuantity(), actualUpdatedQuantity);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update quantity by not existing cart item id")
    void updateQuantityOfBooksInShoppingCart_NotExistingId_NotFound() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(updateItemRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/cart/cart-items/{id}", INDEX_OF_NOT_EXISTING_CI)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
