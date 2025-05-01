package jiny.futurevia.service.modules.product.application;

import jiny.futurevia.service.modules.product.domain.Flavor;
import jiny.futurevia.service.modules.product.endpoint.FlavorController;
import jiny.futurevia.service.modules.product.endpoint.FlavorController.FlavorResponse;
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
public class FlavorService {
    private final FlavorRepository flavorRepository;

    @Transactional
    public void save(String name) {
        Flavor flavor = Flavor.from(name);
        flavorRepository.save(flavor);
    }

    public List<FlavorResponse> findAll() {
        List<Flavor> flavors = flavorRepository.findAll();
        List<FlavorResponse> flavorResponses = new ArrayList<>();
        for (Flavor flavor : flavors) {
            FlavorResponse response = new FlavorResponse(flavor.getName());
            flavorResponses.add(response);
        }
        return flavorResponses;
    }

}
