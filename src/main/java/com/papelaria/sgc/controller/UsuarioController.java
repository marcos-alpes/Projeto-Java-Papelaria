package com.papelaria.sgc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.papelaria.sgc.dto.LoginDTO;
import com.papelaria.sgc.dto.UsuarioDTO;
import com.papelaria.sgc.model.Usuario;
import com.papelaria.sgc.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody UsuarioDTO dto) {
        Usuario usuario = usuarioService.cadastrar(dto);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO dto) {
        boolean loginValido = usuarioService.login(dto);

        if (loginValido) {
            return ResponseEntity.ok("Login realizado com sucesso");
        }

        return ResponseEntity.badRequest().body("Usuário ou senha inválidos");
    }
}