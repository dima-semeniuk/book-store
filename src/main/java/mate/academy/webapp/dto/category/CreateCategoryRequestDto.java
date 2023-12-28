package mate.academy.webapp.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    private static final String NOT_NULL = "can't be null.";
    @NotNull(message = NOT_NULL)
    private String name;
    private String description;
}
