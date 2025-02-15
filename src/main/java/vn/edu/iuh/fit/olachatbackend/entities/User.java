package vn.edu.iuh.fit.olachatbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.edu.iuh.fit.olachatbackend.enums.Role;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String userId;

    String email;
    String phone;
    String username;
    String password;
    String fullName;

    @Enumerated(EnumType.STRING)
    Role role;
}
