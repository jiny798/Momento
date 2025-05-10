package jiny.futurevia.service.modules.product.endpoint;

import jakarta.validation.Valid;
import jiny.futurevia.service.modules.product.application.CategoryService;
import jiny.futurevia.service.modules.product.application.FlavorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/category")
    public ResponseEntity<Void> createCategory(@RequestBody @Valid CategoryRequest request) throws Exception {
        categoryService.save(request.name);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    public record CategoryRequest(String name) {}
    public record CategoryResponse(String name) {}
}
