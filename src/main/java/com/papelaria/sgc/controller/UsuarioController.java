package com.papelaria.sgc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.papelaria.sgc.dto.LoginDTO;
import com.papelaria.sgc.dto.RespostaDTO;
import com.papelaria.sgc.dto.UsuarioDTO;
import com.papelaria.sgc.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ✅ CADASTRAR USUÁRIO
    @PostMapping
    public ResponseEntity<RespostaDTO> cadastrar(@RequestBody UsuarioDTO dto) {
        try {
            usuarioService.cadastrar(dto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new RespostaDTO("Usuário cadastrado com sucesso"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RespostaDTO(e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RespostaDTO("Erro interno ao cadastrar usuário"));
        }
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<RespostaDTO> login(@RequestBody LoginDTO dto) {
        try {
            boolean loginValido = usuarioService.login(dto);

            if (loginValido) {
                return ResponseEntity.ok(new RespostaDTO("Login realizado com sucesso"));
            }

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new RespostaDTO("Usuário ou senha inválidos"));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RespostaDTO("Erro interno no login"));
        }
    }
}