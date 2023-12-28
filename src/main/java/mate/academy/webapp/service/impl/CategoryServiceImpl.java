package mate.academy.webapp.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.webapp.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.webapp.dto.category.CategoryResponseDto;
import mate.academy.webapp.dto.category.CreateCategoryRequestDto;
import mate.academy.webapp.exception.EntityNotFoundException;
import mate.academy.webapp.mapper.BookMapper;
import mate.academy.webapp.mapper.CategoryMapper;
import mate.academy.webapp.model.Category;
import mate.academy.webapp.repository.CategoryRepository;
import mate.academy.webapp.repository.book.BookRepository;
import mate.academy.webapp.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;

    @Override
    public List<CategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryResponseDto findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id: " + id)
        );
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto save(CreateCategoryRequestDto requestDto) {
        Category category = categoryRepository.save(categoryMapper.toModel(requestDto));
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryResponseDto updateById(Long id, CreateCategoryRequestDto requestDto) {
        categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find and update category by id: " + id)
        );
        Category category = categoryMapper.toModel(requestDto);
        category.setId(id);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findAllByCategoryId(categoryId).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
