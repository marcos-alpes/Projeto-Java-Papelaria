package com.papelaria.sgc.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.papelaria.sgc.model.Cliente;
import com.papelaria.sgc.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final VendaService vendaService;

    public ClienteService(ClienteRepository clienteRepository, VendaService vendaService) {
        this.clienteRepository = clienteRepository;
        this.vendaService = vendaService;
    }

    public Cliente salvar(Cliente cliente) {
        validarCliente(cliente);

        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new RuntimeException("CPF ja cadastrado.");
        }

        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente nao encontrado."));
    }

    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Cliente cliente = buscarPorId(id);
        validarCliente(clienteAtualizado);

        if (clienteRepository.existsByCpfAndIdNot(clienteAtualizado.getCpf(), id)) {
            throw new RuntimeException("CPF ja cadastrado.");
        }

        cliente.setNome(clienteAtualizado.getNome());
        cliente.setCpf(clienteAtualizado.getCpf());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        cliente.setEndereco(clienteAtualizado.getEndereco());

        return clienteRepository.save(cliente);
    }

    public void excluir(Long id) {
        Cliente cliente = buscarPorId(id);

        if (vendaService.clientePossuiVendas(id)) {
            throw new RuntimeException("Cliente nao pode ser removido porque possui vendas registradas.");
        }

        clienteRepository.delete(cliente);
    }

    private void validarCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new RuntimeException("Nome e obrigatorio.");
        }

        if (cliente.getCpf() == null || cliente.getCpf().isBlank()) {
            throw new RuntimeException("CPF e obrigatorio.");
        }

        if (cliente.getEmail() == null || !cliente.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new RuntimeException("Email invalido.");
        }
    }
}
