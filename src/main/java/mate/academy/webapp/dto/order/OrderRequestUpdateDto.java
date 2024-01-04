package mate.academy.webapp.dto.order;

import jakarta.validation.constraints.NotNull;
import mate.academy.webapp.model.Order;

public record OrderRequestUpdateDto(
        @NotNull(message = "can't be empty.") Order.Status status) {
}
