package mate.academy.webapp.dto.cartitem;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequestUpdateDto {
    @Positive(message = "must be greater than 0.")
    private Integer quantity;
}
