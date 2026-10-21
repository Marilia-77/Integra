package com.integra.demo.services;

import com.integra.demo.dto.LoginRequestDTO;
import com.integra.demo.dto.LoginResponseDTO;
import com.integra.demo.dto.UsuarioRequestDTO;
import com.integra.demo.entities.Usuario;
import com.integra.demo.repositories.UsuarioRepository;
import com.integra.demo.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Usuario cadastrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado");
        }
        Usuario u = new Usuario();
        u.setEmail(dto.email());
        u.setSenha(passwordEncoder.encode(dto.senha())); // 🔐 criptografa
        u.setRole(dto.role() != null ? dto.role() : "ALUNO");
        return usuarioRepository.save(u);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario u = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (!passwordEncoder.matches(dto.senha(), u.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        String token = jwtService.gerarToken(u.getEmail(), u.getRole());
        return new LoginResponseDTO(token, u.getEmail(), u.getRole());
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}