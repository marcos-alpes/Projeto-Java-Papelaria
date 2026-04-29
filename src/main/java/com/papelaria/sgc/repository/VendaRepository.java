package com.papelaria.sgc.repository;

import com.papelaria.sgc.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository de Venda.
 * Design Pattern Repository: abstrai o acesso ao banco de dados.
 * Extende JpaRepository que já fornece operações básicas (save, findById, findAll, delete).
 */
@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    // Busca vendas por período (entre duas datas)
    List<Venda> findByDataBetween(LocalDate inicio, LocalDate fim);

    // Busca todas as vendas de um cliente específico
    List<Venda> findByClienteId(Long clienteId);

    // Busca vendas por período E cliente
    List<Venda> findByClienteIdAndDataBetween(Long clienteId, LocalDate inicio, LocalDate fim);

    // Query para relatório: soma de vendas por mês em um ano específico
    @Query("SELECT MONTH(v.data) AS mes, SUM(v.valorTotal) AS total " +
           "FROM Venda v WHERE YEAR(v.data) = :ano " +
           "GROUP BY MONTH(v.data) ORDER BY MONTH(v.data)")
    List<Object[]> findVendasMensaisPorAno(@Param("ano") int ano);

    // Verifica se existe alguma venda para um cliente (usado pela Andressa ao deletar cliente)
    boolean existsByClienteId(Long clienteId);
}