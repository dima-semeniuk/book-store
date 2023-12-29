package mate.academy.webapp.service;

import mate.academy.webapp.dto.cartitem.CartItemRequestDto;
import mate.academy.webapp.dto.cartitem.CartItemRequestUpdateDto;
import mate.academy.webapp.dto.cartitem.CartItemResponseDto;
import mate.academy.webapp.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.webapp.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCartDto(Long userId);

    void createNewShoppingCart(User user);

    void addBookToShoppingCart(CartItemRequestDto requestDto, Long userId);

    CartItemResponseDto updateQuantityOfBooksInShoppingCart(Long userId, Long id,
                                                   CartItemRequestUpdateDto requestUpdateDto);

    void deleteCartItemById(Long userId, Long id);
}
