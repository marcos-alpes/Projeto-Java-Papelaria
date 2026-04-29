package com.papelaria.sgc.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO (Data Transfer Object) para Venda.
 * Design Pattern DTO: separa os dados trafegados na API da entidade do banco.
 * Evita expor campos internos e previne problemas de serialização de entidades JPA.
 */
public class VendaDTO {

    private Long id;
    private LocalDate data;
    private Long clienteId;
    private Long usuarioId;
    private BigDecimal valorTotal;
    private List<ItemVendaDTO> itens;

    // Construtor vazio (necessário para desserialização do JSON)
    public VendaDTO() {}

    // Construtor completo
    public VendaDTO(Long id, LocalDate data, Long clienteId, Long usuarioId,
                    BigDecimal valorTotal, List<ItemVendaDTO> itens) {
        this.id = id;
        this.data = data;
        this.clienteId = clienteId;
        this.usuarioId = usuarioId;
        this.valorTotal = valorTotal;
        this.itens = itens;
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

    public List<ItemVendaDTO> getItens() { return itens; }
    public void setItens(List<ItemVendaDTO> itens) { this.itens = itens; }
}