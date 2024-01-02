package mate.academy.webapp.mapper;

import mate.academy.webapp.config.MapperConfig;
import mate.academy.webapp.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.webapp.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}
