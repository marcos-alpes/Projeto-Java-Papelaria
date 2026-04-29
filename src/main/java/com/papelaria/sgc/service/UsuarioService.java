package com.papelaria.sgc.service;

import org.springframework.stereotype.Service;

import com.papelaria.sgc.dto.LoginDTO;
import com.papelaria.sgc.dto.UsuarioDTO;
import com.papelaria.sgc.model.Usuario;
import com.papelaria.sgc.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario cadastrar(UsuarioDTO dto) {
        Usuario usuario = new Usuario();

        usuario.setUsername(dto.getUsername());
        usuario.setSenha(dto.getSenha());
        usuario.setPerfil(dto.getPerfil());

        return usuarioRepository.save(usuario);
    }

    public boolean login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByUsername(dto.getUsername());

        if (usuario == null) {
            return false;
        }

        return usuario.getSenha().equals(dto.getSenha());
    }
}