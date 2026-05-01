package com.papelaria.sgc.repository;

import com.papelaria.sgc.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p WHERE p.quantidadeEstoque < p.estoqueMinimo")
    List<Produto> findProdutosComEstoqueBaixo();
}
