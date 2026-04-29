package com.papelaria.sgc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.papelaria.sgc.dto.LoginDTO;
import com.papelaria.sgc.dto.RespostaDTO;
import com.papelaria.sgc.dto.UsuarioDTO;
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
    public ResponseEntity<RespostaDTO> cadastrar(@RequestBody UsuarioDTO dto) {
        try {
            usuarioService.cadastrar(dto);
            return ResponseEntity.ok(new RespostaDTO("Usuário cadastrado com sucesso"));
        } catch (RuntimeException erro) {
            return ResponseEntity.badRequest().body(new RespostaDTO(erro.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<RespostaDTO> login(@RequestBody LoginDTO dto) {
        boolean loginValido = usuarioService.login(dto);

        if (loginValido) {
            return ResponseEntity.ok(new RespostaDTO("Login realizado com sucesso"));
        }

        return ResponseEntity.badRequest().body(new RespostaDTO("Usuário ou senha inválidos"));
    }
}