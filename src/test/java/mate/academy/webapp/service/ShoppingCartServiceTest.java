package mate.academy.webapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import mate.academy.webapp.dto.cartitem.CartItemRequestCreateDto;
import mate.academy.webapp.dto.cartitem.CartItemRequestUpdateDto;
import mate.academy.webapp.dto.cartitem.CartItemResponseDto;
import mate.academy.webapp.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.webapp.exception.EntityNotFoundException;
import mate.academy.webapp.mapper.CartItemMapper;
import mate.academy.webapp.mapper.ShoppingCartMapper;
import mate.academy.webapp.model.Book;
import mate.academy.webapp.model.CartItem;
import mate.academy.webapp.model.Category;
import mate.academy.webapp.model.ShoppingCart;
import mate.academy.webapp.model.User;
import mate.academy.webapp.repository.CartItemRepository;
import mate.academy.webapp.repository.ShoppingCartRepository;
import mate.academy.webapp.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    private static final Long NOT_EXISTING_CART_ITEM_ID = 50L;
    private static Category category;
    private static Book book;
    private static User user;
    private static ShoppingCart shoppingCart;
    private static ShoppingCart shoppingCartWithItems;
    private static CartItem cartItem;
    private static CartItem savedCartItem;
    private static CartItemRequestCreateDto itemRequestDto;
    private static CartItemRequestUpdateDto updateItemRequestDto;
    private static ShoppingCartResponseDto responseDto;
    private static ShoppingCartResponseDto updatedResponseDto;
    private static CartItemResponseDto itemResponseDto;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeAll
    static void beforeAll() {
        category = new Category()
                 .setId(1L)
                .setName("Drama")
                .setDescription("Drama description");

        book = new Book()
                .setId(1L)
                .setTitle("Book")
                .setAuthor("Author")
                .setIsbn("12345as")
                .setPrice(BigDecimal.valueOf(30))
                .setCategories(Set.of(category));

        user = new User()
                .setId(2L)
                .setEmail("user@gmail.com")
                .setPassword("$2a$10$dal35LUiS7lorGdjPF2z0ed4Dk7XYFOdHig/rqPdVWhPTuMuBxb1K")
                .setFirstName("Adam")
                .setLastName("Smith")
                .setShippingAddress("Sme address, 100");

        shoppingCart = new ShoppingCart()
                .setId(1L)
                .setUser(user);

        cartItem = new CartItem()
                .setShoppingCart(shoppingCart)
                .setBook(book);

        savedCartItem = new CartItem()
                .setId(1L)
                .setShoppingCart(shoppingCart)
                .setBook(book);

        shoppingCartWithItems = new ShoppingCart()
                .setId(1L)
                .setUser(user)
                .setCartItems(Set.of(cartItem));

        itemRequestDto = new CartItemRequestCreateDto()
                .setBookId(1L)
                .setQuantity(5);

        itemResponseDto = new CartItemResponseDto()
                .setId(1L)
                .setBookId(1L)
                .setBookTitle("Book")
                .setQuantity(5);

        responseDto = new ShoppingCartResponseDto()
                .setId(1L)
                .setUserId(2L)
                .setCartItems(Set.of(itemResponseDto));

        updateItemRequestDto = new CartItemRequestUpdateDto();
        updateItemRequestDto.setQuantity(7);

        updatedResponseDto = new ShoppingCartResponseDto()
                .setId(1L)
                .setUserId(2L)
                .setCartItems(Set.of(itemResponseDto
                        .setQuantity(updateItemRequestDto.getQuantity())));
    }

    @Test
    @DisplayName("Add book to shopping cart")
    public void addBookToShoppingCart_ValidItemRequestCreateDto_ReturnShoppingCartResponseDto() {
        Mockito.when(shoppingCartRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        Mockito.when(cartItemMapper.toModel(itemRequestDto)).thenReturn(cartItem);
        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(savedCartItem);
        Mockito.when(shoppingCartRepository
                .findShoppingCartWithCartItemsAndBooksByUserId(user.getId()))
                        .thenReturn(Optional.of(shoppingCartWithItems));
        Mockito.when(shoppingCartMapper.toDto(shoppingCartWithItems)).thenReturn(responseDto);

        ShoppingCartResponseDto actual = shoppingCartService
                .addBookToShoppingCart(itemRequestDto, user.getId());
        assertNotNull(actual);
        assertEquals(responseDto, actual);
    }

    @Test
    @DisplayName("Get user shopping cart with cart items")
    public void getShoppingCartDto_ExistingId_ReturnShoppingCartResponseDto() {
        Mockito.when(shoppingCartRepository.findShoppingCartWithCartItemsAndBooksByUserId(
                user.getId()))
                .thenReturn(Optional.of(shoppingCartWithItems));
        Mockito.when(shoppingCartMapper.toDto(shoppingCartWithItems)).thenReturn(responseDto);

        ShoppingCartResponseDto actual = shoppingCartService.getShoppingCartDto(user.getId());
        assertEquals(responseDto, actual);
    }

    @Test
    @DisplayName("Update quantity of books in shopping cart")
    public void updateQuantityOfBooksInShoppingCart_ValidRequestDto_ReturnCartResponseDto() {
        Mockito.when(shoppingCartRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        Mockito.when(cartItemRepository.findByIdAndShoppingCartId(savedCartItem.getId(),
                        shoppingCart.getId())).thenReturn(Optional.of(savedCartItem));
        Mockito.when(cartItemRepository.save(savedCartItem))
                .thenReturn(savedCartItem.setQuantity(updateItemRequestDto.getQuantity()));
        Mockito.when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(updatedResponseDto);

        ShoppingCartResponseDto actual = shoppingCartService
                .updateQuantityOfBooksInShoppingCart(user.getId(), savedCartItem.getId(),
                       updateItemRequestDto);
        assertEquals(updatedResponseDto, actual);
    }

    @Test
    @DisplayName("Update quantity of book, not existing cart item id")
    public void updateQuantityOfBooks_NotExistCartItemId_EntityNotFoundExceptionExpected() {
        Mockito.when(shoppingCartRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        Mockito.when(cartItemRepository.findByIdAndShoppingCartId(NOT_EXISTING_CART_ITEM_ID,
                shoppingCart.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartService.updateQuantityOfBooksInShoppingCart(
                        user.getId(), NOT_EXISTING_CART_ITEM_ID, updateItemRequestDto)
        );

        String expectedMessage = "Can't find and update cart item by id: "
                + NOT_EXISTING_CART_ITEM_ID;
        assertEquals(expectedMessage, exception.getMessage());
    }
}
