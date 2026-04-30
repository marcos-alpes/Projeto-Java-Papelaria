package com.papelaria.sgc.repository;

import com.papelaria.sgc.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByQuantidadeEstoqueLessThan(Integer estoqueMinimo);
}