package mate.academy.webapp.service;

import java.util.List;
import mate.academy.webapp.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.webapp.dto.category.CategoryResponseDto;
import mate.academy.webapp.dto.category.CreateCategoryRequestDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryResponseDto> getAll(Pageable pageable);

    CategoryResponseDto findById(Long id);

    CategoryResponseDto save(CreateCategoryRequestDto requestDto);

    CategoryResponseDto updateById(Long id, CreateCategoryRequestDto requestDto);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable);
}
