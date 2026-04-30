package com.papelaria.sgc.repository;

import com.papelaria.sgc.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByDataBetween(LocalDate inicio, LocalDate fim);

    List<Venda> findByClienteId(Long clienteId);

    List<Venda> findByClienteIdAndDataBetween(Long clienteId, LocalDate inicio, LocalDate fim);

    @Query("SELECT MONTH(v.data) AS mes, SUM(v.valorTotal) AS total " +
           "FROM Venda v WHERE YEAR(v.data) = :ano " +
           "GROUP BY MONTH(v.data) ORDER BY MONTH(v.data)")
    List<Object[]> findVendasMensaisPorAno(@Param("ano") int ano);

    boolean existsByClienteId(Long clienteId);
}