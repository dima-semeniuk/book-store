package mate.academy.webapp.mapper;

import mate.academy.webapp.config.MapperConfig;
import mate.academy.webapp.dto.cartitem.CartItemRequestDto;
import mate.academy.webapp.dto.cartitem.CartItemResponseDto;
import mate.academy.webapp.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemResponseDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookById")
    CartItem toModel(CartItemRequestDto requestDto);
}
