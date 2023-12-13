package mate.academy.webapp.dto.book;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    private static final String NOT_NULL = "can't be null.";
    private static final String GREATER_THAN_ZERO = "must be greater than 0.";

    @NotNull(message = NOT_NULL)
    private String title;
    @NotNull(message = NOT_NULL)
    private String author;
    @NotNull(message = NOT_NULL)
    private String isbn;
    @NotNull(message = GREATER_THAN_ZERO)
    @Positive
    private BigDecimal price;
    private String description;
    private String coverImage;
}
