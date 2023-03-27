package io.github.maciejwojcik913.RegistrationAndLogin.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<UserRole, Long> {

    Optional<UserRole> findByName(String name);

    default UserRole getDefaultRole() {
        return findByName("ROLE_USER")
                .orElse(save(new UserRole("USER_ROLE")));
    }
}
