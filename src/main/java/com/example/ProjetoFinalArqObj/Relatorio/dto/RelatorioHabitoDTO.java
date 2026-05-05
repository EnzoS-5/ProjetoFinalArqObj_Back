package com.example.ProjetoFinalArqObj.Relatorio.dto;

import com.example.ProjetoFinalArqObj.Habito.Habito;

public record RelatorioHabitoDTO(
        Integer id,
        String titulo,
        String descricao,
        boolean cumprido,
        int streakInterno
) {
    public static RelatorioHabitoDTO of(Habito habito) {
        return new RelatorioHabitoDTO(
                habito.getId(),
                habito.getTitulo(),
                habito.getDescricao(),
                habito.isRegistroDiario(),
                habito.getStreakInterno()
        );
    }
}
