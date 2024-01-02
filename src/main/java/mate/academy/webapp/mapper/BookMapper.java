package mate.academy.webapp.mapper;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.webapp.config.MapperConfig;
import mate.academy.webapp.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.webapp.dto.book.BookResponseDto;
import mate.academy.webapp.dto.book.CreateBookRequestDto;
import mate.academy.webapp.model.Book;
import mate.academy.webapp.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookResponseDto toDto(Book book);

    @AfterMapping
    default void setCategoriesIds(@MappingTarget BookResponseDto responseDto, Book book) {
        Set<Long> categoryIds = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        responseDto.setCategoryIds(categoryIds);
    }

    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Named("bookById")
    default Book bookById(Long id) {
        return Optional.ofNullable(id)
                .map(Book::new)
                .orElse(null);
    }
}
