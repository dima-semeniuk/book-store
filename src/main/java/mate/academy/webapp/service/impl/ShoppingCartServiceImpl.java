package mate.academy.webapp.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.webapp.dto.cartitem.CartItemRequestDto;
import mate.academy.webapp.dto.cartitem.CartItemRequestUpdateDto;
import mate.academy.webapp.dto.cartitem.CartItemResponseDto;
import mate.academy.webapp.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.webapp.exception.EntityNotFoundException;
import mate.academy.webapp.mapper.CartItemMapper;
import mate.academy.webapp.mapper.ShoppingCartMapper;
import mate.academy.webapp.model.CartItem;
import mate.academy.webapp.model.ShoppingCart;
import mate.academy.webapp.model.User;
import mate.academy.webapp.repository.CartItemRepository;
import mate.academy.webapp.repository.ShoppingCartRepository;
import mate.academy.webapp.service.CartItemService;
import mate.academy.webapp.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemService cartItemService;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public void addBookToShoppingCart(CartItemRequestDto requestDto, Long userId) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);
        cartItemService.createCartItem(shoppingCart, requestDto);
    }

    @Override
    public void createNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCartDto(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findShoppingCartWithCartItemsAndBooksByUserId(userId).orElseThrow(
                        () -> new EntityNotFoundException("Can't find shopping cart by user id: "
                                + userId)
                );
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteCartItemById(Long userId, Long id) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);
        cartItemRepository.findByIdAndShoppingCartId(id, shoppingCart.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item by id: " + id)
        );
        cartItemService.deleteById(id);
    }

    @Override
    public CartItemResponseDto updateQuantityOfBooksInShoppingCart(
            Long userId, Long id, CartItemRequestUpdateDto requestUpdateDto) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id,
                shoppingCart.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Can't find and update cart "
                            + "item by id: " + id)
        );
        cartItem.setQuantity(requestUpdateDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    private ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by user id: "
                        + userId)
        );
    }
}
