package jiny.futurevia.service.modules.product.endpoint;

import jakarta.validation.Valid;
import jiny.futurevia.service.modules.common.response.ApiResponse;
import jiny.futurevia.service.modules.product.application.FlavorService;
import jiny.futurevia.service.modules.product.infra.repository.FlavorRepository;
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
public class FlavorController {

    private final FlavorService flavorService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/flavors")
    public ApiResponse<Void> createFlavor(@RequestBody @Valid FlavorRequest request) throws Exception {
        flavorService.save(request.name);
        return ApiResponse.success();
    }

    @GetMapping("/flavors")
    public ApiResponse<List<FlavorResponse>> getFlavors() {
        return ApiResponse.success(flavorService.findAll());
    }

    public record FlavorRequest(String name) {
    }

    public record FlavorResponse(String name) {
    }
}
