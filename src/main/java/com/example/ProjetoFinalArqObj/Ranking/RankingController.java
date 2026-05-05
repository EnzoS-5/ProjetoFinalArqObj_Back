package com.example.ProjetoFinalArqObj.Ranking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping
    public RankingResponseDTO listarRankingCompleto() {
        return rankingService.listarRankingCompleto();
    }

    @GetMapping("/top/{limite}")
    public RankingResponseDTO listarTop(@PathVariable Integer limite) {
        return rankingService.listarTop(limite);
    }

    @GetMapping("/minha-posicao")
    public RankingUsuarioDTO buscarMinhaPosicao() {
        return rankingService.buscarMinhaPosicao();
    }
}
