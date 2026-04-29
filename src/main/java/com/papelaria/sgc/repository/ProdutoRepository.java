package com.papelaria.sgc.repository;

import com.papelaria.sgc.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}