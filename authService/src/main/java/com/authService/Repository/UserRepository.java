package com.authService.Repository;

import com.authService.Entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserCredentials,Long> {
    Optional<UserCredentials> findByUsername(String username);
}
