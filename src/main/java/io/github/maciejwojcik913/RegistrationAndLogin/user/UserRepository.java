package io.github.maciejwojcik913.RegistrationAndLogin.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.login = :expr OR u.email = :expr")
    Optional<User> findByLoginOrEmail(@Param("expr")String expression);
}