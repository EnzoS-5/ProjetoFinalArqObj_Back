package com.example.ProjetoFinalArqObj.Relatorio;

import com.example.ProjetoFinalArqObj.Relatorio.dto.RelatorioResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping
    public RelatorioResponseDTO gerarDoUsuarioLogado() {
        return relatorioService.gerarDoUsuarioLogado();
    }

    @GetMapping("/{userId}")
    public RelatorioResponseDTO gerarPorUsuario(@PathVariable Integer userId) {
        return relatorioService.gerarPorUsuario(userId);
    }
}
