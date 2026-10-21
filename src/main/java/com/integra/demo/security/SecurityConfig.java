package com.integra.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // habilita @PreAuthorize
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configure(http))
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 🔓 Rotas públicas
                .requestMatchers("/usuarios/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()  // 1º admin
                .requestMatchers("/aluno/login").permitAll()
                .requestMatchers("/aluno").permitAll()                       // ajuste conforme necessidade
                .requestMatchers("/professores").permitAll()
                .requestMatchers("/professores/login").permitAll()
                .requestMatchers("/nota/**", "/frequencia/**", "/ocorrencia/**",
                        "/materia/**", "/turma/**", "/recomendacoes/**").hasAnyRole("ADMIN", "PROFESSOR")

                // 🔒 Rotas protegidas (descomente quando estiver pronto)
                // .requestMatchers("/usuarios/**").hasRole("ADMIN")
                // .requestMatchers("/nota/**", "/frequencia/**").hasAnyRole("ADMIN","PROFESSOR")
                // .requestMatchers("/ocorrencia/**").hasAnyRole("ADMIN","PROFESSOR")

                .anyRequest().permitAll()  // temporariamente liberado para testes
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}