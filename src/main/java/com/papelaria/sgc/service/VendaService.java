package com.papelaria.sgc.service;

import com.papelaria.sgc.dto.ItemVendaDTO;
import com.papelaria.sgc.dto.VendaDTO;
import com.papelaria.sgc.model.ItemVenda;
import com.papelaria.sgc.model.Venda;
import com.papelaria.sgc.repository.ItemVendaRepository;
import com.papelaria.sgc.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de Vendas.
 * Contém todas as regras de negócio do módulo de vendas.
 * Regras implementadas:
 *  - Venda não pode ser registrada sem itens
 *  - Valor total é calculado automaticamente
 *  - Estoque é atualizado após a venda (via chamada ao ProdutoService)
 */
@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ItemVendaRepository itemVendaRepository;

    // ATENÇÃO: O ProdutoService será injetado aqui.
    // Quando vocês juntarem o projeto, descomentar a linha abaixo:
    // @Autowired
    // private ProdutoService produtoService;

    /**
     * Registra uma nova venda.
     * @Transactional garante que tudo é salvo ou nada é salvo (atomicidade).
     */
    @Transactional
    public VendaDTO registrarVenda(VendaDTO dto) {
        // REGRA DE NEGÓCIO: Venda precisa ter pelo menos 1 item
        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new RuntimeException("Não é possível registrar uma venda sem itens.");
        }

        // Cria a entidade Venda
        Venda venda = new Venda();
        venda.setData(LocalDate.now()); // Data é sempre a data atual
        venda.setClienteId(dto.getClienteId());
        venda.setUsuarioId(dto.getUsuarioId());

        // Calcula o valor total e cria os itens
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemVendaDTO itemDTO : dto.getItens()) {
            // REGRA: Verificar estoque antes de vender
            // Quando integrar com a Júlia, descomentar:
            // produtoService.verificarEstoque(itemDTO.getProdutoId(), itemDTO.getQuantidade());

            ItemVenda item = new ItemVenda();
            item.setVenda(venda);
            item.setProdutoId(itemDTO.getProdutoId());
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(itemDTO.getPrecoUnitario());

            // Subtotal = preço unitário * quantidade
            BigDecimal subtotal = itemDTO.getPrecoUnitario()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));
            item.setSubtotal(subtotal);

            venda.getItens().add(item);
            valorTotal = valorTotal.add(subtotal);
        }

        // REGRA: Valor total calculado automaticamente
        venda.setValorTotal(valorTotal);

        // Salva a venda (e os itens por causa do CascadeType.ALL)
        Venda vendaSalva = vendaRepository.save(venda);

        // REGRA: Atualizar estoque após venda
        // Quando integrar com a Júlia, descomentar:
        // for (ItemVenda item : vendaSalva.getItens()) {
        //     produtoService.diminuirEstoque(item.getProdutoId(), item.getQuantidade());
        // }

        return toDTO(vendaSalva);
    }

    /**
     * Lista todas as vendas.
     */
    public List<VendaDTO> listarTodas() {
        return vendaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca venda pelo ID.
     */
    public VendaDTO buscarPorId(Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada com ID: " + id));
        return toDTO(venda);
    }

    /**
     * Busca vendas por período.
     */
    public List<VendaDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return vendaRepository.findByDataBetween(inicio, fim)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca vendas de um cliente específico.
     */
    public List<VendaDTO> buscarPorCliente(Long clienteId) {
        return vendaRepository.findByClienteId(clienteId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Relatório: vendas mensais de um ano (para gráfico).
     * Retorna array com [mês, total].
     */
    public List<Object[]> relatorioVendasMensais(int ano) {
        return vendaRepository.findVendasMensaisPorAno(ano);
    }

    /**
     * Verifica se um cliente possui vendas (usado ao deletar cliente).
     */
    public boolean clientePossuiVendas(Long clienteId) {
        return vendaRepository.existsByClienteId(clienteId);
    }

    // ============= MÉTODOS PRIVADOS (conversão Entidade <-> DTO) =============

    private VendaDTO toDTO(Venda venda) {
        List<ItemVendaDTO> itensDTO = venda.getItens()
                .stream()
                .map(this::itemToDTO)
                .collect(Collectors.toList());

        return new VendaDTO(
                venda.getId(),
                venda.getData(),
                venda.getClienteId(),
                venda.getUsuarioId(),
                venda.getValorTotal(),
                itensDTO
        );
    }

    private ItemVendaDTO itemToDTO(ItemVenda item) {
        return new ItemVendaDTO(
                item.getId(),
                item.getProdutoId(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal()
        );
    }
}