package jiny.futurevia.service.modules.product.application;

import jiny.futurevia.service.modules.product.domain.Category;
import jiny.futurevia.service.modules.product.domain.Flavor;
import jiny.futurevia.service.modules.product.endpoint.CategoryController;
import jiny.futurevia.service.modules.product.endpoint.CategoryController.CategoryResponse;
import jiny.futurevia.service.modules.product.endpoint.FlavorController;
import jiny.futurevia.service.modules.product.infra.repository.CategoryRepository;
import jiny.futurevia.service.modules.product.infra.repository.FlavorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void save(String name) {

        Category category = categoryRepository.findByName(name);
        if (category != null) {
            throw new IllegalArgumentException("Category already exists");
        }
        category = Category.createCategory(name);
        categoryRepository.save(category);
    }

    public List<CategoryResponse> findAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse response = new CategoryResponse(category.getName());
            categoryResponses.add(response);
        }
        return categoryResponses;
    }
}
