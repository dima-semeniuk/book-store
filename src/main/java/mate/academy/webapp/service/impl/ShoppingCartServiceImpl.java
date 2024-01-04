package mate.academy.webapp.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.webapp.dto.cartitem.CartItemRequestCreateDto;
import mate.academy.webapp.dto.cartitem.CartItemRequestUpdateDto;
import mate.academy.webapp.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.webapp.exception.EntityNotFoundException;
import mate.academy.webapp.mapper.CartItemMapper;
import mate.academy.webapp.mapper.ShoppingCartMapper;
import mate.academy.webapp.model.CartItem;
import mate.academy.webapp.model.ShoppingCart;
import mate.academy.webapp.model.User;
import mate.academy.webapp.repository.CartItemRepository;
import mate.academy.webapp.repository.ShoppingCartRepository;
import mate.academy.webapp.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartResponseDto addBookToShoppingCart(CartItemRequestCreateDto requestDto,
                                                         Long userId) {
        ShoppingCart shoppingCart = findByUserId(userId);
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
        shoppingCart = findShoppingCartWithCartItemsAndBooksByUserId(userId);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void createNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCartDto(Long userId) {
        ShoppingCart shoppingCart = findShoppingCartWithCartItemsAndBooksByUserId(userId);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteCartItemById(Long userId, Long id) {
        ShoppingCart shoppingCart = findByUserId(userId);
        cartItemRepository.findByIdAndShoppingCartId(id, shoppingCart.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item by id: " + id)
        );
        cartItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateQuantityOfBooksInShoppingCart(
            Long userId, Long id, CartItemRequestUpdateDto requestUpdateDto) {
        ShoppingCart shoppingCart = findByUserId(userId);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id,
                shoppingCart.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Can't find and update cart "
                            + "item by id: " + id)
        );
        cartItem.setQuantity(requestUpdateDto.getQuantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private ShoppingCart findByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by user id: "
                        + userId));
    }

    private ShoppingCart findShoppingCartWithCartItemsAndBooksByUserId(Long userId) {
        return shoppingCartRepository
                .findShoppingCartWithCartItemsAndBooksByUserId(userId).orElseThrow(
                        () -> new EntityNotFoundException("Can't find shopping cart by user id: "
                                + userId));
    }
}
