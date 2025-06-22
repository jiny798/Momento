package jiny.futurevia.service.modules.account.infra.repository;

import jiny.futurevia.service.modules.account.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String name);

    @Override
    void delete(Role role);

}