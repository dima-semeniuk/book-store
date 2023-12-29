package mate.academy.webapp.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.webapp.dto.cartitem.CartItemRequestDto;
import mate.academy.webapp.mapper.CartItemMapper;
import mate.academy.webapp.model.CartItem;
import mate.academy.webapp.model.ShoppingCart;
import mate.academy.webapp.repository.CartItemRepository;
import mate.academy.webapp.repository.book.BookRepository;
import mate.academy.webapp.service.CartItemService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public void createCartItem(ShoppingCart shoppingCart, CartItemRequestDto requestDto) {
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
    }
}
