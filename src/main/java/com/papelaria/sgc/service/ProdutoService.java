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

    public Produto salvar(Produto produto) {

        if (produto.getPreco() < 0) {
            throw new RuntimeException("Preço não pode ser negativo");
        }

        if (produto.getQuantidadeEstoque() < 0) {
            throw new RuntimeException("Estoque não pode ser negativo");
        }

        return repository.save(produto);
    }

    public List<Produto> listar() {
        return repository.findAll();
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public List<Produto> estoqueBaixo() {
        return repository.findAll()
                .stream()
                .filter(p -> p.getQuantidadeEstoque() <= p.getEstoqueMinimo())
                .toList();
    }
}