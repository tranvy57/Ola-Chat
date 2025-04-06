package vn.edu.iuh.fit.olachatbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue
    private UUID id;

    private String tokenHash;
    private String deviceId;
    private Instant expiryDate;
    private boolean revoked = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
