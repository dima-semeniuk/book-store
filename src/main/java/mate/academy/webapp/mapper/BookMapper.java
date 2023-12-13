package mate.academy.webapp.mapper;

import mate.academy.webapp.config.MapperConfig;
import mate.academy.webapp.dto.book.BookDto;
import mate.academy.webapp.dto.book.CreateBookRequestDto;
import mate.academy.webapp.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
