package com.booking.roome.repository;

import com.booking.roome.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> getAllByActive(boolean active);
    Optional<User> getUserByIdAndActive(int id, boolean active);
    User getUserByEmailAndPassword(String email, String password);
    User getUserByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
