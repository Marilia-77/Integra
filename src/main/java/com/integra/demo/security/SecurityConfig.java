package com.integra.demo.security;

import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Rotas públicas
                .requestMatchers("/usuarios/login", "/aluno/login", "/professores/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/aluno", "/professores").permitAll()
                .requestMatchers("/actuator/health").permitAll()

                // Admin
                .requestMatchers("/usuarios/**").hasRole("ADMIN")

                // Professor e Admin podem listar alunos e acessar perfil
                .requestMatchers(HttpMethod.GET, "/aluno").hasAnyRole("ADMIN", "PROFESSOR")
                .requestMatchers(HttpMethod.GET, "/professores/me").hasAnyRole("ADMIN", "PROFESSOR")

                // Aluno pode ver seus próprios dados
                .requestMatchers(HttpMethod.GET, "/aluno/me").hasAnyRole("ALUNO", "ADMIN", "PROFESSOR")
                .requestMatchers(HttpMethod.GET, "/nota/aluno/**").hasAnyRole("ALUNO", "ADMIN", "PROFESSOR")
                .requestMatchers(HttpMethod.GET, "/frequencia/aluno/**").hasAnyRole("ALUNO", "ADMIN", "PROFESSOR")
                .requestMatchers(HttpMethod.GET, "/ocorrencia/aluno/**").hasAnyRole("ALUNO", "ADMIN", "PROFESSOR")
                .requestMatchers(HttpMethod.GET, "/recomendacoes/ocorrencia/**").hasAnyRole("ALUNO", "ADMIN", "PROFESSOR")

                // Professor e Admin gerenciam o resto
                .requestMatchers("/nota/**", "/frequencia/**", "/ocorrencia/**",
                                 "/materia/**", "/turma/**", "/recomendacoes/**").hasAnyRole("ADMIN", "PROFESSOR")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}