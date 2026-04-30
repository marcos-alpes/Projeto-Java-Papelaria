package com.papelaria.sgc.service;

import com.papelaria.sgc.dto.ItemVendaDTO;
import com.papelaria.sgc.dto.VendaDTO;
import com.papelaria.sgc.model.ItemVenda;
import com.papelaria.sgc.model.Produto;
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

    // Injeta o ProdutoService da Júlia para verificar e atualizar estoque
    @Autowired
    private ProdutoService produtoService;

    // ==========================
    // REGISTRAR VENDA
    // ==========================
    @Transactional
    public VendaDTO registrarVenda(VendaDTO dto) {

        // REGRA: Venda precisa ter pelo menos 1 item
        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new RuntimeException("Não é possível registrar uma venda sem itens.");
        }

        Venda venda = new Venda();
        venda.setData(LocalDate.now());
        venda.setClienteId(dto.getClienteId());
        venda.setUsuarioId(dto.getUsuarioId());

        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemVendaDTO itemDTO : dto.getItens()) {

            // Busca o produto pelo ID usando o service da Júlia
            Produto produto = produtoService.buscarPorId(itemDTO.getProdutoId());

            // REGRA: Verifica se tem estoque suficiente
            if (produto.getQuantidadeEstoque() < itemDTO.getQuantidade()) {
                throw new RuntimeException(
                    "Estoque insuficiente para o produto: " + produto.getNome() +
                    ". Disponível: " + produto.getQuantidadeEstoque() +
                    ", solicitado: " + itemDTO.getQuantidade()
                );
            }

            // Cria o item da venda
            ItemVenda item = new ItemVenda();
            item.setVenda(venda);
            item.setProdutoId(produto.getId());
            item.setQuantidade(itemDTO.getQuantidade());

            // Usa o preço atual do produto (não o que veio do frontend)
            BigDecimal preco = BigDecimal.valueOf(produto.getPreco());
            item.setPrecoUnitario(preco);

            BigDecimal subtotal = preco.multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));
            item.setSubtotal(subtotal);

            venda.getItens().add(item);
            valorTotal = valorTotal.add(subtotal);
        }

        // REGRA: Valor total calculado automaticamente
        venda.setValorTotal(valorTotal);

        // Salva a venda e os itens
        Venda vendaSalva = vendaRepository.save(venda);

        // REGRA: Atualiza o estoque de cada produto após a venda
        for (ItemVenda item : vendaSalva.getItens()) {
            Produto produto = produtoService.buscarPorId(item.getProdutoId());
            produto.setQuantidadeEstoque(
                produto.getQuantidadeEstoque() - item.getQuantidade()
            );
            produtoService.salvar(produto);
        }

        return toDTO(vendaSalva);
    }

    // ==========================
    // LISTAR TODAS
    // ==========================
    public List<VendaDTO> listarTodas() {
        return vendaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ==========================
    // BUSCAR POR ID
    // ==========================
    public VendaDTO buscarPorId(Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada com ID: " + id));
        return toDTO(venda);
    }

    // ==========================
    // BUSCAR POR PERÍODO
    // ==========================
    public List<VendaDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return vendaRepository.findByDataBetween(inicio, fim)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ==========================
    // BUSCAR POR CLIENTE
    // ==========================
    public List<VendaDTO> buscarPorCliente(Long clienteId) {
        return vendaRepository.findByClienteId(clienteId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ==========================
    // RELATÓRIO MENSAL (para gráfico)
    // ==========================
    public List<Object[]> relatorioVendasMensais(int ano) {
        return vendaRepository.findVendasMensaisPorAno(ano);
    }

    // ==========================
    // VERIFICA SE CLIENTE TEM VENDAS
    // (usado pela Andressa ao tentar deletar cliente)
    // ==========================
    public boolean clientePossuiVendas(Long clienteId) {
        return vendaRepository.existsByClienteId(clienteId);
    }

    // ==========================
    // CONVERSÃO Entidade → DTO
    // ==========================
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