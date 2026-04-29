package com.papelaria.sgc.controller;

import com.papelaria.sgc.dto.VendaDTO;
import com.papelaria.sgc.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller de Relatórios.
 * Endpoints dedicados para geração de relatórios de vendas.
 * Separado do VendaController para seguir o princípio da responsabilidade única (SRP).
 */
@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {

    @Autowired
    private VendaService vendaService;

    /**
     * GET /api/relatorios/periodo?inicio=2025-01-01&fim=2025-12-31
     * Relatório de vendas em um período.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<VendaDTO>> relatorioVendasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(vendaService.buscarPorPeriodo(inicio, fim));
    }

    /**
     * GET /api/relatorios/cliente/{clienteId}
     * Relatório de vendas de um cliente específico.
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VendaDTO>> relatorioVendasPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(vendaService.buscarPorCliente(clienteId));
    }

    /**
     * GET /api/relatorios/grafico-anual?ano=2025
     * Dados para o gráfico de vendas mensais do ano.
     * Retorna: { "meses": [1,2,3,...], "totais": [1500.00, 2000.00, ...] }
     */
    @GetMapping("/grafico-anual")
    public ResponseEntity<Map<String, Object>> graficoVendasAnuais(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") int ano) {

        List<Object[]> dados = vendaService.relatorioVendasMensais(ano);

        // Prepara arrays de meses e totais para o frontend renderizar o gráfico
        int[] meses = new int[12];
        double[] totais = new double[12];

        for (Object[] linha : dados) {
            int mes = ((Number) linha[0]).intValue();
            double total = ((Number) linha[1]).doubleValue();
            meses[mes - 1] = mes;
            totais[mes - 1] = total;
        }

        // Preenche os meses que não tiveram venda com 0
        for (int i = 0; i < 12; i++) {
            if (meses[i] == 0) meses[i] = i + 1;
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("ano", ano);
        resposta.put("meses", meses);
        resposta.put("totais", totais);

        return ResponseEntity.ok(resposta);
    }
}