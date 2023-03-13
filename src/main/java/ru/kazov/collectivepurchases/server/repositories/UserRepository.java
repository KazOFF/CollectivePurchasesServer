package ru.kazov.collectivepurchases.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kazov.collectivepurchases.server.models.dao.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
