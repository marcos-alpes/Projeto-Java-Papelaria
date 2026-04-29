package com.papelaria.sgc.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    // Relacionamento com cliente (feito pela Andressa)
    // Aqui usamos só o ID para não criar dependência circular entre módulos
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    // Relacionamento com usuário (feito pelo Marcos)
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    // Um venda tem vários itens
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens = new ArrayList<>();

    // Construtores
    public Venda() {}

    public Venda(LocalDate data, Long clienteId, Long usuarioId) {
        this.data = data;
        this.clienteId = clienteId;
        this.usuarioId = usuarioId;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public List<ItemVenda> getItens() { return itens; }
    public void setItens(List<ItemVenda> itens) { this.itens = itens; }
}