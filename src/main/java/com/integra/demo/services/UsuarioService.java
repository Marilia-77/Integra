package com.integra.demo.services;

import com.integra.demo.dto.LoginRequestDTO;
import com.integra.demo.dto.LoginResponseDTO;
import com.integra.demo.dto.UsuarioRequestDTO;
import com.integra.demo.dto.UsuarioResponseDTO;
import com.integra.demo.entities.Usuario;
import com.integra.demo.exception.ConflictException;
import com.integra.demo.exception.UnauthorizedException;
import com.integra.demo.repositories.UsuarioRepository;
import com.integra.demo.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    private UsuarioResponseDTO toResponse(Usuario u) {
        return new UsuarioResponseDTO(u.getIdUsuario(), u.getEmail(), u.getRole());
    }

    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new ConflictException("E-mail já cadastrado");
        }
        Usuario u = new Usuario();
        u.setEmail(dto.email());
        u.setSenha(passwordEncoder.encode(dto.senha()));
        u.setRole(dto.role() != null ? dto.role() : "ALUNO");
        return toResponse(usuarioRepository.save(u));
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario u = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new UnauthorizedException("Credenciais inválidas"));

        if (!passwordEncoder.matches(dto.senha(), u.getSenha())) {
            throw new UnauthorizedException("Credenciais inválidas");
        }

        String token = jwtService.gerarToken(u.getEmail(), u.getRole());
        return new LoginResponseDTO(token, u.getEmail(), u.getRole());
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }
}