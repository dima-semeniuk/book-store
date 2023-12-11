package mate.academy.webapp.dto.book;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotNull(message = "can't be null.")
    private String title;
    @NotNull(message = "can't be null.")
    private String author;
    @NotNull(message = "can't be null.")
    private String isbn;
    @NotNull(message = "can't be null.")
    @Positive
    private BigDecimal price;
    private String description;
    private String coverImage;
}
