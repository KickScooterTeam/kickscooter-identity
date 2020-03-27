package com.softserve.identityservice.repository;

import com.softserve.identityservice.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findById(UUID id);
    boolean existsByEmail(String email);
    Optional<AppUser> findByVerifyToken(UUID token);
    @Transactional
    @Modifying
    @Query("update AppUser u set u.isBlocked=true where u.id = ?1")
    int updateBlockedStatus(UUID uuid);
}
