package mate.academy.webapp.dto.book;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private Set<Long> categoryIds;
    private String description;
    private String coverImage;
}
