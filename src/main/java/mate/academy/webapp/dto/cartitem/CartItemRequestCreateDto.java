package mate.academy.webapp.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class CartItemRequestCreateDto {
    @NotNull(message = "can't be empty.")
    private Long bookId;
    @Positive(message = "must be greater than 0.")
    private Integer quantity;
}
