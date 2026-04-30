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

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ItemVendaRepository itemVendaRepository;
    
    @Autowired
    private ProdutoService produtoService;

    @Transactional
    public VendaDTO registrarVenda(VendaDTO dto) {
        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new RuntimeException("Não é possível registrar uma venda sem itens.");
        }

        Venda venda = new Venda();
        venda.setData(LocalDate.now()); 
        venda.setClienteId(dto.getClienteId());
        venda.setUsuarioId(dto.getUsuarioId());

        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemVendaDTO itemDTO : dto.getItens()) {

            produtoService.verificarEstoque(itemDTO.getProdutoId(), itemDTO.getQuantidade());

            ItemVenda item = new ItemVenda();
            item.setVenda(venda);
            item.setProdutoId(itemDTO.getProdutoId());
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(itemDTO.getPrecoUnitario());

            BigDecimal subtotal = itemDTO.getPrecoUnitario()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));
            item.setSubtotal(subtotal);

            venda.getItens().add(item);
            valorTotal = valorTotal.add(subtotal);
        }

        venda.setValorTotal(valorTotal);

        Venda vendaSalva = vendaRepository.save(venda);

        for (ItemVenda item : vendaSalva.getItens()) {
             produtoService.diminuirEstoque(item.getProdutoId(), item.getQuantidade());
        }

        return toDTO(vendaSalva);
    }

    public List<VendaDTO> listarTodas() {
        return vendaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VendaDTO buscarPorId(Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada com ID: " + id));
        return toDTO(venda);
    }

    public List<VendaDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return vendaRepository.findByDataBetween(inicio, fim)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<VendaDTO> buscarPorCliente(Long clienteId) {
        return vendaRepository.findByClienteId(clienteId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Object[]> relatorioVendasMensais(int ano) {
        return vendaRepository.findVendasMensaisPorAno(ano);
    }

    public boolean clientePossuiVendas(Long clienteId) {
        return vendaRepository.existsByClienteId(clienteId);
    }


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