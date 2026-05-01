package com.papelaria.sgc.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.papelaria.sgc.dto.LoginDTO;
import com.papelaria.sgc.dto.UsuarioDTO;
import com.papelaria.sgc.model.Usuario;
import com.papelaria.sgc.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final Map<String, LocalDateTime> tokens = new ConcurrentHashMap<>();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario cadastrar(UsuarioDTO dto) {
        String username = dto.getUsername().trim();
        Usuario usuarioExistente = usuarioRepository.findByUsername(username);

        if (usuarioExistente != null) {
            throw new RuntimeException("Usuario ja existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setSenha(encoder.encode(dto.getSenha()));
        usuario.setPerfil(dto.getPerfil());

        return usuarioRepository.save(usuario);
    }

    public TokenLogin login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByUsername(dto.getUsername());

        if (usuario == null || !encoder.matches(dto.getSenha(), usuario.getSenha())) {
            return null;
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiraEm = LocalDateTime.now().plusHours(1);
        tokens.put(token, expiraEm);

        return new TokenLogin(token, expiraEm);
    }

    public boolean tokenValido(String token) {
        LocalDateTime expiraEm = tokens.get(token);
        return expiraEm != null && expiraEm.isAfter(LocalDateTime.now());
    }

    public record TokenLogin(String token, LocalDateTime expiraEm) {}
}
