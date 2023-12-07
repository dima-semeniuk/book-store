package mate.academy.webapp.dto;

import java.math.BigDecimal;

public record BookSearchParametersDto(String[] authors, BigDecimal priceFrom, BigDecimal priceTo) {
}
