package app.config;

import app.model.Permission;
import app.model.Role;
import app.model.User;
import app.repository.PermissionRepository;
import app.repository.RoleRepository;
import app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            UserRepository userRepo,
            RoleRepository roleRepo,
            PermissionRepository permRepo,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // Only run if no users exist
            if (userRepo.count() > 0) {
                return;
            }

            // 1. Create base permissions
            Permission adminManage = permRepo.save(new Permission("admin.manage", "Full admin access"));
            Permission userManage = permRepo.save(new Permission("user.manage", "Manage users"));

            // 2. Create ADMIN role with permissions
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setDescription("System administrator");
            adminRole.setPermissions(Set.of(adminManage, userManage));
            adminRole = roleRepo.save(adminRole);

            // 3. Create the first admin user
            User admin = new User();
            admin.setName("Administrator");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123")); // change later
            admin.setRoles(Set.of(adminRole));

            userRepo.save(admin);

            System.out.println("✔ First admin user created: admin@example.com / admin123");
        };
    }
}
