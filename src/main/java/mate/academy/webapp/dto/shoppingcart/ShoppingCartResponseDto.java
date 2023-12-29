package mate.academy.webapp.dto.shoppingcart;

import java.util.Set;
import lombok.Data;
import mate.academy.webapp.dto.cartitem.CartItemResponseDto;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
