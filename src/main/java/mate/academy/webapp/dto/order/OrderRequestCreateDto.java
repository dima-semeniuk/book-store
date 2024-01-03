package mate.academy.webapp.dto.order;

import jakarta.validation.constraints.NotBlank;

public record OrderRequestCreateDto(
        @NotBlank String shippingAddress) {
}
