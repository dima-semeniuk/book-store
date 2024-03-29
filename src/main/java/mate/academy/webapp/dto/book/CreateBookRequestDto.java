package mate.academy.webapp.dto.book;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateBookRequestDto {
    private static final String NOT_NULL = "can't be null.";
    private static final String GREATER_THAN_ZERO = "must be greater than 0.";
    @NotNull(message = NOT_NULL)
    private String title;
    @NotNull(message = NOT_NULL)
    private String author;
    @NotNull(message = NOT_NULL)
    private String isbn;
    @NotNull(message = NOT_NULL)
    @Positive(message = GREATER_THAN_ZERO)
    private BigDecimal price;
    @NotNull(message = NOT_NULL)
    private Set<Long> categories;
    private String description;
    private String coverImage;
}
