package mate.academy.webapp.service;

import mate.academy.webapp.dto.cartitem.CartItemRequestDto;
import mate.academy.webapp.model.ShoppingCart;

public interface CartItemService {
    void deleteById(Long id);

    void createCartItem(ShoppingCart shoppingCart, CartItemRequestDto requestDto);
}
