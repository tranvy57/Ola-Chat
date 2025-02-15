package vn.edu.iuh.fit.olachatbackend.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.edu.iuh.fit.olachatbackend.entities.Message;
import vn.edu.iuh.fit.olachatbackend.entities.User;
import vn.edu.iuh.fit.olachatbackend.enums.Role;
import vn.edu.iuh.fit.olachatbackend.repositories.MessageRepository;
import vn.edu.iuh.fit.olachatbackend.repositories.UserRepository;

@Configuration
public class ApplicationInitConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) { // Được khởi chạy mỗi khi application start
        return args -> {
            // tạo một user admin
            if (userRepository.findByUsername("admin").isEmpty()) {

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role(Role.ADMIN)
                        .build();

                userRepository.save(user);
            }
        };
    }

    @Bean
    CommandLineRunner initData(MessageRepository messageRepository) {
        return args -> {
            if (messageRepository.count() == 0) {
                Message message = Message.builder()
                        .content("Test insert message from Zy cute")
                        .build();

                messageRepository.save(message);
                System.out.println("Inserted a test message into MongoDB!");
            }
        };
    }
}