package com.integra.demo.controllers;

import com.integra.demo.dto.LoginRequestDTO;
import com.integra.demo.dto.LoginResponseDTO;
import com.integra.demo.dto.UsuarioRequestDTO;
import com.integra.demo.entities.Usuario;
import com.integra.demo.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /** Login — público, gera o token JWT */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.login(dto));
    }

    /** Apenas ADMIN pode criar novos usuários */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> cadastrar(@RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.cadastrar(dto));
    }

    /** Apenas ADMIN pode listar usuários */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
}