package mate.academy.webapp.dto.book;

import java.math.BigDecimal;

public record BookSearchParametersDto(String[] authors, BigDecimal priceFrom, BigDecimal priceTo) {
}
