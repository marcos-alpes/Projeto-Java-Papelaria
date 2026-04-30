package com.papelaria.sgc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.papelaria.sgc.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    
    boolean existsByCpf(String cpf);

}