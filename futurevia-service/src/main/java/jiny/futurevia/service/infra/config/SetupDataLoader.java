package jiny.futurevia.service.infra.config;

import jiny.futurevia.service.modules.account.domain.entity.Role;
import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import jiny.futurevia.service.modules.account.infra.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final String ROLE_USER = "ROLE_USER";

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        setupData();
        alreadySetup = true;
    }

    private void setupData() {
        Role role = roleRepository.findByRoleName(ROLE_USER);

        if (role != null) {
            return;
        }

        Role userRole = Role.builder()
                .roleName("ROLE_USER")
                .roleDesc("사용자")
                .build();

        Role adminRole = Role.builder()
                .roleName("ROLE_ADMIN")
                .roleDesc("관리자")
                .build();

        roleRepository.save(userRole);
        roleRepository.save(adminRole);
    }

}