package mate.academy.webapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.webapp.dto.cartitem.CartItemRequestDto;
import mate.academy.webapp.dto.cartitem.CartItemRequestUpdateDto;
import mate.academy.webapp.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.webapp.model.User;
import mate.academy.webapp.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing user's shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new category", description = "Create new category")
    public ShoppingCartResponseDto addBookToShoppingCart(Authentication authentication,
                                      @RequestBody @Valid CartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addBookToShoppingCart(requestDto, user.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get user's shopping cart", description = "Get user's shopping cart")
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCartDto(user.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cart-items/{id}")
    @Operation(summary = "Update book's quantity by cart item ID",
            description = "Update book's quantity by cart item ID")
    public ShoppingCartResponseDto updateQuantityOfBooksInShoppingCart(
            Authentication authentication, @PathVariable Long id,
            @RequestBody @Valid CartItemRequestUpdateDto requestUpdateDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateQuantityOfBooksInShoppingCart(user.getId(), id,
                requestUpdateDto);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cart-items/{id}")
    @Operation(summary = "Delete cart item by ID", description = "Delete cart item from "
            + "shopping cart by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication authentication, @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.deleteCartItemById(user.getId(), id);
    }
}
