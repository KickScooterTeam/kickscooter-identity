package com.softserve.identityservice.model;

import com.softserve.identitystarter.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AppUser {
    @Id
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    @Column(name = "blocked_status")
    private boolean isBlocked;
    @Column(name = "is_verified")
    private boolean isVerified;
    @Column(name = "verify_token")
    private UUID verifyToken;
    @CreatedDate
    private LocalDateTime created;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Role> role;
}
