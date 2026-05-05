package com.example.ProjetoFinalArqObj.Ranking.dto;

import java.util.List;

public record RankingResponseDTO(
        int totalUsuarios,
        List<RankingUsuarioDTO> usuarios
) {
    public static RankingResponseDTO of(List<RankingUsuarioDTO> usuarios) {
        return new RankingResponseDTO(
                usuarios.size(),
                List.copyOf(usuarios)
        );
    }
}
