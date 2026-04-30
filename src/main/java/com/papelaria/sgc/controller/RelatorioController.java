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

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {

    @Autowired
    private VendaService vendaService;

    @GetMapping("/periodo")
    public ResponseEntity<List<VendaDTO>> relatorioVendasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(vendaService.buscarPorPeriodo(inicio, fim));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VendaDTO>> relatorioVendasPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(vendaService.buscarPorCliente(clienteId));
    }
    
    @GetMapping("/grafico-anual")
    public ResponseEntity<Map<String, Object>> graficoVendasAnuais(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") int ano) {

        List<Object[]> dados = vendaService.relatorioVendasMensais(ano);

        int[] meses = new int[12];
        double[] totais = new double[12];

        for (Object[] linha : dados) {
            int mes = ((Number) linha[0]).intValue();
            double total = ((Number) linha[1]).doubleValue();
            meses[mes - 1] = mes;
            totais[mes - 1] = total;
        }

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