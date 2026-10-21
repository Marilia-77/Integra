package com.integra.demo.security;

import com.integra.demo.entities.Usuario;
import com.integra.demo.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class AdminInitializer {

    @Bean
    CommandLineRunner criarAdmin(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByEmail("admin@integra.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail("admin@integra.com");
                admin.setSenha(encoder.encode("admin123"));
                admin.setRole("ADMIN");
                repo.save(admin);
            }
        };
    }
}