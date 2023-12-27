package mate.academy.webapp.mapper;

import mate.academy.webapp.config.MapperConfig;
import mate.academy.webapp.dto.category.CategoryResponseDto;
import mate.academy.webapp.dto.category.CreateCategoryRequestDto;
import mate.academy.webapp.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto requestDto);
}
