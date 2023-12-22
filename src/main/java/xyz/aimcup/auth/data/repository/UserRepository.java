package xyz.aimcup.auth.data.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.aimcup.auth.data.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
