package mate.academy.webapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import mate.academy.webapp.model.ShoppingCart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    private static final Long USER_ID = 2L;
    private static final String EXPECTED_USER_FIRST_NAME = "Adam";
    private static final String EXPECTED_USER_EMAIL = "user@gmail.com";
    private static final int EXPECTED_CART_ITEMS = 2;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Find shopping cart with cart items and books by user id")
    @Sql(scripts = {
            "classpath:database/books/add-books-to-book-table.sql",
            "classpath:database/categories/add-categories-to-categories-table.sql",
            "classpath:database/books-categories/add-books-categories-relations.sql",
            "classpath:database/users/add-user-to-users-table.sql",
            "classpath:database/shopping-carts/add-shopping-cart-to-shopping-carts-table.sql",
            "classpath:database/cart-items/add-cart-items-to-cart-items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/cart-items/delete-cart-items-from-cart-items-table.sql",
            "classpath:database/books/delete-books-from-book-table.sql",
            "classpath:database/categories/delete-categories-from-categories-table.sql",
            "classpath:database/books-categories/delete-books-categories-relations.sql",
            "classpath:database/shopping-carts/delete-shopping-carts-from-sh-carts-table.sql",
            "classpath:database/users/delete-users-from-users-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findShoppingCartWithCartItemsAndBooksByUserId_UserIdOne_ReturnShoppingCart() {

        Optional<ShoppingCart> actualOptional = shoppingCartRepository
                .findShoppingCartWithCartItemsAndBooksByUserId(USER_ID);
        ShoppingCart actual = actualOptional.get();

        assertNotNull(actual);
        assertEquals(EXPECTED_USER_EMAIL, actual.getUser().getEmail());
        assertEquals(EXPECTED_USER_FIRST_NAME, actual.getUser().getFirstName());
        assertEquals(EXPECTED_CART_ITEMS, actual.getCartItems().size());
    }
}
