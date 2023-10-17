package com.booking.roome.repository;

import com.booking.roome.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> getUserByUsernameOrEmailAndPassword(String email, String username, String password);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
