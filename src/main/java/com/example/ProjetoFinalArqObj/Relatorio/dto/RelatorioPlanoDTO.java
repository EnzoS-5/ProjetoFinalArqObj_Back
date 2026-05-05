package com.example.ProjetoFinalArqObj.Relatorio.dto;

import com.example.ProjetoFinalArqObj.Plano.Plano;

import java.util.List;

public record RelatorioPlanoDTO(
        Integer id,
        String titulo,
        String descricao,
        List<Integer> habitoIds,
        boolean cumprido
) {
    public static RelatorioPlanoDTO of(Plano plano) {
        return new RelatorioPlanoDTO(
                plano.getId(),
                plano.getTitulo(),
                plano.getDescricao(),
                plano.getHabitos() == null ? List.of() : plano.getHabitos().stream().map(habito -> habito.getId()).toList(),
                plano.isConcluido()
        );
    }
}
