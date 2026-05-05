package com.example.ProjetoFinalArqObj.Ranking;

import java.util.List;

public record RankingResponseDTO(
        int totalUsuarios,
        List<RankingUsuarioDTO> usuarios
) {
    public static RankingResponseDTO of(Ranking ranking) {
        return new RankingResponseDTO(
                ranking.getUsuarios().size(),
                ranking.getUsuarios()
        );
    }
}
