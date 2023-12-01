package mate.academy.webapp.mapper;

import mate.academy.webapp.config.MapperConfig;
import mate.academy.webapp.dto.BookDto;
import mate.academy.webapp.dto.CreateBookRequestDto;
import mate.academy.webapp.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
