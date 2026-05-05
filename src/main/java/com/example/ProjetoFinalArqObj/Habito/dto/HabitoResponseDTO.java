package com.example.ProjetoFinalArqObj.Habito.dto;

import com.example.ProjetoFinalArqObj.Habito.Habito;
public record HabitoResponseDTO(
        Integer id,
        Integer userId,
        String titulo,
        String descricao,
        boolean registroDiario,
        int streakInterno,
        boolean ativo
) {
    public static HabitoResponseDTO of(Habito habito) {
        return new HabitoResponseDTO(
                habito.getId(),
                habito.getUser().getId(),
                habito.getTitulo(),
                habito.getDescricao(),
                habito.isRegistroDiario(),
                habito.getStreakInterno(),
                habito.isAtivo()
        );
    }
}
