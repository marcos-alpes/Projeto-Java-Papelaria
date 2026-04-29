package com.papelaria.sgc.repository;

import com.papelaria.sgc.model.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository de ItemVenda.
 * Fornece acesso ao banco para os itens de uma venda.
 */
@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {

    // Busca todos os itens de uma venda específica
    List<ItemVenda> findByVendaId(Long vendaId);

    // Verifica se existe algum item vendido com esse produto
    // Útil para a Júlia saber se pode deletar um produto
    boolean existsByProdutoId(Long produtoId);
}