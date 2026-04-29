package com.papelaria.sgc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.papelaria.sgc.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsername(String username);

}