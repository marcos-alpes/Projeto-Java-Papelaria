package com.papelaria.sgc.service;

import com.papelaria.sgc.model.Produto;
import com.papelaria.sgc.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    // ==========================
    // SALVAR (COM VALIDAÇÕES)
    // ==========================
    public Produto salvar(Produto produto) {

        // 🔴 VALIDAÇÕES
        if (produto.getPreco() < 0) {
            throw new RuntimeException("Preço não pode ser negativo");
        }

        if (produto.getQuantidadeEstoque() < 0) {
            throw new RuntimeException("Estoque não pode ser negativo");
        }

        // 🔴 EVITAR DUPLICADO (POR NOME)
        List<Produto> produtos = repository.findAll();

        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(produto.getNome())
                    && (produto.getId() == null || !p.getId().equals(produto.getId()))) {

                throw new RuntimeException("Já existe um produto com esse nome");
            }
        }

        return repository.save(produto);
    }

    // ==========================
    // LISTAR
    // ==========================
    public List<Produto> listar() {
        return repository.findAll();
    }

    // ==========================
    // BUSCAR POR ID
    // ==========================
    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    // ==========================
    // DELETAR
    // ==========================
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    // ==========================
    // ESTOQUE BAIXO
    // ==========================
    public List<Produto> estoqueBaixo() {
        return repository.findProdutosComEstoqueBaixo();
    }
}
