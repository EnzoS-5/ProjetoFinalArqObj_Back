package com.example.ProjetoFinalArqObj.Plano.dto;

import java.util.List;

public record CriarPlanoRequestDTO(
        String titulo,
        String descricao,
        List<Integer> habitoIds,
        Integer metaId
) {
}
