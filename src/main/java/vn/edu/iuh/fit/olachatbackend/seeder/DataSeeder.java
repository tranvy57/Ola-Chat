//package vn.edu.iuh.fit.olachatbackend.seeder;
//
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import vn.edu.iuh.fit.olachatbackend.entities.User;
//import vn.edu.iuh.fit.olachatbackend.enums.AuthProvider;
//import vn.edu.iuh.fit.olachatbackend.enums.Role;
//import vn.edu.iuh.fit.olachatbackend.enums.UserStatus;
//import vn.edu.iuh.fit.olachatbackend.repositories.UserRepository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class DataSeeder {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @PostConstruct
//    public void seedUsers() {
//        if (userRepository.count() == 0) {
//            List<User> users = List.of(
//                    User.builder()
//                            .id("11111111-1111-1111-1111-111111111111")
//                            .username("0365962232")
//                            .email("ntanhquan.sly@gmail.com")
//                            .password(passwordEncoder.encode("0365962232"))
//                            .displayName("Nguyễn Quân")
//                            .role(Role.ADMIN)
//                            .authProvider(AuthProvider.LOCAL)
//                            .status(UserStatus.ACTIVE)
//                            .createdAt(LocalDateTime.now())
//                            .build(),
//
//                    User.builder()
//                            .id("22222222-2222-2222-2222-222222222222")
//                            .username("0962527550")
//                            .email("tranvy.art@gmail.com")
//                            .password(passwordEncoder.encode("0962527550"))
//                            .displayName("Thúy Vy Cute")
//                            .role(Role.ADMIN)
//                            .authProvider(AuthProvider.LOCAL)
//                            .status(UserStatus.ACTIVE)
//                            .createdAt(LocalDateTime.now())
//                            .build(),
//
//                    User.builder()
//                            .id("33333333-3333-3333-3333-333333333333")
//                            .username("0901407421")
//                            .email("0901407421@0901407421.com")
//                            .password(passwordEncoder.encode("0901407421"))
//                            .displayName("Thanh Nhứt DZ")
//                            .role(Role.USER)
//                            .authProvider(AuthProvider.LOCAL)
//                            .status(UserStatus.ACTIVE)
//                            .createdAt(LocalDateTime.now())
//                            .build(),
//
//                    User.builder()
//                            .id("44444444-4444-4444-4444-444444444444")
//                            .username("0349559593")
//                            .email("0349559593@0349559593.com")
//                            .password(passwordEncoder.encode("0349559593"))
//                            .displayName("Tấn Phát DZ")
//                            .role(Role.ADMIN)
//                            .authProvider(AuthProvider.LOCAL)
//                            .status(UserStatus.ACTIVE)
//                            .createdAt(LocalDateTime.now())
//                            .build(),
//
//                    User.builder()
//                            .id("55555555-5555-5555-5555-555555555555")
//                            .username("0767459058")
//                            .email("0767459058@0767459058.com")
//                            .password(passwordEncoder.encode("0767459058"))
//                            .displayName("Bảo Thông DZ")
//                            .role(Role.ADMIN)
//                            .authProvider(AuthProvider.LOCAL)
//                            .status(UserStatus.ACTIVE)
//                            .createdAt(LocalDateTime.now())
//                            .build()
//            );
//
//            for (User user : users) {
//                boolean exists = userRepository.existsByUsername(user.getUsername());
//                if (!exists) {
//                    userRepository.save(user);
//                    System.out.println("✅ Inserted user: " + user.getUsername());
//                } else {
//                    System.out.println("⚠️ User already exists: " + user.getUsername());
//                }
//            }
//        }
//    }
//}
