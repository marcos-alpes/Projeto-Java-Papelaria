package com.papelaria.sgc.controller;

import com.papelaria.sgc.dto.VendaDTO;
import com.papelaria.sgc.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller de Vendas.
 * Expõe os endpoints REST para o frontend consumir.
 * Prefixo: /api/vendas
 */
@RestController
@RequestMapping("/api/vendas")
@CrossOrigin(origins = "*") // Permite chamadas do frontend (HTML/JS)
public class VendaController {

    @Autowired
    private VendaService vendaService;

    /**
     * POST /api/vendas
     * Registra uma nova venda.
     * Body: VendaDTO com lista de itens
     */
    @PostMapping
    public ResponseEntity<?> registrarVenda(@RequestBody VendaDTO dto) {
        try {
            VendaDTO vendaSalva = vendaService.registrarVenda(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(vendaSalva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET /api/vendas
     * Lista todas as vendas.
     */
    @GetMapping
    public ResponseEntity<List<VendaDTO>> listarTodas() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }

    /**
     * GET /api/vendas/{id}
     * Busca uma venda pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(vendaService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/vendas/periodo?inicio=2025-01-01&fim=2025-12-31
     * Busca vendas por período.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<VendaDTO>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(vendaService.buscarPorPeriodo(inicio, fim));
    }

    /**
     * GET /api/vendas/cliente/{clienteId}
     * Busca todas as vendas de um cliente.
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VendaDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(vendaService.buscarPorCliente(clienteId));
    }
}