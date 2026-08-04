package vn.edu.fpt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.entity.Category;
import vn.edu.fpt.repository.CategoryRepository;
import vn.edu.fpt.mapper.DtoMapper;
import vn.edu.fpt.dto.course.CategoryDto;

import java.util.*;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository repository;
    private final DtoMapper dtoMapper;

    public CategoryService(CategoryRepository categoryRepository, DtoMapper dtoMapper) {
        this.repository = categoryRepository;
        this.dtoMapper = dtoMapper;
    }

    private CategoryDto toSimpleDto(Category category, java.util.Map<Integer, Integer> courseCountMap) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .status(category.getStatus())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .courseCount(courseCountMap != null ? courseCountMap.getOrDefault(category.getId(), 0) : 0)
                .build();
    }

    public List<CategoryDto> getActiveParentCategories() {
        // 1. Fetch all active categories in a single query
        List<Category> allCategories = repository.findByStatus("ACTIVE");

        // 2. Fetch course counts for published courses grouped by category
        List<Object[]> counts = repository.findCourseCountsByCategoryStatus("ACTIVE");
        Map<Integer, Integer> courseCountMap = new HashMap<>();
        for (Object[] row : counts) {
            Integer catId = (Integer) row[0];
            Long count = (Long) row[1];
            courseCountMap.put(catId, count.intValue());
        }

        // 3. Map basic fields to DTOs in memory
        List<CategoryDto> parentDtos = new ArrayList<>();
        Map<Integer, CategoryDto> dtoMap = new HashMap<>();

        for (Category category : allCategories) {
            CategoryDto dto = CategoryDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .description(category.getDescription())
                    .status(category.getStatus())
                    .courseCount(courseCountMap.getOrDefault(category.getId(), 0))
                    .children(new LinkedHashSet<>())
                    .build();
            dtoMap.put(category.getId(), dto);
        }

        // 4. Build the hierarchy tree in memory (exactly 0 extra database queries)
        for (Category category : allCategories) {
            CategoryDto dto = dtoMap.get(category.getId());
            if (category.getParent() == null) {
                parentDtos.add(dto);
            } else {
                Integer parentId = category.getParent().getId();
                dto.setParentId(parentId);
                CategoryDto parentDto = dtoMap.get(parentId);
                if (parentDto != null) {
                    parentDto.getChildren().add(dto);
                }
            }
        }

        return parentDtos;
    }

    public List<Category> findAll() {
        return repository.findAll();
    }

    /////List of parent Category
    public List<CategoryDto> findByParentIsNullAndStatus(String status) {
        List<Category> categoryList = repository.findByParentIsNullAndStatus(status);
        List<CategoryDto> dto = new ArrayList<>();
        for (Category ca : categoryList) {
            dto.add(toSimpleDto(ca, null));
        }
        return dto;
    }

    ////List of Child Category
    public List<CategoryDto> findByParentIsNotNulAndStatus(String status) {
        List<Category> categoryDtoList = repository.findByParentIsNotNullAndStatus(status);
        List<CategoryDto> dto = new ArrayList<>();
        for (Category c : categoryDtoList) {
            dto.add(toSimpleDto(c, null));
        }
        return dto;
    }



    public Category findByIdAndStatus(Integer id, String status) {
        return repository.findByIdAndStatus(id, status)
                .orElseThrow(() -> new RuntimeException("Category not found or inactive"));
    }


    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public Category save(Category entity) {
        return repository.save(entity);
    }


}
